/**TypeExample.java**/
package org.tangerine.apiassistant.sample.api;

import java.util.List;

import org.tangerine.apiassistant.sample.vo.ClientApiRspVO;
import org.tangerine.apiresolver.core.mapping.ApiExportor;

/**
 * 提供了不同返回类型API例子演示功能
 * @summary 类型例子
 * @author weird
 */
public class TypeExample implements ApiExportor {
	
	/**
	 * 返回值为原始类型的API例子
	 * @summary 返回值为原始类型的API例子
	 * @author weird
	 * @version 1.0
	 * @return int原始类型值 e.g:1
	 */
	public int returnPrimitiveType() {
		return 1;
	}
	/**
	 * 返回值为数组类型的API例子
	 * @summary 返回值为数组类型的API例子
	 * @author weird
	 * @version 1.0
	 * @return 数组类型值 
	 */
	public int[] returnArrayType() {
		return null;
	}
	/**
	 * 返回值为java bean对象的例子
	 * @summary 返回java bean对象的例子
	 * @author weird
	 * @version 1.0
	 * @param name 名称; e.g:tom
	 * @return 客户端响应结果VO对象
	 */
	public ClientApiRspVO returnBeanType() {
		return null;
	}
	/**
	 * 返回值为List集合的例子
	 * @summary 返回值为List集合的例子
	 * @author weird
	 * @version 1.0
	 * @return List集合
	 */
	public List<String> returnListType() {
		return null;
	}
}
