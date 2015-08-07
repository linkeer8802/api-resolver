package org.tangerine.apiassistant.sample.api.child.sub;

import org.tangerine.apiresolver.annotation.ApiMapping;
import org.tangerine.apiresolver.annotation.Param;

@ApiMapping("demo31")
public interface Demo31 {

    /**
     * sayHello31 API 例子演示
     * @author weird
     * @version 1.0
     * @param name 名称; e.g:tom
     * @return sayHello欢迎信息; e.g:hello tom
     */
	@ApiMapping("sayHello31")
	public String sayHello31(@Param(value="name") String name);
}
