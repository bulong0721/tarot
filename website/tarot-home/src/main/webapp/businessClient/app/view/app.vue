<template>
	<div>
		<mt-header title="微信端抽奖设置">
			<mt-button @click="golist" icon="back" slot="left" v-if="$route.path != '/'">返回</mt-button>
		</mt-header>
		<div class="page-header-main">
	    	<router-view class="view"></router-view>
	    </div>
	</div>
</template>

<script>
	import Vue from 'vue';
	import Header from 'mint-ui/lib/header';
	import 'mint-ui/lib/header/style.css';
	Vue.component(Header.name, Header);

	export default {
        methods :{
            golist () {
                this.$route.router.go('/');
            }
        }
	}
</script>

<style>
	body {margin: 0; padding: 0; color: #202020}
	h1,h2,h3,h4,h5,h6 {font-weight: normal;}
	ul,li {margin: 0; padding: 0; list-style: none;}
	::-webkit-input-placeholder  {color:#727272}
	body,input,button,textarea {font-family: 'Microsoft YaHei';font-size: 20px;}
	header.mint-header {height: 76px; background: #4285f4}
	header h1.mint-header-title {font-size: 20px;}
	button,input,textarea {border-radius: 3px; border: none; background: none;}
	input,textarea {border:#cbcbcb 1px solid; padding: 5px;}
	textarea {color:#727272}
	input:focus ,button:focus{outline:none}
	button {border: #c5c5c5 1px solid; color: #737373;}
	table {border: #c5c5c5 1px solid; width: 100%;}
	table td,table th {padding: 18px 0; font-weight: normal;}
	thead th {border-bottom: #d8d8d8 1px solid;}
	tbody tr:nth-child(even) td {background: #fafafa}
	table button {margin-left: 20px; background: #e9e9e9; font-size: 18px; color: #000; padding: 3px 10px;}
	em {font-style: inherit;}
	.tMore {color: #4a90e2}

	.view {
	  transition: all .3s ease;
	}
	.am-enter, .am-leave {
	  opacity: 0;
	  transform: translate3d(0, 10px, 0);
	}
	.v-link-active {
	  color: red;
	}
	[v-cloak] {
	  display: none;
	}
	.too {text-align: right; margin-top: 20px;}
	.too button {padding: 10px 50px;}
	/* .too button:first-child {margin-right: 20px;} */
	.text-left {text-align: left !important;}
	.text-center {text-align: center}
</style>