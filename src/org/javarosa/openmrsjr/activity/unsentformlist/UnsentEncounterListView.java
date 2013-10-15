package org.javarosa.openmrsjr.activity.unsentformlist;

import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

public class UnsentEncounterListView extends List {
	public final Command CMD_BACK = new Command("Back", Command.SCREEN, 5);
	public final Command CMD_SENDALL = new Command("Send all", Command.SCREEN,
			5);
	public final Command CMD_SENDCURRENT = new Command("Send Current",
			Command.SCREEN, 5);
	public final Command CMD_DISCARDALL = new Command("Discard All",
			Command.SCREEN, 5);

	public final Command CMD_GO_COHORTS= new Command("View Cohorts", Command.SCREEN, 5); 

	
	public UnsentEncounterListView(String title, boolean admin) {
		super(title, List.IMPLICIT);

		createView();
	}

	public void createView() {
		OrderedHashtable unsentFormList = OpenMRSJRUtil.getUnsentFormList();
		Enumeration keys = unsentFormList.keys();
		while (keys.hasMoreElements()) {
			Integer nextElement = (Integer) keys.nextElement();
			int id = nextElement.intValue();
			String name = (String) unsentFormList.get(nextElement);
			append((String) (id + " | " + name), null);
		}
		addCommand(CMD_SENDALL);
		addCommand(CMD_SENDCURRENT);
		addCommand(CMD_DISCARDALL);
		addCommand(CMD_GO_COHORTS);
		addCommand(CMD_BACK);
	}

	public void refresh() {
		System.out.println("Refreshing list");
		this.deleteAll();
		this.createView();
	}

}
