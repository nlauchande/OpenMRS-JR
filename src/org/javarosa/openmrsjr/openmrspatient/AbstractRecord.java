package org.javarosa.openmrsjr.openmrspatient;

public abstract class AbstractRecord {

	private static final int INVALID_RECORD_ID = -1;
	private int recordId = INVALID_RECORD_ID;

	/**
	 * Gets the record identifier.
	 * 
	 * @return the unique numeric record identifier.
	 */
	public int getRecordId() {
		return recordId;
	}

	/**
	 * Sets the unique identifier of the record.
	 * 
	 * @param recordId
	 *            the unique record identifier.
	 */
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	/**
	 * Determines if this is a new record.
	 * 
	 * @return true if the record is new.
	 */
	public boolean isNew() {
		return getRecordId() == INVALID_RECORD_ID;
	}
}
