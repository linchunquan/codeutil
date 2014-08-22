<%@ page contentType="text/html;charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
    <title>世纪风行综合业务管理系统</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="codeutil/JSLib/ext/resources/css/ext-all3.css" />
    <!-- 
    <link rel="stylesheet" type="text/css" href="codeutil/JSLib/ext/resources/css/ext-patch.css" />
     -->
    <link rel="stylesheet" type="text/css" href="codeutil/resource/css/style.css">
    <link rel="stylesheet" type="text/css" href="codeutil/resource/css/ext-more.css">
    
    <script type="text/javascript" src="codeutil/JSLib/ext/adapter/ext/ext-base.js"></script>
    
  <script type="text/javascript" src="codeutil/JSLib/ext/ext-all.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/ext/src/locale/zh-cn.js"></script>
  <script type="text/javascript">Ext.BLANK_IMAGE_URL = 'codeutil/JSLib/ext/resources/images/default/s.gif';</script>
  
  
  <script type="text/javascript" src="grids/CapacityGrid.js"></script>
  <script type="text/javascript" src="grids/CityGrid.js"></script>
  <script type="text/javascript" src="grids/CostFactoryToTransPointGrid.js"></script>
  <script type="text/javascript" src="grids/CostTransPointToCountyGrid.js"></script>
  <script type="text/javascript" src="grids/DropsizeGrid.js"></script>
  <script type="text/javascript" src="grids/FactoryGrid.js"></script>
  <script type="text/javascript" src="grids/PCHBWGrid.js"></script>
  <script type="text/javascript" src="grids/PCMZGrid.js"></script>
  <script type="text/javascript" src="grids/PCRBWGrid.js"></script>
  <script type="text/javascript" src="grids/ProductGrid.js"></script>
  <script type="text/javascript" src="grids/SKUGrid.js"></script>
  <script type="text/javascript" src="grids/SailForecastGrid.js"></script>
  <script type="text/javascript" src="grids/SailHistoryForDemandGrid.js"></script>
  <script type="text/javascript" src="grids/SailHistoryForTransCostGrid.js"></script>
  <script type="text/javascript" src="grids/TransPointGrid.js"></script>
  
</head>
<body>
   <script type="text/javascript">
  	Ext.onReady(function(){
		Ext.QuickTips.init();
		var grid = new CapacityGrid();
	    var viewport = new Ext.Viewport({
	        layout:'fit',
	        items:[grid]
	       // items:[simple]
	    });
		viewport.doLayout();
	});
  </script>
  <div id="form"></div>
</body>
</html>
