/**
 * 
 */
package org.javarosa.openmrsjr.applogic;

import java.io.IOException;

import org.javarosa.openmrsjr.activity.formlist.OpenMRSJRFormListState;
import org.javarosa.openmrsjr.activity.openmrspatientidselect.OpenMRSPatientIDSelectTransitions;
import org.javarosa.openmrsjr.activity.openmrspatientidselect.PropertyOpenMRSIdInitController;
import org.javarosa.openmrsjr.activity.property.PropertyInitController;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatient;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.util.PropertyUtils;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.model.xform.XFormSerializingVisitor;

/**
 * @author ctsims
 *
 */
public class OpenMRSPatientIDSelectState implements OpenMRSPatientIDSelectTransitions {
	
	PropertyOpenMRSIdInitController controller;
	
	public OpenMRSPatientIDSelectState() {
		this.controller = new PropertyOpenMRSIdInitController();
	}
	
	public PropertyOpenMRSIdInitController  getController()
	{
		return this.controller;
	}
	
	public void back() {
		// TODO Auto-generated method stub
		new OpenMRSPatientSelectState().start();
	}
	
	public void start () {
		System.out.println("Starting...");
		controller = this.getController();
		controller.setTransitions(this);
		controller.start();
	}
	
	public void viewFormsList() {
		PropertyManager._().setProperty(OpenMRSJRAppProperties.PATIENT_IDENTIFIER_PROPERTY,
				"" + controller.getOpenMRSPatientId() );
		System.out.println(" Patient Identifier is : "+controller.getOpenMRSPatientId() );
		//Means that there is no current patient
		PropertyUtils.initializeProperty(OpenMRSJRAppProperties.PATIENT_ID_PROPERTY, "00000");

		new OpenMRSJRFormListState().start();
	
	}
}
