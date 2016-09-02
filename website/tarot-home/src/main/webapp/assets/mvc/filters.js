angular
    .module('myee')
    .filter('sizeFormatter', sizeFormatter)
    .filter('dateFormatter', dateFormatter)
    .filter('inputType',inputType)
    .filter('myeeUrlImg',myeeUrlImg)

//计算文件大小的单位
function sizeFormatter() {
    return function (value) {
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
function dateFormatter($filter) {
    return function (value) {
        if (value) {
            return $filter('date')(new Date(value), 'yyyy-MM-dd HH:mm:ss');
        }
        return '-';
    }
}

//根据扩展名列表判断文件是否包含在里面，返回true1/false0
function inputType(){
    var img  = ['png','jpg','jpeg','gif','bmp'],
        video = ['swf','avi','rmvb','mp3','mp4','mp5','mkv','wmv','csf','3gp'],
        txt = ['txt','pdf'],
        material = ['wav','apk','txt','bin','img'];
    return function(val,type){
        switch(type)
        {
            case 'img':
                return img.indexOf(val);
                break;
            case 'video':
                return video.indexOf(val);
                break;
            case 'txt':
                return txt.indexOf(val);
                break;
            case 'material':
                return material.indexOf(val);
                break;
            default:
                return false;
        }
    }
}

//木爷图片链接
function myeeUrlImg($rootScope){
    return function (val){
        return val?$rootScope.Constant.myeeUrlImg+val:$rootScope.Constant.myeeDefaultUrlImg;
    }
}