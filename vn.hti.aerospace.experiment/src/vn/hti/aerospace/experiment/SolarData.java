package vn.hti.aerospace.experiment;

public class SolarData {
	
	private RecordTime recordTime;
	private float openCircuitVoltage;
	private float shortCircuitCurrent;
	
	public SolarData() {}
	
	public SolarData(int year, int month, int day, int hour, int minute, int second, float voltage, float current) {
		this.recordTime = new RecordTime(year, month, day, hour, minute, second);
		this.openCircuitVoltage = voltage;
		this.shortCircuitCurrent = current;
	}	
	
	public SolarData(RecordTime recordTime, float voltage, float current) {
		this.recordTime = recordTime;
		this.openCircuitVoltage = voltage;
		this.shortCircuitCurrent = current;
	}
	
	public float getSolarSupplyPower() {
		return openCircuitVoltage * shortCircuitCurrent;
	}

	public float getOpenCircuitVoltage() {
		return openCircuitVoltage;
	}

	public void setOpenCircuitVoltage(float openCircuitVoltage) {
		this.openCircuitVoltage = openCircuitVoltage;
	}

	public float getShortCircuitCurrent() {
		return shortCircuitCurrent;
	}

	public void setShortCircuitCurrent(float shortCircuitCurrent) {
		this.shortCircuitCurrent = shortCircuitCurrent;
	}
	
	public RecordTime getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(RecordTime recordTime) {
		this.recordTime = recordTime;
	}
	
	public String getInfo() {
		StringBuilder info = new StringBuilder();
		info.append(recordTime.getInfo());
		info.append("  Voc=");
		info.append(openCircuitVoltage);
		info.append("  Isc=");
		info.append(shortCircuitCurrent);
		return info.toString();
	}
}
