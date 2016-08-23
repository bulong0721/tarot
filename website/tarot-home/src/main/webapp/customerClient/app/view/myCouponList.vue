<template>
	<div>
		<mt-navbar class="page-part" :selected.sync="status">
			<mt-tab-item id="1">未使用</mt-tab-item>
			<mt-tab-item id="0">已使用</mt-tab-item>
			<mt-tab-item id="2">已过期</mt-tab-item>
		</mt-navbar>
		<mt-spinner type="snake" color="#ff5721" v-if="state"></mt-spinner>

		<ul class="couponList" >
			<li v-for="c in couponList" class="data" :class="{ gray: status == 0 || status == 2  }">
				<div class="ctitle"><h2>{{c.priceName}}</h2><span>NO.{{c.checkCode}}</span></div>
				<div class="ccare">
					<span>注意事项:</span>
					<p v-for="d in c.priceDescription | des_br" v-if="d && $index==0">• {{d}}</p>
					<p>• 使用有效期：{{c.priceStartDate}} ~ {{c.priceEndDate}}</p>
				</div>
				<div class="cmore" @click="goview(c.id)"><span>详情<i class="arrow"></i></span></div>
				<img class="ico" v-if="status==0" :src="state_ico1" />
				<img class="ico" v-if="status==2" :src="state_ico2" />
			</li>
			<li class="nodata" v-if="couponList.length<=0 && !state">暂无数据</li>
		</ul>
	</div>
</template>
<script>
	import resource from '../server/resource';
	export default {
		route: {
			data({to,next}){
				this.mList(this.status)
			}
		},
        methods:{
        	mList(state){
        		//if(!this.data[state]){
        			this.data[state] = [];
					resource.post(this,'api/info/getInfoByStatusAndKeyId',{code:this.code,keyId:this.keyId,status:state}).then((res) => {
						this.state = false;
						if(res.dataMap.keyId){
							localStorage.keyId = res.dataMap.keyId;
						}
						return this.data[state] = res.dataMap.result || [];
					}).then((res) => {
						this.couponList = this.data[state];
					})
				/*}else{
					this.couponList = this.data[state]
				}*/
        	},
    		goview(id){
    			this.$route.router.go('/myCouponView/'+id+'/'+this.keyId);
    		}
        },
		watch:{
			'status': function (n, o) {
				this.state = true
				this.couponList = [];
				this.mList(n)
			}
		},
	  	data(){
		    return {
		    	status: '1',
		    	state_ico1: './dist/img/coupon_ion_ok.png',
		    	state_ico2: './dist/img/coupon_ion_over.png',
		    	couponList:{},
		    	data:[],
		    	keyId:localStorage.keyId || '',
		    	code:resource.urlGet('code'),
		    	state:true
		    }
	  	}
	}
