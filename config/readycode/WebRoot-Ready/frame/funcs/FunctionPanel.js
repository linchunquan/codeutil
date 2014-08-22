var FuncManager=function(){
	this.funcMap=new HashMap();
};  
Ext.extend(FuncManager, Object, {
    getFunctionPanel:function(funcId){
    	return this.funcMap.get(funcId);
    },
    registerFunction:function(functionPanel){
    //	alert('register function:'+functionPanel.getId());
    	this.funcMap.put(functionPanel.getId(),functionPanel);
    }
});
var FunctionManager=new FuncManager();
FunctionPanel = Ext.extend(Ext.Panel, {
	initComponent : function() {
        FunctionPanel.superclass.initComponent.call(this);  
        FunctionManager.registerFunction(this)
    },  
    closable: true,
    autoScroll:true,
    closeAction:'hide',
    listeners:{
    	close:function(){
    		//alert('close!');
    		this.hide();
    	}
    },
    iconCls: 'icon-cls' 
});

var AppFrame1=Ext.extend(FunctionPanel,{
	initComponent : function() {
        AppFrame1.superclass.initComponent.call(this); 
        this.add([this.nav,this.tabs]);
    }, 
	autoScroll :false,
	id: 'func-appFrame',
	title:'AppFrame',
	layout: 'border',
	nav:null,
	tabs:null
	//,
   // items: [this.nav, this.tabs]
});

