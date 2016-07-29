export function configRouter (router) {
    router.map({
        '/': {
            title:'我的奖券',
            name:'myCouponList',
            component: require('./view/myCouponList.vue')
        },
        '/myCouponView': {
            title:'我的详情',
            name:'myCouponView',
            component: require('./view/myCouponView.vue')
        },
    });

    router.redirect({
        '*':"/myCouponList"
    });

    router.beforeEach(transition => {
        document.title = transition.to.title || 'myee';
        transition.next();
    });


}
