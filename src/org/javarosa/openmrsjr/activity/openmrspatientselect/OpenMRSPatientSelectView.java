package org.javarosa.openmrsjr.activity.openmrspatientselect;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.javarosa.entity.api.EntitySelectController;
import org.javarosa.entity.model.Entity;
import org.javarosa.entity.model.view.EntitySelectSortPopup;
import org.javarosa.entity.model.view.EntitySelectView;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatient;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatientEntity;

public class OpenMRSPatientSelectView extends EntitySelectView<OpenMRSPatient> {
	
	private static Command cmdGoCohortList = new Command("View Cohorts", Command.SCREEN, 5); 
	private static Command cmdGoUnsentEncounters = new Command("View Unsent", Command.SCREEN, 5);
	private static Command cmdGoFormList = new Command("View Forms", Command.SCREEN, 5);
	private static Command cmdSelect = new Command("Select Patient", Command.SCREEN, 5);
	private static Command cmdExit = new Command("Exit", Command.SCREEN, 5);
	
	protected OpenMRSPatientSelectController controller;
	protected OpenMRSPatientEntity entityPrototype;

	public OpenMRSPatientSelectView(
			OpenMRSPatientSelectController controller,
			OpenMRSPatientEntity entityPrototype, String title, int newMode) {
		super(controller, entityPrototype, title, newMode);
		this.controller = controller;
		this.entityPrototype = entityPrototype;
		addCommand(cmdSelect);
		addCommand(cmdGoCohortList);
		addCommand(cmdGoUnsentEncounters);
		addCommand(cmdGoFormList);
		//addCommand(cmdExit);
		
	}
	
	public void _commandAction(Command cmd, Displayable d) {
		if (d == this) {
			if (cmd == exitCmd) {
				controller.exit();
			} else if (cmd == sortCmd) {
				EntitySelectSortPopup<OpenMRSPatient> pssw = new EntitySelectSortPopup<OpenMRSPatient>(this, controller, entityPrototype);
				pssw.show();
			} else if (cmd == newCmd) {
				controller.newEntity();
			} else if (cmd == cmdGoFormList){
				controller.goFormList();
			} else if (cmd == cmdGoUnsentEncounters){
				controller.goUnsentEncounters();
			} else if (cmd == cmdGoCohortList){
				controller.goCohortList();
			} else if (cmd == cmdSelect){
				this.processSelect();
			}
	
		}
	}

	protected void processSelect() {
		if (rowIDs.size() > 0) {
			int rowID = rowID(selectedIndex);
			if (rowID == INDEX_NEW) {
				controller.newEntity();
			} else {
				controller.itemSelected(rowID);
			}
		}
	}
	
	
	

}
