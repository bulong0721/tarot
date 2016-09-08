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
                return {state:img.indexOf(val),error:'请上传png,jpg,gif,bmp图片格式！'};
                break;
            case 'video':
                return {state:video.indexOf(val),error:'请上传avi,mp4,mkv视频格式！'};
                break;
            case 'txt':
                return {state:txt.indexOf(val),error:'请上传txt,pdf文件格式！'};
                break;
            case 'material':
                return {state:material.indexOf(val),error:'请上传wav,apk,txt,bin,img文件格式！'};
                break;
            default:
                return false;
        }
    }
}

//木爷图片链接
function myeeUrlImg(baseConstant){
    return function (val){
        return val?baseUrl.pushUrl+val:baseConstant.myeeDefaultUrlImg;
    }
}