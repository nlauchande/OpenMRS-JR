package org.javarosa.openmrsjr.applogic;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.storage.IStorageIterator;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.entity.api.EntitySelectState;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.activity.cohortlist.DownloadCohortListState;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListState;
import org.javarosa.openmrsjr.activity.openmrspatientselect.OpenMRSPatientSelectController;
import org.javarosa.openmrsjr.activity.openmrspatientselect.OpenMRSPatientSelectTransitions;
import org.javarosa.openmrsjr.activity.openmrspatientselect.OpenMRSPatientSelectView;
import org.javarosa.openmrsjr.activity.unsentformlist.UnsentEncounterListState;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatient;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatientEntity;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatientEntryState;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSXFormUtils;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.OpenMRSConnector;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.services.transport.TransportListener;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.user.model.User;

import de.enough.polish.util.HashMap;

public class OpenMRSPatientSelectState extends
		EntitySelectState<OpenMRSPatient> implements TransportListener, OpenMRSPatientSelectTransitions {

	private static final IStorageUtility STORAGE = StorageManager
			.getStorage(OpenMRSPatient.STORAGE_KEY);

	public OpenMRSPatientSelectState(int cohortid) {
		User user = OpenMRSJRContext._().getUser();
		try {
			OpenMRSJRUtil.DownloadCohort(cohortid, user, this);
		} catch (TransportException e) {

			e.printStackTrace();
		}
	}
	
	public void start () {
		System.out.println("Starting...");
		controller = this.getController();
		controller.setTransitions(this);
		controller.start();
	}

	public OpenMRSPatientSelectState() {
	}

	protected OpenMRSPatientSelectController getController() {
		return new OpenMRSPatientSelectController("Patient Select",
				STORAGE, new OpenMRSPatientEntity(), OpenMRSPatientSelectView.NEW_IN_MENU,true, true);
//		return new EntitySelectController<OpenMRSPatient>("Patient Select",
//				STORAGE, new OpenMRSPatientEntity(), EntitySelectView.NEW_IN_MENU,true);
	}

	public void cancel() {
		OpenMRSJRContext._().setPatientID(-1);
		new DownloadCohortListState().start();
	}

	public void entitySelected(int id) {
		OpenMRSPatient read = (OpenMRSPatient) STORAGE.read(id);
		OpenMRSJRContext._().setPatientID(read.getOpenmrsID());
		PropertyManager._().setProperty(OpenMRSJRAppProperties.PATIENT_ID_PROPERTY,
				"" + read.getOpenmrsID());
		new OpenMRSJRFormListState().start();
	}

	public void newEntity() {
		final OpenMRSPatientSelectState patSel = this;
		new OpenMRSPatientEntryState(patSel).start();
	}

	public void empty() {
		new DownloadCohortListState().start();
	}

	public void onChange(TransportMessage message, String remark) {
	}

	public void onStatusChange(TransportMessage message) {

		try {
			SimpleHttpTransportMessage httpMessage = (SimpleHttpTransportMessage) message;
			if (httpMessage.isSuccess()) {

				
				DataInputStream zdis = OpenMRSConnector.getDataInputStreamZInputStream(httpMessage.getResponseBody());

				
				IStorageUtility patients = StorageManager
						.getStorage(OpenMRSPatient.STORAGE_KEY);

				//Vector<OpenMRSPatient> pl = OpenMRSXFormUtils
				//		.deserializePatientStream(zdis);

				HashMap<Integer, OpenMRSPatient> savedPatients = new HashMap<Integer, OpenMRSPatient>();

				IStorageIterator iterate = STORAGE.iterate();
				while (iterate.hasMore()){
					OpenMRSPatient savedPatient = (OpenMRSPatient) iterate.nextRecord();
					savedPatients.put(new Integer(savedPatient.getOpenmrsID()), savedPatient);
				}

				//for (int i = 0; i < pl.size(); i++) {
				//	OpenMRSPatient omrspatient = (OpenMRSPatient) pl
				//			.elementAt(i);

				int len = zdis.readInt();

				for (int i = 0 ; i < len; i++)
				{
					OpenMRSPatient omrspatient= new OpenMRSPatient();

					//patient.read(dis);
					omrspatient.setOpenmrsID(OpenMRSXFormUtils.readInteger(zdis));
					omrspatient.setPrefix(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setFamilyName(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setMiddleName(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setGivenName(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setGender(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setBirthDate(OpenMRSXFormUtils.readDate(zdis));
					omrspatient.setPatientIdentifier(OpenMRSXFormUtils.readUTF(zdis));
					omrspatient.setNewPatient(zdis.readBoolean());
					
					try {
						// patients.read(id);
						OpenMRSPatient savePatient = savedPatients.get(new Integer(
								omrspatient.getOpenmrsID()));
							
						if (savePatient !=null) {
							patients.remove(savePatient);
							System.out.println("OpenMRSPatient already exists - deleted from RMS: "
									+ omrspatient.getFamilyName());
						}
						
					patients.write(omrspatient);
			    /**		System.out.println("OpenMRSPatient Saved: "
								+ omrspatient.getFamilyName());**/

					} catch (StorageFullException e) {
						J2MEDisplay.showError("Save Error", "Memory full!");
					}
				}
			} else {
				System.out.println("HTTPMESSAGE status is not success!");
			}

		} catch (IOException e) {
			J2MEDisplay.showError("Save Error Patient.", "Memory full-IOException!"+e.getMessage());

			e.printStackTrace();
		} catch (InstantiationException e) {
			J2MEDisplay.showError("Save Error Patient.", "InstantiationException!"+e.getMessage());

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			J2MEDisplay.showError("Save Error Patient.", "IllegalAccessException!"+e.getMessage());

			e.printStackTrace();
		} catch (Exception e) {
			J2MEDisplay.showError("Save Error Patient.", "GenericException!"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.start();
	}

	public void cohortList() {
		new DownloadCohortListState().start();
	}
	
	public void exit()
	{
		OpenMRSJRUtil.exit();
	}

	public void formList() {
		new OpenMRSJRFormListState().start();
	}

	public void unsentEncounters() {
		new UnsentEncounterListState().start();
		
	}
	
}
