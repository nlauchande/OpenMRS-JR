package org.javarosa.openmrsjr.activity.formlist;

public interface OpenMRSJRFormListTransitions {

	void formSelected (int formID);
//	void viewSaved ();
	void goPatient ();
	void settings ();
	void cohortList();

//	void addUser ();
	void downloadFormList();
	
}
