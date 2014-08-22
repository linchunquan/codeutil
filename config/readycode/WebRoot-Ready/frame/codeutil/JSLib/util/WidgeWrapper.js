//创建函数，用作静态方法
WidgeWrapper = new function(){};

WidgeWrapper.wrapGridAndForm = function(id, grid, form, eastWidth){
	form.widgeProxy.onSuccessSave = function(){
		grid.refreshCurrentPage();
	}
	form.disable();
	grid.onSelectedValueChange=function(selectionModel){
		var prop = {};
		if(grid.getSelectRecord()){
			Ext.apply(prop,grid.getSelectRecord().data);
			form.enable();
			form.widgeProxy.setWidgeProperties(form.widgeProxy,prop);
		}
		else{
			form.disable();
			form.widgeProxy.resetWidgeAndId();
		}		
	}
	var appFrame = new AppFrame({
	    id: id,
	    layoutMode: 1,
	    center: grid,
	    eastWidth: eastWidth,
	    east: form
	});
	return appFrame;
}

WidgeWrapper.wrapGridAndFormOfOneToOneRelation = function(id, grid, form, eastWidth){
	form.disable();
	grid.onSelectedValueChange=function(selectionModel){
		var prop = {};
		if(grid.getSelectRecord()){
			Ext.apply(prop,grid.getSelectRecord().data);
			form.enable();
			form.widgeProxy.updateFromParent(prop.id);
		}
		else{
			form.disable();
			form.widgeProxy.resetWidgeAndId();
		}
	}
	var appFrame=new AppFrame({
	    id: id,
	    layoutMode: 1,
	    center:grid,
	    eastWidth:eastWidth,
	    east:form
	});
	return appFrame;
}

WidgeWrapper.wrapGridAndGridOfOneToManyRelation = function(id, parentGrid, childGrid, eastWidth){
	childGrid.disable();
	parentGrid.onSelectedValueChange=function(selectionModel){
		if(parentGrid.getSelectRecord()){
			childGrid.enable();
			childGrid.parent_id=parentGrid.getSelectRecord().data.id;
			//alert("parent_id1:"+childGrid.parent_id);
			childGrid.dynamicQuery();
		}
		else{
			childGrid.disable();
			//childGrid.parent_id=0;
			//alert("parent_id2:"+childGrid.parent_id);
			//childGrid.dynamicQuery();
		}
	}
	var appFrame=new AppFrame({
	    id: id,
	    layoutMode: 1,
	    center:parentGrid,
	    eastWidth:eastWidth,
	    east:childGrid
	});
	return appFrame;
}

WidgeWrapper.wrapGridAndGridOfOneToManyRelation2 = function(id, parentGrid, childGrid, westWidth){
	childGrid.disable();
	parentGrid.onSelectedValueChange=function(selectionModel){
		if(parentGrid.getSelectRecord()){
			childGrid.enable();
			childGrid.parent_id=parentGrid.getSelectRecord().data.id;
			//alert("parent_id1:"+childGrid.parent_id);
			childGrid.dynamicQuery();
		}
		else{
			childGrid.disable();
			//childGrid.parent_id=0;
			//alert("parent_id2:"+childGrid.parent_id);
			//childGrid.dynamicQuery();
		}
	}
	var appFrame=new AppFrame({
	    id: id,
	    layoutMode: 6,
	    west:parentGrid,
	    westWidth:westWidth,
	    center:childGrid
	});
	return appFrame;
}


WidgeWrapper.wrapGridAndWidges = function(id, grid, widges, eastWidth, parentGrid, centerSouth){
	
	var tabWidges=[];
	Ext.each(widges,function(widge){
	    if(!widge.notAddToTab)
	        tabWidges.push(widge);
	    
	});
	
	
	var widgeTabs = new Ext.TabPanel({
		activeTab: 0,
		items:tabWidges,
		bodyStyle:"padding:3px"
	});
	
	Ext.each(widges,function(widge){
		if(widge.relateType==0){
			widge.widgeProxy.onSuccessSave = function(){
				grid.refreshCurrentPage();
			}
		}
	});

	grid.onSelectedValueChange=function(selectionModel){
		if(grid.getSelectRecord()){
			Ext.each(widges,function(widge){
				var prop = {};
				Ext.apply(prop,grid.getSelectRecord().data);
				widge.enable();
				switch(widge.relateType){
					case 0:
						widge.widgeProxy.setWidgeProperties(widge.widgeProxy,prop);
						break;
					case 1:
						widge.widgeProxy.updateFromParent(prop.id);
						break;
					case 2:
						widge.parent_id=parentGrid.getSelectRecord().data.id;
						widge.dynamicQuery();
						break;
					case 3:
						widge.widgeProxy.updateFromChild(prop[widge.idForParent]);
						break;
				}
			});
		}
		else{
			Ext.each(widges,function(widge){
				widge.disable();
				if(widge.widgeProxy){
					widge.widgeProxy.resetWidgeAndId();
				}
			});
		}
	}
	var appFrame=null;
	if(centerSouth){ 
        appFrame = new AppFrame({
		    id: id,
		    layoutMode: 7,
		    center:grid,
		    southHeight:eastWidth,
		    south:widgeTabs
		});	
	}
	else{
		appFrame = new AppFrame({
		    id: id,
		    layoutMode: 1,
		    center:grid,
		    eastWidth:eastWidth,
		    east:widgeTabs
		});
	}
	return appFrame;
}
