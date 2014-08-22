if(Ext.isIE){
	if(Ext.isIE6||Ext.isIE7||Ext.isIE8){
		//Ext.MessageBox.alert("警告","您当前的浏览器版本低于9.0，为取得最佳的系统使用效果，建议你安装Google Chrome Frame.");
		Ext.MessageBox.confirm('提示', '您当前的IE浏览器版本低于9.0，为取得最佳的系统使用效果，<br/>建议您安装Google Chrome Frame.<br/>是否从本服务器下载Google Chrome Frame?', 
		function(btn, text){
		    if (btn == 'yes'){
		    	window.open("command.htm?cmd=downloadGCF");
		    	aler("安装完毕后，请重启IE浏览器.");
		    }
		});
	};
	//alert('您正在使用的是 Microsoft Internet Explorer  浏览器，请下载并安装Google Chrome Frame插件再使用本系统.');
	//alert('我们做了一个艰难的决定，对于IE用户，需下载并安装我们提供的加速插件才能正常使用！');
	//window.location.href="command.htm?cmd=downloadSoftware";
	//window.open("command.htm?cmd=downloadSoftware");
}

/*
function systemInitialize(){
	Ext.Ajax.request({
		url :'command.htm?cmd=systemInitialize',
		success : function(response) {
			alert('系统初始化完成！');
		},
		failure : function() {
			//alert("操作失败");
		}
	})
}*/
MainPanel = function(){
    MainPanel.superclass.constructor.call(this, {
        id:'doc-body',
        region:'center',
        margins:'-2 3 1 0',
        resizeTabs: true,
        minTabWidth: 135,
        tabWidth: 135,
        plugins: new Ext.ux.TabCloseMenu(),
        enableTabScroll: true,
        activeTab: 0,

        items: {
            id:'welcome-panel',
            title: '欢迎',
            autoLoad: {url: 'welcome.jsp', scope: this},
            iconCls:'icon-docs',
            bodyStyle:'background-color: #E0E0E0',
            style:'background-color: #E0E0E0',
            autoScroll: true
        }
    });
   // this.on('add',function(){this.el.unmask();});
};

Ext.extend(MainPanel, Ext.TabPanel, {
/**
 * ！！注册事件的方法！！
 *
    initEvents : function(){
        MainPanel.superclass.initEvents.call(this);
        this.body.on('click', this.onClick, this);
    },

    onClick: function(e, target){
    	//注册事件的业务逻辑写在这里...
    },
  */
    autoDestroy: false ,
    
    

    loadClass : function(href, cls, tabName){
    	//alert('tabName:'+tabName);
    	//this.el.mask('加载中...', 'x-mask-loading');
    	
    	if('SearchHotel'===cls){
    		window.open("../search.htm?radio=false");
    		return;
    	}
    	else{
    		var id = 'docs-' + cls;
	        var tab = this.getComponent(id);
	        if(tab){
	            this.setActiveTab(tab);
	        }else{
	        	var panel=FunctionManager.getFunctionPanel('func-'+cls);
	        	if(panel==null){
	        		panel=Ext.getCmp('func-unfinish');
	        	}
	        	panel.setTitle(tabName);
	            var p = this.add(panel);
	            this.setActiveTab(p);
	        }
    	}
    	
      //  this.el.unmask();
    }
});

var m_functionMenu = null;
var m_mainPanel = null;

var fullScreen = function (){
	m_functionMenu.collapse(true);
	Ext.getCmp("northPanel").collapse(true);
	m_mainPanel.expand(true);
}

var osLogout = function(){
	Ext.Ajax.request({
		url : 'command.htm?cmd=logout',
		success:function(){
			document.location.reload();
		}
	})
}

var changeLoginPassword = function(){
	//promp显示密码
	//http://www.sencha.com/forum/showthread.php?26670-Ext-Message-Problem&highlight=password%20confirmation
	//http://www.sencha.com/forum/showthread.php?28626-Ext.Msg-prompt-as-password-control
	//http://hi.baidu.com/fuwenpan/item/21f6488b2f9e485c850fab37
	Ext.MessageBox.getDialog().body.child('input').dom.type='password';
	Ext.MessageBox.show({
	    title:'修改密码',
	    inputType :'password',
	    msg:'请输入新的密码:',
	    prompt:true,
	    width: 230,
	    buttons: Ext.MessageBox.OKCANCEL,
	    fn:function(btn, text){
	    	Ext.MessageBox.getDialog().body.child('input').dom.type='text';
	  		if (btn == 'ok'){
	  			if(text){
					Ext.Ajax.request({
						url : 'command.htm',
						method : 'POST',
						params: { cmd: 'changePassword', password:text},
						success:function(){
							alert('修改密码成功，请重新登录！');
							osLogout();
						},
						failed:function(){
							Ext.MessageBox.alert("修改密码失败！");
						}
					})
	  			}
	  			else{
	  				Ext.MessageBox.alert("密码不能为空！");
	  			}
	  		}
	    },
	    animEl: 'changepwd',
   		icon: Ext.MessageBox.INFO
	});
}

