<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>API详细</title>
		<style type="text/css">
			.optional {font-style: italic;}
			.required {font-size: 0.9em; font-weight: bold;}
			tr .middle {text-align: center;}
			tr .left {text-align: left;}
			tr .right {text-align: right;padding: 5px 10px;}
			#content-api-desc ul>li span {display: block;float: left;}
		</style>
	</head>
	<body>
		<div class="main-wrap">
			<div class="title-wrap">
				<h1 class="open-bg title">${apiDoc.mapping}&nbsp;&nbsp;${apiDoc.name}</h1>
			</div>
			<div class="content-wrap">
				<p class="title-intro">${apiDoc.desc}</p>
				<div class="content-block">
					<div id="content-api-desc">
						<h2 class="head-title">简要信息</h2>
						<table cellspacing="0" style="width:350px;">
							<tr>
								<th class="right" width="30%">映射地址</th>
								<td class="left" width="70%">${apiDoc.mapping}</td>
							</tr>
							<tr>
								<th class="right" width="30%">作者</th>
								<td class="left" width="70%">${apiDoc.author!'---'}</td>
							</tr>
							<tr>
								<th class="right" width="30%">版本</th>
								<td class="left" width="70%">${apiDoc.version!'---'}</td>
							</tr>							
						</table>
					</div>
					<div id="content-api-param">
						<h2 class="head-title">输入参数</h2>
						<table cellspacing="0" class="api-table" style="width:1000px;">
							<thead>
								<tr>	
									<th width="15%" class="middle">名称</th>
									<th width="10%" class="middle">类型</th>
									<th width="10%" class="middle">是否必须</th>
									<th width="15%" class="middle">示例值</th>
									<th width="10%" class="left">默认值</th>
									<th width="40%" class="left">描述</th>
								</tr>
							</thead>
							<tbody>
								<#list apiDoc.params as param>
									<tr>
										<td class="middle">${param.name}</td>
										<td class="middle">${param.type}</td>
										<#if param.required?exists>
											<td class="middle${param.required?string(' required',' optional')}">${param.required?string("必须","可选")}</td>
										<#else>
											<td class="left">---</td>
										</#if>
										<td class="middle">${param.exampleValue!'---'}</td>
										<td class="left">${param.defaultValue!'---'}</td>
										<td class="left">${param.desc}</td>
									</tr>
								</#list>
							</tbody>
						</table>
					</div>	
					<div id="content-api-result">
						<h2 class="head-title">返回结果</h2>
						<table cellspacing="0" class="api-table" style="width:1000px;">
							<thead>
								<tr>	
									<th width="15%" class="middle">类型</th>
									<th width="30%" class="middle">引用类型</th>
									<th width="15%" class="left">示例值</th>
									<th width="40%" class="left">描述</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<#if apiDoc.resultDoc.isSimpleType>
										<td class="middle">${apiDoc.resultDoc.type}</td>
									<#else>	
										<td class="middle"><a href="api-type-${apiDoc.resultDoc.type}.html">${apiDoc.resultDoc.type}</a></td>
									</#if>
									<#if apiDoc.resultDoc.refTypes?size == 0 >
										<td class="middle">---</td>	
										<td class="left">${apiDoc.resultDoc.exampleValue}</td>
									<#else>
										<td class="middle">
											<#list apiDoc.resultDoc.refTypes as refType>
												<#if !refType.isSimpleType>
													<a href="api-type-${refType.name}.html">${refType.name}</a>
												<#else>
													${refType.name}	
												</#if>
											</#list>
										</td>
										<td class="left">---</td>
									</#if>
									<td class="left">${apiDoc.resultDoc.desc}</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="content-api-result-example" style="width:1000px;">
						<h2 class="head-title"> 返回示例</h2>
						<#if apiDoc.resultExample?? >
							<div><pre style="background:#eee;padding: 15px;border: 1px solid #ccc;">${apiDoc.resultExample!''}</pre></div>
						</#if>
					</div>													
				</div>
			</div>
		</div>
	</body>
</html>