package org.javarosa.openmrsjr.activity.cohortlist;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

public class DownloadCohortListView extends List {

	public final Command CMD_EXIT = new Command("Exit", Command.SCREEN, 5);
	public final Command CMD_DOWNLOADCOHORTSLIST = new Command("Download Cohort List", Command.SCREEN,5);
	public final Command CMD_SELECTBYPATIENTID = new Command("Entry by Patient ID", Command.SCREEN,5);
	public final Command CMD_SELECT = new Command("Select", Command.SCREEN,5);
	
	public DownloadCohortListView (String title, boolean admin) {
		super(title, List.IMPLICIT);
		//append((String) "Downloading cohorts...", null);
		//addCommand(CMD_SELECT);
		addCommand(CMD_DOWNLOADCOHORTSLIST);
		addCommand(CMD_SELECTBYPATIENTID);
		addCommand(CMD_EXIT);
		
	}
	
}