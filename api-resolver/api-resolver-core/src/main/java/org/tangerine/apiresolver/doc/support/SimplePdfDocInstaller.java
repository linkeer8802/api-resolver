package org.tangerine.apiresolver.doc.support;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tangerine.apiresolver.doc.AbstractApiDocInstaller;
import org.tangerine.apiresolver.doc.DocContainer;
import org.tangerine.apiresolver.doc.DocInstallArgs;
import org.tangerine.apiresolver.doc.entity.ApiCategory;
import org.tangerine.apiresolver.doc.entity.ApiDoc;
import org.tangerine.apiresolver.doc.entity.ApiTypeDoc;
import org.tangerine.apiresolver.doc.entity.ParamDoc;
import org.tangerine.apiresolver.doc.entity.RefTypeDoc;
import org.tangerine.apiresolver.doc.entity.ResultDoc;
import org.tangerine.apiresolver.doc.support.DefaultPdfTable.TBDirection;
import org.tangerine.apiresolver.util.FileUtil;
import org.tangerine.apiresolver.util.ItextUtil;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

public class SimplePdfDocInstaller extends AbstractApiDocInstaller {

	FontSelector titleFont = ItextUtil.getFontSelector(16, Font.BOLD, Color.BLACK);
	// 章的字体
	FontSelector chFont = ItextUtil.getFontSelector(16, Font.BOLD, Color.BLACK);
	// 节的字体
	FontSelector secFont = ItextUtil.getFontSelector(14, Font.BOLD, Color.BLACK);
	// 正文项目的字体
	FontSelector itemFont = ItextUtil.getFontSelector(10, Font.BOLD, Color.BLACK);		
	// 正文的字体
	FontSelector textFont = ItextUtil.getFontSelector(9, Font.NORMAL, Color.BLACK);
	// 正文的字体
	FontSelector textbFont = ItextUtil.getFontSelector(9, Font.BOLD, Color.BLACK);		
	// 备注的字体
	FontSelector remarkFont = ItextUtil.getFontSelector(8, Font.ITALIC, Color.BLACK);
	
	public static final String REFTYPE_LINK_PREFIX = "refType-";
	
	private Document document;
	private PdfWriter writer;
	private Map<Chapter, ApiCategory> categoryChapters;
	
	public SimplePdfDocInstaller(DocInstallArgs docInstallArgs) throws Exception {
		super(docInstallArgs);
		categoryChapters = new LinkedHashMap<Chapter, ApiCategory>();
	}
	
