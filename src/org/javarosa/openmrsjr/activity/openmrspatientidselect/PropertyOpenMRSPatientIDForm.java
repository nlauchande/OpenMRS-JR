package org.javarosa.openmrsjr.activity.openmrspatientidselect;

import java.util.Vector;

import javax.microedition.lcdui.Command;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.locale.Localization;
import org.javarosa.formmanager.view.chatterbox.widget.LabelWidget;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

import de.enough.polish.ui.FramedForm;
import de.enough.polish.ui.TextField;

public class PropertyOpenMRSPatientIDForm extends FramedForm {

	private final static int DEFAULT_COMMAND_PRIORITY = 1;

	public final static Command CMD_BACK = new Command(Localization
			.get("menu.Back"), Command.SCREEN, DEFAULT_COMMAND_PRIORITY);
	public final static Command CMD_GOTOFORM = new Command("Goto forms", Command.ITEM, DEFAULT_COMMAND_PRIORITY);

	public static final Command CMD_RESET = new Command("Reset", Command.ITEM,
			DEFAULT_COMMAND_PRIORITY);

	private TextField openMRSIDField;

	private String error="";
	
	public PropertyOpenMRSPatientIDForm(String title) {
		super(title);
		init();
		addCommand(CMD_BACK);
		addCommand(CMD_GOTOFORM);
	}

	private void init() {
		initControls();
	}

	private void initControls() {
		this.openMRSIDField = new TextField("OpenMRS Patient Identifier: ",
				"", 50, TextField.ANY);
		
	    append(this.openMRSIDField);
	    
	    this.focus(this.openMRSIDField);
		this.openMRSIDField.setDefaultCommand(CMD_GOTOFORM);

	}

	public String getopenMRSIDField() {
		return openMRSIDField.getString();
	}

	public void setopenMRSIDField(String s) {
		openMRSIDField.setText(s);
	}

	public void reset() {
		openMRSIDField.setText("");
	
	}

}
