package org.tangerine.apiresolver.util;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;

public class ItextUtil {
	
	public static Font courierFont = FontFactory.getFont(FontFactory.COURIER);
	
	public static Font chineseFont = new Font(getBaseFont());

	
	public static BaseFont getBaseFont() {
		try {
			return BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Font getChineseFont(float size, int style, Color color) {
		Font difference = chineseFont.difference(chineseFont);
		difference.setSize(size);
		difference.setStyle(style);
		difference.setColor(color);
		return difference;
	}
	
	public static Font getCourierFont(float size, int style, Color color) {
		Font difference = chineseFont.difference(courierFont);
		difference.setSize(size);
		difference.setStyle(style);
		difference.setColor(color);
		return difference;
	}
	
	public static FontSelector getFontSelector(float size, int style, Color color) {
		FontSelector selector = new FontSelector();
		selector.addFont(getCourierFont(size, style, color));
		selector.addFont(getChineseFont(size, style, color));
		return selector;
	}
	
	public static void setLocalGoto(Phrase phrase, String localGoto) {
		for (Object elmt : phrase.getChunks()) {
			((Chunk) elmt).setLocalGoto(localGoto);
		};
	}
	
	public static void setLocalDestination(Phrase phrase, String destination) {
		for (Object elmt : phrase.getChunks()) {
			((Chunk) elmt).setLocalDestination(destination);
		};
	}
}
