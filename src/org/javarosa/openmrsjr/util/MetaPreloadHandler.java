/**
 * 
 */
package org.javarosa.openmrsjr.util;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.IntegerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.utils.IPreloadHandler;
import org.javarosa.core.services.PropertyManager;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.user.model.User;

/**
 * @author ctsims
 * 
 */
public class MetaPreloadHandler implements IPreloadHandler {
	private static final String MIDLET_VERSION_PROPERTY = "MIDlet-Version";
	private User u;
	private String patientID;

	public MetaPreloadHandler(User u) {
		this.u = u;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javarosa.core.model.utils.IPreloadHandler#handlePostProcess(org.javarosa
	 * .core.model.instance.TreeElement, java.lang.String)
	 */
	public boolean handlePostProcess(TreeElement node, String params) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javarosa.core.model.utils.IPreloadHandler#handlePreload(java.lang
	 * .String)
	 */
	public IAnswerData handlePreload(String preloadParams) {
		if (preloadParams.equals("UserName")) {
			return new StringData(u.getUsername());
		} else if (preloadParams.equals("UserID")) {
			return new StringData(String.valueOf(u.getID()));
		} else if (preloadParams.equals(OpenMRSJRAppProperties.PATIENT_ID_PROPERTY)) {
			
			String tmp = PropertyManager._().getProperty(
					OpenMRSJRAppProperties.PATIENT_ID_PROPERTY).elementAt(0)
					.toString();
			if (tmp.equals("00000") )//No patient selected 
			{
				return new StringData("");
			}
			else
			{
				try {
					int i = Integer.valueOf(tmp);
					return new IntegerData(i);
				} catch (Exception E) {
					return new StringData(tmp);
				}
			}
			
		}else if ( preloadParams.equals(OpenMRSJRAppProperties.PATIENT_IDENTIFIER_PROPERTY) )
		{
			
			String tmp = PropertyManager._().getProperty(
					OpenMRSJRAppProperties.PATIENT_IDENTIFIER_PROPERTY).elementAt(0)
					.toString();

			try {
				int i = Integer.valueOf(tmp);
				return new IntegerData(i);
			} catch (Exception E) {
				return new StringData(tmp);
			}
			
		}
		
		System.out.println("FAILED to preload: " + preloadParams);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javarosa.core.model.utils.IPreloadHandler#preloadHandled()
	 */
	public String preloadHandled() {
		return "context";
	}

}
