package org.javarosa.openmrsjr.activity.savedformlist;

import org.javarosa.openmrsjr.activity.cohortlist.DownloadCohortListState;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListState;

public class OpenMRSJRSavedFormListState implements OpenMRSJRSavedFormListTransitions{
	public void start() {
		OpenMRSJRSavedFormListController ctrl = new OpenMRSJRSavedFormListController();
		ctrl.setTransitions(this);
		ctrl.start();
	}

	public void back() {
		new OpenMRSJRFormListState().start();
	}
	
	public void cohortList() {
		new DownloadCohortListState().start();
	}


	public void savedFormSelected(int intValue) {
		// TODO show saved form
	}
}
