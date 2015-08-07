<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${docInstallArgs.docName}引用类型</title>
		<style type="text/css">
			tr .middle {text-align: center;}
			tr .left {text-align: left;}
		</style>
	</head>
	<body>
		<div class="main-wrap">
			<div class="title-wrap">
				<h1 class="open-bg title">${apiTypeDoc.name}</h1>
			</div>
			<div class="content-wrap">
				<p class="title-intro">${apiTypeDoc.desc}</p>
				<div class="content-block">
					<div id="content-api-param">
						<h2 class="head-title">属性</h2>
						<table cellspacing="0" class="api-table" style="width:600px;">
							<thead>
								<tr>	
									<th width="20%" class="middle">名称</th>
									<th width="20%" class="middle">类型</th>
									<th width="20%" class="left">示例值</th>
									<th width="40%" class="left">描述</th>
								</tr>
							</thead>
							<tbody>
								<#list apiTypeDoc.attrs as attr>
									<tr>
										<td class="middle">${attr.name}</td>
										<#if attr.isSimpleType>
											<td class="middle">${attr.type}</td>
										<#else>
											<td class="middle"><a href="api-type-${attr.type}.html">${attr.type}</a></td>
										</#if>
										<td class="left">${attr.exampleValue}</td>
										<td class="left">${attr.desc}</td>
									</tr>
								</#list>
							</tbody>
						</table>
					</div>	
				</div>
			</div>
		</div>
	</body>
</html>