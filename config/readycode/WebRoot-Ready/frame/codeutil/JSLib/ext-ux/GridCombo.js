Ext.form.GridComboBox = Ext.extend(Ext.form.TriggerField, {
    /**** configure property ****/
    gridName:null,
    valueField:null,
    displayField:null,
    findCmd:null,
    selTitle:null,
    selWidth:463,
    selHeight:300,
    /****************************/
    editable:false,
    initGrid:function(){
        var thisWidge = this;
        if(!this.grid){
            var grid = this.grid = Ext.create({
                border:false
            },this.gridName);
            
            grid.un("render",grid.dynamicQuery,grid);
            
            var thisWidge = this;
            var valueField = this.valueField;
            
            var win = this.win = new Ext.Window({
                title: this.selTitle,
                closeAction:'hide',
                width:this.selWidth,
                height:this.selHeight,
                layout:'fit',
                modal: true,
                items:[grid],
                buttonAlign:"center",
                buttons : [{
                    text:'选择',
                    handler : function(button, event) {
                        if(grid.getSelectRecord()){
                            thisWidge.setValue(grid.getSelectRecord().data[valueField]);
                            win.hide();
                        }
                        else{
                            Ext.MessageBox.alert("","请选择其中一条记录!");
                        }
                        
                    },
                    scope :this
                },{
                    text:'取消',
                    handler:function(button,event){
                        win.hide();
                    },
                    scope :this
                }]
            });
            
            this.win.on("show", function(){
                //alert('win show!');
                grid.onGetQueryCondition=function(queryCondition){
                   
                    if(thisWidge.parent&&thisWidge.depField){
                        queryCondition.push(thisWidge.depField+"='"+thisWidge.parent.value+"'");
                    }
                }
	            grid.dynamicQuery();
	            
	        }, this );
	        
	        //grid.store.on("load",function(){alert("load!"+thisWidge.gridName);});
	        
	        //双击事件  
		    grid.addListener("rowdblclick",function(grid, rowIndex, columnIndex, e){  
		        if(grid.getSelectRecord()){
                    thisWidge.setValue(grid.getSelectRecord().data[valueField]);
                    win.hide();
                }
                else{
                    Ext.MessageBox.alert("","请选择其中一条记录!");
                }
		    });  
        }
    },
    getValue : function(){
        var value = '';
        if( typeof(this.valueField) === "undefined" || this.valueField === null || this.valueField === ''){         
            value = Ext.form.ComboBox.superclass.getValue.call(this);
        }else{
            value = Ext.isDefined(this.value) ? this.value : '';
        }
        //alert("Value from combo:"+value);
        return value;
    },
    setValue : function(v){
        //alert("v = " + v);
        if(v===this.value){
            return;
        }
        if(typeof(v) === "undefined" || v === null || v === ''){
           this.value = v;
           Ext.form.ComboBox.superclass.setValue.call(this, v);
           if(this.child!=null){
               this.child.setValue(null);
               this.child.depFieldValue=null;
           }
           return;
        }
        //重置级联的子下拉框
        //alert("update child!");
        if(this.child!=null){
            this.child.setValue(null);
            this.child.depFieldValue=v;
            //alert("this.child.depFieldValue:"+this.child.depFieldValue);
        }
        
        var displayField = this.displayField;
        var thisWidge = this;
        var m_params = {cmd:this.findCmd, sort:this.valueField, dir:"ASC",start:0, limit:5, queryCondition:" "+this.valueField+"='"+v+"'"};
            
        Ext.Ajax.request({
            url : 'command.htm',
            method : 'POST',
            params:m_params,
            success : function(response) {
                //alert("response:"+response.responseText);
                var data = Ext.util.JSON.decode(response.responseText).root;
                if(data && data.length>0){
                    if(data.length>0){
                        Ext.form.ComboBox.superclass.setValue.call(thisWidge, data[0][displayField]);
                        thisWidge.value = v;
                        return;
                    }
                }
                Ext.form.ComboBox.superclass.setValue.call(thisWidge, '');
                
            },
            failure : function() {
                Ext.form.ComboBox.superclass.setValue.call(thisWidge, '');
            }
        })
        
        this.value = v;
        return this;
    },
    onTriggerClick:function(){
        this.initGrid();
        var pos = this.getPosition();
        pos[1] = pos[1] + 23;
        this.win.setPosition(pos);
        this.win.show();
    }
});
Ext.reg('gridcombo', Ext.form.GridComboBox);

GridComboBoxUtil = new function(){};
GridComboBoxUtil.assocTowComboBoxes = function(parent, child, depField){
    parent.child = child;
    child.parent = parent;
    child.depField = depField;
}