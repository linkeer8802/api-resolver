package org.tangerine.apiresolver.util;

public class StringUtil {

	public static int getStrCount(String source, String target) {
		int count = -1;
		int index = -1;
		int fromIndex = 0;
		do {
			index = source.indexOf(target, fromIndex);
			fromIndex = index + target.length();
			count++;
		} while (index != -1);
		
		return count;
	}
}
