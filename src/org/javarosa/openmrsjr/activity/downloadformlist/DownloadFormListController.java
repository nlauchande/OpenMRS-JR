package org.javarosa.openmrsjr.activity.downloadformlist;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.j2me.view.J2MEDisplay;

public class DownloadFormListController  implements CommandListener {

	DownloadFormListTransitions transitions;
	DownloadFormListView view;
	

	OrderedHashtable formInfo;

	public DownloadFormListController() {
//		formInfo = JRDemoUtil.getFormList();

//		Vector formNames = new Vector();
//		for (Enumeration e = formInfo.elements(); e.hasMoreElements();)
//			formNames.addElement(e.nextElement());

		view = new DownloadFormListView("Choose Form", OpenMRSJRContext
				._().getUser().isAdminUser());
		view.setCommandListener(this);
	}

	public void setTransitions(DownloadFormListTransitions transitions) {
		this.transitions = transitions;
	}

	public void start() {
		J2MEDisplay.setView(view);
	}

	public void commandAction(Command c, Displayable d) {
		if (d == view) {
//			if (c == List.SELECT_COMMAND) {
//				int index = view.getSelectedIndex();
//				if (index != -1)
//					transitions.formSelected(((Integer) formInfo.keyAt(index))
//							.intValue());
//			} else if (c == view.CMD_BACK) {
//				transitions.back();
//			} else if (c == view.CMD_SETTINGS) {
//				transitions.settings();
//			} else if (c == view.CMD_VIEW_SAVED) {
//				transitions.viewSaved();
//			} else if (c == view.CMD_ADD_USER) {
//				transitions.addUser();
//			} 
//			else if (c == view.CMD_GET_FORMS) {
//				transitions.downloadForms();
//			}
			
			if (c== view.CMD_CANCEL){
				transitions.cancel();
			}

		}
	}
}
