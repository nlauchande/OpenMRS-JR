package org.javarosa.openmrsjr.applogic;

import org.javarosa.openmrsjr.util.OpenMRSJRGetFormHttpState;
import org.javarosa.services.transport.TransportMessage;

class GetSpecificFormHttpState extends OpenMRSJRGetFormHttpState {

		protected String url;
		private final OpenMRSGetFormListHttpState getFormListState;
		protected String requestedVariables;
		
		public GetSpecificFormHttpState(OpenMRSGetFormListHttpState getFormListState,String requestVariables) {
			this.getFormListState = getFormListState;
			this.requestedVariables = requestVariables;
		}

		public void setURL(String url) {
			this.url = url;
		}
		
		public String getRequestedVariables()
		{
			return this.requestedVariables;
		}

		public String getURL() {
			System.out.println("Downloading this form: " + url);
			return url;
		}

		public void done() {
			getFormListState.downloadNextForm();
		}
		
		public void onChange(TransportMessage message, String remark) {
		}
	}