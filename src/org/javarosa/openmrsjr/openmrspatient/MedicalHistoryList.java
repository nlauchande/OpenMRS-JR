package org.javarosa.openmrsjr.openmrspatient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class MedicalHistoryList implements Persistent {

	private Vector history;

	public MedicalHistoryList() {

	}

	public MedicalHistoryList(Vector history) {
		super();
		this.history = history;
	}

	public Vector getHistory() {
		return history;
	}

	public void setHistory(Vector history) {
		this.history = history;
	}

	public void addHistory(PatientMedicalHistory history) {
		if (this.history == null)
			this.history = new Vector();
		this.history.addElement(history);
	}

	public void addPatientFields(Vector historyList) {
		if (historyList != null) {
			if (history == null)
				history = historyList;
			else {
				for (int i = 0; i < historyList.size(); i++)
					this.history.addElement(historyList.elementAt(i));
			}
		}
	}

	public int size() {
		if (getHistory() == null)
			return 0;
		return getHistory().size();
	}

	public PatientMedicalHistory getHistory(int index) {
		return (PatientMedicalHistory) getHistory().elementAt(index);
	}

	public void remove(PatientMedicalHistory history) {
		getHistory().removeElement(history);
	}

	/*
	 * public Object getPatintFiledValue(int fieldId,Integer patientId){ for(int
	 * i=0; i<size(); i++){ PatientFieldValue fieldVal = getValue(i);
	 * if(fieldVal.getFieldId() == fieldId && fieldVal.getPatientId() ==
	 * patientId.intValue()) return fieldVal.getValue(); } return null; }
	 */

	/**
	 * Reads the patient medical history collection object from the supplied
	 * stream.
	 * 
	 * @param dis
	 *            - the stream to read from.
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void read(DataInputStream dis) throws IOException,
			InstantiationException, IllegalAccessException {
		setHistory(PersistentHelper.read(dis, new PatientMedicalHistory()
				.getClass(), dis.readInt()));
	}

	/**
	 * Writes the patient medical history collection object to the supplied
	 * stream.
	 * 
	 * @param dos
	 *            - the stream to write to.
	 * @throws IOException
	 */
	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(getHistory(), dos, 0);
	}
}
