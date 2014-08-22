DataStatusColumn = function(grid, className , cfg){

	var defaultCfg = {
		grid:grid,
		className:className,
		width: 30,
        header: '<div class="task-col-hd"></div>',
        menuDisabled:true,
        fixed: true,
        renderer: function(value, cellmeta, record, rowIndex, columnIndex, store){
        	if(record.data.dataStatus==1){
            	return '<div class="icon-enable"></div>';
        	}
            else{
            	return '<div class="icon-disable""></div>';
            }
        }
	};
	if(cfg){
		Ext.apply(defaultCfg,cfg);
	}
	Ext.apply(this,defaultCfg);
	
	var gridWidge = this.grid;
	var thisWidge = this;
	
	var getRecord = function(t){
		var index = gridWidge.getView().findRowIndex(t);
		return gridWidge.store.getAt(index);
	};
	//
	this.onMousedown=function(e, t){
		
		//e.stopEvent();
        var record = getRecord(t);
            
        if(Ext.fly(t).hasClass('icon-enable')){
           
            record.set('dataStatus', 0);
            record.commit();
            //gridWidge.store.applyFilter();
            //alert("set datastatus:"+0+" for "+thisWidge.className+" json:"+Ext.util.JSON.encode(record.data));
        }
        else if(Ext.fly(t).hasClass('icon-disable')){
            record.set('dataStatus', 1);
            record.commit();
            //gridWidge.store.applyFilter();
            //alert("set datastatus:"+0+" for "+thisWidge.className+" json:"+Ext.util.JSON.encode(record.data));
        }
        Ext.Ajax.request({
			url : 'command.htm?cmd=saveOrUpdate'+thisWidge.className,
			method : 'POST',
			failure : function() {
				Ext.MessageBox.show({
		           title: '错误',
		           msg: '保存数据失败.',
		           buttons: Ext.MessageBox.OK,
		           icon: Ext.MessageBox.ERROR
		        });
		        gridWidge.refreshCurrentPage();
			},
			params : {
				json : Ext.util.JSON.encode(record.data)
			}
		});
    }
}
Ext.extend(DataStatusColumn, Object, {
	
    getRowClass:function(r){
        var d = r.data;
        if(d.dataStatus==0){
            return 'data-disable';
        }
        return '';
    }
});