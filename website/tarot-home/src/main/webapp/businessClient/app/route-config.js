export function configRouter (router) {

    router.map({
        '/': {
            name:'rulelist',
            component: require('./view/ruleList.vue')
        },
        '/ruleview': {
            name:'ruleview',
            component: require('./view/ruleView.vue')
        },
    });

    router.redirect({
        '*':"/rulelist"
    });
}
