package org.javarosa.openmrsjr.applogic;

import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.user.api.LoginState;
import org.javarosa.user.model.User;

public class OpenMRSJRLoginState extends LoginState {

	public void start() {
		OpenMRSJRContext._().setUser(null);
		OpenMRSJRLoginController controller = this.getController();
		controller.setTransitions(this);
		controller.start();
	}

	protected OpenMRSJRLoginController getController() {
		return new OpenMRSJRLoginController();
	}

	public void exit() {
		OpenMRSJRUtil.exit();
	}

	public void loggedIn(User u) {
		OpenMRSJRContext._().setUser(u);
		new OpenMRSPatientSelectState().start();
	}
}
