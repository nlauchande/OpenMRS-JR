package org.javarosa.openmrsjr.applogic;

import org.javarosa.core.services.locale.Localization;
import org.javarosa.openmrsjr.activity.property.PropertyInitState;
import org.javarosa.splashscreen.api.SplashScreenState;

public class OpenMRSJRSplashScreenState extends SplashScreenState {

	public OpenMRSJRSplashScreenState() {
		super(Localization.get("splashscreen"));
	}

	public void done() {
		new PropertyInitState().start();
	}

}
