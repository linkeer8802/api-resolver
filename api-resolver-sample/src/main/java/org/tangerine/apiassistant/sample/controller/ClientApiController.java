package org.tangerine.apiassistant.sample.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tangerine.apiassistant.sample.vo.ClientApiRspVO;
import org.tangerine.apiresolver.core.ApiResolver;
import org.tangerine.apiresolver.exception.NoSuchAPIException;
import org.tangerine.apiresolver.exception.ParameterMissingException;

/**
 * 求应客户端新版接口请求入口
 * @author weird
 *
 */
@Controller
public class ClientApiController {

	@Resource
	private ApiResolver apiResolver;
	
	/**
	 * 接口请求入口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/i", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Object clientApi(HttpServletRequest request, HttpServletResponse response) {
		
		ClientApiRspVO resp = invoke(request);
		request.setAttribute("result", resp);
		
		return resp;
	}

	private ClientApiRspVO invoke(HttpServletRequest request) {
		ClientApiRspVO resp = new ClientApiRspVO();
		try {
			
			Object result = apiResolver.resolve(
								request.getParameter("function"), 
								getParameters(request));
			
			if (result instanceof ClientApiRspVO) {
				resp = (ClientApiRspVO) result;
			} else {
				resp = new ClientApiRspVO(result);
			}
			
		} catch (ParameterMissingException e) {
			resp.respFailure("400", e.getMessage());
			
		} catch (NoSuchAPIException e) {
			resp.respFailure("404", e.getMessage());
			
		} catch (Exception e) {
			resp.respFailure("接口调用失败。");
		}
		
		return resp;
	}

//	private static HashMap<String, Object> clientRspVo2Map(ClientRspVO result) throws IllegalAccessException,
//			InvocationTargetException {
//		HashMap<String, Object> data = new HashMap<String, Object>();
//		for (Field field : result.getClass().getDeclaredFields()) {
//			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(result.getClass(), field.getName());
//			data.put(propertyDescriptor.getName(), 
//					propertyDescriptor.getReadMethod().invoke(result));
//		}
//		
//		return data;
//	}
	
	private Map<String, String[]> getParameters(HttpServletRequest request) {
		
		Map<String, String[]> parameterMap = new HashMap<String, String[]>(request.getParameterMap());
		Enumeration<String> attributeNames = request.getAttributeNames();
		while(attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
			parameterMap.put(name, new String[]{request.getAttribute(name).toString()});
		}
		
		return parameterMap;
	}
	
//	/**
//	 * 接口参数说明
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping(value="/info", method = {RequestMethod.GET})
//	public String info(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		ApiMetaResolver apiMetaResolver = new ApiMetaResolver(
//						request.getParameter("function"), apiResolver);
//		model.addAttribute("apiParameterMetas", apiMetaResolver.getApiSimpleParameterMetas());
//		
//		return "api/info";
//	}
//	/**
//	 * 接口样例
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping(value="/example", method = {RequestMethod.GET})
//	public String example(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		ApiMetaResolver apiMetaResolver = new ApiMetaResolver(
//						request.getParameter("function"), apiResolver);
//		model.addAttribute("apiParameterMetas", apiMetaResolver.getApiSimpleParameterMetas());
//		
//		ClientApiRspVO resp = invoke(request);
//		model.addAttribute("resp", JsonUtil.toHtmlPrettyJson(resp));
//		
//		return "api/example";
//	}
//	/**
//	 * 接口调试
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception 
//	 */
//	@RequestMapping(value="/test", method = {RequestMethod.GET})
//	public String test(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		ApiMetaResolver apiMetaResolver = new ApiMetaResolver(
//						request.getParameter("function"), apiResolver);
//		model.addAttribute("apiParameterMetas", apiMetaResolver.getApiSimpleParameterMetas());
//		
//		return "api/test";
//	}
}
