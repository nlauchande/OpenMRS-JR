package org.javarosa.openmrsjr.activity.formlist;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

public class OpenMRSJRFormListView extends List {
	public final Command CMD_DOWNLOAD_FORMLIST = new Command("Download Forms", Command.OK, 1);
	public final Command CMD_SETTINGS = new Command("Settings", Command.SCREEN, 5);
	public final Command CMD_ADD_USER = new Command("Add User", Command.SCREEN, 5);
	public final Command CMD_VIEW_SAVED = new Command("View Saved", Command.OK, 1);
	public final Command CMD_GO_PATIENT = new Command("Select Patient", Command.BACK, 1);
	public final Command CMD_GO_COHORTS = new Command("View Cohorts", Command.SCREEN, 5); 

	public OpenMRSJRFormListView (String title, Vector formNames, boolean admin) {
		super(title, List.IMPLICIT);
	
		for (int i = 0; i < formNames.size(); i++) {
			append((String)formNames.elementAt(i), null);
		}
		
		addCommand(CMD_GO_PATIENT);
		addCommand(CMD_GO_COHORTS ); 

		if (admin) {
			addCommand(CMD_SETTINGS);
			addCommand(CMD_DOWNLOAD_FORMLIST);
		}
	}
	
}
