package org.javarosa.openmrsjr.activity.cohortlist;

public interface DownloadCohortListTransitions {
	
	void cohortSelected (int cohortID);
	void back ();
	void downloadcohortList();
	void selectByPatientID();
	void exit();
}
