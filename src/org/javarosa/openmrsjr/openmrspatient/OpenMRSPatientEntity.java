package org.javarosa.openmrsjr.openmrspatient;

import java.util.Random;
import java.util.Vector;

import org.javarosa.core.model.utils.DateUtils;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.javarosa.entity.model.Entity;
import org.javarosa.patient.model.Patient;

public class OpenMRSPatientEntity extends Entity<OpenMRSPatient> {
	
	protected String ID;
	protected String familyName;
	protected String givenName;
	protected String middleName;
	protected int age;	
	protected int gender;
		
	protected boolean alive;
		
	protected String[] normalizedName;
	protected String normalizedID;
	protected int openmrsID;
	
	public String entityType() {
		return "OpenMRSPatient";
	}
	public int getOpenmrsID(){
		return openmrsID;
	}
	
	
	public String getID() {
		return ID;
	}
	
	public String getName() {
		String name = "";
		
		if ((givenName!=null) && (givenName.length() > 0)) {
			if (name.length() > 0) {
				name += ", ";
			}
			name += givenName;
		}
		
		if ((middleName !=null) && (middleName.length() > 0)) {
			if (name.length() > 0) {
				name += " ";
			}
			name += middleName;
		}
		
		if ((familyName !=null) && (familyName.length() > 0)) {
			if (name.length() > 0) {
				name += " ";
			}
			name += familyName;
		}
		
		return name;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	protected static String normalizeID (String ID) {
		StringBuffer sb = new StringBuffer();
		char[] carr = ID.toCharArray();
		
		for (int i = 0; i < carr.length; i++) {
			char c = carr[i];
			if (Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c)) {
				if (Character.isLowerCase(c))
					c = Character.toUpperCase(c);
				sb.append(c);
			}
		}
			
		return sb.toString();
	}

	public OpenMRSPatientEntity factory() {
		return new OpenMRSPatientEntity();
	}

	public String[] getHeaders(boolean detailed) {
		//#if javarosa.patientselect.formfactor == nokia-s40
		//# String[] shortHeaders = {"Name", "ID", "Age/Sex"};
		//#else
		//String[] shortHeaders = {"Name", "ID", "Sex"};
		String[] shortHeaders = {"OpenMRS Patient List                 "};
		//#endif

		String[] longHeaders = {"Name", "ID", "Sex", "DOB", "Age", "Phone", "Village"};
		
		return detailed ? longHeaders : shortHeaders;
		
	}

	public String[] getLongFields(OpenMRSPatient p) {
		String[] fields = new String[getHeaders(true).length];
		
		String sexStr;
		switch (gender) {
		case Patient.SEX_MALE: sexStr = "Male"; break;
		case Patient.SEX_FEMALE: sexStr = "Female"; break;
		default: sexStr = "Unknown"; break;
		}
		
		Random r = new Random();
		
		String village = null;
		switch(r.nextInt(10)) {
		case 0: village = "Mikocheni"; break;
		case 1: village = "Bagamoyo"; break;
		case 2: village = "Mbezi"; break;
		case 3: village = "Kariakoo"; break;
		case 4: village = "Msasani"; break;
		case 5: village = "Kinondoni"; break;
		case 6: village = "Tabora"; break;
		case 7: village = "Kigoma"; break;
		case 8: village = "Ifakara"; break;
		case 9: village = "Kigomboni"; break;
		}
		
		String phone = "07";
		for (int i = 0; i < 8; i++)
			phone += (r.nextInt(10));
		
		fields[0] = getName();
		fields[1] = getID();
		fields[2] = sexStr;
		fields[3] = DateUtils.formatDate(p.getBirthDate(), DateUtils.FORMAT_HUMAN_READABLE_SHORT);
		fields[4] = (age == -1 ? "?" : age + "");
		fields[5] = phone;
		fields[6] = village;
		
		return fields;
	}

	public String[] getShortFields() {
		String[] fields = new String[getHeaders(false).length];
		fields[0] = getName();
//		fields[1] = getID();
//		
//		String sexStr;
	
		//#if javarosa.patientselect.formfactor != nokia-s40
			
//		switch (gender) {
//		case Patient.SEX_MALE: sexStr = "M"; break;
//		case Patient.SEX_FEMALE: sexStr = "F"; break;
//		default: sexStr = "?"; break;
//		}
		
//		fields[2] = (age == -1 ? "?" : age + "") + "/" + sexStr;
		
		//#else
		
//		switch (gender) {
//		case Patient.SEX_MALE: sexStr = "M"; break;
//		case Patient.SEX_FEMALE: sexStr = "F"; break;
//		default: sexStr = "?"; break;
//		}
//		
//		fields[2] = (age == -1 ? "?" : age + "") + "/" + sexStr;
//	
		//#endif
		
		return fields;
	}

	protected void loadEntity(OpenMRSPatient p) {
		ID = ExtUtil.emptyIfNull(p.getIdentifier());
		familyName = ExtUtil.emptyIfNull(p.getFamilyName());
		givenName = ExtUtil.emptyIfNull(p.getGivenName());
		middleName = ExtUtil.emptyIfNull(p.getMiddleName());
		age = p.getAge();
		gender = p.getGender();
		
		
		normalizedName = normalizeNames();
		normalizedID = normalizeID(getID());
		
		alive = p.isAlive();
		openmrsID = p.getOpenmrsID();
	}
	
	protected static Vector normalizeName (String name) {
		StringBuffer sb = new StringBuffer();
		char[] carr = name.toCharArray();
		
		for (int i = 0; i < carr.length; i++) {
			char c = carr[i];
			if (Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c)) {
				if (Character.isLowerCase(c))
					c = Character.toUpperCase(c);
				sb.append(c);
		//	} else if (c == '\'' || c == '-') {
		//		//do nothing
			} else {
				sb.append(' ');
			}
		}
			
		return DateUtils.split(sb.toString(), " ", true);
	}
	
	protected void concatVector (Vector base, Vector append) {
		for (int i = 0; i < append.size(); i++)
			base.addElement(append.elementAt(i));
	}
	

	private String[] normalizeNames() {
		Vector nameFrags = new Vector();
		concatVector(nameFrags, normalizeName(familyName));
		concatVector(nameFrags, normalizeName(givenName));
		concatVector(nameFrags, normalizeName(middleName));
		
		String[] nameNorm = new String[nameFrags.size()];
		for (int i = 0; i < nameNorm.length; i++)
			nameNorm[i] = (String)nameFrags.elementAt(i);
		return nameNorm;
	}
	
	public String toString(){
		String tmp = "";
		
		tmp += "ID: " + ID + "; ";
		tmp += "familyName: " +  familyName + "; ";
		tmp += "givenName: " +  givenName + "; ";
		tmp += "middleName: " +  middleName + "; ";
		tmp += "age: " +  age  + "; ";	
		tmp += "gender: " +  gender + "; ";
		tmp += "alive: " +  alive + "; ";
		tmp += "normalizedID: " +  normalizedID + "; ";
		tmp += "openmrsID: " +  openmrsID  + "; ";
		return tmp;
	}
		
}