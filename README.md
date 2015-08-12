# api-resolver
a useful tool for resolve api request mapping and automatic generation api doc.
##概述
api-resolver提供api统一请求入口解析和api代码同步生成API接口文档的实用工具；省去了诸如API接口版本管理、接口签名、API加密、响应结果格式处理、异常处理等实用功能，提供了API请求映射apiMapping功能（类似于springmvc的requestMapping），同时还提供了同步生成API接口文档（代码即文档）的强大功能，接口编写者不需要另外编写API接口文档，只要在编写API接口代码时写好代码注释，编译代码后即可同步生成接口文档，缩短了编写API接口时间，减少API接口使用的难度。
##如何使用
  1、在您的项目的Maven POM 文件里加入：
  
  - api-resolver-core依赖配置
  
		<dependency>
			<groupId>com.github.linkeer8802</groupId>
			<artifactId>api-resolver-core</artifactId>
			<version>${apiResolver.version}</version>
		</dependency>
		
  - 添加生成接口文档时plugin配置	
  
			<plugin>
				<groupId>com.github.linkeer8802</groupId>
				<artifactId>api-resolver-maven-plugin</artifactId>
				<version>1.0</version>
				<executions>
					<execution>
						<id>apiDocFetch</id>
						<phase>compile</phase>
						<goals>
							<goal>apiDocFetch</goal>
						</goals>
					</execution>
					<execution>
						<id>apiDocInstall</id>
						<phase>install</phase>
						<goals>
							<goal>apiDocInstall</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<subpackages>
						<subpackage>org.tangerine.apiassistant.sample.api</subpackage>
					</subpackages>
					<excludes>
						<exclude>org.tangerine.apiassistant.sample.api.child</exclude>
					</excludes>
					<debug>false</debug>
				</configuration>
			</plugin>		
		
  2、定义相应的bean

![](http://img.blog.csdn.net/20150812113553918?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

  3、调用请求解析方法

    Object result = apiResolver.resolve(request)；//输入：HttpServletRequest对象，输出：API接口返回值

结合springmvc调用例子

![](http://img.blog.csdn.net/20150812140414949?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

##功能详细说明

> 接口请求映射

接口请求映射提供了API请求映射apiMapping功能（类似于springmvc的requestMapping）,客户端调用API接口的请求参数可以智能匹配到API接口方法中的参数。

- 提供两种API请求映射方式：

  1）基于类方法名的简单映射方式

    在API接口实现类中实现org.tangerine.apiresolver.core.mapping.ApiExportor接口即可，映射的路径为：类名称.方法名称（类名称首字母小写并去除Service、Api、API、Exportor等关键字）

  2）基于注解的映射方式
    在API接口实现类和方法上添加org.tangerine.apiresolver.annotation.ApiMapping注解，映射的路径可在注解中配置。（基于注解的映射方式可配置API接口的版本信息，推荐使用）

- 接口参数类型匹配

  1、基本类型参数

    API接口方法参数名与API请求参数名一致，如：xxxx?param1=value1&param2=value2，匹配到接口方法中对应的参数，public String sayHello(String param1, String param2)

  2、java bean类型参数

    与基本类型一样，API请求地址中的参数名匹配到java bean中的字段(Field)属性中。

  3、数组集合类型参数

    在API请求地址中的参数中添加多个同名称的参数名即可，如：xxxx?param1=value1&param1=value2&param2=value3&param2=value4，匹配到接口方法中对应的参数，public String sayHello(String[] param1, List<String> param2)

  4、Map类型参数
    Map类型参数匹配格式为：参数名 + _ + map键=map值，如：xxxx?mapParm_key1=value1&mapParm_key2=value2，匹配到接口方法中对应的参数，public String sayHello(Map<String, Object> mapParm)

（推荐在API接口方法参数中使用注解的方式配置参数信息，提供更多的功能）

> 接口文档生成

  maven插件配置信息说明

    1、apiDocFetch
    - subpackages：要生成api接口文档的java包，（包括子包）
    - excludes：不需要生成api接口文档的java包
    - sourcepaths：api接口源代码目录，默认为${project.build.sourceDirectory}
    - debug：是否输出debug信息
    
    2、apiDocInstall
    - docName：文档名称
    - version：版本信息
    - showResultExample：生成文档时是否显示返回结果事例信息，默认为true
    - installDir：生成文档目录，默认为${user.home}/apiresolver/


##未来版本规划

添加签名、加密、在线API调试功能

##联系作者
QQ:362221946

Email:362221946@qq.com
