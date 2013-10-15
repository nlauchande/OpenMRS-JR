package org.javarosa.openmrsjr.applogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;

import org.javarosa.core.model.CoreModelModule;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.condition.IFunctionHandler;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.utils.IPreloadHandler;
import org.javarosa.core.reference.ReferenceManager;
import org.javarosa.core.reference.RootTranslator;
import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.properties.JavaRosaPropertyRules;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.core.services.transport.payload.IDataPayload;
import org.javarosa.core.util.JavaRosaCoreModule;
import org.javarosa.core.util.PropertyUtils;
import org.javarosa.formmanager.FormManagerModule;
import org.javarosa.j2me.J2MEModule;
import org.javarosa.j2me.util.DumpRMS;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.model.xform.XFormsModule;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatientModule;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.Constants;
import org.javarosa.openmrsjr.util.MetaPreloadHandler;
import org.javarosa.openmrsjr.util.OpenMRSConnector;
import org.javarosa.patient.PatientModule;
import org.javarosa.resources.locale.LanguagePackModule;
import org.javarosa.resources.locale.LanguageUtils;
import org.javarosa.services.transport.CommUtil;
import org.javarosa.services.transport.TransportManagerModule;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.core.util.StreamsUtil;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.services.transport.impl.sms.SMSTransportMessage;
import org.javarosa.user.activity.UserModule;
import org.javarosa.user.model.User;
import org.javarosa.user.utility.UserUtility;
import org.javarosa.xform.util.XFormUtils;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

import de.enough.polish.util.HashMap;

public class OpenMRSJRContext {

	private static OpenMRSJRContext instance;
	
	public static OpenMRSJRContext _ () {
		if (instance == null) {
			instance = new OpenMRSJRContext();
		}
		return instance;
	}
	
	private MIDlet midlet;
	private User user;
	private OpenMRSURLManager urlmanager;
	private int patientID;
	private int cohortID;
	
	
	public void createNewOpenMRSURLManager(){
		this.urlmanager = new OpenMRSURLManager();	
	}
	public OpenMRSURLManager getOpenMRSURLManager(){
		if (urlmanager == null) createNewOpenMRSURLManager();
		return urlmanager;
	}
	
	
	public void setMidlet(MIDlet m) {
		this.midlet = m;
		J2MEDisplay.init(m);
	}
	
	public MIDlet getMidlet() {
		return midlet;
	}
	
	public void init (MIDlet m) {
		DumpRMS.RMSRecoveryHook(m);
		
		setMidlet(m);
		urlmanager = new OpenMRSURLManager();
		loadModules();
		
		IStorageUtility forms = StorageManager.getStorage(FormDef.STORAGE_KEY);
		//#if (javarosa.dev.shortcuts == true)
		if (forms.getNumRecords() == 0) {
			loadForms(forms);
		}
		//#endif
		addCustomLanguages();
		setProperties();
	
		UserUtility.populateAdminUser();
		loadRootTranslator();
	}
	
	public void loadRootTranslator(){
		ReferenceManager._().addRootTranslator(new RootTranslator("jr://images/", "jr://resource/"));
		ReferenceManager._().addRootTranslator(new RootTranslator("jr://audio/", "jr://resource/"));
	}
	
	private void loadForms (IStorageUtility forms) {
		try {
			forms.write(XFormUtils.getFormFromResource("/sms_test.xml"));
			forms.write(XFormUtils.getFormFromResource("/patient-entry.xhtml"));
		} catch (StorageFullException e) {
			throw new RuntimeException("uh-oh, storage full [forms]"); 
		}
	}
	
	private void loadModules() {
		new J2MEModule().registerModule();
		new JavaRosaCoreModule().registerModule();
		new CoreModelModule().registerModule();
		new XFormsModule().registerModule();
		new TransportManagerModule().registerModule();
		new UserModule().registerModule();
		new OpenMRSPatientModule().registerModule();
		new PatientModule().registerModule();
		new FormManagerModule().registerModule();
		new LanguagePackModule().registerModule();
	}
	
