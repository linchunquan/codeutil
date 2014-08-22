/*var functions =
	{id:"apidocs",iconCls:"icon-collapse-all",text:"系统功能",singleClickExpand:true,children:[
	    {id:"test",text:"应用测试",iconCls:"icon-pkg",singleClickExpand:true, children:[
	        {text:"AppFrame1",id:"appFrame1",iconCls:"icon-cls",tabName:"AppFrame1",leaf:true},
	        {text:"AppFrame2",id:"appFrame2",iconCls:"icon-cls",tabName:"AppFrame2",leaf:true},
	        {text:"AppFrame3",id:"appFrame3",iconCls:"icon-cls",tabName:"AppFrame3",leaf:true},
	        {text:"AppFrame4",id:"appFrame4",iconCls:"icon-cls",tabName:"AppFrame4",leaf:true}
	    ]},
		{text:"基础资料管理",iconCls:"icon-pkg",singleClickExpand:true, children:[]},
		{text:"会展业务管理",iconCls:"icon-pkg",singleClickExpand:true, children:[	
			{href:"#",text:"招展信息发布",id:"ewqave",iconCls:"icon-cls",tabName:"招展信息发布",leaf:true},
			{href:"#",text:"参展报名处理",id:"ewewqq",iconCls:"icon-cls",tabName:"参展报名处理",leaf:true},
			{href:"#",text:"参展事务安排",id:"qwwere",iconCls:"icon-cls",tabName:"参展事务安排",leaf:true},
			{href:"#",text:"会展现场管理",id:"qwwere",iconCls:"icon-cls",tabName:"会展现场管理",leaf:true}
		]},
		{text:"旅游业务管理",iconCls:"icon-pkg",singleClickExpand:true, children:[]}
	]
};*/

functions =	[
	{id:"test",text:"应用测试",iconCls:"icon-pkg",singleClickExpand:true, children:[
	    {text:"AppFrame1",id:"appFrame1",iconCls:"icon-cls",tabName:"AppFrame1",leaf:true},
	    {text:"AppFrame2",id:"appFrame2",iconCls:"icon-cls",tabName:"AppFrame2",leaf:true},
	    {text:"AppFrame3",id:"appFrame3",iconCls:"icon-cls",tabName:"AppFrame3",leaf:true},
	    {text:"AppFrame4",id:"appFrame4",iconCls:"icon-cls",tabName:"AppFrame4",leaf:true},
	    {text:"AppFrame4.1",id:"appFrame5",iconCls:"icon-cls",tabName:"AppFrame4.1",leaf:true},
	    {text:"AppFrame4.2",id:"appFrame6",iconCls:"icon-cls",tabName:"AppFrame4.2",leaf:true},
	    {text:"AppFrame5",id:"appFrame7",iconCls:"icon-cls",tabName:"AppFrame5",leaf:true},
	    {text:"AppFrame6",id:"appFrame8",iconCls:"icon-cls",tabName:"AppFrame6",leaf:true}
	]}
];
FunctionMenu = function() {
    FunctionMenu.superclass.constructor.call(this, {
        id:'api-tree',
        region:'west',
        frame:false,
        split:true,
        width: 180,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins: '-2 0 1 3',
        cmargins:'0 0 0 0',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        //animCollapse:false,
        //animate: false,
        title:'功能菜单',
        iconCls:'icon-accordian',
        collapseMode:'mini',
        loader: new Ext.tree.TreeLoader({
			preloadChildren: true,
			clearOnLoad: false
		}),
        root: new Ext.tree.AsyncTreeNode({
            id:'root',
            text:'root',
            expanded:true,
            children:functions
        }),
        collapseFirst:false
    });
    this.getSelectionModel().on('beforeselect', function(sm, node){
        return node.isLeaf();
    });
};
Ext.extend(FunctionMenu, Ext.tree.TreePanel, {
    initComponent: function(){
        this.hiddenPkgs = [];
        Ext.apply(this, {
            tbar:[ ' ',
			{
                iconCls: 'icon-expand-all',
				tooltip: 'Expand All',
				text:'展开',
                handler: function(){ this.root.expand(true); },
                scope: this
            }, '-', {
                iconCls: 'icon-collapse-all',
                tooltip: 'Collapse All',
                text:'收拢',
                handler: function(){ this.root.collapse(true); },
                scope: this
            }]
        })
        FunctionMenu.superclass.initComponent.call(this);
    }
});
