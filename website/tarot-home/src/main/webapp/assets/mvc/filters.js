angular
    .module('myee')
    .filter('sizeFormatter',sizeFormatter)
    .filter('dateFormatter',dateFormatter)

    //计算文件大小的单位
    function sizeFormatter(){
        return function (value){
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
    }

    //日期格式化
    function dateFormatter($filter){
        return function (value){
            if (value) {
                return $filter('date')(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
            return '-';
        }
    }