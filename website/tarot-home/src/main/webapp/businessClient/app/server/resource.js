const Api = {
	url:'/tarot_test/'
}
import Vue from 'vue';
import Toast from 'mint-ui/lib/toast';
import 'mint-ui/lib/toast/style.css';
Vue.component(Toast.name, Toast);

export default {
	ruleList:null,
	post (that,path, params) {
		return new Promise(function(resolve, reject) {
			that.$http.post(Api.url+path, params,{
		        headers: {
		            "X-Requested-With": "XMLHttpRequest",
		        },
		        emulateJSON: true
		    }).then((res) => {
			    resolve(res.json());
			}, (res) => {
				Toast('连接断开，再稍后再试');
			    reject(res);
			});
		});
	},
	getData(data = null){
		return data !== null ? this.ruleList = data : this.ruleList;
	},
	urlGet(name){
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"),
        r = window.location.search.substr(1).match(reg);
        if (r != null){
        	return unescape(r[2]);
        }else{
        	Toast('非法操作');
        	return null;
        }
	}
}
