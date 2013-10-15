package org.javarosa.openmrsjr.openmrspatient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Persistent {
	void write(DataOutputStream dos) throws IOException;

	void read(DataInputStream dis) throws IOException, InstantiationException,
			IllegalAccessException;
}
