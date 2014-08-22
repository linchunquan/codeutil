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
    <link rel="stylesheet" type="text/css" href="codeutil/JSLib/ext-ux/fileuploadfield/css/fileuploadfield.css">
    <link rel="stylesheet" type="text/css" href="customize/resource/css/style.css">

</head>
<body>
  <div id="loading-mask" style=""></div>
  <div id="loading">
    <div class="loading-indicator"><img src="codeutil/resource/images/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>正在加载...</div>
  </div>
  <script type="text/javascript" src="codeutil/JSLib/ext/adapter/ext/ext-base.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/ext/ext-all.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/ext/src/locale/zh-cn.js"></script>
  <script type="text/javascript">Ext.BLANK_IMAGE_URL = 'codeutil/JSLib/ext/resources/images/default/s.gif';</script>
  <script type="text/javascript">var currentUser = ${sessionScope.USER_JSON};</script>
  
  <script type="text/javascript" src="codeutil/JSLib/util/TabCloseMenu.js"></script>
  <script type="text/javascript" src="funcs/funcmenu.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/util/HashMap.js"></script>
  <script type="text/javascript" src="funcs/FunctionPanel.js"></script>
  <script type="text/javascript" src="frame.js"></script>
  <script type="text/javascript" src="funcs/functions.js"></script> 
  <script type="text/javascript" src="codeutil/JSLib/ext-ux/RowExpander/RowExpander.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/ext-ux/fileuploadfield/FileUploadField.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/ext-ux/GridCombo.js"></script>
  
  <script type="text/javascript" src="codeutil/JSLib/ext-ux/Ext.ux.layout.TableFormLayout.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/util/GenericWidgeProxy.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/util/WidgeWrapper.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/common/Validator.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/common/Columns.js"></script>
  <script type="text/javascript" src="codeutil/JSLib/common/ColumnRender.js"></script>
  <script type="text/javascript" src="customize/ExtendedValidator.js"></script>
[ImportWidgeProxyJS]
[ImportGridJS]
</body>
</html>
