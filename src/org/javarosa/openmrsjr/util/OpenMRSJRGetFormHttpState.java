package org.javarosa.openmrsjr.util;

import java.io.ByteArrayInputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.api.State;
import org.javarosa.core.model.FormDef;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.core.util.TrivialTransitions;
import org.javarosa.formmanager.api.GetFormHttpState;
import org.javarosa.formmanager.view.ProgressScreen;
import org.javarosa.j2me.log.CrashHandler;
import org.javarosa.j2me.log.HandledCommandListener;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.services.transport.TransportListener;
import org.javarosa.services.transport.TransportMessage;
import org.javarosa.services.transport.TransportService;
import org.javarosa.services.transport.impl.TransportException;
import org.javarosa.services.transport.impl.simplehttp.SimpleHttpTransportMessage;
import org.javarosa.services.transport.senders.SenderThread;
import org.javarosa.xform.util.XFormUtils;

public abstract class OpenMRSJRGetFormHttpState implements State,TrivialTransitions,HandledCommandListener,TransportListener {

		protected ProgressScreen progressScreen;

		private ByteArrayInputStream bin;

		private SenderThread sendThread;


		public OpenMRSJRGetFormHttpState() {
			
		}
		
		public abstract String getURL();
		
		public abstract String getRequestedVariables();

		public void fetchForm(){

			SimpleSpecialHttpTransportMessage message = new SimpleSpecialHttpTransportMessage(getRequestedVariables(),getURL());
			message.setCacheable(false);
			message.setHttpConnectionMethod("POST");
			try {
				sendThread = TransportService.send(message,1,0);
				sendThread.addListener(this);
			} catch (TransportException e) {
				//TODO: Isn't there a screen where this can be displayed?
				fail("Transport Error while downloading form!" + e.getMessage());
			}
		}

		public void start() {
			this.progressScreen = initProgressScreen();
			J2MEDisplay.setView(progressScreen);
			fetchForm();
		}
		
		protected ProgressScreen initProgressScreen() {
			return new ProgressScreen("Downloadng","Please Wait. Fetching Form...", this);
		}
		
		public void fail(String message) {
			progressScreen.setText(message);
			progressScreen.addCommand(progressScreen.CMD_RETRY);
		}

		public void commandAction(Command c, Displayable d) {
			CrashHandler.commandAction(this, c, d);
		}  

		public void _commandAction(Command command, Displayable display) {
			if(display == progressScreen){
				if(command==progressScreen.CMD_CANCEL){
					sendThread.cancel();
					done();
				} if(command == progressScreen.CMD_RETRY) {
					start();
				}
			}
		}
		
		public void process(byte[] response) {
			IStorageUtility formStorage = StorageManager.getStorage(FormDef.STORAGE_KEY);

			bin = new ByteArrayInputStream(response);
			try {
				formStorage.write(XFormUtils.getFormFromInputStream(bin));
			} catch (StorageFullException e) {
				throw new RuntimeException("Whoops! Storage full : " + FormDef.STORAGE_KEY);
			}
			done();
		}
		
		public void onChange(TransportMessage message, String remark) {
			progressScreen.setText(remark);
		}

		public void onStatusChange(TransportMessage message) {
			SimpleSpecialHttpTransportMessage httpMessage = (SimpleSpecialHttpTransportMessage)message;
			if(httpMessage.isSuccess()) {
				process(httpMessage.getResponseBody());
			} else {
				fail("Failure while fetching XForm: " + message.getFailureReason());
			}
		}
		
}
