package org.javarosa.openmrsjr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Vector;

import javax.microedition.io.HttpConnection;

import org.javarosa.core.api.State;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.utils.DateUtils;
import org.javarosa.core.services.storage.IStorageIterator;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListState;
import org.javarosa.openmrsjr.activity.savedformlist.OpenMRSJRSavedFormListState;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.applogic.OpenMRSJRSplashScreenState;
import org.javarosa.patient.model.Patient;
import org.javarosa.services.transport.TransportListener;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.services.transport.TransportService;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.services.transport.senders.SenderThread;
import org.javarosa.user.model.User;

public class OpenMRSJRUtil {

	static OrderedHashtable formList;
	private static OrderedHashtable savedFormList;
	private static OrderedHashtable unsentFormList;

	public static String getAppProperty(String key) {
		return OpenMRSJRContext._().getMidlet().getAppProperty(key);
	}

	public static void start() {
		new OpenMRSJRSplashScreenState().start();
	}

	public static void exit() {
		OpenMRSJRContext._().getMidlet().notifyDestroyed();
	}

	public static void goToList(boolean formList) {
		((State) (formList ? new OpenMRSJRFormListState()
				: new OpenMRSJRSavedFormListState())).start();
	}

	/**
	 * 
	 * generate and store in RMS several sample patients from the file
	 * "testpatients"
	 * 
	 * 
	 * @param prms
	 */
	public static void loadDemoPatients(IStorageUtility patients) {
		final String patientsFile = "/testpatients";

		// #debug debug
		System.out.println("Initializing the test patients ");

		// read test patient data into byte buffer
		byte[] buffer = new byte[4000]; // make sure buffer is big enough for
		// entire file; it will not grow
		// to file size (budget 40 bytes per patient)
		InputStream is = System.class.getResourceAsStream(patientsFile);
		if (is == null) {
			String err = "Test patient data file: " + patientsFile
					+ " not found";
			// #debug error
			System.out.println(err);
			throw new RuntimeException(err);
		}

		int len = 0;
		try {
			len = is.read(buffer);
		} catch (IOException e) {
			// #debug error
			throw new RuntimeException(e.getMessage());
		}

		// copy byte buffer into character string
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++)
			sb.append((char) buffer[i]);
		buffer = null;
		String data = sb.toString();

		// split lines
		Vector lines = DateUtils.split(data, "\n", false);
		data = null;

