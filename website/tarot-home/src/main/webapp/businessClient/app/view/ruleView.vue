<template>
	<div id="ruleview" v-show="storeId">
		<ul>
			<li class="list"><div><label class="t"><em>*</em>奖品名称</label></div><div><input type="text" placeholder="请输入奖品名称" maxlength="20" v-model="v.name" /><div></li>
			<li class="list"><div><label class="t"><em>*</em>奖品数量</label></div><div><input type="number" placeholder="请输入奖品数量" v-model="v.total" min="0"/><div></li>
			<li class="list"><div><label><em>*</em>使用有效期</label></div><div class="date"><input type="date" v-model="v.startDate" placeholder="开始时间" min="{{today}}"> - <input type="date" v-model="v.endDate" placeholder="结束时间" min="{{today}}"><div></li>
			<li class="list"><div><label>使用说明1</label></div><div><textarea v-model="v.des1" rows="3" cols="20" placeholder="请输入使用说明1" maxlength="30"></textarea><div></li>
			<li class="list"><div><label>使用说明2</label></div><div><textarea v-model="v.des2" rows="3" cols="20" placeholder="请输入使用说明2" maxlength="30"></textarea><div></li>
			<li class="list"><div><label>使用说明3</label></div><div><textarea v-model="v.des3" rows="3" cols="20" placeholder="请输入使用说明3" maxlength="30"></textarea><div></li>
		</ul>
		<div class="too"><button @click="save()">确定</button></div>
	</div>
</template>
<script>
	import Vue from 'vue';
	import resource from '../server/resource';
	import Toast from 'mint-ui/lib/toast';
	import 'mint-ui/lib/toast/style.css';
	Vue.component(Toast.name, Toast);

	export default {
		route: {
			data({to,next}){
				let r = resource.getData(),data = null;
					if(r){
						let d = r.description.split('<br />');
						data = {
							id:r.id,
							name:r.name,
							total:r.total,
							startDate:r.startDate,
							endDate:r.endDate,
							des1:d[0],
							des2:d[1],
							des3:d[2]
						}
					}
				next({
					v: data
				});
			}
		},
        methods :{
        	save(){
        		let v = this.v;
		    	if(!v || !v.name || !v.endDate || !v.startDate){
		    		Toast('请按照规则填写，并提交');
		    		return false;
		    	}else{
		    		let des = [];

		    		v.des1 && des.push(v.des1);
		    		v.des2 && des.push(v.des2);
		    		v.des3 && des.push(v.des3);

					let data = {
					    "store": {
					        "id":this.storeId
					    },
					    "prices": [
					         {
					         	"id":v.id || '',
					            "level":"",
					            "name":v.name,
					            "description":des.join('<br />'),
								"startDate":v.startDate,
								"endDate":v.endDate,
								"total": v.total || 0
					        }
					    ]
					}

					if(v.startDate>v.endDate){
						Toast('结束日期不能小于开始日期');
						return;
					}

					resource.post(this,'api/activity/saveOrUpdate',{activityJson:JSON.stringify(data)}).then((res) => {
						//res.status == 0?Toast('添加成功'):Toast('添加失败');
						if(res.status == 0){
							this.$route.router.go('/');
							Toast(data.prices[0].id?'修改成功':'添加成功');
						}else{
							Toast(data.prices[0].id?res.statusMessage:'添加失败');
						}
					});
		    	}
        	}
        },
	  	data(){
	  		let d = new Date(),
	  			m = d.getMonth()+1;
		    return {
		    	v:{},
		    	today:d.getFullYear()+'-'+(m>9?m:'0'+m)+'-'+d.getDate(),
		    	storeId:resource.urlGet('storeId')
		    }
	  	}
	}
</script>
<style>
	#ruleview {width: 60%; margin:0 auto;font-size: 18px;}
	#ruleview ul {margin: 30px 0;}
	#ruleview ul li {margin-bottom: 20px;}
	#ruleview ul label {text-align: right; margin-right: 20px;vertical-align: top;display: block;position: relative;}
	#ruleview ul label.t {margin-top: 8px;}
	#ruleview input,#ruleview textarea {width: 100%;font-size: 18px;}
	#ruleview input {padding: 8px 5px}
	#ruleview .too {text-align: center; margin-top: 15px;}
	#ruleview em {position: absolute; left: 0;color:#f3402a;font-size: 35px;line-height: 35px;}
	#ruleview .date input {width: 160px; padding: 3px; text-align: center;}
	.list {display:-moz-box; display:-webkit-box; display:box; width: 100%;}
	.list div {-moz-box-flex:1.0; -webkit-box-flex:1.0; box-flex:1.0;}
	.list div:first-child {max-width:130px; min-width:130px;}
</style>