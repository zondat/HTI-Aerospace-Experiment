package vn.hti.infra.exportation.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelCell {
	
	private int column;
	private String content;
	private Cell cell;
	
	public ExcelCell() {
	}
	
	public ExcelCell(int col, String content) {
		this.column = col;
		this.content = content;
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void serialize(Row row) {
		cell = row.createCell(column);
		cell.setCellValue(content);
	}
}