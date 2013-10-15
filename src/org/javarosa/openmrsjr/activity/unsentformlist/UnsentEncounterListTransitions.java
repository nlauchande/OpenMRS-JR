package org.javarosa.openmrsjr.activity.unsentformlist;

public interface UnsentEncounterListTransitions {
	
	public void sendSelected(int selectedIndex);
	public void sendAll();
	public void discardAll();
	public void back();
	public void cohortList();

	

}