Ext.onReady(function(){
	
	 Ext.QuickTips.init();
	
	// Extend timeout for all Ext.Ajax.requests to 90 seconds.  
    // Ext.Ajax is a singleton, this statement will extend the timeout  
    // for all subsequent Ext.Ajax calls.  
    Ext.Ajax.timeout = 600000;  
	
	var unfinishfunction=new FunctionPanel({
		id: 'func-unfinish',
		title:'未完成功能',
	    autoLoad: 'unfinishfunc.jsp'
	});

   

    var functionMenu = m_functionMenu = new FunctionMenu();
    mainPanel = m_mainPanel = new MainPanel();
    
    
    functionMenu.on('click', function(node, e){
    	 //createGrid();
         if(node.isLeaf()){
            e.stopEvent();
            mainPanel.loadClass(node.attributes.href, node.id , node.attributes.tabName);
         }
    });
/*
    mainPanel.on('tabchange', function(tp, tab){
    	alert('tabId:'+tab.getId());
        functionMenu.selectClass('root/apidocs/baseDataManage/busDataManage'); 
    });*/
	
    
    var viewport = new Ext.Viewport({
        layout:'border',
        items:[ 
        /*{
        	height: 61,
            region:'north',
            margins: '0 0 5 0'
		},*/
        {
        	border: false,
        	collapseMode: 'mini',
			collapsible: true,
        	header: false,
        	html: '<div id="bgcolorDiv" style="z-index:1; width:100%; height:90px; background-image:url(codeutil/resource/images/MainFrame/bgcolor.png);">' +
        		  '<div id="15GameDiv" style="z-index:3; position:absolute; top:5px; width:631px; height:56px; background-image:url(codeutil/resource/images/MainFrame/logotitle.png);"></div>' +
        		  '<div id="bannerDiv" style="z-index:2; position:absolute; right:1px; top:-2px; bottom:2px; width:672px; height:90px; background-image:url(codeutil/resource/images/MainFrame/banner.png);"></div>' +
				  '<div id="div_loginInfo" style="z-index:4; font-size:10pt; color:#CF0; position:absolute; right:0px; bottom:0px; overflow:hidden;"></div></div>',
        	margins: '1 1 5 1',
			minSize: 90,
			maxSize: 90,
			region: 'north',
			id:'northPanel',
			split: true
        },
        functionMenu, mainPanel ,{
				region: 'south',
				height: 24,
				border:false,
				margins: '0 0 0 0',
	        	bbar:   new Ext.Toolbar({
				    items: [
				    	
				    	'->',
				    	/*{
				    		id:'checkNewOrder',
						    text: '检测到新订单...',
						    handler: function(){
						        //Ext.Msg.alert('Click', 'You did something.');
						    	mainPanel.loadClass("", "UnhandledOrder" , "未处理订单");
						    },
						    iconCls: 'icon-orderAlert'
						},*/
						
				        {xtype: 'tbspacer', width: 0},
				         '-',
				        '操作员：'+currentUser.userName,
				        {xtype: 'tbspacer', width: 50},
				         '-',
				         {xtype: 'tbspacer', width: 5},
				        '登录时间：'+(new Date(currentUser.loginTime)).format('Y-m-d H:i:s'),
				        {xtype: 'tbspacer', width: 50},
				         '-',
				        {xtype: 'tbspacer', width: 5},
				        '登录角色：'+currentUser.roles
				    ]
				})
			}]
    });
    functionMenu.expandPath('/root/apidocs');
    viewport.doLayout();
	setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);
    
	
    //定时器，每秒刷新一次时间，每分钟与服务器做一次时间同步
	Ext.TaskMgr.start({
		run: function(){
			var loginInfo = "<b>当前用户：</b>&nbsp;"+ 
			currentUser.userName+"("+currentUser.roles+")" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>系统时间：</b>&nbsp;" + 
			(new Date()).format('Y-m-d H:i:s')+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"[<a href='javascript:fullScreen();' style='color:#CF0;'>全屏</a>]&nbsp;&nbsp;&nbsp;" +
			"[<a href='javascript:osLogout();' style='color:#CF0;'>注销</a>]&nbsp;&nbsp;&nbsp;";
			document.getElementById('div_loginInfo').innerHTML = loginInfo;
			/** for heart beat
			Ext.Ajax.request({
				url : 'command.htm',
				method : 'POST',
				params: { cmd: 'isUnhandledOrderExisted'},
				success:function(response) {
					var isUnhandledOrderExisted = Ext.util.JSON.decode(response.responseText);
					if(isUnhandledOrderExisted){
						Ext.getCmp('checkNewOrder').show();
					}
					else{
						Ext.getCmp('checkNewOrder').hide();
					}
				}
			})*/
		},
		interval: 3000
	});
	
});

