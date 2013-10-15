package org.javarosa.openmrsjr.activity.formlist;

import org.javarosa.core.api.State;
import org.javarosa.openmrsjr.activity.cohortlist.DownloadCohortListState;
import org.javarosa.openmrsjr.activity.savedformlist.OpenMRSJRSavedFormListState;
import org.javarosa.openmrsjr.applogic.OpenMRSGetFormListHttpState;
import org.javarosa.openmrsjr.applogic.OpenMRSJRFormEntryState;
import org.javarosa.openmrsjr.applogic.OpenMRSPatientSelectState;
import org.javarosa.services.properties.api.PropertyUpdateState;
import org.javarosa.user.api.AddUserState;
import org.javarosa.user.model.User;

public class OpenMRSJRFormListState implements OpenMRSJRFormListTransitions, State {

	public void start() {
		OpenMRSJRFormListController ctrl = new OpenMRSJRFormListController();
		ctrl.setTransitions(this);
		ctrl.start();
	}

	public void formSelected(int formID) {
		new OpenMRSJRFormEntryState(formID).start();
	}

	public void viewSaved() {
		new OpenMRSJRSavedFormListState().start();
	}

	public void goPatient() {
		new OpenMRSPatientSelectState().start();
	}

	public void settings() {
		new PropertyUpdateState() {
			public void done() {
				new OpenMRSJRFormListState().start();
			}
		}.start();
	}

	public void addUser() {
		new AddUserState() {
			public void cancel() {
				new OpenMRSJRFormListState().start();
			}

			public void userAdded(User newUser) {
				new OpenMRSJRFormListState().start();
			}
		}.start();
	}

	public void downloadFormList() {
		new OpenMRSGetFormListHttpState().start();
	}

	public void cohortList() {
		new DownloadCohortListState().start();
	}
}
