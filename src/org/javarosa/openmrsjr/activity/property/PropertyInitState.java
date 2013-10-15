package org.javarosa.openmrsjr.activity.property;

import org.javarosa.core.api.State;
import org.javarosa.core.util.PropertyUtils;
import org.javarosa.openmrsjr.applogic.OpenMRSJRLoginState;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

public class PropertyInitState implements State, PropertyInitTransitions  {

	PropertyInitController controller;
	PropertyForm view;
	private String error="";
	
	public void start() {
		controller = this.getController();
		controller.setTransitions(this);
		this.view = controller.getView();

		controller.start();
	}
	
	public void setError(String error)
	{
		this.error = error;
	}
	
	protected PropertyInitController getController() {
		return new PropertyInitController();
	}

	public void exit() {
		OpenMRSJRUtil.exit();
	}

	public void login() {

		
		
		view.getURL();
		PropertyUtils.initializeProperty(OpenMRSJRAppProperties.BASE_URL,view.getURL());
		new OpenMRSJRLoginState().start();
		
		
	}

	public void reset() {
		controller.resetView();
		
	}

}
