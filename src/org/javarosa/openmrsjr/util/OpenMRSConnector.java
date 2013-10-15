package org.javarosa.openmrsjr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;


import com.jcraft.jzlib.ZInputStream;

public class OpenMRSConnector {

	public static DataInputStream getDataInputStreamZInputStream(byte[] 
			responseBody) throws Exception {

		InputStream bis = new ByteArrayInputStream(responseBody);
		
		DataInputStream zdis = null;
		DataInputStream dis = new DataInputStream(bis);
		
		ZInputStream zis = new ZInputStream(dis);
		zdis = new DataInputStream(zis);

		int status = zdis.readByte();

		if (status == Constants.STATUS_FAILURE) {
			zdis = null;
			throw new IOException("Connection failed. Please try again.");
		} else if (status == Constants.STATUS_ACCESS_DENIED) {
			zdis = null;
			throw new IOException(
					"Access denied. Check your username and password.");
		} else {
			return zdis;
		}	
	}
	
	public static InputStream GetConnectionPostStream(String url, String username,
			String password, int action, String serializerKey, String locale,
			int cohort)
	{
		ByteArrayOutputStream baus = new ByteArrayOutputStream(   );
		DataOutputStream dos = new DataOutputStream(baus);
		
		try {
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.writeUTF(serializerKey);
			dos.writeUTF(locale);
			dos.writeByte(action);
			if (cohort>0)
				dos.writeInt(cohort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(baus.toByteArray());
	}
	
	public static InputStream GetConnectionPostStreamFormUpload(String url, String username,
			String password, int action, String serializerKey, String locale,
			String content)
	{
		ByteArrayOutputStream baus = new ByteArrayOutputStream(   );
		DataOutputStream dos = new DataOutputStream(baus);
		
		try {
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.writeUTF(serializerKey);
			dos.writeUTF(locale);
			dos.writeByte(action);
			dos.writeByte(1);
			dos.writeUTF(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dos.writeUTF()
		
		return new ByteArrayInputStream(baus.toByteArray());
	}
	
	public static boolean VerifyConnection()
	{
	
		return true;
	}
	
	
}
