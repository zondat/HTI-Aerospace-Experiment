package vn.hti.infra.exportation.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelFile {

	private String filePath;
	private List<ExcelSheet> ownedSheets;
	
	public ExcelFile() {
		ownedSheets = new ArrayList<ExcelSheet>();
	}
	
	public ExcelFile(String filePath) {
		this.filePath = filePath;
		ownedSheets = new ArrayList<ExcelSheet>();
	}
	
	public void serialize() throws IOException {
        // Create Workbook
        Workbook workbook = getWorkbook(filePath);
 
        for (ExcelSheet excelSheet : ownedSheets) {
        	excelSheet.serialize(workbook);
        }
 
        // Create file excel
        createOutputFile(workbook, filePath);
        System.out.println("Done!!!");
	}
	
	// Create sheet
	public ExcelSheet createSheet(String sheetName) {
		ExcelSheet sheet = new ExcelSheet(sheetName);
		ownedSheets.add(sheet);
		return sheet;
	}
	
    // Create output file
    private void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }
	
    // Create workbook
    private Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;
 
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
 
        return workbook;
    }
	
	public List<ExcelSheet> getOwnedSheets() {
		return ownedSheets;
	}

	public void setOwnedSheets(List<ExcelSheet> ownedSheets) {
		this.ownedSheets = ownedSheets;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	
    public static void main(String[] args) throws IOException {
        final String excelFilePath = "model/books.xlsx";
        ExcelFile file = new ExcelFile(excelFilePath);
        ExcelSheet sheet1 = file.createSheet("Sheet 1");
        ExcelRow row1 = sheet1.createRow();
        row1.createCell("Cell1");
        row1.createCell("Cell2");
        row1.createCell("Cell3");
        ExcelRow row2 = sheet1.createRow();
        row2.createCell("Cell1");
        row2.createCell("Cell2");
        row2.createCell("Cell3");
        file.serialize();
    }
}