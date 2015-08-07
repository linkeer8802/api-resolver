package org.tangerine.apiassistant.sample.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.tangerine.apiassistant.sample.vo.ClientApiRspVO;
import org.tangerine.apiassistant.sample.vo.ParamBean;
import org.tangerine.apiassistant.sample.vo.ParamBeanWithAnnotation;
import org.tangerine.apiassistant.sample.vo.ReturnBean;
import org.tangerine.apiresolver.annotation.Param;
import org.tangerine.apiresolver.core.mapping.ApiExportor;

/**
 * 提供了简单API例子演示功能
 * @summary 简单API Demo
 * @author weird
 */
@Service
public class Demo implements ApiExportor {

	List<String> ls;
    /**
     * sayHello API 例子演示
     * @summary sayHello API 例子
     * @author weird
     * @version 1.0
     * @param name 名称; e.g:tom
     * @return sayHello欢迎信息; e.g:hello tom
     */
	public String sayHello(@Param(value="name") String name) {
		
		return "hello " + name;
	}
	/**
	 * sayHello API变种例子演示
	 * @summary sayHello API变种例子
	 * @author weird
	 * @version 1.0
	 * @param name 名称; e.g:tom
	 * @param optionalParam 可选字符串参数
	 * @param defaultParam 带默认值的参数
	 * @return sayHello欢迎信息和回显参数信息; e.g:hello tom
	 */
	public String sayHello2(@Param(value="name") String name,
			@Param(value="optionalParam", required=false) String optionalParam,
			@Param(value="defaultParam", defaultValue="i am default param") String defaultParam) {

		return "hello " + name + ";[optionalParam:" + optionalParam + ",defaultParam:" + defaultParam + "]";
	}
	/**
	 * api参数为java bean对象的例子演示
	 * @summary api参数为java bean对象的例子
	 * @author weird
	 * @version 1.0
	 * @param paramBean java bean对象参数
	 * @return 参数为java bean对象的字符串形式
	 */
	public String testParamBean(ParamBean paramBean) {
		return paramBean.toString();
	}
	/**
	 * api参数为java bean对象和普通参数混合的例子演示
	 * @summary api参数为java bean对象和普通参数混合的例子
	 * @author weird
	 * @version 1.0
	 * @param name 名称; e.g:tom
	 * @param paramBean java bean对象参数
	 * @return 参数为java bean对象的字符串形式
	 */
	public String testParamBeanWithAnnotation(@Param(value="name") String name, ParamBeanWithAnnotation paramBean) {
		return "hello " + name + ";paramBean:" + paramBean;
	}
	/**
	 * api参数为map对象的例子演示
	 * @summary api参数为map对象的例子
	 * @author weird
	 * @version 1.0
	 * @param map1 map对象参数1
	 * @param map2 map对象参数2
	 * @return 参数为map对象的字符串形式
	 */
	public String testParamMap(@Param("map1") Map<String, Object> map1, Map<String, Object> map2) {
		return "map1:" + map1.toString() + ";map2:" + map2;
	}
	/**
	 * 返回java bean对象的例子演示
	 * @summary 返回java bean对象的例子
	 * @author weird
	 * @version 1.0
	 * @param name 名称; e.g:tom
	 * @return 客户端响应结果VO对象
	 */
	public ClientApiRspVO testResultOfBean(@Param(value="name") String name) {
		return new ClientApiRspVO("hello " + name);
	}
	
    /**
     * 供需市场最新列表信息
     * @summary 获得供需市场最新列表信息
     * @author weird
     * @version 1.0
     * @param sdType 供需类型 e.g:all
     * @param categoryCode 分类编码 e.g:11
     * @param areaCode	区域编码 e.g: 4001
     * @param page 当前页 e.g: 1
     * @param size 每页显示的记录数 e.g:10
     * @return 供需市场列表信息
     */
	public List<ReturnBean> getSupplyDemands(
									@Param(value="type", required=false) String sdType,
									@Param(value="categoryCode", required=false) String categoryCode, 
									@Param(value="area", required=false) String areaCode, 
									@Param(value="page", defaultValue="1") Integer page, 
									@Param(value="size", defaultValue="10") Integer size) {
		
		return null;
	}
}
