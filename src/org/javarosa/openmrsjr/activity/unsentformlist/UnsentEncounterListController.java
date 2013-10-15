package org.javarosa.openmrsjr.activity.unsentformlist;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;

public class UnsentEncounterListController  implements CommandListener {

	UnsentEncounterListTransitions transitions;
	UnsentEncounterListView view;

	public UnsentEncounterListController() {
		view = new UnsentEncounterListView("Unsent Encounters", OpenMRSJRContext._().getUser()
				.isAdminUser());
		view.setCommandListener(this);

	}
	public void start(){
		J2MEDisplay.setView(view);
	}
	public void setTransitions(UnsentEncounterListTransitions transitions) {
		this.transitions = transitions;
	}
	

	public void commandAction(Command c, Displayable d) {
		if (d == view) {
			if (c == view.CMD_BACK) {
				transitions.back();
			}else if(c == view.CMD_DISCARDALL){
				transitions.discardAll();
//			}else if(c == view.SELECT_COMMAND){
//				System.out.println("cohortSelected: " + view.getSelectedIndex());
//				transitions.(view.getSelectedIndex());
			}else if(c==view.CMD_SENDALL){
				transitions.sendAll();
			}else if (c==view.CMD_SENDCURRENT){
				System.out.println("cohortSelected: " + view.getSelectedIndex());
				transitions.sendSelected(view.getSelectedIndex());
			}else if (c == view.CMD_GO_COHORTS){
				transitions.cohortList();
			}
		}
	}
}
