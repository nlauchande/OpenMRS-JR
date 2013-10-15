package org.javarosa.openmrsjr.activity.openmrspatientselect;

import org.javarosa.entity.api.transitions.EntitySelectTransitions;

public interface OpenMRSPatientSelectTransitions extends
		EntitySelectTransitions {
	
	public void entitySelected (int id);
	public void cohortList();
	public void unsentEncounters();
	public void formList();
	public void exit();

}
