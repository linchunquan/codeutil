<%@ page contentType="text/html;charset=UTF-8"%>
<HTML><HEAD><META http-equiv="Content-Type" content="text/html;charset=UTF-8">
		
		<TITLE>系统欢迎页面</TITLE>
		<LINK href="codeutil/resource/css/LoginStatus.css" rel="stylesheet">
		<STYLE type="text/css">
			body {
				background-color: #E0E0E0;
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
				font:italic 11px "宋体",Arial,Times;
			}
			.bak {
				background-attachment: fixed;
				background-image: url(img/MainFrame/wel_02.jpg);
				background-repeat: repeat-x;
				background-position: bottom;
				font:italic 11px "宋体",Arial,Times;
			}
		</STYLE>
	</HEAD><BODY>
	<table style="background-color:#ffffff" width="100%" height="10%">
		<tr><td></td></tr>
	</table>
		<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" >
			<TBODY><TR>
				<TD width=100>
					<TABLE border="0" cellspacing="0" cellpadding="0">
						<TBODY>
						<TR>
							<TD colspan="3" height='70' style="background-image:url(codeutil/resource/images/welcome/wel_03.jpg)"/>
						</TR>
						<TR height=100>
							<TD background="codeutil/resource/images/welcome/wel_05.jpg" width="102" height="257">
							</TD>
							<TD width="411" background="codeutil/resource/images/welcome/wel_06.jpg">
								<TABLE width="100%" border="0" cellpadding="3" cellspacing="0">
									<TBODY><TR>
										<TD>
											<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
												<TBODY><TR>
													<TD width="48%" valign="top">
														<TABLE width="100%" border="0" cellspacing="6" cellpadding="0" style="color:#666;font:10pt arial;">
															<TBODY><TR>
																<TD class="w12">欢迎您：</TD>
															</TR>

															<TR>
																<TD width="44%" class="w12">
																	&nbsp;&nbsp;&nbsp;&nbsp;
																	<FONT color="blue"><B>${sessionScope.USER_KEY.userName}(${sessionScope.USER_KEY.roles})
																	</B> </FONT>
																</TD>

															</TR>
															<TR>
																<TD class="w12">登录名称：${sessionScope.USER_KEY.userName}</TD>
															</TR>
															<TR>
																<TD class="w12">所属部门：${sessionScope.USER_KEY.department}</TD>
															</TR>
															<TR>
																<TD class="w12">角色名称：${sessionScope.USER_KEY.roles}</TD>
															</TR>
															<TR>
																<TD class="w12">服务器IP：<%=request.getLocalAddr()%></TD>
															</TR>
															<TR>
																<TD class="w12">服务端口：<%=request.getLocalPort()%></TD>
															</TR>
															<TR>
																<TD class="w12">客户端IP：<%=request.getRemoteAddr()%></TD>
															</TR>
															<!-- 
															<TR>
																<TD class="w12">浏览器：<%=request.getHeader("User-Agent")%></TD>
															</TR> -->
															<TR>
																<TD class="w12">
																	<INPUT id = "changepwd" type="button" onclick="javascript:parent.changeLoginPassword();" value="登录密码修改">
																</TD>
															</TR>
														</TBODY></TABLE>
													</TD>
												</TR>
											</TBODY></TABLE>
										</TD>
									</TR>
									<TR>
										<TD>
											<TABLE width="100%" border="0" cellpadding="3" cellspacing="0">
												<TBODY><TR>
													<TD class="w12">
												</TR>

											</TBODY></TABLE>
										</TD>
									</TR>
								</TBODY></TABLE>
							</TD>
							<TD background="codeutil/resource/images/welcome/wel_07.jpg" width="237">
							</TD>
						</TR>
						<TR>
							<TD colspan="3">
								<IMG src="codeutil/resource/images/welcome/wel_08.png" width="750" height="133"/>
							</TD>
						</TR>
					</TBODY></TABLE>
				</TD>
				<TD style="background-image:url(codeutil/resource/images/welcome/welcomeLeft.png)" align="right" valign="top">
				    <div style="margin:30 30 0 0">&nbsp; 
					<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="130" height="130" >
					  <param name="movie" value="codeutil/resource/flash/clock/clock80.swf" />
					  <param name="quality" value="high" />
					  <param name="wmode" value="transparent" />
					  <embed src="codeutil/resource/flash/clock/clock80.swf" width="130" height="130" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" wmode="transparent"></embed>
					</object>
					</div>
				    <!--  
				    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="130" height="130">
					  <param name="movie" value="../resource/flash/clock/clock121.swf" />
					  <param name="quality" value="high" />
					  <param name="wmode" value="transparent" />
					  <embed src="../resource/flash/clock/clock121.swf" width="130" height="130" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" wmode="transparent"></embed>
					</object>
				    -->
				    <!--  
				    <div style="fload:left;">
					    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0">
						      <param name="movie" value="../resource/flash/clock/clock121.swf"/>
						      <param name="quality" value="high" />
						      <param name="wmode" value="transparent"/>
						      <param name="menu" value="false"/>
						      <embed src="../resource/flash/clock/clock121.swf" pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" wmode="transparent" menu="false"></embed>
						</object>
					</div>
					-->
				</TD>
			</TR>
		</TBODY></TABLE>
</BODY></HTML>
