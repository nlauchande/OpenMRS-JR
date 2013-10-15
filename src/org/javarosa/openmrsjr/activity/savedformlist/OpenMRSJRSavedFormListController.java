package org.javarosa.openmrsjr.activity.savedformlist;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.j2me.log.CrashHandler;
import org.javarosa.j2me.log.HandledCommandListener;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

public class OpenMRSJRSavedFormListController implements HandledCommandListener {
	OpenMRSJRSavedFormListTransitions transitions;
	OpenMRSJRSavedFormListView view;

	OrderedHashtable formInfo;

	public OpenMRSJRSavedFormListController() {
		formInfo = OpenMRSJRUtil.getSavedFormList();

		Vector formNames = new Vector();
		for (Enumeration e = formInfo.elements(); e.hasMoreElements();){
			formNames.addElement(e.nextElement());
		}

		view = new OpenMRSJRSavedFormListView("Saved Forms", formNames);
		view.setCommandListener(this);
	}

	public void setTransitions(OpenMRSJRSavedFormListTransitions transitions) {
		this.transitions = transitions;
	}

	public void start() {
		J2MEDisplay.setView(view);
	}
	
	public void commandAction(Command c, Displayable d) {
		CrashHandler.commandAction(this, c, d);
	} 

	public void _commandAction(Command c, Displayable d) {
		if (d == view) {
			if (c == view.CMD_BACK) {
				transitions.back();
			} else if (c == view.CMD_VIEW_SAVED) {
				int index = view.getSelectedIndex();
				if (index != -1)
					transitions.savedFormSelected(((Integer)formInfo.keyAt(index)).intValue());
			} else if (c == view.CMD_GO_COHORTS){
				transitions.cohortList();
			}
		}
	}
}
