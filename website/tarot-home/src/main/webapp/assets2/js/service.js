/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($resource, $filter) {
    var me = this;
    me.routes = $resource('assets/route.json').query();

    me.sizeFormatter = function (value, opts, row) {
        if (!row.leaf || 0 == value) {
            return '-';
        }
        if (value > 1024 * 1024 * 1024) {
            return Math.ceil(value / (1024 * 1024 * 1024)) + 'G';
        }
        if (value > 1024 * 1024) {
            return Math.ceil(value / (1024 * 1024)) + 'M';
        }
        if (value > 1024) {
            return Math.ceil(value / 1024) + 'K';
        }
        return value + 'B';
    }

    me.dateFormatter = function (value, opts, row) {
        return $filter('date')(new Date(value),'yyyy-MM-dd HH:mm:ss');
    }

    me.checkFormatter = function (value, opts, row) {
        return $filter('date')(new Date(value),'yyyy-MM-dd HH:mm:ss');
    }
}

angular
    .module('clover')
    .service('ConstService', constServiceCtor);