package vn.hti.aerospace.experiment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.knowm.xchart.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SolarDaySample {

	private File file = null;
	private List<SolarData> data;
	private RecordTime startRecordTime;
	private RecordTime endRecordTime;

	// Xml tag
	private String powerTag = "power";
	private String openCircuitVoltage = "voltage";
	private String shortCircuitCurrent = "current";
	
	public SolarDaySample(String filePath) {
		file = new File(filePath);
		data = new ArrayList<SolarData>();
		readData();
	}
	
	
	public void readStartRecordDates() {
		String fileName = file.getName();
		String[] timeData = fileName.split("_");
		String dateStr = timeData[0];
		String startTimeStr = timeData[1];
		
		startRecordTime = new RecordTime(2023, 
										Integer.parseInt(dateStr.substring(2, 4)), 
										Integer.parseInt(dateStr.substring(0, 2)), 
										Integer.parseInt(startTimeStr.substring(0, 2)),
										Integer.parseInt(startTimeStr.substring(2, 4)), 0);
		
		String endTimeStr = timeData[2];
		if (!endTimeStr.contains("_")) {
			endRecordTime = new RecordTime(2023, 
					Integer.parseInt(dateStr.substring(2, 4)), 
					Integer.parseInt(dateStr.substring(0, 2)), 
					Integer.parseInt(endTimeStr.substring(0, 2)),
					Integer.parseInt(endTimeStr.substring(2, 4)), 0);
		}
		else {
			endRecordTime = new RecordTime();
			endRecordTime.setYear(2023);
			endRecordTime.setMonth(Integer.parseInt(dateStr.substring(2, 4)));
			endRecordTime.setDay(Integer.parseInt(dateStr.substring(0, 2)));
			endRecordTime.setHour(0);
			endRecordTime.setMinute(0);
			endRecordTime.setSecond(0);
		}
		
	}
	
	public void drawData() {
		List<Integer> time = new ArrayList();
		List<Float> power = new ArrayList();
		
		for (SolarData sample : data) {
			time.add(data.indexOf(sample));
			power.add(sample.getSolarSupplyPower());
		}
		
		XYChart solarPowerChart = QuickChart.getChart("Solar Effective Power Chart", "Time", "Power", "EffectivePower(time)", time, power);
		
		// Show it
	    new SwingWrapper(solarPowerChart).displayChart();	
	}
	
	public boolean readData() {
		try {
            if (file == null || !file.exists()) return false;
            readStartRecordDates();
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList powerNodeList = doc.getElementsByTagName(powerTag);
            for (int i=0; i<powerNodeList.getLength(); i++) {
            	RecordTime recordTime =  RecordTime.computeTimeFromElapsedTime(startRecordTime, i);
            	Node powerNode = powerNodeList.item(i);
            	Element power = (Element) powerNode;
            	Node openCircuitVoltageNode = power.getElementsByTagName(openCircuitVoltage).item(0);
            	float openCircuitVoltage = Float.parseFloat(getNodeContent(openCircuitVoltageNode));
            	Node shortCircuitCurrentNode = power.getElementsByTagName(shortCircuitCurrent).item(0);
            	float shortCircuitCurrent = Float.parseFloat(getNodeContent(shortCircuitCurrentNode)); 
            	createData(recordTime, openCircuitVoltage, shortCircuitCurrent);
            }
            
            return true;
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
		
	private String getNodeContent(Node node) {
		Element eElement = (Element) node;
        String nameValue = eElement.getTextContent();
        return nameValue;
	}
	
	public SolarData createData(RecordTime time, float ocv, float scc) {
		SolarData newData =  new SolarData(time, ocv, scc);
		this.data.add(newData);
		return newData;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<SolarData> getData() {
		return data;
	}

	public void setData(List<SolarData> data) {
		this.data = data;
	}
	
	public RecordTime getStartRecordTime() {
		return startRecordTime;
	}

	public void setStartRecordTime(RecordTime startRecordTime) {
		this.startRecordTime = startRecordTime;
	}

	public RecordTime getEndRecordTime() {
		return endRecordTime;
	}

	public void setEndRecordTime(RecordTime endRecordTime) {
		this.endRecordTime = endRecordTime;
	}
	
	public String getInfo() {
		StringBuilder info = new StringBuilder();
		info.append(getFile().getName());
		info.append("\n");
		
		for (SolarData dat : data) {
			info.append(dat.getInfo());
			info.append("\n");
		}
		return info.toString();
	}
	
	public static void main(String[] args) {
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\0703_0950_1800.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\1003_1000_1800.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\0903_1030_1800.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\1103_1000_1800.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\1303_1400_1715.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\1403_1000_1800.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\2003_1000_1830.TXT";
//		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\2103_1230_1830.TXT";
		String filePath = "D:\\Workspaces\\eclipse\\HTI-Aerospace-Experiment\\vn.hti.aerospace.experiment\\data\\2203_1000_1900.TXT";
		SolarDaySample dailyDataSample = new SolarDaySample(filePath);
//		System.out.println(dailyDataSample.getInfo());
		dailyDataSample.drawData();
	}
	
}
