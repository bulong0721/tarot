<template>
	<div class="couponView">
		<div class="cw_main">
			<span class="cw_logo" ><img :src="logo" /></span>
			<span class="cw_tit">{{view.priceName}}</span>
			<span class="cw_cop">优惠码：{{view.checkCode}}</span>
			<div class="line">
				<span></span>
				<span></span>
			</div>
			<p>
				<span v-for="d in des" >{{$index+1}}.{{d.n}}</span>
				<span>{{view.priceDescription | des_br 'true'}}.使用效期：{{view.priceStartDate}} ~ {{view.priceEndDate}}</span>
			</p>
		</div>
	</div>
</template>
<script>
	import resource from '../server/resource';
	export default {
		route: {
			data({to,next}){
				resource.post(this,'api/info/getPrice',{id:to.params.id,keyId:to.params.keyId}).then((res) => {
					let view = res.dataMap.result,_des = view.priceDescription.split('<br />'),tmp=[];
					for(var key in _des){
						tmp.push({n:_des[key]})
					}
					next({
						view: view,
						des:tmp
					});
				})
			}
		},
	  	data(){
		    return {
		    	logo: './dist/img/logo.jpg',
		    	view:{},
		    	des:{}
		    }
	  	}
	}
</script>
<style>
	.couponView {height: 100%; width: 100%; position: absolute; background: #ff5721 url('http://www.myee7.com/tarot_test/customerClient/dist/img/coupon_bg.jpg') top left no-repeat;background-size: 100%; padding: 0 20px; box-sizing: border-box;}
	.cw_main {background: #fff; border-radius: 5px; width: 100%; padding-bottom: 20px; margin-top: 30%; position: relative; text-align: center;}
	.cw_main span {display: block;}
	.cw_main .cw_logo {margin-top: -60px; display: inline-block;}
	.cw_main .cw_logo img {border-radius: 50%}
	.cw_main .line {border-bottom: #c2c2c2 1px dashed; position: relative; margin: 65px 30px}
	.cw_main .line span {width: 40px; height: 40px; background: #ff5721; position: absolute; border-radius: 50%;top: -20px;}
	.cw_main .line span:first-child {left: -52px}
	.cw_main .line span:last-child {right: -52px}
	.cw_main .cw_tit {font-size: 28px;margin: 20px 0;}
	.cw_main .cw_cop {font-size: 25px; color: #ff5721}
	.cw_main p {text-align: left; font-size: 20px; margin: 0 30px;}
	.cw_main p span {margin-bottom: 10px;}
	@media screen and (max-width: 415px) {
		.cw_main .cw_logo {margin-top: -35px;}
		.cw_main .cw_logo img {width: 70px; height: 70px}
		.cw_main .cw_tit {font-size: 24px;}
		.cw_main .cw_cop {font-size: 21px;}
		.cw_main .line {margin: 45px 30px;}
		.cw_main p {font-size: 15px;}
	}
</style>
