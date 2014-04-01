var exec = require('cordova/exec');
module.exports = {
    location: function(success,fail,act) {
	if(act){
		var action = "stop";
	}else{
		var action = "get";
	}
	var code = {
		"61": "GPS定位结果",
		"62": "扫描整合定位依据失败。此时定位结果无效。",
		"63": "网络异常，没有成功向服务器发起请求。此时定位结果无效。",
		"65": "定位缓存的结果。",
		"66": "离线定位结果。通过调用时对应的返回结果",
		"67": "离线定位失败。通过调用时对应的返回结果",
		"68": "网络连接失败时，查找本地离线定位时对应的返回结果",
		"161": "表示网络定位结果",
		"162": "服务端定位失败。"
	};
	
        exec(function(pos){
		var errcode = pos.LocType;
		if(errcode == 61 || errcode == 65 || errcode == 161){
			success(pos);
		}else{
			if(typeof(fail)!="undefined"){
				if(errcode>=162){
					errcode = 162;
				}
				fail(code[errcode])
			};
		}
	},function(err){},"Baidu", action ,[]);
	}

};
