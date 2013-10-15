package org.javarosa.openmrsjr.applogic;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.locale.Localization;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.activity.property.PropertyInitState;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.services.transport.TransportListener;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.user.api.AddUserController;
import org.javarosa.user.api.LoginController;
import org.javarosa.user.api.transitions.LoginTransitions;
import org.javarosa.user.model.User;
import org.javarosa.user.view.LoginForm;

public class OpenMRSJRLoginController extends LoginController implements
		TransportListener {

//	protected LoginTransitions transitions;
//	protected LoginForm view;
	

	public OpenMRSJRLoginController() {
		super(null, AddUserController.PASSWORD_FORMAT_ALPHA_NUMERIC,false);
		
	}

	protected void performCustomUserValidation() {

		try {

			String baseURL = OpenMRSJRContext._().getOpenMRSURLManager().getBaseURL();
			
			
			//String POST_URL = baseURL	+ "/moduleServlet/xforms/xformDataUpload?batchEntry=false&uname="+ super.view.getUserName() + "&pw="+ super.view.getPassWord() + "";

			String POST_URL = baseURL	+ "/moduleServlet/xforms/xformDataUpload";

			
			PropertyManager._().setProperty(OpenMRSJRAppProperties.POST_URL_LIST_PROPERTY, POST_URL);
			PropertyManager._().setProperty(OpenMRSJRAppProperties.POST_URL_PROPERTY, POST_URL);
			
			System.out.println("JRDemoLoginController.performCustomUserValidation() =  Username: " + super.view.getUserName() + ", Password: " + super.view.getPassWord());
		
			OpenMRSJRUtil.validateOpenMRSUser(super.view.getUserName(), super.view
					.getPassWord(), this);
			
		
			
		} 
		//TODO: FIX THIS SPAGGETHI
		catch (TransportException e) {
			e.printStackTrace();
			System.out
					.println("Unable to validating user details on remote server."
							+ e.getMessage());
			//J2MEDisplay.showError(Localization.get("activity.login.error"),
				//	Localization.get("activity.login.tryagain"));
		
			PropertyInitState propertyInitState = new PropertyInitState();
			propertyInitState.setError(Localization.get("activity.login.tryagain")+e.getMessage()+"Transport exception");
			propertyInitState.start();
		}
		catch(Exception e) // IN CASE URL IS NOT OK
		{  //TODO : FIX THIS CODE A BIT :In case the url is wrong 
			e.printStackTrace();
//			System.out
//					.println("Unable to validating user details on remote server."
//						+ e.getMessage());
		///	J2MEDisplay.showError(Localization.get("activity.login.error"),
//					Localization.get("activity.login.tryagain"));
	
			PropertyInitState propertyInitState = new PropertyInitState();
			propertyInitState.setError(Localization.get("activity.login.tryagain")+"Generic Exception");
			propertyInitState.start();	
		}
	}

	public void onChange(TransportMessage message, String remark) {

	}

	public void onStatusChange(TransportMessage message) {
	  try {
	
		byte[] response = ((SimpleHttpTransportMessage) message)
				.getResponseBody();
		String resp = new String(response);
		System.out.println("Resp: " + resp);

		if (resp.equals("SUCCESS")) {
			User user = new User(super.view.getUserName(), super.view
					.getPassWord(), "201", User.ADMINUSER);

			super.view.setLoggedInUser(user);
			OpenMRSJRContext._().getOpenMRSURLManager().setUser(user);
			OpenMRSJRContext._().setCohortID(1);

			IStorageUtility users = StorageManager.getStorage(User.STORAGE_KEY);
			try {
				users.write(user);
			} catch (StorageFullException e) {
				J2MEDisplay.showError("Save Error", "Memory full!");
			}

			transitions.loggedIn(user);
		} else {
			PropertyInitState propertyInitState = new PropertyInitState();
			propertyInitState.setError(Localization.get("activity.login.tryagain")+"On status change not success connection"+resp);
			propertyInitState.start();
			
///			J2MEDisplay.showError(Localization
//					.get("activity.login.loginincorrect"), Localization
//					.get("activity.login.tryagain"));
		}
		
		
		} catch (Exception e) {
			PropertyInitState propertyInitState = new PropertyInitState();
			propertyInitState.setError(Localization.get("activity.login.tryagain")+e.getMessage()+"Exception on status change");
			propertyInitState.start();
//			J2MEDisplay.showError(Localization
//					.get("activity.login.loginincorrect"), Localization
//					.get("activity.login.tryagain")+" Verify also the url of the server.");
		}
	}

}
