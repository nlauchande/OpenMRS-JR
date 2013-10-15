/**
 * 
 */
package org.javarosa.openmrsjr.applogic;

import java.io.IOException;

import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.model.xform.XFormSerializingVisitor;

/**
 * @author ctsims
 *
 */
public abstract class OpenMRSJRFormTransportState extends FormTransportState {
	
	public OpenMRSJRFormTransportState(FormInstance tree) throws IOException {
		super(OpenMRSJRContext._().buildMessage(new XFormSerializingVisitor().createSerializedPayload(tree)));
	}

}
