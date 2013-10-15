package org.javarosa.openmrsjr.activity.property;

import java.util.Vector;

import javax.microedition.lcdui.Command;

import org.javarosa.core.services.PropertyManager;
import org.javarosa.core.services.locale.Localization;
import org.javarosa.formmanager.view.chatterbox.widget.LabelWidget;
import org.javarosa.openmrsjr.properties.OpenMRSJRAppProperties;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

import de.enough.polish.ui.FramedForm;
import de.enough.polish.ui.TextField;

public class PropertyForm extends FramedForm {

	private final static int DEFAULT_COMMAND_PRIORITY = 1;

	public final static Command CMD_CANCEL_LOGIN = new Command(Localization
			.get("menu.Exit"), Command.SCREEN, DEFAULT_COMMAND_PRIORITY);
	public final static Command CMD_LOGIN = new Command(Localization
			.get("menu.Login"), Command.ITEM, DEFAULT_COMMAND_PRIORITY);

	public static final Command CMD_RESET = new Command("Reset", Command.ITEM,
			DEFAULT_COMMAND_PRIORITY);

	private TextField urlField;

	private String error="";
	
	public PropertyForm(String title,String error) {
		super(title);
		this.error = error;
		init();
		// addCommand(CMD_CANCEL_LOGIN);
		addCommand(CMD_LOGIN);
	}

	private void init() {
		initControls();
	}

	private void initControls() {

		this.urlField = new TextField("URL: ",
				"", 50, TextField.ANY);
		
		Vector baseURL = PropertyManager._().getProperty(OpenMRSJRAppProperties.BASE_URL);
		
	    if(baseURL != null) {
	    	String loadedBaseURL = baseURL.elementAt(0).toString();
	    	this.urlField.setText(loadedBaseURL);
	    	System.out.println("BASEURL found: Writing BaseURL from RMS to View = " + loadedBaseURL);
	    	
	    }

	    append(this.urlField);
	    
	    if (error.length()>0)
	    {
	    	append(new TextField("Error:", error,80, TextField.UNEDITABLE));
	    }
	    
	    this.focus(this.urlField);
		this.urlField.setDefaultCommand(CMD_LOGIN);

	}

	public String getURL() {
		return urlField.getString();
	}

	public void setURL(String s) {
		urlField.setText(s);
	}

	public void reset() {
		urlField.setText("");
	
	}

}
