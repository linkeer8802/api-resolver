<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>API列表</title>
	</head>
	<body>
		<div class="main-wrap">
			<h1>API列表</h1>
			<div class="api-list">
				<ul>
					<#list apiCategorys as item>
						<li class="api-list-item">
							<span><a class="link" href="api-summary-${item.cid}.html">${item.name}</a></span>
							<p class="api-list-intro">${item.desc}</p>
						</li>
					</#list>
				</ul>
			</div>
		</div>
	</body>
</html>