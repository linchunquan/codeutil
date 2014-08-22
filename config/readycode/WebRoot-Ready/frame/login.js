
Ext.onReady(function(){
	
	Ext.QuickTips.init();
	
	//Ext.form.Field.prototype.msgTarget = 'side';
	
	// Extend timeout for all Ext.Ajax.requests to 90 seconds.  
    // Ext.Ajax is a singleton, this statement will extend the timeout  
    // for all subsequent Ext.Ajax calls.  
    Ext.Ajax.timeout = 600000;
    
    //var userNameValidateText = "用户名不正确";
    //var passwordValidateText = "登录密码不正确";
    
    var userNameValidateText = "";
    var passwordValidateText = "";
    
    var userNameEmptyText = "用户名不能为空";
    var passwordEmptyText = "登录密码不能为空";
    
    var userNameValidator = function(textValue){
    	if(!isNotNullValue(textValue)||textValue.length==0){
    		return userNameEmptyText;
    	}
    	return isNotNullValue(userNameValidateText)&&userNameValidateText.length>0 ? userNameValidateText:true;
    }
    var passwordValidator = function(textValue){
    	if(!isNotNullValue(textValue)||textValue.length==0){
    		return passwordEmptyText;
    	}
    	return isNotNullValue(passwordValidateText)&&passwordValidateText.length>0 ? passwordValidateText:true;
    }
    
    var validate = function(){
    	var v1 = Ext.getCmp('userName').validate();
		var v2 = Ext.getCmp('password').validate();
		return v1&&v2;
    }
    
    var login = function(){
    	
    	win.body.mask('正在登陆...', 'x-mask-loading');
    	
    	userNameValidateText = "";
		passwordValidateText = "";
		
		validate();
    	
    	Ext.Ajax.request({
    		url: 'command.htm',
    		params:{ 
	        	cmd: 'login',
	        	userName: Ext.getCmp('userName').getValue(),
	        	password: Ext.getCmp('password').getValue()
            },
    		success:function(response){
    			
    			win.body.unmask();
    			
				var validateText = Ext.util.JSON.decode(response.responseText);
    			userNameValidateText = validateText.userNameValidateText;
				passwordValidateText = validateText.passwordValidateText;
				
				Ext.getCmp('userNameValidateText').setText(userNameValidateText);
	        	Ext.getCmp('passwordValidateText').setText(passwordValidateText);
				
				if(validate()){
					window.location = "frame.jsp";
				}
    		},
    		failure:function(){
    			Ext.MessageBox.show({
		           title: '错误',
		           msg: '登录失败，请检查客户端与服务器之间的连接是否正常.',
		           buttons: Ext.MessageBox.OK,
		           animEl: 'loginWin',
		           icon: Ext.MessageBox.ERROR
		        });
    			win.body.unmask();
    		}
    	})
    }
    
    var reset = function(){
    	Ext.getCmp('userName').setValue("");
	    Ext.getCmp('password').setValue("");
    }
    
    
    
    var win = new Ext.Window({

    	id:'loginWin',
		layout:'fit',
		width:500,
		height:300,
		plain: true,
		closable :false,
		resizable :false,
		modal:true,
		items: new Ext.Panel({
		
			border:false,
			html: '<div id="bgcolorDiv" style="z-index:1; width:100%; height:90px; background-image:url(codeutil/resource/images/MainFrame/bgcolor.png);">' +
        		  '<div id="15GameDiv" style="z-index:3; position:absolute; top:5px; width:631px; height:56px; background-image:url(codeutil/resource/images/MainFrame/logotitle.png);"></div>' +
        		  '<div id="bannerDiv" style="z-index:2; position:absolute; right:1px; top:-2px; bottom:2px; width:672px; height:90px; background-image:url(codeutil/resource/images/MainFrame/banner.png);"></div>' +
				  '<div id="div_loginInfo" style="z-index:4; font-size:10pt; color:#CF0; position:absolute; right:0px; bottom:0px; overflow:hidden;"></div></div>'+
				  '<div id="user" style="z-index:5;position:absolute; top:134px; left:100px;width:16px; height:16px;" class="icon-user"></div>'+
				  '<div id="user" style="z-index:5;position:absolute; top:164px; left:100px;width:16px; height:16px;" class="icon-key"></div>',
        	layout:'absolute',
		    layoutConfig: {
		        // layout-specific configs go here
		
		        extraCls: 'x-abs-layout-item'
		    },
        	baseCls: 'x-plain',
		    defaultType: 'textfield',
		    items: [
			    {
			        x: 120,
			        y: 135,
			        xtype:'label',
			        text: '用户名:'
			    },{
			        x: 180,
			        y: 130,
			        id: 'userName',
			        name: 'userName',
			        allowBlank:false,
			        validator:userNameValidator,
			        anchor:'70%'
			    },{
			        x: 345,
			        y: 135,
			        id: 'userNameValidateText',
			        xtype:'label',
			        style:'color:red',
			        text: ''
			    },{
			        x: 120,
			        y: 165,
			        xtype:'label',
			        text: '密码:'
			    },{
			        x: 180,
			        y: 160,
			        id: 'password',
			        name: 'password',
			        allowBlank:false,
			        inputType:'password',
			        validator:passwordValidator,
			        anchor: '70%'
			    },{
			        x: 345,
			        y: 165,
			        id: 'passwordValidateText',
			        xtype:'label',
			        style:'color:red',
			        text: ''
			    }
		    ]
		}),
		buttonAlign:'center',
		buttons: [
			{text:'登录',handler:login},{text: '重置',handler:reset}
		]
	
	});
    
    win.show(this);
    
    new Ext.KeyMap("userName",{key: Ext.EventObject.ENTER,fn:login});
    new Ext.KeyMap("password",{key: Ext.EventObject.ENTER,fn:login});
});