package vn.hti.infra.exportation.excel;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelSheet {

	private String name = "Sheet1";
	private List<ExcelRow> ownedRows;
	private Sheet sheet;
	
	public ExcelSheet() {
		ownedRows = new ArrayList<ExcelRow>();
	}
	
	public ExcelSheet(String name) {
		this.name = name;
		ownedRows = new ArrayList<ExcelRow>();
	}
	
	public ExcelRow createRow() {
		ExcelRow row = new ExcelRow(ownedRows.size());
		ownedRows.add(row);
		return row;
	}
	
	public void merge(int fromRow, int toRow, int fromCol, int toCol) {
		if (sheet != null)
		sheet.addMergedRegion(new CellRangeAddress(fromRow, toRow, fromCol, toCol));
	}
	
	public void serialize(Workbook workbook) {
        // Create sheet
		sheet  = workbook.createSheet(name); // Create sheet with sheet name
 
        // Write data
        for (ExcelRow row : ownedRows) {
        	row.serialize(sheet);
        }
 
        // Auto resize column width
        if (sheet.getPhysicalNumberOfRows() > 0) {
        	int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
            autosizeColumn(sheet, numberOfColumn);
        }
	}

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<ExcelRow> getOwnedRows() {
		return ownedRows;
	}

	public void setOwnedRows(List<ExcelRow> ownedRows) {
		this.ownedRows = ownedRows;
	}
	
	public void addRow(ExcelRow row) {
		this.ownedRows.add(row);
	}
}