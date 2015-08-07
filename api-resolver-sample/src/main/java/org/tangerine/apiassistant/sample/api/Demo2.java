package org.tangerine.apiassistant.sample.api;

import org.springframework.stereotype.Service;
import org.tangerine.apiresolver.annotation.ApiMapping;

/**
 * 提供了简单求和重载API例子演示功能
 * @summary 求和重载API例子
 * @author weird
 */
@Service
@ApiMapping("demo2")
public class Demo2 {

    /**
     * int数组求和 API例子演示
     * @summary int数组求和
     * @author weird
     * @version 1.0
     * @param elemts 元素列表; e.g:{1,2}
     * @return 元素列表之和; e.g:100
     */
	@ApiMapping("int.sum")
	public int sum(int[] elemts) {
		int sum = 0;
		for (int i : elemts) {
			sum = sum + i;
		}
		return sum;
	}
	
    /**
     * Integer数组求和API例子演示
     * @summary Integer数组求和
     * @author weird
     * @version 1.0
     * @param elemts 元素列表; e.g:{1,2}
     * @return 元素列表之和; e.g:100
     */
	@ApiMapping("Integer.sum")
	public int sum(Integer[] elemts) {
		int sum = 0;
		for (int i : elemts) {
			sum = sum + i;
		}
		return sum;
	}
	
    /**
     * Integer二维数组求和API例子演示
     * @summary Integer二维数组求和
     * @author weird
     * @version 1.0
     * @param elemts 元素列表; e.g:{{1,2},{3,4}}
     * @return 元素列表之和; e.g:100
     */
	@ApiMapping("Integer.two.sum")
	public int sum(Integer[][] elemts) {
		int sum = 0;
		for (Integer[] i : elemts) {
			for (int j = 0; j < i.length; j++) {
				sum = sum + j;
			}
		}
		return sum;
	}
	
    /**
     * int二维数组求和 API例子演示
     * @summary int二维数组求和 
     * @author weird
     * @version 1.0
     * @param elemts 元素列表; e.g:{{1,2},{3,4}}
     * @return 元素列表之和; e.g:100
     */
	@ApiMapping("int.two.sum")
	public int sum(int[][] elemts) {
		int sum = 0;
		for (int[] i : elemts) {
			for (int j = 0; j < i.length; j++) {
				sum = sum + j;
			}
		}
		return sum;
	}
}