	private void addCustomLanguages() {
//		Localization.registerLanguageFile("Afrikaans", "./messages_afr.txt");
//		Localization.registerLanguageFile("Dari", "./messages_dari.txt");
//		Localization.registerLanguageFile("Espagnol", "./messages_es.txt");
//		Localization.registerLanguageFile("Swahili", "./messages_sw.txt");
		//Localization.registerLanguageFile("English", "./messages_en.txt");		
	}
	
	private void setProperties() {
		User omrsuser = new User("admin","test","101", User.ADMINUSER);
		
		PropertyManager._().addRules(new JavaRosaPropertyRules());
		PropertyManager._().addRules(new OpenMRSJRAppProperties());
		PropertyUtils.initializeProperty("DeviceID", PropertyUtils.genGUID(25));
		PropertyUtils.initializeProperty(OpenMRSJRAppProperties.PATIENT_ID_PROPERTY, "00000");
		PropertyUtils.initializeProperty("logenabled", "Enabled");
		LanguageUtils.initializeLanguage(true, "en");
	}
	
	public void setUser (User u) {
		this.user = u;
		OpenMRSJRContext._().getOpenMRSURLManager().setUser(u);
//		System.out.println("Username: "+ ( u == null? "NULL": u.getUsername() ));
//		System.out.println("Password: "+( u == null? "NULL" : u.getPassword() ));
	}
	
	public User getUser () {
		return user;
	}
	
	public void setPatientID (int patID) {
		this.patientID = patID;
	}
	
	public int getPatientID () {
		return this.patientID;
	}

	public void setCohortID(int cohortID) {
		this.cohortID = cohortID;
	}

	public int getCohortID() {
		return cohortID;
	}
	
	public TransportMessage buildMessage(IDataPayload payload) {
		//Right now we have to just give the message the stream, rather than the payload,
		//since the transport layer won't take payloads. This should be fixed _as soon 
		//as possible_ so that we don't either (A) blow up the memory or (B) lose the ability
		//to send payloads > than the phones' heap.
		
		
		System.out.println("JRDemoContext.java - buildMessage(payload)");
		
		if (payload == null) throw new NullPointerException("Payload is null");
		
		byte[] arr;
		
		try {
			arr = StreamsUtil.readFromStream(payload.getPayloadStream(),-1);
		} catch (IOException e) {
			throw new RuntimeException("Error Serializing Data to be transported");
		}
		
		String destination = null;
		
//	1	destination = payload.getDestination();
		 
		if (destination == null){
				//destination = PropertyManager._().getSingularProperty(OpenMRSJRAppProperties.POST_URL_PROPERTY);
			    destination = getOpenMRSURLManager().getUploadFormURL();
		}
		System.out.println("Destination: " + destination);
		
		if (payload.getPayloadType()==IDataPayload.PAYLOAD_TYPE_SMS){
		
			if (!destination.startsWith("sms://")){
				destination  = "sms://" + destination;
			}
			System.out.println("Updated destination: " + destination);
			
			String content = CommUtil.getString(arr);
			System.out.println("Building SMSTransportMessage: " + content + "== with number: " + destination  );
			return new SMSTransportMessage(content, destination );
			
		}else{
			//OLD APPROACH
		    //return new SimpleHttpTransportMessage(arr, destination);
			
			
			//NEW APPROACH
			//Produce an SimpleHTTPTransportMessage for data upload
			//
			//getUser().getUsername();
			
		   	InputStream is = OpenMRSConnector.GetConnectionPostStreamFormUpload(destination , getUser().getUsername(), getUser().getPassword(), Constants.ACTION_UPLOAD_FORMS,"xforms.xformSerializer","", CommUtil.getString(arr));

		   	SimpleHttpTransportMessage message = null;
		   	try {
		   		message =new SimpleHttpTransportMessage(is,destination);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
		   	//return SimpleHTTPTransportMessage(is,destination);
		}
	}
	
	public Vector<IPreloadHandler> getPreloaders() {
		Vector<IPreloadHandler> handlers = new Vector<IPreloadHandler>();
		MetaPreloadHandler meta = new MetaPreloadHandler(this.getUser());
		handlers.addElement(meta);
		return handlers;		
	}
	
	public Vector<IFunctionHandler> getFuncHandlers () {
		return null;
	}
}
