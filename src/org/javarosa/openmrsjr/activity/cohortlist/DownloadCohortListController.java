package org.javarosa.openmrsjr.activity.cohortlist;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.user.model.User;

public class DownloadCohortListController implements CommandListener {

	DownloadCohortListTransitions transitions;
	DownloadCohortListView view;

	public DownloadCohortListController() {
		view = new DownloadCohortListView("Cohort List", OpenMRSJRContext._().getUser()
				.isAdminUser());
		view.setCommandListener(this);
	}

	public void start(){
		J2MEDisplay.setView(view);
	}

	public void setTransitions(DownloadCohortListTransitions transitions) {
		this.transitions = transitions;
	}

	public void commandAction(Command c, Displayable d) {
		if (d == view) {
			if (c == view.CMD_EXIT) {
				transitions.exit();
			}else if(c == view.CMD_DOWNLOADCOHORTSLIST){
				transitions.downloadcohortList();
			}else if(c == view.CMD_SELECTBYPATIENTID){
				transitions.selectByPatientID();
			}else if(c == view.SELECT_COMMAND){
				transitions.cohortSelected(view.getSelectedIndex());
			}
		}
	}

}
