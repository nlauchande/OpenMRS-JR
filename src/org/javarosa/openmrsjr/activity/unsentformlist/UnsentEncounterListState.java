package org.javarosa.openmrsjr.activity.unsentformlist;

import java.io.IOException;

import org.javarosa.core.api.State;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.services.storage.IStorageIterator;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.formmanager.api.CompletedFormOptionsState;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.openmrsjr.activity.cohortlist.DownloadCohortListState;
import org.javarosa.openmrsjr.applogic.OpenMRSJRFormTransportState;
import org.javarosa.openmrsjr.applogic.OpenMRSPatientSelectState;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

import de.enough.polish.util.StringTokenizer;

public class UnsentEncounterListState implements
		UnsentEncounterListTransitions, State {

	UnsentEncounterListController controller;
	UnsentEncounterListView view;
	
	private void refreshView(){
		view.refresh();
	}

	public void start() {
		controller = getController();
		this.view = controller.view;
		controller.setTransitions(this);
		controller.start();
	}

	public UnsentEncounterListController getController() {
		return new UnsentEncounterListController();
	}

	public void sendAll() {

		System.out.println("Sending all forms!");

		final IStorageUtility forms = StorageManager
				.getStorage(FormInstance.STORAGE_KEY);
		IStorageIterator fi = forms.iterate();
		while (fi.hasMore()) {
			FormInstance instanceData = (FormInstance) fi.nextRecord();

			CompletedFormOptionsState completed = new CompletedFormOptionsState(
					instanceData) {

				public void sendData(final FormInstance data) {
					FormTransportState send;
					try {
						send = new OpenMRSJRFormTransportState(data) {

							public void done() {
								forms.remove(data);
								refreshView();
								System.out.println("Done sending unsent form!");
								sendAll();
							}

							public void sendToBackground() {
								refreshView();
								System.out.println("Done(SendToBackground) sending unsent form!");
								new UnsentEncounterListState().start();
							}

						};
					} catch (IOException e) {
						throw new RuntimeException(
								"Unable to serialize XML Payload!");
					}
					send.start();
				}

				public void sendToFreshLocation(FormInstance data) {
					throw new RuntimeException(
							"Sending to non-default location disabled");
				}

				public void skipSend(FormInstance data) {

				}
			};
			completed.start();

		}

	}

	public void sendSelected(int selectedIndex) {

		StringTokenizer st = new StringTokenizer(view.getString(selectedIndex),
				"|");
		int id = Integer.parseInt(st.nextToken().trim());
		String name = st.nextToken().trim();
		System.out.println("ID: " + id + ", Name: " + name);

		final IStorageUtility forms = StorageManager
				.getStorage(FormInstance.STORAGE_KEY);
		IStorageIterator fi = forms.iterate();
		while (fi.hasMore()) {
			FormInstance instanceData = (FormInstance) fi.nextRecord();

			CompletedFormOptionsState completed = new CompletedFormOptionsState(
					instanceData) {

				public void sendData(final FormInstance data) {
					FormTransportState send;
					try {
						send = new OpenMRSJRFormTransportState(data) {

							public void done() {
								forms.remove(data);
								refreshView();
								
								System.out.println("Done sending unsent form!");
								new UnsentEncounterListState().start();
							}

							public void sendToBackground() {
								refreshView();
								System.out.println("Done (SendToBackground) sending unsent form!");
								new UnsentEncounterListState().start();
							}

						};
					} catch (IOException e) {
						throw new RuntimeException(
								"Unable to serialize XML Payload!");
					}
					send.start();
				}

				public void sendToFreshLocation(FormInstance data) {
					throw new RuntimeException(
							"Sending to non-default location disabled");
				}

				public void skipSend(FormInstance data) {

				}
			};
			completed.start();

		}

	}

	public void discardAll() {
		IStorageUtility forms = StorageManager
			.getStorage(FormInstance.STORAGE_KEY);
		forms.removeAll();
		new OpenMRSPatientSelectState().start();
		
	}

	public void back() {
		new OpenMRSPatientSelectState().start();
	}

	public void cohortList() {
		new DownloadCohortListState().start();
	}

}
