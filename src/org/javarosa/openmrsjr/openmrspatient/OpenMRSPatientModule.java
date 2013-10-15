package org.javarosa.openmrsjr.openmrspatient;

import org.javarosa.core.api.IModule;
import org.javarosa.core.services.storage.StorageManager;

public class OpenMRSPatientModule implements IModule {

	public void registerModule() {
		StorageManager.registerStorage(OpenMRSPatient.STORAGE_KEY,
				OpenMRSPatient.class);

	}

}
