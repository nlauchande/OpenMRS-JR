package org.javarosa.openmrsjr.applogic;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.services.storage.IStorageIterator;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.openmrsjr.applogic.GetFormListHttpState;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListState;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.services.transport.TransportMessage;

import de.enough.polish.util.HashMap;
import de.enough.polish.util.StringTokenizer;

public class OpenMRSGetFormListHttpState extends GetFormListHttpState {

	private Vector<String> formURLS = new Vector<String>();

	public byte[] fetched() {
		return null;
	}

	public void cancel() {
		new OpenMRSJRFormListState().start();
	}

	public String getUserName() {
		return OpenMRSJRContext._().getUser().getUsername();
	}

	public String getUserPassword() {
		return OpenMRSJRContext._().getUser().getPassword();
	}

	public String getUrl() {
		String url = OpenMRSJRContext._().getOpenMRSURLManager()
				.getDownloadFormListURL();
		System.out.println("DownloadFormListURL: " + url);
		return url;
	}

	public void onChange(TransportMessage message, String remark) {
	}

	public void process(byte[] response) {

		IStorageUtility formStorage = StorageManager
				.getStorage(FormDef.STORAGE_KEY);
		IStorageIterator iterate = formStorage.iterate();
		HashMap<String, FormDef> savedForms = new HashMap<String, FormDef>();
		while (iterate.hasMore()) {
			try {
				FormDef fd = (FormDef) iterate.nextRecord();
				System.out.println("Form Name: " + fd.getName());
				savedForms.put(fd.getName(), fd);
			} catch (Exception e) {
			}
		}

		System.out.println("response: " + response);
		// FIXME - resolve the responses to be received from the
		// webserver
		if (response == null) {
			// TODO: I don't think this is even possible.
			// fail("Null Response from server");
			System.out.println("Null Response from server");
		} else if (response.equals("WebServerResponses.GET_LIST_ERROR")) {
			// fail("Get List Error from Server");
			System.out.println("Get List Error from Server");
		} else if (response.equals("WebServerResponses.GET_LIST_NO_SURVEY")) {
			// fail("No survey error from server");
			System.out.println("No survey error from server");
		} else {
			String tmp = new String(response);
			System.out.println("Tmp: " + tmp);

			StringTokenizer st = new StringTokenizer(tmp, "<>");
			String formNum = "";
			String formName = "";

			while (st.hasMoreTokens()) {

				String s = st.nextToken();

				if (s.equals("id")) {
					formNum = st.nextToken();
					st.nextToken();

				}
				if (s.equals("name")) {
					formName = st.nextToken();
					FormDef fd = savedForms.get(formName);
					if (fd != null) {
						System.out.println("Form exists in RMS - deleting it");
						formStorage.remove(fd);
						savedForms.remove(formName);
					}
					formURLS.addElement(formNum);
					st.nextToken();

				}
				if (s.equals("/xform")) {
					st.nextToken();
					System.out.println(formNum + " - " + formName);
				}

			}

			for (int i = 0; i < formURLS.size(); i++)
				System.out.println(i + " :" + (String) formURLS.elementAt(i));

			downloadNextForm();
		}
	}

	public void downloadNextForm() {
		if (formURLS.size() > 0) {
			try {
				int formID = Integer.parseInt(formURLS.elementAt(0));
				formURLS.removeElementAt(0);

				String newURL = OpenMRSJRContext._().getOpenMRSURLManager()
						.getDownloadFormURL(formID);

				
				
				String messageBody = "xsltKey=xforms.xsltJR&target=xform&uname="+getUserName()+"&pw="+getUserPassword()+"&formId="+String.valueOf(formID)+"&excludeLayout=true";

				GetSpecificFormHttpState state = new GetSpecificFormHttpState(
						this,messageBody );
				state.setURL(newURL);
				state.start();
				

			} catch (NullPointerException E) {
				System.out.println("No more forms!");
			}
		} else {
			OpenMRSJRUtil.refreshFormList();
			new OpenMRSJRFormListState().start();
		}
	}

	public void _commandAction(Command c, Displayable d) {
		// TODO Auto-generated method stub
	}

	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub
	}
}