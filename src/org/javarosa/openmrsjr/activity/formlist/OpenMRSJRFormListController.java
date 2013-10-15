package org.javarosa.openmrsjr.activity.formlist;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.javarosa.core.util.OrderedHashtable;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListTransitions;
import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListView;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.j2me.view.J2MEDisplay;

public class OpenMRSJRFormListController implements CommandListener {

	protected OpenMRSJRFormListTransitions transitions;
	protected OpenMRSJRFormListView view;

	protected OrderedHashtable formInfo;

	public OpenMRSJRFormListController() {
		formInfo = OpenMRSJRUtil.getFormList();

		Vector formNames = new Vector();
		
		for (Enumeration ids = formInfo.keys(); ids.hasMoreElements(); ){
			Integer id = (Integer) ids.nextElement();
			
			String title = (String) formInfo.get(id);
			
//		for (Enumeration e = formInfo.elements(); e.hasMoreElements();){
//			String s = (String)e.nextElement();
//			if (!title.equalsIgnoreCase("jr-patient-single-reg")){
//				System.out.println("Adding : " + title);
				formNames.addElement(title);
//			}else{
//				System.out.println("removing: " + id.toString());
//				formInfo.remove(id);
//			}
		}
		
		view = new OpenMRSJRFormListView("Choose Form", formNames, OpenMRSJRContext
				._().getUser().isAdminUser());
		view.setCommandListener(this);
	}
	public int getSize(){
		return view.size();
	}

	public void setTransitions(OpenMRSJRFormListTransitions transitions) {
		this.transitions = transitions;
	}

	public void start() {
		if (formInfo.size() ==0){
			transitions.downloadFormList();
		}
		J2MEDisplay.setView(view);
	}

	public void commandAction(Command c, Displayable d) {
		if (d == view) {
			if (c == List.SELECT_COMMAND) {
				int index = view.getSelectedIndex();
				if (index != -1)
					transitions.formSelected(((Integer) formInfo.keyAt(index))
							.intValue());
			} else if (c == view.CMD_GO_PATIENT) {
				transitions.goPatient();
			} else if (c == view.CMD_SETTINGS) {
				transitions.settings();
			} else if (c == view.CMD_DOWNLOAD_FORMLIST){
				transitions.downloadFormList();
			} else if (c == view.CMD_GO_COHORTS){
				transitions.cohortList();
			}
		}
	}
}
