package vn.hti.aerospace.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SolarPowerMeasurement {

	private String dataFolder;
	private List<SolarDaySample> solarDataByDay;
	
	public SolarPowerMeasurement() {
		solarDataByDay = new ArrayList<SolarDaySample>();
	}
	
	public SolarPowerMeasurement(String folder) {
		this();
		dataFolder = folder;
	}
	
	public void getDataFromFolder() {
		File folder = new File(dataFolder);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				createSolarDaySample(file.getName());
			}
		}
	}
	
	public SolarDaySample createSolarDaySample(String fileName) {
		SolarDaySample newSample = new SolarDaySample(fileName);
		newSample.readData();
		solarDataByDay.add(newSample);
		return newSample;
	}

	public String getDataFolder() {
		return dataFolder;
	}

	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}

	public List<SolarDaySample> getSolarDataByDay() {
		return solarDataByDay;
	}

	public void setSolarDataByDay(List<SolarDaySample> solarDataByDay) {
		this.solarDataByDay = solarDataByDay;
	}
}
