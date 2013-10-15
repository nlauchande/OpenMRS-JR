package org.javarosa.openmrsjr.activity.downloadformlist;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

public class DownloadFormListView extends List {
	public final Command CMD_CANCEL = new Command("CANCEL", Command.SCREEN, 5);
	
	
	public DownloadFormListView (String title, boolean admin) {
		super(title, List.IMPLICIT);
			
		append((String) "Downloading forms...", null);
				
		addCommand(CMD_CANCEL);
		
	
	}
	
}
