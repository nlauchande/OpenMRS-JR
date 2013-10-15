package org.javarosa.openmrsjr.activity.openmrspatientidselect;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.util.PropertyUtils;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;

public class PropertyOpenMRSIdInitController implements CommandListener {

	protected OpenMRSPatientIDSelectTransitions transitions;
	protected PropertyOpenMRSPatientIDForm view;
	
	public PropertyOpenMRSIdInitController(){
		view = new PropertyOpenMRSPatientIDForm("Properties");
		view.setCommandListener(this);
	}
	
	public PropertyOpenMRSPatientIDForm getView(){ return view; }
	
	
	public void setTransitions (OpenMRSPatientIDSelectTransitions transitions) {
		this.transitions = transitions;
	}
	
	public String getOpenMRSPatientId()
	{
		return view.getopenMRSIDField();
	}
	
	public void start() {
		J2MEDisplay.setView(view);
	}

	public void commandAction(Command c, Displayable d) {
		if (d==view){
			if (c==view.CMD_GOTOFORM){
				transitions.viewFormsList();
			} 
			else if( c==view.CMD_BACK){
				transitions.back();
			} 
			else if (c==view.CMD_RESET){
				view.reset();
			}
		}
	}

	public void resetView() {
		view.reset();
	}
}
