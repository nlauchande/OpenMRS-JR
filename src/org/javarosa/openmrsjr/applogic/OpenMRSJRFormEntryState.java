package org.javarosa.openmrsjr.applogic;

import java.io.IOException;
import java.util.Vector;

import javax.wireless.messaging.MessageConnection;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.condition.IFunctionHandler;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.utils.IPreloadHandler;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.formmanager.api.CompletedFormOptionsState;
import org.javarosa.formmanager.api.FormEntryState;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.formmanager.api.JrFormEntryController;
import org.javarosa.formmanager.api.JrFormEntryModel;
import org.javarosa.formmanager.utility.FormDefFetcher;
import org.javarosa.formmanager.utility.RMSRetreivalMethod;
import org.javarosa.model.xform.XFormSerializingVisitor;
import org.javarosa.openmrsjr.util.OpenMRSJRFormEntryViewFactory;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

public class OpenMRSJRFormEntryState extends FormEntryState {

	protected int formID;
	protected int instanceID;
	boolean cameFromFormList;

	public OpenMRSJRFormEntryState(int formID) {
		init(formID, -1, true);
	}

	public OpenMRSJRFormEntryState(int formID, int instanceID) {
		init(formID, instanceID, false);
	}

	private void init(int formID, int instanceID, boolean cameFromFormList) {
		this.formID = formID;
		this.instanceID = instanceID;
		this.cameFromFormList = cameFromFormList;
	}

	protected JrFormEntryController getController() {

		Vector<IPreloadHandler> preloaders = OpenMRSJRContext._().getPreloaders();
		Vector<IFunctionHandler> funcHandlers = OpenMRSJRContext._()
				.getFuncHandlers();
		FormDefFetcher fetcher = new FormDefFetcher(new RMSRetreivalMethod(
				formID), preloaders, funcHandlers);
		FormDef form = fetcher.getFormDef();

		JrFormEntryController controller = new JrFormEntryController(
				new JrFormEntryModel(form));
		controller.setView(new OpenMRSJRFormEntryViewFactory()
				.getFormEntryView(controller));
		return controller;
		
	}

	public void abort() {
		OpenMRSJRUtil.goToList(cameFromFormList);
	}

	public void formEntrySaved(FormDef form, FormInstance instanceData,
			boolean formWasCompleted) {
		if (formWasCompleted) {

			CompletedFormOptionsState completed = new CompletedFormOptionsState(
					instanceData) {

				public void sendData(FormInstance data) {
					FormTransportState send;
					try {
						System.err.println("Attempting to send using SMS");
						// attempt to send using SMS first.					
						send = new FormSMSTransportState(data) {
							public void done() {
								OpenMRSJRUtil.goToList(cameFromFormList);
							}

							public void sendToBackground() {
								OpenMRSJRUtil.goToList(cameFromFormList);
							}
							
							public void notifyIncomingMessage(
									MessageConnection arg0) {
								
							}
						};
						send.start();
					}catch(Exception e){
						try{
							System.err.println("SMS failed, Attempting to send using HTTP");
							send = new OpenMRSJRFormTransportState(data) {
								
								 public void done() {
									 OpenMRSJRUtil.goToList(cameFromFormList);
								 }
								
								 public void sendToBackground() {
									 OpenMRSJRUtil.goToList(cameFromFormList);
								 }
															
							};
							send.start();
						} catch (IOException ex) {
							System.err.println("Sending using both SMS and HTTP failed. Aborting.");
							throw new RuntimeException(
									"Unable to serialize XML Payload!");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

				public void sendToFreshLocation(FormInstance data) {
					throw new RuntimeException(
							"Sending to non-default location disabled");
				}

				public void skipSend(FormInstance data) {
					IStorageUtility storage = StorageManager
							.getStorage(FormInstance.STORAGE_KEY);
					try {
						System.out.println("writing data: " + data.getName());
						storage.write(data);
					} catch (StorageFullException e) {
						new RuntimeException(
								"Storage full, unable to save data.");
					}
					abort();
				}
			};
			completed.start();
		} else {
			abort();
		}
	}

	public void suspendForMediaCapture(int captureType) {
		throw new RuntimeException("not supported yet!!");
	}

}
