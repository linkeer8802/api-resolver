/**ArrayUtil.java**/
package org.tangerine.apiresolver.util;

import java.lang.reflect.Array;

/**
 * @author weird
 *
 */
public class ArrayUtil {
	/**
	 * 通过反射将指定的 值分配给数组的每个元素。
	 * @author weird
	 * @version 1.0
	 * @param array
	 * @param value
	 */
	public static void fill(Object array, Object value) {
		if (array.getClass().isArray()) {
			for (int index = 0; index < Array.getLength(array); index++) {
				if (Array.get(array, index).getClass().isArray()) {
					fill(Array.get(array, index), value);
				} else {
					Array.set(array, index, value);
				}
			}
		}
	}
	
	public static Object newInstance(Class<?> componentType, int... dimensions) {
		return Array.newInstance(componentType, dimensions);
	}
}
