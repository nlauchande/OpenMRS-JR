package org.javarosa.openmrsjr.activity.property;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.util.PropertyUtils;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;

public class PropertyInitController implements CommandListener {

	protected PropertyInitTransitions transitions;
	protected PropertyForm view;
	
	public PropertyInitController(){
		view = new PropertyForm("Properties","");
		view.setCommandListener(this);
	}
	
	public PropertyForm getView(){ return view; }
	
	
	public void setTransitions (PropertyInitTransitions transitions) {
		this.transitions = transitions;
	}
	
	public void start() {
		J2MEDisplay.setView(view);
	}
	public void initProperties(){
		
		String baseURL = view.getURL();
		PropertyManager._().setProperty(OpenMRSJRAppProperties.BASE_URL, baseURL);
		OpenMRSJRContext._().getOpenMRSURLManager().setBaseURL(baseURL);
		
	}

	public void commandAction(Command c, Displayable d) {
		if (d==view){
			if (c==view.CMD_LOGIN){
				initProperties();
				transitions.login();
			} else if( c==view.CMD_CANCEL_LOGIN){
				transitions.exit();
			} else if (c==view.CMD_RESET){
				transitions.reset();
			}
			
		}
		
	}

	public void resetView() {
		view.reset();
		
	}

	public String getPatientID() {
		// TODO Auto-generated method stub
		view.getURL();
		return null;
	}
}
