package org.javarosa.openmrsjr.openmrspatient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class PatientList implements Persistent {

	/** Collection of patients. */
	private Vector patients;

	/** Constructs a new patient collection. */
	public PatientList() {
		super();
	}

	public PatientList(Vector patients) {
		this();
		setPatients(patients);
	}

	public Vector getPatients() {
		return patients;
	}

	public void setPatients(Vector patients) {
		this.patients = patients;
	}

	public void addPatient(org.javarosa.openmrsjr.openmrspatient.Patient patient) {
		if (patients == null)
			patients = new Vector();
		patients.addElement(patient);
	}

	public void addPatients(Vector patientList) {
		if (patientList != null) {
			if (patients == null)
				patients = patientList;
			else {
				for (int i = 0; i < patientList.size(); i++)
					this.patients.addElement(patientList.elementAt(i));
			}
		}
	}

	public int size() {
		if (getPatients() == null)
			return 0;
		return getPatients().size();
	}

	public org.javarosa.openmrsjr.openmrspatient.Patient getPatient(int index) {
		return (org.javarosa.openmrsjr.openmrspatient.Patient) getPatients().elementAt(index);
	}

	/**
	 * Reads the patient collection object from the supplied stream.
	 * 
	 * @param dis
	 *            - the stream to read from.
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void read(DataInputStream dis) throws IOException,
			InstantiationException, IllegalAccessException {
		int i = dis.readInt();
		//System.out.println("i: " + i);
		Vector patients = PersistentHelper.read(dis, new org.javarosa.openmrsjr.openmrspatient.Patient().getClass(), i);
		setPatients(patients);
	}

	/**
	 * Writes the patient collection object to the supplied stream.
	 * 
	 * @param dos
	 *            - the stream to write to.
	 * @throws IOException
	 */
	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(getPatients(), dos, 0);
	}
}
