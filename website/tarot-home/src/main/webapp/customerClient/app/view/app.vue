<template>
	<div>
    	<router-view class="view" transition="am" transition-mode="out-in"></router-view>
    </div>
</template>
<style>
	body {font-family: 'Microsoft YaHei'; background: #efeff4}
	body,ul,li,h1,h2,h3 {margin: 0; padding: 0;}
	h1,h2,h3 {font-weight: normal;}
	ul,li {list-style: none;}
	i {vertical-align: middle;}
	p {margin: 8px 0}
	.view {transition: all .2s ease;}
	.am-enter, .am-leave {opacity: 0;transform: translate3d(0, 5px, 0);}
	[v-cloak] {display: none;}
</style>