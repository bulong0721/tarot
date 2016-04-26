/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($resource) {
    var me = this;
    me.routes =  $resource('assets/route.json').query();
}

angular
    .module('clover')
    .service('ConstService', constServiceCtor);