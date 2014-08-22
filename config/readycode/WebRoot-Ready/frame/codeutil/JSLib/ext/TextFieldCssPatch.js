Ext.onReady(function(){

	if(Ext.isIE){
	 	var len = document.getElementsByTagName('INPUT').length;
		var divs=document.getElementsByTagName('INPUT');
		for(var i=0;i<len;i++){
			var cn=divs[i].className;
			var div=divs[i];
			//alert(div.className);
			if( cn .indexOf( 'x-form-field')!=-1 || cn .indexOf( 'x-form-text')!=-1 ){
				div.style.marginTop =-1+ 'px';
				div.style.marginBottom =4+ 'px';
		//		div.style.border='1px solid';
			}
		}
	}
});