package org.javarosa.openmrsjr.activity.downloadformlist;


import org.javarosa.core.api.State;

public abstract class DownloadFormListState implements DownloadFormListTransitions, State {

	public void start () {
		DownloadFormListController controller = getController();
		controller.setTransitions(this);
		controller.start();
	}
	
	protected DownloadFormListController getController () {
		return new DownloadFormListController();
	}
	
}
