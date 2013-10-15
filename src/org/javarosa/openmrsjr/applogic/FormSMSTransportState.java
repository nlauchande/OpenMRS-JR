package org.javarosa.openmrsjr.applogic;

import java.io.IOException;

import javax.wireless.messaging.MessageListener;

import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.model.xform.SMSSerializingVisitor;

public abstract class FormSMSTransportState extends FormTransportState implements MessageListener {

	public FormSMSTransportState(FormInstance tree) throws Exception {
		super(OpenMRSJRContext._().buildMessage(new SMSSerializingVisitor().createSerializedPayload(tree)));
	}
	
	public abstract void done() ;
	public abstract void sendToBackground() ;
}
