[Class]WidgeProxy = function(cfg){
	var defaultCfg = {
		saveOrUpdateUrl:'command.htm?cmd=saveOrUpdate[Class]',
		updateFromParentUrl:'command.htm?cmd=findAll[Classes]',
		marginLeft:[marginLeft],
		labelWidth:[labelWidth],
		fieldWidth:[fieldWidth],
		widgeWidth:[widgeWidth],
		columns:[columns],
		widgeMap:{
[widgeMap]
		}
	};
	[GridcomboAssoc]
	if(cfg){
		Ext.apply(defaultCfg,cfg);
	}
	[Class]WidgeProxy.superclass.constructor.call(this, defaultCfg);
};
Ext.extend([Class]WidgeProxy,GenericWidgeProxy,{
	createFormPanel:function(cfg){
		return new [Class]FormPanel(cfg);
	}
});
[Class]FormPanel = Ext.extend(Ext.Panel,{
	frame:true,
	initComponent:function(){
		this.widgeProxy = new [Class]WidgeProxy();
		this.widgeProxy.wrapAsForm(this);	
		//this.widgeProxy.wrapAsFieldSet(this);
		if(this.showButton){
			this.buttonAlign = "center",
		    this.buttons = [{
		        text:'保存',
		        handler : function(button, event) {
					var m_widetProxy = button.ownerCt.ownerCt.widgeProxy;
					m_widetProxy.handleSaveAction(button,false);
				},
		        scope :this
		    },{
		        text:'重置',
		        handler:function(button,event){
		        	//var m_widetProxy =  button.ownerCt.ownerCt.hotelFormPanel.widgeProxy;
		        	var m_widetProxy =  button.ownerCt.ownerCt.widgeProxy;
		        	m_widetProxy.resetWidge();
		        },
		        scope :this
		    }];
		}
		[Class]FormPanel.superclass.initComponent.call(this);
	}
});
[Class]FormWindow = Ext.extend(Ext.Window,{
    title: "窗口",
	closeAction:'hide',
	width:[windowWidth],
	modal: true,
	initFormPanel:function(){
		return new [Class]FormPanel({
	        style:{
	        	margin: '-1px'
	        }
		});
	},
	initWindowItems:function(){
		this.[class]FormPanel=this.initFormPanel();
		this.items=[this.[class]FormPanel];
	},
	initComponent : function(){
		this.initWindowItems();
        [Class]FormWindow .superclass.initComponent.call(this);
    },
    buttonAlign:"center",
    buttons:[{
        text:'保存',
        handler : function(button, event) {
        	var m_widetProxy =  button.ownerCt.ownerCt.[class]FormPanel.widgeProxy;
			m_widetProxy.handleSaveAction(button,true);
		},
        scope :this
    },{
        text:'重置',
        handler:function(button,event){
        	var m_widetProxy =  button.ownerCt.ownerCt.[class]FormPanel.widgeProxy;
        	m_widetProxy.resetWidge();
        },
        scope :this
    },{
        text: '取消',
        handler:function(button,event){
        	button.ownerCt.ownerCt.hide();
        },
        scope :this
    }]
});