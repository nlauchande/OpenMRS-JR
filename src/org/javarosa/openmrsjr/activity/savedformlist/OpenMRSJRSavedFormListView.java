package org.javarosa.openmrsjr.activity.savedformlist;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

public class OpenMRSJRSavedFormListView extends List {

	public final Command CMD_BACK = new Command("Back", Command.BACK, 1);
	public final Command CMD_VIEW_SAVED = new Command("View Saved", Command.OK, 1);
	public final Command CMD_GO_COHORTS= new Command("View Cohorts", Command.SCREEN, 5); 

	
	public OpenMRSJRSavedFormListView (String title, Vector formNames) {
		super(title, List.IMPLICIT);
	
		for (int i = 0; i < formNames.size(); i++) {
			append((String)formNames.elementAt(i), null);
		}
		addCommand(CMD_GO_COHORTS);
		addCommand(CMD_BACK);
	}
}
