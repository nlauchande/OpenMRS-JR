package org.javarosa.openmrsjr.openmrspatient;

import java.io.IOException;
import java.util.Vector;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.condition.IFunctionHandler;
import org.javarosa.core.model.instance.FormInstance;
import org.javarosa.core.model.utils.IPreloadHandler;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.formmanager.api.CompletedFormOptionsState;
import org.javarosa.formmanager.api.FormTransportState;
import org.javarosa.formmanager.api.JrFormEntryController;
import org.javarosa.formmanager.api.JrFormEntryModel;
import org.javarosa.formmanager.utility.FormDefFetcher;
import org.javarosa.formmanager.utility.RMSRetreivalMethod;
import org.javarosa.formmanager.view.IFormEntryView;
import org.javarosa.openmrsjr.applogic.OpenMRSJRContext;
import org.javarosa.openmrsjr.applogic.OpenMRSJRFormTransportState;
import org.javarosa.openmrsjr.applogic.OpenMRSPatientSelectState;
import org.javarosa.openmrsjr.util.OpenMRSJRFormEntryViewFactory;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;
import org.javarosa.patient.entry.activity.PatientEntryState;

public class OpenMRSPatientEntryState extends PatientEntryState{
	
	private OpenMRSPatientSelectState patSel;
	private JrFormEntryController controller;
	private IFormEntryView view;
	private FormInstance instanceData;
	
	public OpenMRSPatientEntryState(OpenMRSPatientSelectState patselstat){
		super();
		this.patSel = patselstat;
	}
	
	public void formEntrySaved(FormDef form, FormInstance instanceData,
			boolean formWasCompleted) {
		if (formWasCompleted) {
			processor.processInstance(instanceData);
			this.instanceData = instanceData;
			onward(instanceData.getID());
		} else {
			abort();
		}
	}
	
	public void onward(int recID) {
		CompletedFormOptionsState completed = new CompletedFormOptionsState(this.instanceData) {

			public void sendData(FormInstance data) {
				OpenMRSJRFormTransportState send;
				try {
					send = new OpenMRSJRFormTransportState(data) {

						public void done() {
							
							OpenMRSJRUtil.goToList(true);
						}

						public void sendToBackground() {
							OpenMRSJRUtil.goToList(true);
						}
					};
					send.start();
				}
				catch(IOException e){
					throw new RuntimeException("Unable to serialize XML Payload!");
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}

			public void sendToFreshLocation(FormInstance data) {
				throw new RuntimeException("Sending to non-default location disabled");
			}

			public void skipSend(FormInstance data) {
				IStorageUtility storage = StorageManager.getStorage(FormInstance.STORAGE_KEY);
				try {
					storage.write(data);
				} catch (StorageFullException e) {
					new RuntimeException("Storage full, unable to save data.");
				}
				abort();
			}
		};
		completed.start();
		
		//patSel.newEntityCreated(recID);
	}

	public void abort() {
		System.out.println("ABORT!");
		patSel.newEntityCreated(-1);
	}

	protected JrFormEntryController getController() {
		Vector<IPreloadHandler> preloaders = OpenMRSJRContext._().getPreloaders();
		Vector<IFunctionHandler> funcHandlers = OpenMRSJRContext._().getFuncHandlers();
		FormDefFetcher fetcher = new FormDefFetcher(new RMSRetreivalMethod(formName), preloaders, funcHandlers);
		FormDef form = fetcher.getFormDef();

		JrFormEntryController controller =  new JrFormEntryController(new JrFormEntryModel(form));
		controller.setView(new OpenMRSJRFormEntryViewFactory().getFormEntryView(controller));
		return controller;
		
	}

	public void start(){
		controller = getController();
		this.view = controller.getView();
		controller.setTransitions(this);
		controller.start();
		
	}

}
