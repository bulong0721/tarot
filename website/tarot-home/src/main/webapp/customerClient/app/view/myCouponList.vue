<template>
	<div>
		<mt-navbar class="page-part" :selected.sync="status">
			<mt-tab-item id="1">未使用</mt-tab-item>
			<mt-tab-item id="0">已使用</mt-tab-item>
			<mt-tab-item id="2">已过期</mt-tab-item>
		</mt-navbar>
		<ul class="couponList">
			<li v-for="c in couponList" :class="{ gray: status == 0 || status == 2  }">
				<div class="ctitle"><h2>{{c.price.name}}</h2><span>NO.{{c.checkCode}}</span></div>
				<div class="ccare">
					<span>注意事项:</span>
					<p v-for="d in c.price.description | des_br" v-if="d && $index==0">• {{d}}</p>
					<p>• {{c.price.startDate}} ~ {{c.price.endDate}}</p>
				</div>
				<div class="cmore" @click="goview($index)"><span>详情<i class="arrow"></i></span></div>
				<img class="ico" v-if="status==0" :src="state_ico1" />
				<img class="ico" v-if="status==2" :src="state_ico2" />
			</li>
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
        		if(!this.data[state]){
        			this.data[state] = [];
					resource.post(this,'api/info/getInfoByStatusAndKeyId',{code:this.code,keyId:this.keyId,status:state}).then((res) => {
						localStorage.keyId = res.dataMap.keyId || '';
						return this.data[state] = res.dataMap.result;
					}).then((res) => {
						this.couponList = this.data[state];
					})
				}else{
					this.couponList = this.data[state]
				}
        	},
    		goview(index){
    			resource.getData(this.couponList[index]);
    			this.$route.router.go('/myCouponView');
    		}
        },
		watch:{
			'status': function (n, o) {
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
		    	code:resource.urlGet('code')
		    }
	  	}
	}
</script>
<style>
	.mint-tab-item-label {border-left: #c2c2c2 1px solid; line-height: 30px;}
	.mint-navbar a:first-child .mint-tab-item-label {border-left:none;}
	.arrow {width: 12px; height: 20px; display: inline-block; background-image:url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAUCAQAAAAT+RSaAAAArUlEQVQoz22QTQsBURSGHx9jZkQkUTY2NjZ+g52dHQssZMEkQkmSfKQkKX/ZOM1i7pn7nt3zdDvvuZBmSBdLRnzD6SVFh4+oflK1eYsakNKqxUvUONyo0uQpakpGqwYPUXMcrWrcRK1wtapwFrXF16oalQ9M7LIUfKcexz4bwZfwXSwF9oJPlOO4xFHwgaKtzY682f8a9fdsFy/ImRUDwTOy+iSPNZPkr/7j2PAPB9UlZe3UlasAAAAASUVORK5CYII=")}
	.couponList {margin: 20px}
	.couponList li { height: 268px; background: #fff url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAPCAYAAADtc08vAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxODI1MDgzNDUyM0IxMUU2OTZDNUExNUQ5RDhGRjA3OCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoxODI1MDgzNTUyM0IxMUU2OTZDNUExNUQ5RDhGRjA3OCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjE4MjUwODMyNTIzQjExRTY5NkM1QTE1RDlEOEZGMDc4IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjE4MjUwODMzNTIzQjExRTY5NkM1QTE1RDlEOEZGMDc4Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+EHpuFAAAAItJREFUeNpi/B+u+J+BAsDEQCGg2AAWMvX9Z2BkeM7AwHiXVAM+MDAxTWVg55jCuODqC9JcwMj4koGZyYlx6Z1r5IZBNLpmEgxg/MOgEbefglj4z8Jwb5kkZdH468+E/6tCmck34P//EIa1Z9f8j9BQQPEcGUn5LzANbGH4z3iOgen/PcahnxcAAgwA5IAp7zx07LgAAAAASUVORK5CYII=') top left repeat-y; margin-bottom: 20px; border-radius: 5px; border-right: #e0e0e4 1px solid; border-bottom: #c1c1c5 1px solid;padding: 25px 25px 10px 40px; box-sizing: border-box; position: relative;}
	.couponList li.gray { -webkit-filter: grayscale(100%); -moz-filter: grayscale(100%); -ms-filter: grayscale(100%); -o-filter: grayscale(100%); filter: grayscale(100%);filter: gray;}
	.couponList .ico {position: absolute; right: 20px; top: 20px;}
	.cmore {position: absolute; bottom: 0; left: 15px; right: 0; line-height: 60px; font-size: 17px; border-top: #c2c2c2 1px solid;}
	.cmore span {position: relative; display: block; padding-left: 20px;}
	.cmore i {position: absolute; right: 20px; top: 20px;}
	.ccare {font-size: 20px;margin-top: 30px;}
	.ctitle {color:#ff5721; overflow: hidden;}
	.ctitle h2 {font-size: 22px; float: left;}
	.ctitle span {font-size:18px; float: right;}
	@media screen and (max-width: 415px) {
		.couponList {margin: 10px;}
		.couponList li {padding: 15px 15px 5px 30px; height: 190px;}
	    .mint-tab-item-label {line-height: 20px; font-size: 15px;}
	    .ctitle h2 ,.ccare{font-size: 16px;}
	    .ctitle span { font-size: 13px;}
	    .ccare {margin-top: 15px;}
	    .cmore {left: 10px; line-height: 45px; font-size: 15px;}
	    .cmore i {right: 15px; top: 14px;}
	    .arrow {width: 10px; height: 15px; background-size: 100% 100%}
	    .couponList .ico { right: 10px; top: 10px; width: 100px;}
	}
</style>