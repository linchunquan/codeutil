function checkObjectAttributes(o){
	var info = "";
	for(attrubute in o) 
	{ 
		//if(o instanceof Function)
		info+=" "+attrubute//+":"+o[attrubute]+" \n"; 
	}
	alert(info);
}
function checkObjectAttributes2(o){
	var info = "";
	for(attrubute in o) 
	{ 
		//if(o instanceof Function)
		info+=" "+attrubute+":"+o[attrubute]+" \n"; 
	}
	alert(info);
}


[Class]Grid = Ext.extend(Ext.grid.GridPanel,{
	pageSize:50,
	parent_id:-1,
	initView:function(){
		this.view = new Ext.grid.GridView({
			deferEmptyText:true,
	        emptyText: '没有数据显示',
	        forceFit: [forceFit]
	    })
    },
	autoScroll: true,
	getRowNumberer:function(){
		return new Ext.grid.RowNumberer({
			renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){					
				if(store.lastOptions&&store.lastOptions.params){
					var startIndex = store.lastOptions.params.start;						
					if(startIndex){						
						return startIndex + rowIndex + 1;
					}
					else{
						return rowIndex + 1;
					}
				}
				else{
					return rowIndex+1;
				}
			}
		});
	},
	//刷新当前页
	refreshCurrentPage:function(){
		this.store.reload();
	},
	//获取当前选中的记录
	getSelectRecord:function(){
		return this.getSelectionModel().getSelected();
	},
	getAllSelectRecordData:function(){
		var records = this.getSelectionModel().getSelections();
		var result = [];
		if(records){
			Ext.each(records,function(record){
				var data = {};
				Ext.apply(data,record.data);
				result.push(data);
			});
		}
		return result;
	},
	//清除选中状态
	deSelectAll:function(){
		this.getSelectionModel().clearSelections();
	},
	//数据初始化
	initGridData:function(){
		this.store.setBaseParam('start',0);
		this.store.setBaseParam('limit',this.pageSize);	
		this.store.load();
	},
	//初始化换页工具条
	initBBar:function(){
		var pageSize=this.pageSize;
		var store=this.store;
		var thisWidge = this;
		this.bbar=new Ext.PagingToolbar({  
            pageSize:pageSize,  
            store:store,  
            displayInfo:true,  
            displayMsg:'显示第 {0} 条到 {1} 条记录，一共 {2} 条',  
            emptyMsg:'没有记录',
            items:['-','每页记录数',{
					xtype:'combo',
					value:pageSize,
					mode : 'local'  ,    //数据模式, local为本地模式, 如果不设置,就显示不停的加载中...
					triggerAction : 'all',  //显示所有下列数.必须指定为'all'
					store : new Ext.data.SimpleStore({  //填充的数据
					   fields : ['text', 'value'],
					   data :[[pageSize, pageSize], [pageSize+10, pageSize+10], [pageSize+20, pageSize+20], [pageSize+30, pageSize+30]]
					}),
					editable:false,
					width:50,
					valueField : 'value',      //传送的值
					displayField : 'text',     //UI列表显示的文本
					listeners:{
						collapse:function(field){
							var currentPageSize = field.ownerCt.ownerCt.pageSize;
							var newPageSize = field.getValue();
							if(currentPageSize!=newPageSize){
								field.ownerCt.pageSize=field.ownerCt.ownerCt.pageSize=newPageSize;
								thisWidge.initGridData();
							}
						}
					}
        		}
        	]
        });
	},
	
	initClums:function(){
		/*var renderIndustry = function (val){
			return CorporationType.IndustryType[val];
		};
		var renderDate = function (format){
			return function(val){
				return Ext.util.Format.date(new Date(val), format);
			}
		};
		*/
		var renderDate = function (format){
			return function(val){
				return val?Ext.util.Format.date(new Date(val), format):"";
			}
		};
		
		var thisWidge = this;
		var renderOptionValue = function(value, cellmeta, record, rowIndex, columnIndex, store){
			var columns = thisWidge.cols;
			var dataIndex = columns[columnIndex].dataIndex;
			var fieldConfig = thisWidge.fieldConfig;
			var optValues = null;
			if(fieldConfig && (optValues=fieldConfig[dataIndex]["optValues"])){
				for(var i =0 ;i < optValues.length;i++){
					var tmpValue = optValues[i];
					if(tmpValue[1]==value){
						return tmpValue[0];
					}
				}
			}
			return value;
		}
[InitDataStatusColumn]
[InitExpanderColumn]
		this.cols = this.columns = [
			this.getRowNumberer()
[InitColumns]
		];
        this.autoExpandColumn = [autoExpandColumn];
	},
	initStore:function(){

		this.store = new Ext.data.JsonStore({
        	autoLoad:false,
		    url:'command.htm?cmd=findAll[Classes]',
		    root:'root',                          
            totalProperty:'totalProperty',
            //定义后台排序
            remoteSort:true,
            //定义默认排序
            sortInfo: {
			    field: 'id',
			    direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
			},
		    fields:[[StoreFields]]
		});

		var thisWidge = this;
		this.store.on("beforeload",function (store, options){
			var queCnd = thisWidge.getQueryCondition();
			delete options.params['queryCondition'];
			if(queCnd){
				if(!this.isBothValueFieldValid()){
					Ext.MessageBox.alert("提示","请您根据提示输入正确的查询条件!");
					return false;
				}
				Ext.apply(options.params, {'queryCondition':queCnd});
			}
			//checkObjectAttributes2(options.params);
			//store.setBaseParam('queryCondition',thisWidge.getQueryCondition());
			if(this.body && this.body.mask){
				this.body.mask('正在加载数据...', 'x-mask-loading');
			}
		},this);
		this.store.on("load",function(){
			if(this.body && this.body.unmask){
				this.body.unmask();
			}
		},this);
		this.store.on("exception",function(){
			this.body.unmask();
		},this);
		//this.loadExhibitInfo();
	},
	
	
	//[[---------------------------------------------------------------------------
	//[[------------------------------- 隐藏/显示控制 -------------------------------
	//[[---------------------------------------------------------------------------
	widegShowControlHelper:function(comoboOperField,comoboQueryField,valueFieldsHideFunction,valueFieldSettingFunction){
		
		if(!this.widegShowControlHelper.hideAllValueFields){
			this.widegShowControlHelper.hideAllValueFields = this.hideAllValueFields;
		}
		
		if(comoboOperField&&!comoboOperField.isVisible()){
			comoboOperField.show();
		}
		valueFieldsHideFunction(this);
		//设置Value Field1
		valueFieldSettingFunction(comoboQueryField.getValue(),this);
	},
	firstQueryWidgetsShowControl : function(){
		//控制按钮的显示
		if(this.actionQuery&&this.actionQuery.isHidden()){
			this.actionQuery.show();
		}
		if(this.actionSecondQuery&&this.actionSecondQuery.isHidden()){
			this.actionSecondQuery.show();
		}
		if(this.actionCancleQueryCondition&&this.actionCancleQueryCondition.isHidden()){
			this.actionCancleQueryCondition.show();
		}
		this.widegShowControlHelper(this.comoboOperField1,this.comoboQueryField1,this.hideAllFirstValueFields,this.setFirstValueField);
	},
	secondQueryWidgetsShowControl : function(){
		
		this.widegShowControlHelper(this.comoboOperField2,this.comoboQueryField2,this.hideAllSecondValueFields,this.setSecondValueField);
	},
	//]]---------------------------------------------------------------------------
	//]]------------------------------- 隐藏/显示控制 -------------------------------
	//]]---------------------------------------------------------------------------
	
	
	//[[----------------------------------------------------------------------------
	//[[-------------------------------隐藏Value Field-------------------------------
	//[[----------------------------------------------------------------------------
	hideAllValueFields : function(textValueField,dateValueField,comboValuexField){
		textValueField.hide();
		dateValueField.hide();
		comboValuexField.hide();
	},
	hideAllFirstValueFields : function(scope){
		if(!scope){
			this.hideAllValueFields(this.textValueField1,this.dateValueField1,this.comboValuexField1);
		}
		else{
			scope.hideAllValueFields(scope.textValueField1,scope.dateValueField1,scope.comboValuexField1);
		}
	},
	hideAllSecondValueFields : function(scope){
		if(!scope){
			this.hideAllValueFields(this.textValueField2,this.dateValueField2,this.comboValuexField2);
		}
		else{
			scope.hideAllValueFields(scope.textValueField2,scope.dateValueField2,scope.comboValuexField2);
		}
	},
	//]]----------------------------------------------------------------------------
	//]]-------------------------------隐藏Value Field-------------------------------
	//]]----------------------------------------------------------------------------
	
	
	//[[----------------------------------------------------------------------------
	//[[-------------------------------设置校验函数/表达式------------------------------
	//[[----------------------------------------------------------------------------
	defautValidateConfig:{
		"string": {regex:/\S+/         ,        regexText:"请您勿输入全为空格的字符串!"},
		"gridcombo": {regex:/\S+/         ,     regexText:"请您勿输入全为空格的字符串!"},
		"int"   : {regex:/^[-\+]?\d+$/ ,        regexText:"请您输入正确的整数格式!"},
		"long"  : {regex:/^[-\+]?\d+$/ ,        regexText:"请您输入正确的整数格式!"},
		"double": {regex:/^[-\+]?\d+(\.\d+)?$/ ,regexText:"请您输入正确的浮点数格式!"}
	},
	setValidateLogicForFieldWidge : function(fieldWidge,fieldConfig){
		
		var shouldValidate = fieldConfig.shouldValidate;
		if(shouldValidate===false){
			fieldWidge.validator=null;
			fieldWidge.regex=null;
			fieldWidge.regexText=null;
			return;
		}
		
		var validator = fieldConfig.validator;
		if(validator&&Ext.isFunction(validator)){
			fieldWidge.validator=validator;
			return;
		}
		
		var regex     = fieldConfig.regex;
		if(regex){
			var regexText = fieldConfig.regexText;
			fieldWidge.regex=regex;
			fieldWidge.regexText=regexText;
			return;
		}
	
		var fieldType = fieldConfig.fieldType;
		var validateConfig = this.defautValidateConfig[fieldType];
		if(fieldType){
			fieldWidge.regex=validateConfig.regex;
			fieldWidge.regexText=validateConfig.regexText;
			return;
		}
	},
	//]]----------------------------------------------------------------------------
	//]]-------------------------------设置校验函数/表达式------------------------------
	//]]----------------------------------------------------------------------------
	
	
	//[[----------------------------------------------------------------------------
	//[[-------------------------------设置初始值-------------------------------------
	//[[----------------------------------------------------------------------------
	defautValueForTextField:{
		"string": "",
		"int"   : "0",
		"long"  : "0",
		"double": "0.0"
	},
	setDefaultValueForTextField:function(textField,fieldConfig){
		var fieldType = fieldConfig.fieldType;
		var defaultValue = this.defautValueForTextField[fieldType];
		if(fieldType){
			textField.setValue(defaultValue);
		}
	},
	//]]----------------------------------------------------------------------------
	//]]-------------------------------设置初始值-------------------------------------
	//]]----------------------------------------------------------------------------
	
	
	//[[----------------------------------------------------------------------------
	//[[-------------------------------设置Value Field-------------------------------
	//[[----------------------------------------------------------------------------
	setValueField : function (fieldIndex,textValueField,dateValueField,comboValuexField){
		var fieldConfig = this.fieldConfig[fieldIndex];
		if(fieldConfig){
			var fieldType = fieldConfig.fieldType;
			var optValues = fieldConfig.optValues;
			if(!optValues){
				if(fieldType=='date'){
					dateValueField.show();
				}
				else{
					textValueField.show();
					this.setValidateLogicForFieldWidge(textValueField,fieldConfig);
					this.setDefaultValueForTextField(textValueField,fieldConfig);
				}
			}
			else{
				comboValuexField.show();
				comboValuexField.getStore().loadData(optValues);
				this.setValidateLogicForFieldWidge(comboValuexField,fieldConfig);
			}
		}
	},
	setFirstValueField : function(fieldIndex,scope){
		var funScope = !scope?this:scope;
		funScope.setValueField(fieldIndex,funScope.textValueField1,funScope.dateValueField1,funScope.comboValuexField1);
	},
	setSecondValueField : function(fieldIndex,scope){
		var funScope = !scope?this:scope;
		funScope.setValueField(fieldIndex,funScope.textValueField2,funScope.dateValueField2,funScope.comboValuexField2);
	},
	//]]----------------------------------------------------------------------------
	//]]-------------------------------设置Value Field-------------------------------
	//]]----------------------------------------------------------------------------
	
	
	//隐藏所有二次查询的Widget
	hideAllSecondQueryWidgets : function(){
		this.comoboQueryField2.hide();
		this.comoboOperField2.hide();
		this.textValueField2.hide();
		this.dateValueField2.hide();
		this.comboValuexField2.hide();
		this.actionSecondQuery.hide();
	},
	
	//隐藏所有二次查询的Widget
	hideAllFirstQueryWidgets : function(){
		this.comoboQueryField1.show();
		this.comoboOperField1.hide();
		this.textValueField1.hide();
		this.dateValueField1.hide();
		this.comboValuexField1.hide();
		this.actionSecondQuery.hide();
	},
	

	//[------------------------------------------------------------------------------
	//[--------------------------------获取查询值--------------------------------------
	//[------------------------------------------------------------------------------
	getValueFieldValue : function(fieldIndex,textValueField,dateValueField,comboValuexField){
		var fieldConfig = this.fieldConfig[fieldIndex];
		if(fieldConfig){
			var fieldType = fieldConfig.fieldType;
			var optValues = fieldConfig.optValues;
			if(!optValues){
				if(fieldType=='date'){
					return dateValueField.getValue().getTime();
				}
				else{
					if(fieldType=='string'|| fieldType == "gridcombo"){
						return "'"+textValueField.getValue()+"'";
					}
					else{
						return textValueField.getValue();
					}
				}
			}
			else{
				if(fieldType=='string'|| fieldType == "gridcombo"){
					return "'"+comboValuexField.getValue()+"'";
				}
				else{
					return comboValuexField.getValue();
				}
				
			}
		}
	},
	getFirstValueFieldValue:function(fieldIndex){
		return this.getValueFieldValue(fieldIndex,this.textValueField1,this.dateValueField1,this.comboValuexField1);
	},
	getSecondValueFieldValue:function(fieldIndex){
		return this.getValueFieldValue(fieldIndex,this.textValueField2,this.dateValueField2,this.comboValuexField2);
	},
	//]------------------------------------------------------------------------------
	//]--------------------------------获取查询值--------------------------------------
	//]------------------------------------------------------------------------------
	
	
	//[------------------------------------------------------------------------------
	//[--------------------------------验证-------------------------------------------
	//[------------------------------------------------------------------------------
	//验证isValid
	isValueFieldValid : function(fieldIndex,textValueField,dateValueField,comboValuexField){
		var fieldConfig = this.fieldConfig[fieldIndex];
		if(fieldConfig){
			var fieldType = fieldConfig.fieldType;
			var optValues = fieldConfig.optValues;
			if(!optValues){
				if(fieldType=='date'){
					return dateValueField.isValid();
				}
				else{
					return textValueField.isValid();
				}
			}
			else{
				return comboValuexField.isValid();
			}
		}
		return true;
	},
	isFirstValueFieldValid:function(fieldIndex){
		return this.isValueFieldValid(fieldIndex,this.textValueField1,this.dateValueField1,this.comboValuexField1);
	},
	isSecondValueFieldValid:function(fieldIndex){
		return this.isValueFieldValid(fieldIndex,this.textValueField2,this.dateValueField2,this.comboValuexField2);
	},
	isBothValueFieldValid:function(){
		
		if(this.comoboOperField1.hidden&&this.comoboOperField2&&this.parent_id>-1){
			return true;
		}
		
		var firstQueryField = this.comoboQueryField1.getValue();
		if(!this.isFirstValueFieldValid(firstQueryField)){
			return false;
		}
		
		var secondQueryField = this.comoboQueryField2.getValue();
		if(secondQueryField&&secondQueryField.length>0){
			if(!this.isSecondValueFieldValid(secondQueryField)){
				return false;
			}
		}
		
		return true;
	},
	//]------------------------------------------------------------------------------
	//]--------------------------------验证-------------------------------------------
	//]------------------------------------------------------------------------------
	
	
	//[------------------------------------------------------------------------------
	//[--------------------------------获取相关的查询条件--------------------------------
	//[------------------------------------------------------------------------------
	onGetQueryCondition:null,
	getQueryCondition : function(){
		
		var queryCondition=new Array();
		
		//分别获取第一、第二查询条件.以后可能会做更多查询条件..
		if(this.comoboOperField1.isVisible()){
			var firstQueryField = this.comoboQueryField1.getValue();
			if(firstQueryField&&firstQueryField.length>0){
				queryCondition.push(firstQueryField+this.comoboOperField1.getValue()+this.getFirstValueFieldValue(firstQueryField));
			}
		}
		if(this.comoboOperField2.isVisible()){
			var secondQueryField = this.comoboQueryField2.getValue();
			if(secondQueryField&&secondQueryField.length>0){
				queryCondition.push(secondQueryField+this.comoboOperField2.getValue()+this.getSecondValueFieldValue(secondQueryField));
			}
		}
		if(this.parent_id>-1){
			queryCondition.push("parent_id="+this.parent_id);
		}
		//checkObjectAttributes2(queryCondition);
		//alert("queryCondition:"+queryCondition)
		if(this.onGetQueryCondition){
			this.onGetQueryCondition(queryCondition);
		}
		return queryCondition.join();
	},
	//]------------------------------------------------------------------------------
	//]--------------------------------获取相关的查询条件--------------------------------
	//]------------------------------------------------------------------------------
	
	
	//[------------------------------------------------------------------------------
	//[--------------------------------动态查询----------------------------------------
	//[------------------------------------------------------------------------------
	dynamicQuery : function(){
		this.store.setBaseParam('start',0);
		this.store.setBaseParam('limit',this.pageSize);
		//this.store.setBaseParam('queryCondition',this.getQueryCondition());
		this.store.load();
	},
	//[------------------------------------------------------------------------------
	//[--------------------------------动态查询----------------------------------------
	//[------------------------------------------------------------------------------
	
	setOperField:function(comoboQueryField,comoboOperField){
		
		var fieldIndex = comoboQueryField.getValue();
		var fieldConfig = this.fieldConfig[fieldIndex];
		if(fieldConfig){
			var fieldType = fieldConfig.fieldType;
			if(fieldType == "string" || fieldType == "gridcombo"){
				comoboOperField.getStore().removeAll();
				comoboOperField.getStore().loadData([['等于',' like '],['不等于','!=']]);
				comoboOperField.setValue(' like ');
			}
			else{
				comoboOperField.getStore().removeAll();
				comoboOperField.getStore().loadData([['等于','='],['大于','>'],['小于','<'],['不等于','!=']]);
				comoboOperField.setValue('=');
			}
		}
	},
	
	initDynamicQueryWidge:function(){

		this.fieldConfig = {[FieldConfig]};
		
		//[-----------------------------------------------------------------------------
		//[--------------------------------初始化下拉属性框--------------------------------
		//[-----------------------------------------------------------------------------
		var lastQueryFieldValue1 = "";
		var lastQueryFieldValue2 = "";
		
		this.comoboQueryField1 = new Ext.form.ComboBox({
			width:135,
			mode:'local',
			triggerAction:'all',
			editable:false,
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :[[QueryFields]]
			}),
			value:lastQueryFieldValue1,
			valueField:'value',
			displayField:'text',
			emptyText:'请选择查询字段'
		});
		this.comoboQueryField2 = new Ext.form.ComboBox({
			width:135,
			mode:'local',
			triggerAction:'all',
			editable:false,
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :[[QueryFields]]
			}),
			value:lastQueryFieldValue2,
			valueField:'value',
			displayField:'text',
			emptyText:'请选择二次查询字段',
			hidden:true
		});
		
		//初始化下拉框事件
		this.comoboQueryField1.on("collapse",function(){
			if(this.comoboQueryField1.getValue()!=lastQueryFieldValue1){
				//设置操作符下拉框
				this.setOperField(this.comoboQueryField1,this.comoboOperField1);
				//隐藏所有二次查询的Widget
				this.hideAllSecondQueryWidgets();
				//组件隐藏-显示控制
				this.firstQueryWidgetsShowControl();
				//设置控制变量
				lastQueryFieldValue1 = this.comoboQueryField1.getValue();
			}

		},this);
		this.comoboQueryField2.on("collapse",function(){
			if(this.comoboQueryField2.getValue()!=lastQueryFieldValue2){
				//设置操作符下拉框
				this.setOperField(this.comoboQueryField2,this.comoboOperField2);
				//组件隐藏-显示控制
				this.secondQueryWidgetsShowControl();
				//设置控制变量
				lastQueryFieldValue2 = this.comoboQueryField2.getValue();
			}
		},this);
		//]-----------------------------------------------------------------------------
		//]--------------------------------初始化下拉属性框--------------------------------
		//]-----------------------------------------------------------------------------
		
		
		//初始化比较操作符
		var operDate = [['等于','='],['大于','>'],['小于','<'],['不等于','!=']];
		this.comoboOperField1 = new Ext.form.ComboBox({
			mode:'local',
			triggerAction:'all',
			width:70,
			editable:false,
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :operDate
			}),
			value:'=',
			valueField:'value',
			displayField:'text',
			hidden:true
		});
		this.comoboOperField2 = new Ext.form.ComboBox({
			mode:'local',
			triggerAction:'all',
			width:70,
			editable:false,
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :operDate
			}),
			value:'=',
			valueField:'value',
			displayField:'text',
			hidden:true
		});
		
		//初始化值field1
		this.textValueField1 = new Ext.form.TextField({
			width:150,
			allowBlank:false,
			hidden:true
		});
		this.dateValueField1 = new Ext.form.DateField({
			width:150,
			allowBlank:false,
			hidden:true
		});
		this.comboValuexField1 = new Ext.form.ComboBox({
			width:150,
			allowBlank:false,
			
			mode:'local',
			triggerAction:'all',
			valueField:'value',
			displayField:'text',
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :[]
			}),
			hidden:true
		});
		
		//初始化值field2
		this.textValueField2 = new Ext.form.TextField({
			width:150,
			allowBlank:false,
			hidden:true
		});
		this.dateValueField2 = new Ext.form.DateField({
			width:150,
			allowBlank:false,
			hidden:true
		});
		this.comboValuexField2 = new Ext.form.ComboBox({
			width:150,
			allowBlank:false,
			mode:'local',
			triggerAction:'all',
			valueField:'value',
			displayField:'text',
			store : new Ext.data.SimpleStore({
	        	fields : ['text', 'value'],
	        	data :[]
			}),
			hidden:true
		});
		
		//初始化查询按钮
		this.actionQuery = new Ext.Action({
			tooltip:'查询',
			text:'按条件查询',
			iconCls: 'icon-search',
			hidden:true,
			handler:function(){
				/*if(this.isBothValueFieldValid()){
					this.dynamicQuery();
				}
				else{
					Ext.MessageBox.alert("提示","请您根据提示输入正确的查询条件!");
				}*/
				this.dynamicQuery();
			},
			scope:this
		});
		this.actionSecondQuery = new Ext.Action({
			text:'二次查询',
			iconCls: 'icon-search-add',
			tooltip: '添加查询条件',
			hidden:true,
			handler:function(){
				this.comoboQueryField2.show();
				this.comoboQueryField2.setValue("");
				//重置该值才能更新
				lastQueryFieldValue2="";
				this.actionSecondQuery.hide();
			},
			scope:this
		});
		this.actionCancleQueryCondition = new Ext.Action({
			tooltip:'撤销查询条件',
			text:'撤销查询',
			hidden:true,
			iconCls: 'icon-search-delete',
			handler:function(){
				
				if(this.comoboQueryField2&&this.comoboQueryField2.isVisible()){
					this.hideAllSecondQueryWidgets();
					this.actionSecondQuery.show();
				}
				else{
					this.hideAllFirstQueryWidgets();
					this.actionCancleQueryCondition.hide();
					this.actionQuery.hide();
					this.comoboQueryField1.setValue("");
					lastQueryFieldValue1="";
				}
				this.dynamicQuery();
			},
			scope:this
		});
		var propWin = this.propWin;
		var thisWidge = this;
		this.actionAdd = new Ext.Action({
			tooltip:'添加记录',
			text:'添加',
			iconCls:'icon-add',
			handler:function(){
				propWin.setIconClass('icon-add');
				propWin.setTitle('添加记录');
				propWin.[class]FormPanel.widgeProxy.resetWidgeAndId();
				if(thisWidge.parent_id>0){
					propWin.[class]FormPanel.widgeProxy.widgeMap["parent_id"]=thisWidge.parent_id;
				}
				propWin.show(thisWidge.getId());
			}
		});
		this.actionEdit = new Ext.Action({
			tooltip:'编辑记录',
			text:'编辑',
			disabled:true,
			iconCls:'icon-edit',
			handler:function(){
				propWin.setIconClass('icon-edit');
				propWin.setTitle('编辑记录');
				propWin.[class]FormPanel.widgeProxy.resetWidge();
				var prop = {};
				Ext.apply(prop,thisWidge.getSelectRecord().data);
				propWin.show(thisWidge.getId());
				propWin.[class]FormPanel.widgeProxy.setWidgeProperties(propWin.[class]FormPanel.widgeProxy,prop);
			}
		});
		this.actionDelete = new Ext.Action({
			tooltip : '删除记录',
			text : '删除',
			disabled : true,
			iconCls : 'icon-delete',
			handler : function() {

				var recordsToDelete = thisWidge.getAllSelectRecordData();

				if (!recordsToDelete || recordsToDelete.length == 0) {
					return;
				}

				Ext.MessageBox.show({
					title : '删除操作',
					msg : '确定删除选中的' + recordsToDelete.length + '条记录吗？',
					width : 300,
					buttons : Ext.MessageBox.YESNO,
					scope : this,
					icon : Ext.MessageBox.QUESTION,
					animEl : thisWidge.getId(),
					fn : function(btn, text) {
						if (btn === 'yes') {
							thisWidge.body.mask('正在删除...', 'x-mask-loading');
							Ext.Ajax.request({
								url : 'command.htm?cmd=delete[Class]',
								method : 'POST',
								success : function() {
									Ext.MessageBox.show({
										title : '消息',
										msg : '删除数据成功.',
										buttons : Ext.MessageBox.OK,
										animEl : thisWidge.getId(),
										icon : Ext.MessageBox.INFO
									});
									thisWidge.refreshCurrentPage();
									thisWidge.body.unmask();
								},
								failure : function() {
									Ext.MessageBox.show({
										title : '错误',
										msg : '删除数据失败.',
										buttons : Ext.MessageBox.OK,
										animEl : thisWidge.getId(),
										icon : Ext.MessageBox.ERROR
									});
									thisWidge.refreshCurrentPage();
									thisWidge.body.unmask();
								},
								params : {
									json : Ext.util.JSON.encode(recordsToDelete)
								}
							})
						}
					}
				});

			}
		});
		
		this.getSelectionModel().on('selectionchange',function(selectionModel){
			if(this.onSelectedValueChange){
				this.onSelectedValueChange(selectionModel);
			}
			if(this.getSelectRecord()){
				this.actionEdit.setDisabled(false);
				this.actionDelete.setDisabled(false);
			}
			else{
				this.actionEdit.setDisabled(true);
				this.actionDelete.setDisabled(true);
			}
		},this);
		
		this.tbar=[
			this.comoboQueryField1,
			this.comoboOperField1,
			this.textValueField1,
			this.dateValueField1,
			this.comboValuexField1,
			this.comoboQueryField2,
			this.comoboOperField2,
			this.textValueField2,
			this.dateValueField2,
			this.comboValuexField2,
			this.actionCancleQueryCondition,
			this.actionSecondQuery,
			this.actionQuery,
			'-',
			this.actionAdd,
			this.actionDelete,
			this.actionEdit
		];
	},
	initPropertyWindow:function(){
		this.propWin = new [Class]FormWindow();
		this.propWin.on('hide',function(){this.refreshCurrentPage()},this);
	},
	initToolbar:function(){
		this.initPropertyWindow();
		this.initDynamicQueryWidge();
	},
	initComponent:function(){
		this.initClums();
		this.initStore();
		this.initBBar();
		this.initToolbar();
		this.initView();
		[Class]Grid.superclass.initComponent.call(this);
		
[InitOnRender]
	},
	//event
	onDeleteRecord:null
});
Ext.reg('[Class]Grid', [Class]Grid);