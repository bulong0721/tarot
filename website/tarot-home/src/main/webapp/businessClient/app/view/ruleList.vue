<template>
	<div id="rulelist" v-show="storeId">
		<span>抽奖活动</span>
		<table v-show="rulelist" cellspacing="0">
			<tbody>
				<tr v-for="l in rulelist">
					<td>
						<label>
							<input type="checkbox" v-model="ck" value="{{l.id}}" :checked="l.activeStatus?true:false" />
							{{l.name}}
						</label>
					</td>
					<td width="10%" class="tMore text-center" @click="goview($index)"><span>编辑</span></td>
					<td  width="10%" class="text-center" @click="delete($index,l.id)"><span>删除</span></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td><button @click="goview()">添加</button></td>
					<td colspan="2"></td>
				</tr>
			</tfoot>
		</table>
		<table v-show="!rulelist" cellspacing="0">
			<tfoot>
				<tr>
					<td><button @click="goview()">添加</button></td>
					<td colspan="2"></td>
				</tr>
			</tfoot>
		</table>
		<div class="too"><button @click="openActivity()">启动</button></div>
	</div>
</template>
<script>
	import Vue from 'vue';
	import resource from '../server/resource';
	import MessageBox from 'mint-ui/lib/message-box';
	import 'mint-ui/lib/message-box/style.css';
	import Toast from 'mint-ui/lib/toast';
	import 'mint-ui/lib/toast/style.css';
	
	Vue.component(MessageBox.name, MessageBox);
	Vue.component(Toast.name, Toast);

	export default {
	  	route: {
		    data({next }){
				resource.post(this,'api/activity/findStoreActivity',{storeId:this.storeId}).then((res) => {
					let r = res.dataMap.result.prices;
					if(r && r.length>0){
						next({
							rulelist:r
						});
					}
				});
		    }
	  	},
        methods :{
            goview (index = null) {
            	resource.getData(index >= 0 && index != null?this.rulelist[index]:'');
                this.$route.router.go('/ruleview');
            },
            delete(index,id){
				MessageBox.confirm('此操作会更改当前抽奖活动,<br />是否继续?').then(action => {
					resource.post(this,'api/price/deleteById',{id:id}).then((res) => {
						if(res.status == 0){
							Toast('删除成功');
							this.rulelist.$remove(this.rulelist[index]);
						}else{
							Toast('删除失败，请稍后再试');
						}
					});
				});
            },
            openActivity(){
				MessageBox.confirm('抽奖设置已经更改,<br />是否继续?').then(action => {
					resource.post(this,'api/activity/openActivity',{storeId:this.storeId,priceIds:this.ck.join(',')}).then((res) => {
						res.status == 0?Toast('启动成功'):Toast('启动失败');
					});
				});
            }
        },
	  	data () {
	    	return {
	     		rulelist: null,
	     		ck:[],
	     		storeId:resource.urlGet('storeId')
	    	}
	  	}
	}
</script>

<style>
	#rulelist {margin: 30px;}
	#rulelist table {margin-top: 10px;}
	#rulelist label {margin-left: 20px}
	#rulelist .too {text-align: right; margin-top: 20px;}
	#rulelist input {-webkit-appearance: none; -moz-appearance: none; appearance: none; outline: 0; background-color: #fff; border: 2px solid #757575; position: relative; width: 25px; height: 25px; vertical-align: top;border-radius: 2px; padding: 0;margin: 0 10px 0 0}
    #rulelist input:checked {background-color: #4a90e2;border-color: #4a90e2;}
	#rulelist input:after {border: 2px solid transparent;border-left: 0;border-top: 0;content: " ";top: 1px;left: 6px;position: absolute;width: 5px;height: 13px;-webkit-transform: rotate(45deg);transform: rotate(45deg);border-color: #fff;}
</style>