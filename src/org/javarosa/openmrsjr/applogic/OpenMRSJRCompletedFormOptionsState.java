package org.javarosa.openmrsjr.applogic;

import java.io.IOException;

import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.formmanager.api.CompletedFormOptionsState;

public class OpenMRSJRCompletedFormOptionsState extends CompletedFormOptionsState{
	public OpenMRSJRCompletedFormOptionsState(FormInstance data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	public void sendData(FormInstance data) {
		OpenMRSJRFormTransportState send;
		try {
			send = new OpenMRSJRFormTransportState(data) {

				public void done() {
					//JRDemoUtil.goToList(cameFromFormList);
				}

				public void sendToBackground() {
					//JRDemoUtil.goToList(cameFromFormList);
				}
				
			};
		} catch (IOException e) {
			throw new RuntimeException("Unable to serialize XML Payload!");
		}
		send.start();
	}

	public void sendToFreshLocation(FormInstance data) {
		throw new RuntimeException("Sending to non-default location disabled");
	}

	public void skipSend(FormInstance data) {
		IStorageUtility storage = StorageManager.getStorage(FormInstance.STORAGE_KEY);
		try {
			System.out.println("writing data: " + data.getName());
			storage.write(data);
		} catch (StorageFullException e) {
			new RuntimeException("Storage full, unable to save data.");
		}
		//abort();
	}
}
