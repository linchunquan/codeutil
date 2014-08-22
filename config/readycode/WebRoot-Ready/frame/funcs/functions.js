Ext.onReady(function(){
	
	var testAppFrame1=new AppFrame({
	    id: 'func-appFrame1',
	    layoutMode: 0,
	    center:new Ext.Panel({
	    	title:"center",
	    	border:false
	    })
	});
	
	var testAppFrame2=new AppFrame({
	    id: 'func-appFrame2',
	    layoutMode: 1,
	    center:new Ext.Panel({
	    	title:"center"
	    }),
	    east:new Ext.Panel({
	    	title:"east"
	    })
	});
	
	var testAppFrame3=new AppFrame({
	    id: 'func-appFrame3',
	    layoutMode: 2,
	    centerCenter:new Ext.Panel({
	    	title:"centerCenter"
	    }),
	    centerSouth:new Ext.Panel({
	    	title:"centerSouth"
	    }),
	    east:new Ext.Panel({
	    	title:"east"
	    })
	});
	
	var testAppFrame4=new AppFrame({
	    id: 'func-appFrame4',
	    layoutMode: 3,
	    centerCenter:new Ext.Panel({
	    	title:"centerCenter"
	    }),
	    centerSouth:new Ext.Panel({
	    	title:"centerSouth"
	    }),
	    eastCenter:new Ext.Panel({
	    	title:"eastCenter"
	    }),
	    eastSouth:new Ext.Panel({
	    	title:"eastSouth"
	    })
	});
	
	var AppFrame5 = Ext.extend(AppFrame, {
		initComponent : function() {
			var eastSouthOpt = {};
			Ext.apply(eastSouthOpt,this.eastSouthOpt);
			eastSouthOpt.height=350;
			this.eastSouthOpt=eastSouthOpt;
	        AppFrame5.superclass.initComponent.call(this);  
	    }
	});
	
	var testAppFrame4_1=new AppFrame5({
	    id: 'func-appFrame5',
	    layoutMode: 3,
	    centerCenter:new Ext.Panel({
	    	title:"centerCenter"
	    }),
	    centerSouth:new Ext.Panel({
	    	title:"centerSouth"
	    }),
	    eastCenter:new Ext.Panel({
	    	title:"eastCenter"
	    }),
	    eastSouth:new Ext.Panel({
	    	title:"eastSouth"
	    })
	});
	
	var testAppFrame4_2=new AppFrame({
	    id: 'func-appFrame6',
	    layoutMode: 3,
	    centerCenter:new Ext.Panel({
	    	title:"centerCenter"
	    }),
	    centerSouth:new Ext.Panel({
	    	title:"centerSouth"
	    }),
	    eastCenter:new Ext.Panel({
	    	title:"eastCenter"
	    }),
	    eastSouth:new Ext.Panel({
	    	title:"eastSouth"
	    })
	});
	
	var testAppFrame5=new AppFrame({
	    id: 'func-appFrame7',
	    layoutMode: 4,
	    eastCenter:new Ext.Panel({
	    	title:"eastCenter"
	    }),
	    eastSouth:new Ext.Panel({
	    	title:"eastSouth"
	    }),
	    center:new Ext.Panel({
	    	title:"eastCenter"
	    })
	});
	
})
