package org.javarosa.openmrsjr.util;


//PORTED FROM ODK-CLINIC
public class Constants {

	/** Value representing a not yet set status. */
	public static final int STATUS_NULL = -1;

	/** Value representing success of an action. */
	public static final int STATUS_SUCCESS = 1;

	/** Value representing failure of an action. */
	public static final int STATUS_FAILURE = 0;
	
	/** Value representing failure of an action. */
	public static final int STATUS_ACCESS_DENIED = 2;

	/** Action to get a list of form definitions. */
	public static final int ACTION_DOWNLOAD_FORMS = 3;

	/** Action to save a list of form data. */
	public static final int ACTION_UPLOAD_FORMS = 5;

	/** Action to download a list of patients from the server. */
	public static final int ACTION_DOWNLOAD_PATIENTS = 6;

	/** Action to download a list of patients from the server. */
	public static final int ACTION_DOWNLOAD_USERS = 7;

	/** Action to download a list of users and forms from the server. */
	public static final int ACTION_DOWNLOAD_COHORTS = 8;

	/** Action to download a list of users and forms from the server. */
	public static final int ACTION_DOWNLOAD_USERS_AND_FORMS = 11;
	
	/** Action to download a list of patients filtered by name and identifier. */
	public static final int ACTION_DOWNLOAD_FILTERED_PATIENTS = 15;
	
	/** The default value for the user serializer class.*/
	public static final String DEFAULT_USER_SERIALIZER= "org.openmrs.module.xforms.DefaultUserSerializer";
	
	/** The default value for the patient serializer class.*/
	public static final String DEFAULT_PATIENT_SERIALIZER = "org.openmrs.module.xforms.DefaultPatientSerializer";
	
    /** The default value for the cohort serializer class.*/
    public static final String DEFAULT_COHORT_SERIALIZER = "org.openmrs.module.xforms.DefaultCohortSerializer";

	/** The default value for the xform serializer class.*/
	public static final String DEFAULT_XFORM_SERIALIZER = "org.openmrs.module.xforms.DefaultXformSerializer";
	
	/** The global property key for the patient download cohort.*/
	public static final String GLOBAL_PROP_KEY_PATIENT_DOWNLOAD_COHORT = "xforms.patientDownloadCohort";
	
	public static final String GLOBAL_PROP_KEY_USE_PATIENT_XFORM = "xforms.usePatientXform";

	public static final String GLOBAL_PROP_KEY_USE_ENCOUNTER_XFORM = "xforms.useEncounterXform";
	
	public static final String USER_DOWNLOAD_URL = "/moduleServlet/xforms/userDownload";

	public static final String FORMLIST_DOWNLOAD_URL = "/moduleServlet/xforms/xformDownload?target=xformslist";
	
	public static final String FORM_DOWNLOAD_URL = "/moduleServlet/xforms/xformDownload?target=xform&excludeLayout=true";
	
	public static final String INSTANCE_UPLOAD_URL = "/module/xforms/xformDataUpload.form";

	public static final int TYPE_STRING = 1;
	public static final int TYPE_INT = 2;
	public static final int TYPE_FLOAT = 3;
	public static final int TYPE_DATE = 4;
	
	
	public static final String KEY_PATIENT_ID = "PATIENT_ID";
	public static final String KEY_PATIENT_NAME = "PATIENT_NAME";
	public static final String KEY_PATIENT_IDENTIFIER = "PATIENT_IDENTIFIER";
	
	public static final String KEY_OBSERVATION_FIELD_NAME = "KEY_OBSERVATION_FIELD_NAME";
}
