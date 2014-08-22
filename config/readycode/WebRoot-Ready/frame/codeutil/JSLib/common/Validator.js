//创建函数，用作静态方法
Validator = new function(){};

//定义校验基本类型的正则表达式
Validator.defautValidateConfig={
	"string": {regex:/\S+/         ,        regexText:"请您勿输入全为空格的字符串!"},
	"int"   : {regex:/^[-\+]?\d+$/ ,        regexText:"请您输入正确的整数格式!"},
	"long"  : {regex:/^[-\+]?\d+$/ ,        regexText:"请您输入正确的整数格式!"},
	"double": {regex:/^[-\+]?\d+(\.\d+)?$/ ,regexText:"请您输入正确的浮点数格式!"},
	"outRangeText":	"数值越界！"
};

//基本类型枚举
Validator.basicType={
	"STRING" : 0,
	"INTEGER": 1,
	"LONG"   : 2,
	"DOUBLE" : 3
}

//判断非法空串：null或者未定义是非法空串
Validator.isNotNullValue = function (value){
	if(typeof(value) === "undefined" || value === null){
		return false;
	}
	else {
		return true;
	}
}

//类型校验
Validator.typeValidate = function(value,type){
	//如果是非法空串，赋值""
	if(!Validator.isNotNullValue(value)){
		value	=	"";
	}
    //执行正则表达式判断类型
	var regex 	  = Validator.defautValidateConfig[type].regex;
	var regexText = Validator.defautValidateConfig[type].regexText;
	if(	regex && !regex.test(value)){
		return regexText;
	}
	else{
		return true;
	}
}

//值域校验
Validator.rangeValidate=function(value,range){

	if(value&&range&&Validator.defautValidateConfig["double"].regex.test(value)){
		
		var doubleValue = parseFloat(value);
		
		if(range.min){
			if(doubleValue<range.min){
				return false;
			}
		}
		if(range.max){
			if(doubleValue>range.max){
				return false;
			}
		}
	}
	return true;
}

//校验，包括类型和值域检查
Validator.validate=function(type,range){
	
	return function(value){
		
		//校验输入值为空的情况
		if(value.length < 1 || value === this.emptyText){
	         if(this.allowBlank){
	             this.clearInvalid();
	             return true;
	         }else{
	             return this.blankText;
	         }
	    }
		
		var result = Validator.typeValidate(value,type);
		if(result!==true){
			return result;
		}
		else{
			if(Validator.rangeValidate(value,range)){
				return true;
			}
			else{
			
				return Validator.defautValidateConfig["outRangeText"]+"<br/>"+value+" 不在值区间：["+(Validator.isNotNullValue(range.min)?range.min:"")+","+(Validator.isNotNullValue(range.max)?range.max:"")+"]";
			}
		}
	}
};

Validator.validateString=function(){
	return Validator.validate("string");
};
Validator.validateInt=function(range){
	return Validator.validate("int",range);
};
Validator.validateLong=function(range){
	return Validator.validate("long",range);
};
Validator.validateDouble=function(range){
	return Validator.validate("double",range);;
};

Validator.getBasicValidator = function(basicType,range){
	switch (basicType){
		case Validator.basicType.STRING:{
			return Validator.validateString();
		}
		case Validator.basicType.INTEGER:{
			return Validator.validateInt(range);
		}
		case Validator.basicType.LONG:{
			return Validator.validateLong(range);
		}
		case Validator.basicType.DOUBLE:{
			return Validator.validateDouble(range);
		}
	}
}