</script>
<style>
	.mint-tab-item-label {border-left: #c2c2c2 1px solid; line-height: 30px;}
	.mint-navbar a:first-child .mint-tab-item-label {border-left:none;}
	.arrow {width: 12px; height: 20px; display: inline-block; background-image:url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAUCAQAAAAT+RSaAAAArUlEQVQoz22QTQsBURSGHx9jZkQkUTY2NjZ+g52dHQssZMEkQkmSfKQkKX/ZOM1i7pn7nt3zdDvvuZBmSBdLRnzD6SVFh4+oflK1eYsakNKqxUvUONyo0uQpakpGqwYPUXMcrWrcRK1wtapwFrXF16oalQ9M7LIUfKcexz4bwZfwXSwF9oJPlOO4xFHwgaKtzY682f8a9fdsFy/ImRUDwTOy+iSPNZPkr/7j2PAPB9UlZe3UlasAAAAASUVORK5CYII=")}
	.couponList {margin: 20px}
	.couponList li.data { /* height: 268px;  */background: #fff url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAEMCAYAAADNkDY1AAAAAXNSR0IArs4c6QAAAlxJREFUeAHtnLtOQkEQhmcOJGLiC9B5KzA+gD0+gI3Ga2zkgSy0s7BQC6OdnTG2NtpppbZqZ2KlQcY9g2YPmwUGtjAh/ylgr/9Zhvn4wzmwTL+HiDCtzzaIWjskPE8kE399vZ4575StuSo1P49IpN5rcKwv0zMPOTkXLOuyBz+zENMLET+V9TXH1hZve6cs26exyh4f3r+2V9AOWHx4sZX5jUpZnY8fH4rNmTXabtJmODkXcgKWg5tU276OjTQKSJmeT6oJAm7qV3NXTldKoYhxBW6ayDKd357JWm2yKMKyOiXFBkP52+XAhUv3O8rkeRiBjnPYX0LHNF8ZAQEH01BHkcaBBCI0Wud3p9GqABpBY/dc+X+cQSO8Mc/PYawN3hiQnYwzaASNoLFNVTJMyQKgETSCRtDor+K0Y9H/Ed8bcRWnR5YkO9P/C8Ab4Y15huN7ozkPun8gJOMMGs3vAu5wBHmIqzhBQECjGSaNHO434n5jgJCvJsOULABvNOMMb/SJqyV4YxAQ0GiGSSMHb4Q3Bgj5ajJMyQLwRjPO8EafuFqCNwYBAY1mmDRy8EZ4Y4CQrybDlCwAbzTjDG/0iasleGMQENBohkkjB2+ENwYI+WoyTMkC8EYzzvBGn7hagjcGAQGNZpg0cvBGeGOAkK8mw5QsAG804wxv9ImrJXhjEBDQaIZJIzfC3sgfQW7Eq129cW3qhoQW4rOirR00lt1GAAcDCpTc+CW3HcEStfJfyuoGGjOX7g/Ri9Hz9WnMmFmoMr5FzFd9xka7dSeOvEdXsjHttvGgxiBbefwANCogiBhjjOsAAAAASUVORK5CYII=') top left repeat-y; margin-bottom: 20px; border-radius: 5px; border-right: #e0e0e4 1px solid; border-bottom: #c1c1c5 1px solid;padding: 25px 25px 10px 40px; box-sizing: border-box; position: relative;}
	/* .couponList li.gray { -webkit-filter: grayscale(100%); -moz-filter: grayscale(100%); -ms-filter: grayscale(100%); -o-filter: grayscale(100%); filter: grayscale(100%);filter: gray;} */
	.couponList .ico {position: absolute; right: 20px; top: 20px;}
	.gray , .gray .ctitle ,.gray .cmore , .gray .cmore i {color: #c2c2c2;}
	.couponList li.data.gray {background: #fff url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAEMCAYAAADNkDY1AAAAAXNSR0IArs4c6QAAAoVJREFUeAHtXD1PwzAQjUukLvyBbuzsZWqH8gNY2GCjP4gBNkZGRjaEkPqhMlSMbLACGxJSJ6LwjELkc5z0Ug9leJGi+Jy7i329l3dJFZuk2PI8N9PpdGyMOUN7H927f+eajsaeXC6XvdVqdY3mqEk5dK5jr7ypsXWY2mHj2OrKmGYOmzfsL2kxZ7RV2yf0L7vd7kW/33+3FmkRsLXWMPyA7mgwGDy7yh0IqmjD+GQ4HApj68g60GzfuPJDSFHrIF0sFr0YB0mWZeeYxo7vRDuCBMbHs9nsBiPZc52YyWRif9M2WwblW+xP2F/TNpaFrp3GUbGrf4Xa66hjUOdh+w42CWIi0Fg3t5r+KhprFCvdTWisKIc6iMaEaAwlRtFHNIIbG+IjThGNCblRZIQUtg+m6BGoseDOnNzoRkNf6rJSlXFjpSrjoWcm145odKNBNCIafG5EEMiNLWLA50Y+N8rbqJSiwRTtgJWqHs58pyqzl+9UZTzIjYiH+o5EbiQ3evgRojqRhJUjRDsgN+rhTG50Mg9NcqOMh54XPLtSJBqJRpsM/PdfnwclePwGubFFEFmpslL1AeTK0WCKdkBu1MOZlaqbu6xUZTQgEY0tYkBuJDdWEOR0RIMp2gG5UQ9ncqOTumjyLY6MB7kR8VDfkciN5EYPP0JUJ5KwcoRoB+RGPZzJjU7moUlulPHQ84JnV4pEI9Fok4H/cOjzoASP3yA3tggiK1VWqj6AXDkaTNEOyI16OP/nShWf1Xy5mVXXxh0p/O0/1oB4xMmDOsNAv6xUMYKrlg7EF1UGxgbLEtzheBi42tqujv20CKtKnOJ4v1Y7oPC7EofttyOZz+d2GY8xdvVSHj8RfJCaPdphJAAAAABJRU5ErkJggg==') top left repeat-y;}
	.cmore {/* position: absolute; bottom: 0; left: 15px; right: 0;  */line-height: 60px; font-size: 17px; border-top: #c2c2c2 1px solid;}
	.cmore span {position: relative; display: block; padding-left: 20px;}
	.cmore i {position: absolute; right: 20px; top: 20px;}
	.ccare {font-size: 20px;margin-top: 30px; overflow: hidden;}
	.ctitle {color:#ff5721; overflow: hidden;}
	.ctitle h2 {font-size: 22px; float: left;}
	.ctitle span {font-size:18px; float: right;}
	.nodata {text-align: center;}
	.kebab-spinner-snake {margin: 10px auto 0;}
	@media screen and (max-width: 415px) {
		.couponList {margin: 10px;}
		.couponList li.data {padding: 15px 15px 5px 30px; /* height: 190px; */}
	    .mint-tab-item-label {line-height: 20px; font-size: 15px;}
	    .ctitle h2 ,.ccare{font-size: 15px;}
	    .ctitle span { font-size: 13px;}
	    .ccare {margin: 15px 0 8px 0;}
	    .cmore {/* left: 10px; */ line-height: 45px; font-size: 15px;margin: 0 -15px 0 -13px;}
	    .cmore i {/* right: 15px; top: 14px; */}
	    .arrow {width: 10px; height: 15px; background-size: 100% 100%}
	    .couponList .ico { right: 10px; top: 10px; width: 100px;}
	}
</style>