AppFrame=Ext.extend(FunctionPanel,{
	initComponent : function() {
        AppFrame.superclass.initComponent.call(this); 
        switch(this.layoutMode){
        	case 0:{
        		this.centerOnlyContainerOpt['items']=[this.center];
        		var centerContainer=new Ext.Container(this.centerOnlyContainerOpt);
        		this.add(centerContainer);
        		break;
        	}
        	case 1:{
        		this.centerContainerOpt['items']=[this.center];
        		
        		var centerContainerOpt={};
        		Ext.apply(centerContainerOpt,this.centerContainerOpt);
        		if(this.noBorder){
        		    centerContainerOpt.margins="0 0 0 0";
        		    centerContainerOpt.border=false;
        		}
        		var centerContainer=new Ext.Panel(centerContainerOpt);
        		
        		this.eastContainerOpt['items']=[this.east];
        		var opt={};
        		Ext.apply(opt,this.eastContainerOpt)
        		if(this.eastWidth){
        			opt.width=this.eastWidth;
        		}
        		if(this.noBorder){
        		    opt.margins="0 0 0 0";
        		    opt.border=false;
        		}
        		var eastContainer=new Ext.Panel(opt);
        		this.add([centerContainer,eastContainer]);
        		break;
        	}
        	case 2:{
        		
        		this.centerCenterOpt['items']=[this.centerCenter];
        		this.centerSouthOpt['items']=[this.centerSouth];
        		this.centerSplicContainerOpt['items']=[this.centerCenterOpt,this.centerSouthOpt];
        		var center=new Ext.Panel(this.centerSplicContainerOpt);
        		this.centerContainerOpt['items']=[center];
        		var centerContainer=new Ext.Panel(this.centerContainerOpt);
        		this.eastContainerOpt['items']=[this.east];
        		var eastContainer=new Ext.Panel(this.eastContainerOpt);
        		this.add([centerContainer,eastContainer]);
        		break;
        	}
        	case 3:{
        		this.centerCenterOpt['items']=[this.centerCenter];
        		this.centerSouthOpt['items']=[this.centerSouth];
        		this.centerSplicContainerOpt['items']=[this.centerCenterOpt,this.centerSouthOpt];
        		var center=new Ext.Panel(this.centerSplicContainerOpt);
        		this.centerContainerOpt['items']=[center];
        		var centerContainer=new Ext.Panel(this.centerContainerOpt);
        		this.eastCenterOpt['items']=[this.eastCenter];
        		this.eastSouthOpt['items']=[this.eastSouth];
        		this.eastSplicContainerOpt['items']=[this.eastCenterOpt,this.eastSouthOpt];
        		var east=new Ext.Panel(this.eastSplicContainerOpt);
        		this.eastContainerOpt['items']=[east];
        		var eastContainer=new Ext.Panel(this.eastContainerOpt);		
        		this.add([centerContainer,eastContainer]);
        		break;
        	}
        	//9-18
        	case 4:{
        		this.centerContainerOpt['items']=[this.center];
        		var centerContainer=new Ext.Panel(this.centerContainerOpt);
        		this.eastCenterOpt['items']=[this.eastCenter];
        		this.eastSouthOpt['items']=[this.eastSouth];
        		this.eastSplicContainerOpt['items']=[this.eastCenterOpt,this.eastSouthOpt];
        		var east=new Ext.Panel(this.eastSplicContainerOpt);
        		this.eastContainerOpt['items']=[east];
        		var eastContainer=new Ext.Panel(this.eastContainerOpt);	
        		this.add([centerContainer,eastContainer]);
        		break;
        	}
        	case 5:{
        		this.centerCenterOpt['items']=[this.centerCenter];
        		this.centerSouthOpt['items']=[this.centerSouth];
        		this.centerSplicContainerOpt['items']=[this.centerCenterOpt,this.centerSouthOpt];
        		var center=new Ext.Panel(this.centerSplicContainerOpt);
        		this.centerContainerOpt['items']=[center];
        		var centerContainer=new Ext.Panel(this.centerContainerOpt);
        		this.add([centerContainer]);
        		break;
        	}
        	case 6:{
        		this.centerContainerOpt['items']=[this.center];
        		var centerContainer=new Ext.Panel(this.centerContainerOpt);
        		this.westContainerOpt['items']=[this.west];
        		var opt={};
        		Ext.apply(opt,this.westContainerOpt)
        		if(this.westWidth){
        			opt.width=this.westWidth;
        		}
        		var westContainer=new Ext.Panel(opt);
        		this.add([centerContainer,westContainer]);
        		break;
        	}
        	case 7:{
        		this.centerContainerOpt['items']=[this.center];
        		
        		var cOpt={};
        		Ext.apply(cOpt,this.centerContainerOpt);
        		cOpt.margins="3 3 3 3";
        		
        		var centerContainer=new Ext.Panel(cOpt);
        		this.southContainerOpt['items']=[this.south];
        		var opt={};
        		Ext.apply(opt,this.southContainerOpt)
        		if(this.southHeight){
        			opt.height=this.southHeight;
        		}
        		var southContainer=new Ext.Panel(opt);
        		
        		this.add([cOpt,southContainer]);
        		break;
        	}
        }
    }, 
	autoScroll :false,
	layout: 'border',
	layoutMode:0,
	center:null,
	east:null,
	centerCenter:null,
	centerSouth:null,
	eastCenter:null,
	eastSouth:null,
	centerOnlyContainerOpt:{
		region:"center",
		layout:"fit"
	},
	centerContainerOpt:{
		region: 'center',
	    margins:'3 0 3 3',
		layout:"fit",
		border:false
	},
	eastContainerOpt:{
		region: 'east',
	    width:200,
	    collapsible: true,
	    collapseMode:'mini',
	    split: true,
	    header:false,
	    margins:'3 3 3 0',
	    cmargins:'3 3 3 3',
		layout:"fit",
		border:false
	},
	westContainerOpt:{
		region: 'west',
	    width:400,
	    collapsible: true,
	    collapseMode:'mini',
	    split: true,
	    header:false,
	    margins:'3 0 3 3',
	    cmargins:'3 3 3 3',
		layout:"fit",
		border:false
	},
	southContainerOpt:{
		region: 'south',
		height:200,
	    collapsible: true,
	    collapseMode:'mini',
	    split: true,
	    header:false,
	    margins:'0 3 3 3',
	    cmargins:'3 3 3 3',
		layout:"fit",
		border:false
	},
	centerCenterOpt:{
		region: 'center',
		layout:"fit",
		border:false
	},
	centerSouthOpt:{
		region: 'south',
	    height:200,
		layout:"fit",
		collapsible: true,
	    collapseMode:'mini',
	    split: true,
	    header:false,
		border:false
	},
	eastCenterOpt:{
		region: 'center',
		layout:"fit",
		border:false
	},
	eastSouthOpt:{
		region: 'south',
	    //height:250,
		height:200,
		layout:"fit",
		collapsible: true,
	    collapseMode:'mini',
	    split: true,
	    header:false,
		border:false
	},
	centerSplicContainerOpt:{
		layout:'border',
        border:false
	},
	eastSplicContainerOpt:{
		layout:'border',
        border:false
	}
});
