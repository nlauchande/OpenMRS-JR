package org.javarosa.openmrsjr.openmrspatient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.IntegerData;
import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.javarosa.core.util.externalizable.PrototypeFactory;
import org.javarosa.patient.model.Patient;

public class OpenMRSPatient extends Patient {
	
	protected int openmrsID; // OpenMRS Database ID

	public static final String STORAGE_KEY = "OPENMRSPATIENT";
	public void setOpenmrsID(int openmrsID) {
		this.openmrsID = openmrsID;
	}

	public int getOpenmrsID() {
		return openmrsID;
	}
	public void readExternal(DataInputStream in, PrototypeFactory pf) throws IOException, DeserializationException {
		
		super.readExternal(in, pf);
		setOpenmrsID(ExtUtil.readInt(in));
	}
	
	public void writeExternal(DataOutputStream out) throws IOException {
		super.writeExternal(out);
		ExtUtil.writeNumeric(out, getOpenmrsID());
	}
	public void setGender(String gender){
		if (gender.toLowerCase().trim().charAt(0)=='m'){
			this.gender = Patient.SEX_MALE;
		} else if (gender.toLowerCase().trim().charAt(0)=='f'){
			this.gender = Patient.SEX_FEMALE;
		} else{
			this.gender = Patient.SEX_UNKNOWN;
		}
	}
	
	public IAnswerData getRecord(String recordType) {
		
		IAnswerData obj = super.getRecord(recordType);
		if (obj == null){
			if (recordType.equals("openmrsID"))
				return new IntegerData(openmrsID);
		}
		return obj;
	}
	

}
