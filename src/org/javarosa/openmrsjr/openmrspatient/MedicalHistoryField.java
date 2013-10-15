package org.javarosa.openmrsjr.openmrspatient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class MedicalHistoryField implements Persistent {

	private String fieldName;
	private Vector values;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Vector getValues() {
		return values;
	}

	public void setValues(Vector values) {
		this.values = values;
	}

	public void read(DataInputStream dis) throws IOException,
			InstantiationException, IllegalAccessException {
		setFieldName(dis.readUTF());
		setValues(PersistentHelper.read(dis, new MedicalHistoryValue()
				.getClass(), dis.readInt()));
	}

	public void write(DataOutputStream dos) throws IOException {
		dos.writeUTF(getFieldName());
		PersistentHelper.write(getValues(), dos, 0);
	}
}