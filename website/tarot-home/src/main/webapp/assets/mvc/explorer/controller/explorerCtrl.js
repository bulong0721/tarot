angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * explorerCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$filter','cfromly','Constants','cAlerts','toaster'];
function explorerCtrl($scope, $resource, $filter,cfromly,Constants,cAlerts,toaster) {

    var iDatatable = 0, iPush = 1, iEditor = 2;
    $scope.activeTab = iDatatable;
    var nodeTypes =[{name:'目录',value:0},{name:'文件',value:1}];
    $scope.treeData = [{path: '/', name: '/', type: 0, salt: '/'}];
    $scope.expandField = {field: 'name'};
    $scope.treeColumns = [
        {
            displayName: '修改时间',
            columnWidth: '15%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.modified) return "-";
                    return $filter('date')(new Date(data.modified), 'yyyy-MM-dd HH:mm:ss');
                }
            }
        },
        {
            displayName: '类型',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.text(row.branch)}}</span>',
            cellTemplateScope: {
                text: function (data) {
                    return data.type == 0 ? '目录' : '文件';
                },
                style: function (data) {
                    return data.type == 0 ? 'label label-primary' : 'label label-danger';
                }
            }
        },
        {
            displayName: '大小',
            columnWidth: '10%',
            cellTemplate: '<span>{{cellTemplateScope.format(row.branch)}}</span>',
            cellTemplateScope: {
                format: function (data) {
                    if (!data.size) return '-';
                    var value = data.size;
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
        },
        {
            displayName: '操作',
            columnWidth: '150',
            cellTemplate: '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-plus" ng-click="cellTemplateScope.add(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-ban" title="文件不能新增"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-pencil" ng-click="cellTemplateScope.edit(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-ban" title="文件夹不能编辑"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.salt == row.branch.storeId && row.branch.path != \'/\'" class="btn-icon fa fa-trash-o" ng-click="cellTemplateScope.delete(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.salt != row.branch.storeId || row.branch.path == \'/\'" class="btn-icon fa fa-ban" title="没有权限删除"></i></a>' +
            '<span class="divider"></span>' +
            '<a><i ng-if="row.branch.type == 1" class="btn-icon fa fa-download" ng-click="cellTemplateScope.download(row.branch)"></i></a>' +
            '<a><i ng-if="row.branch.type == 0" class="btn-icon fa fa-ban" title="文件夹不能下载"></i></a>',
            cellTemplateScope: {
                add: function (data) {
                    $scope.formDataEditor.options.resetModel();
                    $scope.activeTab = iEditor;
                    $scope.formDataEditor.model ={
                        salt:data.salt,
                        path:data.path
                    }
                    $scope.current = data;
                },
                edit: function (data) {
                    $scope.activeTab = iEditor;
                    $scope.formDataEditor.model = {
                        salt:data.salt,
                        name:data.name,
                        path:data.path,
                        type:data.type,
                        editorModel:1
                    }
                    $scope.current = data;
                    $scope.flag1  = true;
                },
                delete: function (data) {
                    $scope.delete(data);
                },
                download: function (data) {
                    $scope.download(data.salt,data.path);
                }
            }
        }
    ];

    $scope.handleSelect = function (data) {
        if (data.type == 0) {
            $resource('../admin/file/search').save({node: data.path}, {}).$promise.then(
                function success(resp) {
                    angular.merge(data.children, resp.rows);
                }
            );
        }
    };

    //点击推送按钮时调用
    $scope.goSend = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        recursionTree(data, arraySelected);
        if(arraySelected.length == 0){
            toaster.warning({ body:"请选择文件！"});
            return;
        }
        var content = "";
        for(var i in arraySelected){
            content += '{"url": "'+ arraySelected[i].url +'","name": "'+ arraySelected[i].name +'"},';
        }
        var formatJSONStr = $scope.formatJSON("[" + content.substr(0,content.length-1)  + "]", true);
        console.log(formatJSONStr.length)
        if(formatJSONStr.length >= 2000){
            toaster.warning({ body:"一次推送文件过多！"});
            return;
        }
        $scope.activeTab = iPush;
        $scope.formData.model.store = {name: Constants.thisMerchantStore.name};
        $scope.formData.model.content = formatJSONStr;
    };

    //递归出所有选中的文件
    function recursionTree(data, arraySelected){
        angular.forEach(data, function(d){
            if(d.checked == true && d.type == 1){
                arraySelected.push({url: d.url, name: d.name});
            }
            if(d.children.length > 0){
                recursionTree(d.children, arraySelected);
            }
        });
    }

    //查询推送设备下拉框内容
    function getDeviceUsedList() {
        var data = $resource("../deviceUsed/list").query();
        return data;
    }

    //查询推送应用下拉框内容
    function getAppList() {
        return [{name: 'gaea', value: 1}];
    }

    var mgrData = {
        fields: [
            {
                id: 'store.name',
                key: 'store.name',
                type: 'c_input',
                templateOptions: {disabled: true, label: '推送门店', placeholder: '推送门店'}
            },
            {
                key: 'uniqueNo',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '选择推送设备', required: true, options: getDeviceUsedList()}
            },
            {
                key: 'appId',
                type: 'c_select',
                className: 'c_select',
                templateOptions: {label: '选择推送应用', required: true, options: getAppList()}
            },
            {key: 'timeout', type: 'datepicker', templateOptions: {label: '过期时间', placeholder: '过期时间',type: 'text', datepickerPopup: 'yyyy-MM-dd', datepickerOptions: {format: 'yyyy-MM-dd'}}},
            {
                key: 'storagePath',
                type: 'c_input',
                ngModelAttrs: {
                    maxlen: {
                        attribute: 'maxlength'
                    }
                },
                templateOptions: {label: '终端存储路径', placeholder: '终端存储路径(长度小于50)', maxlen:50}
            },
            {
                key: 'content',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'},
                    maxlen: { attribute: 'maxlength' }
                },
                templateOptions: {label: '推动内容(长度小于2000)', required: true, placeholder: '推动内容(长度小于2000)', rows: 20, style: 'max-width:1000px', maxlen:2000}
            }
        ],
        api: {
            //read: '../device/used/paging',
            push: '../admin/file/push',
            download: '../admin/file/download',
            delete: '../admin/file/delete',
        }
    };

    var mgrDataEditor = {
        fields: [
            {
                key: 'salt',
                type: 'c_input',
                templateOptions: {disabled: true, label: '隐藏ID', placeholder: '隐藏ID'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return true;   //隐藏
                }
            },
            {
                key: 'editorModel',
                type: 'c_input',
                templateOptions: {disabled: true, label: '编辑模式', placeholder: '编辑模式'},
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return true;   //隐藏
                }
            },
            {
                id: 'path',
                key: 'path',
                type: 'c_input',
                templateOptions: { disabled: true, label: '绝对路径', placeholder: '绝对路径'}
            },
            {
                id: 'name',
                key: 'name',
                type: 'c_input',
                templateOptions: { required: true, label: '节点名称', placeholder: '节点名称'}
            },
            {
                id: 'currPath',
                key: 'currPath',
                type: 'c_input',
                ngModelAttrs: {
                    maxlen: {
                        attribute: 'maxlength'
                    }
                },
                templateOptions: { label: '节点路径', placeholder: '节点路径(长度小于50)',maxlen:50},
                expressionProperties: {
                    'templateOptions.required': 'model.type==0?true:false' ,// disabled when ifEditor is true
                    'templateOptions.disabled': 'model.editorModel==1?true:false' //编辑模式不能修改节点路径
                }
            },
            {
                id: 'type',
                key: 'type',
                type: 'c_select',
                className: 'c_select',
                templateOptions: { required: true, label: '节点类型',  options: nodeTypes },
                expressionProperties: {
                    'templateOptions.disabled': 'model.editorModel==1?true:false' //编辑模式不能修改节点类型
                }
            },
            {
                key: 'resFile',
                type: 'upload',
                name:'',
                templateOptions: {required: false,type: 'file', label: '节点文件' },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.type == 0?true:false;//true新增文件夹时隐藏文件内容输入框 false新增时显示批量修改
                },
                expressionProperties: {
                    'templateOptions.disabled': 'model.ifEditor', // disabled when ifEditor is true
                    //'templateOptions.disabled': 'model.editorModel==1?true:false'
                }
            },
            {
                key: 'ifEditor',
                type: 'c_input',
                className:'formly-min-checkbox',
                templateOptions: {label: '文本编辑', required: false, type: 'checkbox'},
                defaultValue: false,
                hideExpression: function ($viewValue, $modelValue, scope) {
                    var flag = scope.model.editorModel==1?true:false;//是否是编辑模式
                    if(scope.model.ifEditor && flag && $scope.flag1){
                        $scope.showContent($scope.current);
                        $scope.flag1 = false;
                    }
                    return scope.model.type == 0?true:false;//新增文件夹时隐藏
                }
            },
            {
                id: 'content',
                key: 'content',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'},
                    maxlen: { attribute: 'maxlength' }
                },
                templateOptions: {disabled : true,label: '文件内容', placeholder: '文件内容(长度小于2000)',rows: 15,style: 'max-width:500px',maxlen:2000 },
                hideExpression: function ($viewValue, $modelValue, scope) {
                    return scope.model.type == 0?true:false;//true新增文件夹时隐藏文件内容输入框 false新增时显示批量修改
                },
                expressionProperties: {
                    'templateOptions.disabled': '!model.ifEditor' // disabled when ifEditor is false
                }
            }
        ],
        api: {
            getContent: '../admin/content/get',
            create: '../admin/file/create',
            //delete: '../device/used/delete',
        }
    };
    //formly配置项push
    $scope.formData = {
        fields: mgrData.fields
    };

    //formly配置项editor
    $scope.formDataEditor = {
        fields: mgrDataEditor.fields
    };

    //formly提交
    $scope.pushSubmit = function () {
        var formly = $scope.formData;
        if (formly.form.$valid) {
            formly.options.updateInitialValue();
            $resource(mgrData.api.push).save({}, formly.model).$promise.then(
                function success(resp) {
                    if(resp != null && resp.status == 0){
                        toaster.success({ body:resp.statusMessage});
                        $scope.goDataTable();
                    }else{
                        toaster.error({ body:resp.statusMessage});
                    }
                }
            );
        }
    };

    //删除资源
    $scope.delete = function (data) {
        cAlerts.confirm('确定删除?',function(){
            //点击确定回调
            $resource(mgrData.api.delete).save({salt: data.salt, path: data.path}, {}).$promise.then(function success(resp){
                    if(resp != null && resp.status == 0){
                        $scope.deleteDom(data);
                        toaster.success({ body:"删除成功"});
                        $scope.goDataTable();
                    }else{
                        toaster.error({ body:resp.statusMessage});
                    }
            });
        });
    };

    //删除资源dom删除
    $scope.deleteDom = function (data) {
        /*定义变量
        // path:本文件的路径
        // pathLen:路径深度
        // tr:根目录对象 之后对tr目录遍历
        // trIndex:本文件的索引位置
        */
        var path = data.path.split("\\"),pathLen = path.length,tr = $scope.treeData[0].children,trIndex = null;

        //判断是否有删除的文件,如有删除
        if(pathLen>0){
            for(var j=0;j<pathLen;j++){
                pathBreak(tr.length,j);
            }
            tr.splice(trIndex,1);
        }
        //循环查找文件深度
        function  pathBreak(len,j){
            var i = 0;
            while (i < len) {
                if(tr[i].name == path[j]) {
                    tr[i].children.length>0?tr = tr[i].children:trIndex = i
                    break;
                }
                i++;
            }
        }
    }

    $scope.download = function (salt, path) {
        $resource(mgrData.api.download).save({salt: salt, path: path}, {}).$promise.then(function(data) {
            window.location.href = data.rows[0].url;
        });
    };

    //formly提交
    $scope.editorSubmit = function () {
        var formly = $scope.formDataEditor;
        if (formly.form.$valid) {
            var  addFile = $scope.formData_addFile;
            if(!addFile){
                addFile = new FormData();
            }
            if($scope.formDataEditor.model.type ==0){
                $scope.current.children.push({
                    name:$scope.formDataEditor.model.name,
                    path:$scope.current.path+"/"+$scope.formDataEditor.model.currPath,
                    salt:$scope.current.salt,
                    storeId:$scope.current.storeId,
                    url:$scope.current.url+"/"+$scope.formDataEditor.model.currPath,
                    size:0,
                    modified:new Date(),
                    children:[],
                    type:0});
                $scope.goDataTable();
            }else{
                addFile.append('entityText', JSON.stringify($scope.formDataEditor.model));
                $resource(mgrDataEditor.api.create).save({}, addFile).$promise.then(function(res) {
                    if($scope.formDataEditor.model.editorModel==1?true:false){
                        var fileNewName = $scope.formDataEditor.model.name;
                        $scope.current.name = fileNewName;
                        var index = $scope.formDataEditor.model.path.lastIndexOf("\\");
                        var path = $scope.formDataEditor.model.path.substring(0,index);
                        $scope.current.path = path+"\\"+fileNewName;
                    }else{
                        angular.merge($scope.current.children, res.rows);
                    }
                    $scope.goDataTable();
                });
            }

        }
    };
    $scope.formData_addFile = null;
    var unlisten = $scope.$on('fileToUpload', function(event, arg) {
        $scope.formData_addFile = arg;
    });
    $scope.$on('$destroy', unlisten);

    //formly返回
    $scope.goDataTable = function () {
        $scope.activeTab = iDatatable;
    };

    //成功后调用
    function saveSuccess(response) {

    }

    //失败调用
    function saveFailed(response) {
    }

    $scope.showContent = function(data){
        $resource(mgrDataEditor.api.getContent).get({data: data}, {}).$promise.then(function success(resp) {
            if(resp != null && resp.status == 0){
                $scope.formDataEditor.model.content=resp.rows[0].message;
            }else{
                toaster.error({ body:resp.statusMessage});
            }
        });
    }

    $scope.formatJSON = function(txt,compress){
        var indentChar = '    ';
        if(/^\s*$/.test(txt)){
            toaster.error({ body:"数据为空,无法格式化!"});
            return;
        }
        try{var data=eval('('+txt+')');}
        catch(e){
            toaster.error({ body:'数据源语法错误,格式化失败! 错误信息: '+ (e.description,'err')});
            return;
        };
        var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;

        var notify=function(name,value,isLast,indent,formObj){
            nodeCount++;
            for (var i=0,tab='';i<indent;i++ )tab+=indentChar;
            tab=compress?'':tab;
            maxDepth=++indent;
            if(value&&value.constructor==Array){
                draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);
                for (var i=0;i<value.length;i++)
                    notify(i,value[i],i==value.length-1,indent,false);
                draw.push(tab+']'+(isLast?line:(','+line)));
            }else   if(value&&typeof value=='object'){
                draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);
                var len=0,i=0;
                for(var key in value)len++;
                for(var key in value)notify(key,value[key],++i==len,indent,true);
                draw.push(tab+'}'+(isLast?line:(','+line)));
            }else{
                if(typeof value=='string')value='"'+value+'"';
                draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);
            };
        };
        var isLast=true,indent=0;
        notify('',data,isLast,indent,false);
        return draw.join('');
    }
}