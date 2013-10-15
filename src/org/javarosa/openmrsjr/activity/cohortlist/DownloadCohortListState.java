package org.javarosa.openmrsjr.activity.cohortlist;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.javarosa.core.api.State;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.applogic.OpenMRSPatientIDSelectState;
import org.javarosa.openmrsjr.applogic.OpenMRSPatientSelectState;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSXFormUtils;
import org.javarosa.openmrsjr.util.OpenMRSConnector;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.services.transport.TransportListener;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.user.model.User;

import com.jcraft.jzlib.ZInputStream;

import de.enough.polish.util.Map;
import de.enough.polish.util.StringTokenizer;

public class DownloadCohortListState implements DownloadCohortListTransitions, TransportListener, State {
	 

	private DownloadCohortListView view;
	private DownloadCohortListController controller;

	

	public void cohortSelected(int selectedIndex) {
		User u = OpenMRSJRContext._().getUser();

		StringTokenizer st = new StringTokenizer(view.getString(selectedIndex),"|");
		int cohort = Integer.parseInt(st.nextToken().trim());
		OpenMRSJRContext._().setCohortID(cohort);
		OpenMRSPatientSelectState j = new OpenMRSPatientSelectState(cohort);
	}
	
	public void downloadcohortList() {
		
		User u = OpenMRSJRContext._().getUser();
		try {
			OpenMRSJRUtil.DownloadCohortList(u, this);
		} catch (TransportException e) {
			e.printStackTrace();
			J2MEDisplay.showError("Unable to download Cohort List. Check connectivity.", "Connection Problem!");
		}
		
	}
	
	public void start() {
		controller = getController();
		this.view = controller.view;
		controller.setTransitions(this);
		controller.start();
		
		User u = OpenMRSJRContext._().getUser();
		try {
			OpenMRSJRUtil.DownloadCohortList(u, this);
		} catch (TransportException e) {
			e.printStackTrace();
			J2MEDisplay.showError("Unable to download Cohort List. Check connectivity.", "Connection Problem!");
		}
		
	}

	protected DownloadCohortListController getController() {
		return new DownloadCohortListController();
	}

	public void back() {
		new OpenMRSPatientSelectState().start();
	}
	
	public void exit()
	{
		OpenMRSJRUtil.exit();
	}
	
	public void selectByPatientID()
	{
		new OpenMRSPatientIDSelectState().start();
	}

	public void onChange(TransportMessage message, String remark) {
		
	}

	public void onStatusChange(TransportMessage message) {
		try {
			SimpleHttpTransportMessage httpMessage = (SimpleHttpTransportMessage) message;
			if (httpMessage.isSuccess()) {	
				DataInputStream zdis = OpenMRSConnector.getDataInputStreamZInputStream(httpMessage.getResponseBody());
				
/**				InputStream bis = new ByteArrayInputStream(httpMessage
						.getResponseBody());
				
				DataInputStream zdis = null;
				DataInputStream dis = new DataInputStream(bis);
				
				ZInputStream zis = new ZInputStream(dis);
				zdis = new DataInputStream(zis);
**/
				
				//DataInputStrem dis = new DataInputStream()
				
				System.out.println("DEBUG AFRISIS:" + httpMessage
						.getResponseBody() );
				Map<Long, String> cohortMap = OpenMRSXFormUtils.deserialiseCohortStream(zdis);
				Object[] keylist = cohortMap.keys();
				
				view.deleteAll();
				view.setTitle("Select Cohort");
				for (int i =0; i< keylist.length; i++){
					Long longItem = (Long) keylist[i];
					long key = longItem.longValue();
					String name = cohortMap.get(longItem);
					view.append(key + " | " + name, null);
					System.gc();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			J2MEDisplay.showError("Errors occurred parsing response from server.", "Response Problem on cohort list download! IO EXCEPTION");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			J2MEDisplay.showError("Errors occurred parsing response from server.", "Response Problem on cohort list download! GENERIC EXCEPTION");
			
		} 
	}
}
