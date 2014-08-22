GenericWidgeProxy = function(cfg){
	var defaultCfg = {
	/*	labelWidth:100,
		fieldWidth:150,
		widgeWidth:2*(100+150)+18*2-20+5*(2-1),
		marginLeft:-20,
		columns: 2,
		widgeMap:{}*/
	};
	if(cfg){
		Ext.apply(defaultCfg,cfg);
	}
	Ext.apply(this,defaultCfg);
};
Ext.extend(GenericWidgeProxy,Object,{
	CLASS_TYPE:'GenericWidgeProxy',
	getBasicConfig:function(widgeContainer,_widgeProxy){
		var widgeProxy = _widgeProxy?_widgeProxy:this;
		var bCfg = {
			layout:'tableform',
			labelWidth:widgeProxy.labelWidth,
		    labelAlign:'right',
		    bodyStyle:  widgeContainer&&widgeContainer.bodyStyle
		    		  ? widgeContainer.bodyStyle+';margin-left:'+widgeProxy.marginLeft+'px;'
		    	      : 'margin-left:'+widgeProxy.marginLeft+'px;',
		    defaults: {
		        //width: widgeProxy.fieldWidth,
		        //msgTarget: 'side'
		    },
			layoutConfig: {
				columns: widgeProxy.columns,
				labelSeparator:':'
			}
		}
		return bCfg;
	},
	
	getWidgeItems : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		var items = [];
		var widge = null;
		for(attrubute in widgeMap){
			if(attrubute != 'id'){
				widge = widgeMap[attrubute];
				if( widge.shouldAddToTab != true ){
					if(widge.CLASS_TYPE === 'GenericWidgeProxy'){
						var widgeCfg = {colspan:10, width:widge.widgeWidth};
						widge.wrapAsFieldSet(widgeCfg,widge);
						widgeCfg.fieldLabel=widge.fieldLabel,
						items.push(widgeCfg);
					}
					else if(Ext.type(widge) === 'object'){
						items.push(widge);
					}
				}
			}
		}
		return items;
	},
	getWidgeTabItems : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		var items = [];
		var widge = null;
		for(attrubute in widgeMap){
			if(attrubute != 'id'){
				widge = widgeMap[attrubute];
				if( widge.shouldAddToTab === true ){
					if(widge.CLASS_TYPE === 'GenericWidgeProxy'){
						var widgeCfg = {colspan:10, width:widge.widgeWidth};
						widge.wrapAsFieldSet(widgeCfg,widge);
						widgeCfg.title=widge.fieldLabel,
						items.push(widgeCfg);
					}
					else if(Ext.type(widge) === 'object'){
						var widgeCfg={
							title:widge.fieldLabel,
	                		layout:'fit',
	                		items:widge
	                	};
						items.push(widgeCfg);
					}
				}
			}
		}
		return items;
	},
	wrapAsForm:function(widgeContainer,_widgeProxy){
		var widgeProxy = _widgeProxy?_widgeProxy:this;
		var cfg = {}
		Ext.apply(cfg, widgeProxy.getBasicConfig(widgeProxy), {items:widgeProxy.getWidgeItems()});
		
		var tabItems=widgeProxy.getWidgeTabItems(widgeProxy);
		if(tabItems.length>0){
			var basicTabCfg = {
	            xtype:'tabpanel',
	            plain:true,
	            activeTab: 0,
	            height:235,
	            deferredRender: false,
	            defaults:{bodyStyle:'padding:3px'},
	            items:tabItems
	        }
	        Ext.apply(widgeContainer, {items:[cfg,basicTabCfg]});
		}
		else{
			Ext.apply(widgeContainer, cfg);
		}
	},
	wrapAsFieldSet:function(widgeContainer,_widgeProxy){
		var widgeProxy = _widgeProxy?_widgeProxy:this;
		var fieldSetConfig = {
			title:'基本信息',
			xtype:"fieldset",
			collapsible: true
		};
		this.wrapAsForm(fieldSetConfig,widgeProxy);
		var cfg={
			layout:'form',
			items:fieldSetConfig
		};
		Ext.apply(widgeContainer, cfg);
	},
	
	
	
	isNotNullValue : function (value){
		if(typeof(value) === "undefined"||value === null){
			return false;
		}
		else {
			return true;
		}
		
	},
	getFormComponentValue : function(component){
		if( component instanceof Ext.form.DateField ){
			if(component.getValue()&&component.getValue().getTime){
				return component.getValue().getTime();
			}
			else{
				return null;
			}
		}
		else if( component instanceof Ext.form.RadioGroup ){
			return component.getValue().getGroupValue();
		}
		//else if( component instanceof Ext.form.HtmlEditor ){
		//	return component.getValue().replace(_regS,"");
		//}
		else if(component.getValue){
			return component.getValue()
		}
		else if(component.getComponentValue){
			return component.getComponentValue();
		}
		else return null;
	},
	setFormComponentValue : function(component,value){
		if( component instanceof Ext.form.DateField && value){
			return component.setValue(new Date(value));
		}
		else if(component.setValue){
			return component.setValue(value)
		}
		else if(component.setComponentValue){
			return component.setComponentValue(value);
		}
	},
	getWidgeProperties : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		var properties  = {};
		var component = null;
		for(attrubute in widgeMap){
			component = widgeMap[attrubute];
			if(this.isNotNullValue(component)){
				if(component.CLASS_TYPE === 'GenericWidgeProxy'){
					properties[attrubute] = this.getWidgeProperties(component);
				}
				else if(Ext.type(component) === 'object'){
					var value = this.getFormComponentValue(component);
					if(this.isNotNullValue(value)){
						properties[attrubute] = value;
					}
				}
				else{
					properties[attrubute]=component;
				}
			}
		}
		return properties;
	},
	setWidgeProperties : function(_widgeProxy, properties){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		if(this.onSetWidgeProperties){
		    this.onSetWidgeProperties(widgeMap,properties);
		}
		for(attrubute in properties){ 
			var component = widgeMap[attrubute];
			if( this.isNotNullValue(component) && component.CLASS_TYPE === 'GenericWidgeProxy'){
				this.setWidgeProperties(component, properties[attrubute]);
			}
			else if(Ext.type(component) === 'object'){
				this.setFormComponentValue(component, properties[attrubute]);
			}
			else {
				widgeMap[attrubute] = properties[attrubute];
			}
		}
	},
	resetWidge : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		for(attrubute in widgeMap){
			var component = widgeMap[attrubute];
			if(this.isNotNullValue(component)){
				if(component.CLASS_TYPE === 'GenericWidgeProxy'){
					this.resetWidge(component);
				}
				else if(Ext.type(component) === 'object' && component.reset){
					component.reset();
					if(typeof(component.defaultValue) != "undefined"){
						component.setValue(component.defaultValue);
					}
				}
			}
		}
	},
	resetWidgeAndId : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		for(attrubute in widgeMap){
			var component = widgeMap[attrubute];
			if(this.isNotNullValue(component)){
				if(component.CLASS_TYPE === 'GenericWidgeProxy'){
					this.resetWidge(component);
				}
				else if(Ext.type(component) === 'object' && component.reset){
					if(typeof(component.defaultValue) === "undefined"){
						component.setValue(null);
					}
					else{
						component.setValue(component.defaultValue);
					}
						
					//component.reset();
				}
			}
		}
		widgeMap['id']=0;
	},
	validateWidgeProperties : function(_widgeProxy){
		var widgeMap = _widgeProxy?_widgeProxy.widgeMap:this.widgeMap
		var items = [];
		var widge = null;
		var validateResult = true;
		for(attrubute in widgeMap){
			if(attrubute != 'id'){
				widge = widgeMap[attrubute];
				if(this.isNotNullValue(widge)){
					if(widge.CLASS_TYPE === 'GenericWidgeProxy'){
						var v_result = widge.validateWidgeProperties(widge);
						if(validateResult && !v_result){
							validateResult = false;
						}
					}
					else if(widge.validate){
						var v_result = widge.validate();
						if(validateResult && !v_result){
							validateResult = false;
						}
					}
				}
			}
		}
		return validateResult;
	},
	onSuccessSave:null,
	onFailureSave:null,
	saveOrUpdateUrl:null,
	updateFromParentUrl:null,
	updateFromParent:function(parentId){
		//alert('parentId:'+parentId);
		var m_widetProxy = this;
		var m_params = {sort:"id", dir:"ASC",start:0, limit:1, queryCondition:" parent_id like '"+parentId+"'"};
		Ext.Ajax.request({
			url : m_widetProxy.updateFromParentUrl,
			method : 'POST',
			params:m_params,
			success : function(response) {
				//alert('response.responseText:'+response.responseText);
				var data = Ext.util.JSON.decode(response.responseText).root;
				//alert('data:'+data);
				if(data.length>0){
					data = data[0];
				}
				else{
					data = {parent_id:parentId};
				}
				m_widetProxy.setWidgeProperties(m_widetProxy,data);
			}
		});
	},
	updateFromChild:function(idValue){
		var m_widetProxy = this;
		var m_params = {sort:"id", dir:"ASC",start:0, limit:1, queryCondition:" id = "+idValue};
		Ext.Ajax.request({
			url : m_widetProxy.updateFromParentUrl,
			method : 'POST',
			params:m_params,
			success : function(response) {
				//alert('response.responseText:'+response.responseText);
				var data = Ext.util.JSON.decode(response.responseText).root;
				//alert('data:'+data);
				if(data.length>0){
					data = data[0];
				}
				else{
					data = {id:idValue};
				}
				m_widetProxy.setWidgeProperties(m_widetProxy,data);
			}
		});
	},
	handleSaveAction:function(button,hideWindow){
		var m_widge = button.ownerCt.ownerCt;
		var m_widetProxy = this;
		if (m_widetProxy.validateWidgeProperties()) {
			m_widge.body.mask('正在保存...', 'x-mask-loading');
			Ext.Ajax.request({
				//url : 'command.htm?cmd=saveOrUpdateHotel',
				url : m_widetProxy.saveOrUpdateUrl,
				method : 'POST',
				success : function() {
					Ext.MessageBox.show({
			           title: '消息',
			           msg: '保存数据成功.',
			           buttons: Ext.MessageBox.OK,
			           icon: Ext.MessageBox.INFO,
			           fn:function(){
			           	   if(hideWindow){
		               	       m_widge.hide();
		               	   }
			               if(m_widetProxy.onSuccessSave){
			               	   m_widetProxy.onSuccessSave();
			               }
			           }
			        });
					m_widge.body.unmask();
				},
				failure : function() {
					Ext.MessageBox.show({
			           title: '错误',
			           msg: '保存数据失败.',
			           buttons: Ext.MessageBox.OK,
			           icon: Ext.MessageBox.ERROR,
			           fn:function(){
			               if(m_widetProxy.onFailureSave){
			               	   if(hideWindow){
			               	       m_widge.hide();
			               	   }
			               	   if(m_widetProxy.onFailureSave){
				               	   m_widetProxy.onFailureSave();
				               }
			               }
			           }
			        });
					m_widge.body.unmask();
				},
				params : {
					json : Ext.util.JSON.encode(m_widetProxy.getWidgeProperties())
				}
			});
		}
	}
});