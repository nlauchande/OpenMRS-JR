package org.javarosa.openmrsjr.openmrspatient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class PatientFieldList implements Persistent {

	private Vector fields;

	public PatientFieldList() {

	}

	public PatientFieldList(Vector patientFields) {
		super();
		this.fields = patientFields;
	}

	public Vector getFields() {
		return fields;
	}

	public void setFields(Vector fields) {
		this.fields = fields;
	}

	public void addField(PatientField field) {
		if (fields == null)
			fields = new Vector();
		fields.addElement(field);
	}

	public void addPatientFields(Vector fieldList) {
		if (fieldList != null) {
			if (fields == null)
				fields = fieldList;
			else {
				for (int i = 0; i < fieldList.size(); i++)
					this.fields.addElement(fieldList.elementAt(i));
			}
		}
	}

	public int size() {
		if (getFields() == null)
			return 0;
		return getFields().size();
	}

	public PatientField getField(int index) {
		return (PatientField) getFields().elementAt(index);
	}

	/**
	 * Reads the patient field collection object from the supplied stream.
	 * 
	 * @param dis
	 *            - the stream to read from.
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void read(DataInputStream dis) throws IOException,
			InstantiationException, IllegalAccessException {
		setFields(PersistentHelper.read(dis, new PatientField().getClass()));
	}

	/**
	 * Writes the patient field collection object to the supplied stream.
	 * 
	 * @param dos
	 *            - the stream to write to.
	 * @throws IOException
	 */
	public void write(DataOutputStream dos) throws IOException {
		PersistentHelper.write(getFields(), dos);
	}
}
