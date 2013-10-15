package org.javarosa.openmrsjr.applogic;

import org.javarosa.user.model.User;

public class OpenMRSURLManager {
	private String baseURL = null;
	private User user = null;

	public OpenMRSURLManager() {
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getAuthenticateUserURL(String username, String password) {
		try {
			String AUTHENTICATEUSER_URL = baseURL
					+ "/moduleServlet/xforms/userValidator" ;
			return AUTHENTICATEUSER_URL;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getDownloadFormListURL() {
		try {
			String DOWNLOAD_FORM_LIST_URL = baseURL+"/moduleServlet/xforms/xformDownload";
			return DOWNLOAD_FORM_LIST_URL;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getDownloadFormURL(int n) {
		try {
			String DOWNLOAD_FORM_URL = baseURL+"/moduleServlet/xforms/xformDownload";
			return DOWNLOAD_FORM_URL;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getDownloadCohortListURL() {
		try {
			String DOWNLOAD_COHORTLIST_URL = baseURL
			+ "/moduleServlet/xforms/userDownload";
			return DOWNLOAD_COHORTLIST_URL;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public String getUploadFormURL()
	{
		String UPLOAD_URL = baseURL
		+ "/moduleServlet/xforms/userDownload";
		return UPLOAD_URL;
	}

	public String getDownloadCohortURL(int cohort) {
		try {
	     	String DOWNLOAD_PATIENTCOHORT_NN_URL = baseURL
			+ "/moduleServlet/xforms/userDownload";
			return DOWNLOAD_PATIENTCOHORT_NN_URL;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
