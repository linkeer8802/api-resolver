package org.tangerine.apiresolver.doc.support;

import java.awt.Color;
import java.util.List;

import org.tangerine.apiresolver.util.ItextUtil;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;

public class DefaultPdfTable {
	
	public static final FontSelector tableHSelector = ItextUtil.getFontSelector(9, Font.BOLD, Color.BLACK);
	public static final FontSelector tableBSelector = ItextUtil.getFontSelector(9, Font.NORMAL, Color.BLACK);
//	public static final Font tableHFont = new Font(baseFont, 9, Font.BOLD, Color.BLACK);
//	public static final Font tableBFont = new Font(baseFont, 9, Font.NORMAL, Color.BLACK);

	private final static DefaultPdfTable instance = new DefaultPdfTable();
	
	private PdfPTable table;
	
	private TBDirection hType;
	
	private String[] headers;
	
	private int[] relativeWidths;
	
	private int[] dataAlignments;
	
	private List<String[]> gridDtas;
	
	public enum TBDirection {Horizontal,Vertical};
	
	private DefaultPdfTable() {
	}
	
	public static DefaultPdfTable get() {
		return instance;
	}
	
	public PdfPTable create(TBDirection direction, int rows, String[] headers, int[] relativeWidths) throws Exception {
		
		this.hType = direction;
		initTable(rows, relativeWidths);
		
		setHeaders(headers);
		setRelativeWidths(relativeWidths);
		
		return table;
	}

	private void initTable(int rows, int[] relativeWidths) throws DocumentException {
		this.table = new PdfPTable(relativeWidths.length);
		table.setWidths(relativeWidths); //设置每列宽度比例  
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(95f);
		
		for (int i = 0; i < relativeWidths.length; i++) {
			for (int j = 0; j < rows; j++) {
				table.addCell(table.getDefaultCell());
			}
		}
	}
	
	public void setRelativeWidths(int[] relativeWidths) throws DocumentException {
		this.relativeWidths = relativeWidths;
		table.setWidths(relativeWidths); //设置每列宽度比例  
	}
	
	public void setHeaders(String[] headers) {
		this.headers = headers;
		
		if (hType.equals(TBDirection.Horizontal)) {
			int cidx = 0;
			for (String header : headers) {
				setHeaderCell(header, 0, cidx++);
			}
		} else if (hType.equals(TBDirection.Vertical)) {
			int ridx = 0;
			for (String header : headers) {
				setHeaderCell(header, ridx++, 0);
			}
		}
	}
	
	private void setHeaderCell(String text, int ridx, int cidx) {
		PdfPCell headerCell = table.getRow(ridx++).getCells()[cidx];
		if (hType.equals(TBDirection.Horizontal)) {
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		} else if (hType.equals(TBDirection.Vertical)) {
			headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		}
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerCell.setBackgroundColor(Color.GRAY);
		headerCell.setPhrase(tableHSelector.process(text));
	}
	
	public void setRowDatas(List<String[]> gridDtas) {
		this.gridDtas = gridDtas;
		
		if (hType.equals(TBDirection.Horizontal)) {
			int ridx = 1;
			for (String[] row : gridDtas) {
				int cidx = 0;
				for (String data : row) {
					setBodyCell(data, ridx, cidx++);
				}
				ridx++;
			}
		} else if (hType.equals(TBDirection.Vertical)) {
			int ridx = 0;
			for (String[] row : gridDtas) {
				int cidx = 1;
				for (String data : row) {
					setBodyCell(data, ridx, cidx++);
				}
				ridx++;
			}
		}
	}
	
	private void setBodyCell(String text, int ridx, int cidx) {
		PdfPCell bodyCell = table.getRow(ridx).getCells()[cidx];
		bodyCell.setBackgroundColor(Color.LIGHT_GRAY);
		if (text != null) {
			bodyCell.setPhrase(tableBSelector.process(text));
		}
	}
	
	public void setDataAlignments(int[] dataAlignments) {
		this.dataAlignments = dataAlignments;
		
		if (hType.equals(TBDirection.Horizontal)) {
			
			for (int ridx = 1; ridx < table.getRows().size(); ridx++) {
				int cidx = 0;
				for (int alignment : dataAlignments) {
					table.getRow(ridx).getCells()[cidx++].setHorizontalAlignment(alignment);
				}
			}
		} else if (hType.equals(TBDirection.Vertical)) {
			for (int ridx = 0; ridx < table.getRows().size(); ridx++) {
				int cidx = 1;
				for (int alignment : dataAlignments) {
					table.getRow(ridx).getCells()[cidx++].setHorizontalAlignment(alignment);
				}
			}
		}
	}
	
	public PdfPCell getCell(PdfPTable table, int row, int col) {
		return table.getRow(row).getCells()[col];
	}
	
	public PdfPCell[] getColumns(PdfPTable table, int col) {
		PdfPCell[] columns = new PdfPCell[table.getRows().size()];
		for (int rid=0; rid < table.getRows().size(); rid++) {
			columns[rid] = ((PdfPRow)table.getRows().get(rid)).getCells()[col];
		}
		return columns;
	}
}
