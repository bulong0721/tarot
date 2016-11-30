angular
    .module('myee')
    .filter('sizeFormatter', sizeFormatter)
    .filter('dateFormatter', dateFormatter)
    .filter('inputType',inputType)
    .filter('myeeUrlImg',myeeUrlImg)
    .filter('getFileName',getFileName)
    .filter('isNullOrEmptyString',isNullOrEmptyString)
    .filter('isHasProp',isHasProp)
    .filter('toasterManage',toasterManage)

//判断对象是否是空或空字符
function isNullOrEmptyString(){
    return function (obj) {
        if( obj == null){
            return true;
        }
        if( typeof obj == 'string' && obj == ''){
            return true;
        }
        return false;
    }
}

//判断对象是否是{}
function isHasProp(){
    return function (obj) {
        var hasProp = false;
        for (var prop in obj) {
            hasProp = true;
            break;
        }
        return hasProp;
    }
}


//计算文件大小的单位
function sizeFormatter() {
    return function (value) {
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

/**
 * 取文件名
 * @param filepath 文件路径path，带文件名
 * @param type 类型：1:反转字符串;2:取文件名带后缀;3:取文件扩展名;4:取文件名不带后缀;
 * @returns {Function}
 */
function getFileName(){
    //字符串逆转
    function strTurn(str) {
        if (str != "") {
            var str1 = "";
            for (var i = str.length - 1; i >= 0; i--) {
                str1 += str.charAt(i);
            }
            return (str1);
        }
    };

    //取文件名不带后缀
    function getFileNameNoExt(filepath) {
        var pos = strTurn(getFileExt(filepath));
        var file = strTurn(filepath);
        var pos1 = strTurn( file.replace(pos, ""));
        var pos2 = getFileFullName(pos1);
        return pos2;
    }

    //取文件全名名称,取文件名带后缀
    function getFileFullName(filepath) {
        if (filepath != "") {
            var names = filepath.split("\\");
            return names[names.length - 1];
        }
    }

    //取文件后缀名
    function getFileExt(filepath) {
        if (filepath != "") {
            var pos = "." + filepath.replace(/.+\./, "");
            return pos;
        }
    }

    return function (filepath,type){
        switch (type){
            case 1:
                return strTurn(filepath);
            case 2:
                return getFileFullName(filepath);
            case 3:
                return getFileExt(filepath);
            case 4:
                return getFileNameNoExt(filepath);
        }
    }
}

/**
 * 弹提示
 * 0://错误
 * 1://操作成功
 * 2://删除成功
 * 3://查询成功
 * 4://上传成功
 * 5://自定义
 */
function toasterManage(toaster){
    return function(type, message,isSucc){
        console.log(type)
        //var respMessage = response && response != undefined ? (response.statusMessage || '') : "" ;
        message = message || '';
        switch (type) {
            case 0://错误
                toaster.error({body: "出错啦!" + message});
                break;
            case 1://操作成功
                toaster.success({body: "操作成功!"+ message});
                break;
            case 2://删除成功
                toaster.success({body: "删除成功!"+ message});
                break;
            case 3://查询成功
                toaster.success({body: "查询成功!"+ message});
                break;
            case 4://上传成功
                toaster.success({body: "上传成功!"+ message});
                break;
            case 5://自定义
                isSucc?toaster.success({body: message}):toaster.error({body: message});
                break;
            default :
                toaster.error({body: message});
        }
    }
}
