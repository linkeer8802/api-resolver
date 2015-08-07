<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>API摘要</title>
	</head>
	<body>
		<div class="main-wrap">
			<h1 class="title">${apiCategory.name}</h1>
			<p class="title-intro">${apiCategory.desc}</p>
			<div class="sub-wrap">
					<h3 class="head-title">API摘要</h3>
					<div class="api-list">
						<#list apiSummarys as item>
							<p>
								<span class="l">
								<a title="${item.mapping}" href="api-detail-${item.id}.html" class="link">${item.mapping}</a>
								<span>${item.name}</span>
							</p>
						</#list>
					</div>
				</div>
		</div>
	</body>
</html>