		// parse patients
		for (int i = 0; i < lines.size(); i++) {
			String line = (String) lines.elementAt(i);
			if (line.trim().length() == 0)
				continue;
			Vector pat = DateUtils.split(line, "|", false);
			if (pat.size() != 6)
				throw new RuntimeException("Malformed patient data at line: "
						+ (i + 1));

			try {
				patients.write(parseSinglePatient(i, pat));
			} catch (StorageFullException e) {
				throw new RuntimeException("uh-oh, storage full [patients]"); // TODO:
				// handle
				// this
			}
		}
	}

	private static Patient parseSinglePatient(int i, Vector pat) {
		Patient p = new Patient();
		p.setFamilyName((String) pat.elementAt(0));
		p.setGivenName((String) pat.elementAt(1));
		p.setMiddleName((String) pat.elementAt(2));
		p.setPatientIdentifier((String) pat.elementAt(3));
		p.setGender("m".equals((String) pat.elementAt(4)) ? Patient.SEX_MALE
				: Patient.SEX_FEMALE);
		p.setBirthDate(DateUtils.dateAdd(DateUtils.today(), -Integer
				.parseInt((String) pat.elementAt(5))));
		System.out.println(p.toString());
		return p;
	}

	public static void initAdminUser(String defaultPassword) {
		IStorageUtility users = StorageManager.getStorage(User.STORAGE_KEY);
		boolean adminUserFound = false;

		IStorageIterator ui = users.iterate();
		while (ui.hasMore()) {
			User user = (User) ui.nextRecord();
			if (User.ADMINUSER.equals(user.getUserType())) {
				adminUserFound = true;
				break;
			}
		}

		if (!adminUserFound) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(defaultPassword);
			admin.setUserType(User.ADMINUSER);

			try {
				users.write(admin);
			} catch (StorageFullException e) {
				throw new RuntimeException("uh-oh, storage full [users]"); // TODO:
				// handle
				// this
			}
		}
	}

	public static User demoUser() {
		User demo = new User("admin", "Munaf123", "101");
		demo.setUserType(User.ADMINUSER);
		return demo;
	}

	public static void refreshFormList() {
		formList = null;
	}

	public static SenderThread sendHTTPMessage(String str, String url) {
		SimpleHttpTransportMessage message = new SimpleHttpTransportMessage(
				str, url);
		message.setCacheable(false);

		try {
			SenderThread sendThread = TransportService.send(message);
			sendThread.addListener(new TransportListener() {

				public void onChange(TransportMessage message, String remark) {
					if (message.getStatus() == 200) {
					} else if (message.getStatus() == 401) {
					}
				}

				public void onStatusChange(TransportMessage message) {
					if (message.getStatus() == 200) {
					} else if (message.getStatus() == 401) {
					}

				}

			});
			return sendThread;
		} catch (TransportException e) {
			System.out.println("Transport Error while downloading form!"
					+ e.getMessage());
			return null;
		}

	}

	// cache this because the storage utility doesn't yet support quick
	// meta-data iterationHTTP_POST("
	public static OrderedHashtable getFormList() {
		if (formList == null) {
			formList = new OrderedHashtable();
			IStorageUtility forms = StorageManager
					.getStorage(FormDef.STORAGE_KEY);
			IStorageIterator fi = forms.iterate();
			while (fi.hasMore()) {
				try {
					FormDef f = (FormDef) fi.nextRecord();
					formList.put(new Integer(f.getID()), f.getTitle());
				} catch (Exception e) {
				}
			}
		}
		return formList;
	}

	// cache this because the storage utility doesn't yet support quick
	// meta-data iteration
	public static OrderedHashtable getSavedFormList() {
		if (savedFormList == null) {
			savedFormList = new OrderedHashtable();
			IStorageUtility forms = StorageManager
					.getStorage(FormInstance.STORAGE_KEY);
			IStorageIterator fi = forms.iterate();
			while (fi.hasMore()) {
				FormInstance f = (FormInstance) fi.nextRecord();
				savedFormList.put(new Integer(f.getID()), f.getName());
			}
		}
		return savedFormList;
	}

	public static OrderedHashtable getUnsentFormList() {
		OrderedHashtable unsentFormList = new OrderedHashtable();
		IStorageUtility forms = StorageManager
				.getStorage(FormInstance.STORAGE_KEY);
		IStorageIterator fi = forms.iterate();
		while (fi.hasMore()) {
			FormInstance f = (FormInstance) fi.nextRecord();
			unsentFormList.put(new Integer(f.getID()), f.getName());
		}
		return unsentFormList;

	}

	public static void validateOpenMRSUser(String username, String password,
			TransportListener listener) throws TransportException {

		String url = OpenMRSJRContext._().getOpenMRSURLManager()
				.getAuthenticateUserURL(username, password);
	
		if (url==null) throw new TransportException("Unable to get AuthenticateUser URL");

		SimpleHttpTransportMessage message = new SimpleHttpTransportMessage(
				"uname="+username+"&pw="+password,url);
		message.setHttpConnectionMethod(HttpConnection.POST);
		message.setCacheable(false);

		SenderThread sendThread = TransportService.send(message, 1, 0);
		sendThread.addListener(listener);

	}

	public static String replaceToken(String url, String token, String value) {
		String replacement = url;
		while (replacement.indexOf(token) >= 0) {
			int start = replacement.indexOf(token);
			replacement = replacement.substring(0, start)
					+ value
					+ replacement.substring(start + token.length(), replacement
							.length());
		}
		return replacement;
	}

	public static void DownloadCohort(int cohort, User user,
			TransportListener listener) throws TransportException {

		if (OpenMRSJRContext._().getOpenMRSURLManager().getUser() == null){
			OpenMRSJRContext._().getOpenMRSURLManager().setUser(user);
		}
		String url = OpenMRSJRContext._().getOpenMRSURLManager()
				.getDownloadCohortURL(cohort);
		
		if (url==null) throw new TransportException("Unable to get CohortList URL");

		//POST APPROACH
		String serializerKey = "xforms.patientSerializer";
		String username = user.getUsername();
		String password =user.getPassword();
		String locale = "";
	
		byte action = Constants.ACTION_DOWNLOAD_PATIENTS;
		
		InputStream is = OpenMRSConnector.GetConnectionPostStream(url, username, password, action, serializerKey, locale, cohort);
		
		SimpleHttpTransportMessage message = null;
		try { 
			message = new SimpleHttpTransportMessage(
					is, url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		message.setHttpConnectionMethod("POST");
		message.setContentType("application/octet-stream");
		
		message.setCacheable(false);

		SenderThread sendThread = TransportService.send(message, 1, 0);
		sendThread.addListener(listener);

	}

	public static void DownloadCohortList(User user, TransportListener listener)
			throws TransportException {

		if (OpenMRSJRContext._().getOpenMRSURLManager().getUser() == null){
			OpenMRSJRContext._().getOpenMRSURLManager().setUser(user);
		}
		
		String url = OpenMRSJRContext._().getOpenMRSURLManager()
				.getDownloadCohortListURL();
		if (url==null) throw new TransportException("Unable to get CohortList URL");

		
		//POST APPROACH
		String serializerKey = "xforms.cohortSerializer";
		String username = user.getUsername();
		String password =user.getPassword();
		String locale = "";
		byte action = Constants.ACTION_DOWNLOAD_COHORTS;

		//POST APPROACH
		InputStream is = OpenMRSConnector.GetConnectionPostStream(url, username, password, action, serializerKey, locale, -1);
		
		SimpleHttpTransportMessage message = null;
		try {
			message = new SimpleHttpTransportMessage(
					is, url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.setHttpConnectionMethod("POST");
		message.setContentType("application/octet-stream");
		message.setCacheable(false);

		SenderThread sendThread = TransportService.send(message, 1, 0);
		sendThread.addListener(listener);

	}
}