	@Override
	public void init() throws Exception {
		File installDir = new File(docInstallArgs.getInstallDir(), "pdf");
		FileUtil.mkdir(installDir);
		
		File pdfFile = new File(installDir, docInstallArgs.getDocName() + ".pdf");
		FileOutputStream fos = new FileOutputStream(pdfFile);
        this.document = new Document(PageSize.A4);
        this.writer = PdfWriter.getInstance(document, fos);
		//加密文档
        writer.setEncryption(null, "123456".getBytes(), PdfWriter.ALLOW_COPY, PdfWriter.STANDARD_ENCRYPTION_128);
        writer.createXmpMetadata();
        writer.setPageEvent(new PdfPageEventHelper(){
        	@Override
        	public void onEndPage(PdfWriter writer, Document document) {
        		addWatermark(writer, document);
        		addHeaderAndFooter(writer, document);
        	}
        });
        
        document.open();
        
        initBaseInfo();
	}
	/**
	 * 添加水印  
	 * @author weird
	 * @version 1.0
	 * @param writer
	 * @param document
	 */
	private void addWatermark(PdfWriter writer, Document document) {
        String content = "generate by apiresolver";
        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, 
        		ItextUtil.getFontSelector(35, Font.NORMAL, new Color(240, 240, 240)).process(content), 250, 400, 25);
	}
	
	private void addHeaderAndFooter(PdfWriter writer, Document document) {
		if (document.getPageNumber() == 1) {
			return;
		}
        String header = docInstallArgs.getDocName() + " version" + docInstallArgs.getVersion(); 
        PdfContentByte directContent = writer.getDirectContent();
		Phrase headerPhare = ItextUtil.getFontSelector(9, Font.NORMAL, Color.LIGHT_GRAY).process(header);
		headerPhare.add(new LineSeparator(0.5f, 2.5f, Color.LIGHT_GRAY, Element.ALIGN_LEFT, -5f));
		ColumnText.showTextAligned(directContent, Element.ALIGN_LEFT, 
        			headerPhare, document.left(), document.top() + 20, 0);
        
        String footer = Integer.valueOf(document.getPageNumber()).toString();
		ColumnText.showTextAligned(directContent, Element.ALIGN_LEFT, 
				textbFont.process(footer), document.getPageSize().getWidth() / 2.0f - 20, document.bottom() - 20, 0);
	}
	
	@Override
	public void destroy() throws Exception {
		document.close();
	}
	
	public static BaseFont getBaseFont() {
		try {
			return BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void initBaseInfo() throws DocumentException {
		//文档标题
        Paragraph title = new Paragraph(titleFont.process(docInstallArgs.getDocName() + "\n"));
        title.setAlignment("Center");
        Paragraph datetime = new Paragraph(itemFont.process(new SimpleDateFormat("\nyyyy-MM-dd").format(new Date())));
        datetime.setAlignment("Center");
        Paragraph ver = new Paragraph(itemFont.process("Version 1.0"));
        ver.setAlignment("Center");
        document.add(title);
        document.add(datetime);
        document.add(ver);
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installCategorys(java.util.List)
	 */
	@Override
	public void installCategorys(List<ApiCategory> apiCategorys) throws Exception {
        int ch = 1;
        for (ApiCategory apiCategory : apiCategorys) {
        	Paragraph paApiCategory = new Paragraph(chFont.process(apiCategory.getName() + "\n"));
        	Chapter chapter = new Chapter(paApiCategory, ch++);
        	Paragraph paragraph = new Paragraph(textFont.process(apiCategory.getDesc()));
        	paragraph.setSpacingAfter(10f);
        	paragraph.setIndentationLeft(8f);
			chapter.add(paragraph);
        	
        	categoryChapters.put(chapter, apiCategory);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installApiDocs(java.util.List)
	 */
	@Override
	public void installApiDocs(List<ApiDoc> apiDocs) throws Exception {
		for (Chapter chapter : categoryChapters.keySet()) {
			ApiCategory apiCategory = categoryChapters.get(chapter);
			for (ApiDoc apiDoc : DocContainer.get().getApiDocQuery().queryByCategory(apiCategory.getCid())) {
				Section section = chapter.addSection(new Paragraph(secFont.process(apiDoc.getName())));
				section.setIndentation(10);
				section.setIndentationLeft(10);
				section.setBookmarkOpen(false);
				section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
				//desc
				section.add(new Paragraph(textFont.process(apiDoc.getDesc())));
				//summary
				addSummary(section, apiDoc);
				//parameters
				addParameters(section, apiDoc);
				//Result
				addResult(section, apiDoc);
				//ResultExample
				addResultExample(section, apiDoc);
			}
			document.add(chapter);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.tangerine.apiresolver.doc.support.ApiDocInstall#installApiTypeDocs(java.util.List)
	 */
	@Override
	public void installApiTypeDocs(List<ApiTypeDoc> apiTypeDocs) throws Exception {
    	Paragraph paRefType = new Paragraph(chFont.process("引用类型参考\n"));
    	Chapter chapter = new Chapter(paRefType, categoryChapters.size()+1);
    	
    	for (ApiTypeDoc apiTypeDoc : apiTypeDocs) {
    		Phrase chunk = secFont.process(apiTypeDoc.getName());
    		ItextUtil.setLocalDestination(chunk, REFTYPE_LINK_PREFIX + apiTypeDoc.getName());
			Section section = chapter.addSection(new Paragraph(chunk));
			section.setIndentation(10);
			section.setIndentationLeft(10);
			section.setBookmarkOpen(false);
			section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);
			//desc
			section.add(new Paragraph(textFont.process(apiTypeDoc.getDesc())));
			//属性
			addTypeAttrs(section, apiTypeDoc.getAttrs());
		}
    	
    	document.add(chapter);
	}
	
	private void addTypeAttrs(Section section, List<ResultDoc> attrs) throws Exception {
		
	      Paragraph paAttrs = new Paragraph(itemFont.process("属性列表"));
	      paAttrs.setSpacingBefore(6f);
	      paAttrs.setSpacingAfter(8f);
	      
	      List<String[]> rowDatas = new ArrayList<String[]>();
	      for (ResultDoc resultDoc : attrs) {
	    	  rowDatas.add(new String[]{resultDoc.getName(), null, 
	    			  		resultDoc.getExampleValue(), resultDoc.getDesc()});
	      }
	      
	      PdfPTable attrsTable = DefaultPdfTable.get().create(TBDirection.Horizontal, rowDatas.size()+1,
				        		new String[]{"名称", "类型", "示例值", "描述"}, 
				        		new int[]{20, 40, 20, 40});
	      DefaultPdfTable.get().setDataAlignments(new int[]{Element.ALIGN_CENTER, Element.ALIGN_CENTER,
	      													Element.ALIGN_CENTER, Element.ALIGN_LEFT});
	      DefaultPdfTable.get().setRowDatas(rowDatas);
	      int ridx = 1;
	      for (ResultDoc resultDoc : attrs) {
		      //type
	    	  Phrase phraseType = DefaultPdfTable.tableBSelector.process(resultDoc.getType());
	    	  if (!resultDoc.getIsSimpleType()) {
	    		  ItextUtil.setLocalGoto(phraseType, REFTYPE_LINK_PREFIX + resultDoc.getType());
	    	  }
	    	  DefaultPdfTable.get().getCell(attrsTable, ridx++, 1).setPhrase(new Phrase(phraseType));
	      }
	      
	      Paragraph wrapperTable = new Paragraph();
//	      wrapperTable.setIndentationLeft(12f);
	      wrapperTable.add(attrsTable);
	     section.add(paAttrs);
	     section.add(wrapperTable);
	}

	private void addSummary(Section section, ApiDoc apiDoc) throws Exception {
		
		Paragraph summary = new Paragraph(itemFont.process("★简要信息"));
		summary.setSpacingBefore(6f);
		summary.setSpacingAfter(8f);
      
		List<String[]> rowDatas = new ArrayList<String[]>();
		rowDatas.add(new String[]{apiDoc.getMapping()});
		rowDatas.add(new String[]{apiDoc.getAuthor()});
		rowDatas.add(new String[]{apiDoc.getVersion()});
		PdfPTable table = DefaultPdfTable.get().create(TBDirection.Vertical, rowDatas.size(), 
							new String[]{"映射地址", "作者", "版本"}, new int[]{1, 10});
		DefaultPdfTable.get().setDataAlignments(new int[]{Element.ALIGN_LEFT});
		DefaultPdfTable.get().setRowDatas(rowDatas);
		Paragraph wrapperTable = new Paragraph();
		wrapperTable.setIndentationLeft(12f);
		wrapperTable.add(table);
		section.add(summary);
		section.add(wrapperTable);
	}
	
	private void addParameters(Section section, ApiDoc apiDoc) throws Exception {
		
      Paragraph inputParams = new Paragraph(itemFont.process("★输入参数"));
      inputParams.setSpacingBefore(6f);
      inputParams.setSpacingAfter(8f);
      
      List<String[]> rowDatas = new ArrayList<String[]>();
      for (ParamDoc paramDoc : apiDoc.getParams()) {
    	  String required = paramDoc.getRequired() ? "必须" : "可选";
    	  rowDatas.add(new String[]{paramDoc.getName(), paramDoc.getType(), 
    			  required, paramDoc.getExampleValue(), paramDoc.getDefaultValue(), paramDoc.getDesc()});
      }
      
      PdfPTable paramTable = DefaultPdfTable.get().create(TBDirection.Horizontal, rowDatas.size()+1,
			        		new String[]{"名称", "类型", "是否必须", "示例值", "默认值", "描述"}, 
			        		new int[]{15, 10, 10, 15, 10, 40});
      DefaultPdfTable.get().setDataAlignments(new int[]{Element.ALIGN_CENTER, Element.ALIGN_CENTER,
      					Element.ALIGN_CENTER, Element.ALIGN_CENTER, Element.ALIGN_CENTER, Element.ALIGN_LEFT});
      DefaultPdfTable.get().setRowDatas(rowDatas);
      Paragraph wrapperTable = new Paragraph();
      wrapperTable.setIndentationLeft(12f);
      wrapperTable.add(paramTable);
      section.add(inputParams);
      section.add(wrapperTable);
	}
	
	private void addResult(Section section, ApiDoc apiDoc) throws Exception {
		
	      Paragraph result = new Paragraph(itemFont.process("★返回结果"));
	      result.setSpacingBefore(6f);
	      result.setSpacingAfter(8f);
	      
	      List<String[]> rowDatas = new ArrayList<String[]>();
	      if (apiDoc.getResultDoc() != null) {
	    	  rowDatas.add(new String[]{null, null, apiDoc.getResultDoc().getExampleValue(), apiDoc.getResultDoc().getDesc()});
	      }
	      
	      PdfPTable resultTable = DefaultPdfTable.get().create(TBDirection.Horizontal, rowDatas.size()+1,
				        		new String[]{"类型", "引用类型", "示例值", "描述"}, 
				        		new int[]{15, 30, 15, 40});
	      DefaultPdfTable.get().setDataAlignments(new int[]{Element.ALIGN_CENTER, Element.ALIGN_CENTER,
	      													Element.ALIGN_CENTER, Element.ALIGN_LEFT});
	      DefaultPdfTable.get().setRowDatas(rowDatas);
	      
	      if (apiDoc.getResultDoc() != null) {
	    	  //type
	    	  Phrase phraseType = DefaultPdfTable.tableBSelector.process(apiDoc.getResultDoc().getType());
	    	  if (!apiDoc.getResultDoc().getIsSimpleType()) {
	    		  ItextUtil.setLocalGoto(phraseType, REFTYPE_LINK_PREFIX + apiDoc.getResultDoc().getType());
	    	  }
	    	  DefaultPdfTable.get().getCell(resultTable, 1, 0).setPhrase(phraseType);
	    	  //refType
	    	  Phrase refTypePhrase = new Phrase();
	    	  for (RefTypeDoc refTypeDoc : apiDoc.getResultDoc().getRefTypes()) {
	    		  Phrase chunk = DefaultPdfTable.tableBSelector.process(refTypeDoc.getName() + " ");
	    		  if (!refTypeDoc.getIsSimpleType()) {
	    			  ItextUtil.setLocalGoto(chunk, REFTYPE_LINK_PREFIX + refTypeDoc.getName());
	    		  }
	    		  refTypePhrase.add(chunk);
	    	  }
	    	  DefaultPdfTable.get().getCell(resultTable, 1, 1).setPhrase(refTypePhrase);
	      }
	      
	      Paragraph wrapperTable = new Paragraph();
	      wrapperTable.setIndentationLeft(12f);
	      wrapperTable.add(resultTable);
	      section.add(result);
	      section.add(wrapperTable);
	}
	
	private void addResultExample(Section section, ApiDoc apiDoc) throws Exception {
		
	      Paragraph result = new Paragraph(itemFont.process("★返回示例"));
//	      result.setLeading(6f);
	      result.setSpacingBefore(6f);
	      result.setSpacingAfter(8f);
	      
	      PdfPTable table = new PdfPTable(1);
	      table.setWidthPercentage(95f);
	      table.setHorizontalAlignment(Element.ALIGN_LEFT);
	      PdfPCell pdfPCell = table.getDefaultCell();
	      pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	      pdfPCell.setBackgroundColor(Color.LIGHT_GRAY);
	      if (apiDoc.getResultDoc() != null && apiDoc.getResultExample() != null) {
	    	  pdfPCell.setPhrase(DefaultPdfTable.tableBSelector.process(apiDoc.getResultExample()));
	      }
	      table.addCell(pdfPCell);
	      Paragraph wrapperTable = new Paragraph();
	      wrapperTable.setSpacingAfter(8f);
	      wrapperTable.setIndentationLeft(12f);
	      wrapperTable.add(table);
	      section.add(result);
	      section.add(wrapperTable);
	}
}
