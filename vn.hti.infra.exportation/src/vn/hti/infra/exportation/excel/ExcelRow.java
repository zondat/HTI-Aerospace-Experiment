package vn.hti.infra.exportation.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelRow {
	
	private int index = 0;
	private List<ExcelCell> ownedCells;
	private Row row;
	
	public ExcelRow() {
		ownedCells = new ArrayList<ExcelCell>();
	}
	
	public ExcelRow(int index) {
		this.index = index;
		ownedCells = new ArrayList<ExcelCell>();
	}
	
	public ExcelCell createCell(String content) {
		ExcelCell cell = new ExcelCell(ownedCells.size(), content);
		ownedCells.add(cell);
		return cell;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<ExcelCell> getOwnedCells() {
		return ownedCells;
	}

	public void setOwnedCells(List<ExcelCell> ownedCells) {
		this.ownedCells = ownedCells;
	}

	public void addCell(ExcelCell cell) {
		ownedCells.add(cell);
	}

	public void serialize(Sheet sheet) {
		row = sheet.createRow(index);
		
		for (ExcelCell cell : ownedCells) {
			cell.serialize(row);
		}
	}
	
}