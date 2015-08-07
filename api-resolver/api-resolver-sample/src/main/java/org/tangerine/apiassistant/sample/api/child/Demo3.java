package org.tangerine.apiassistant.sample.api.child;

import org.tangerine.apiresolver.annotation.Param;
import org.tangerine.apiresolver.core.mapping.ApiExportor;

public interface Demo3 extends ApiExportor {

    /**
     * sayHello3 API 例子演示
     * @author weird
     * @version 1.0
     * @param name 名称; e.g:tom
     * @return sayHello欢迎信息; e.g:hello tom
     */
	public String sayHello3(@Param(value="name") String name);
}
