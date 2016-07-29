const Api = {
	url:'http://127.0.0.1:8080/'
}
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
				console.log('服务器开小差了');
			    reject(res);
			});
		});
	},
	getData(data = null){
		return data !== null ? this.ruleList = data : this.ruleList;
	}
}
