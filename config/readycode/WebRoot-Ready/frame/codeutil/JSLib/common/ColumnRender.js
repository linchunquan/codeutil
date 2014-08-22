//创建函数，用作静态方法
ColumnRender = new function(){};

ColumnRender.renderCheckboxValueYES_NO = function(value, cellmeta, record, rowIndex, columnIndex, store){
	return value?"是":"否";
}

ColumnRender.renderCheckboxValueHAS_HASNOT = function(value, cellmeta, record, rowIndex, columnIndex, store){
	return value?"有":"无";
}
