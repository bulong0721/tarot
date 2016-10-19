/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($resource, $q,$rootScope) {
    var vm = this;

    //获取产品类型
    vm.productOpts = $resource('./product/type/productOpts').query();

    //从后台拿商户类型
    vm.merchantType = $resource('./merchant/typeList4Select').query();

    //从后台拿商户菜系
    vm.merchantCuisine = $resource('./merchant/cuisineList4Select').query();


    //切换商户
    //vm.thisMerchant = {};
    //vm.getSwitchMerchant = function () {
    //    var deferred = $q.defer();
    //    $resource('../admin/merchant/getSwitch').get({}, function (resp) {
    //        if (resp.rows.length > 0) {
    //            vm.thisMerchant = resp.rows[0];
    //        }
    //        deferred.resolve(vm.thisMerchant);
    //    });
    //    return deferred.promise;
    //}

    //切换门店
    vm.thisMerchantStore = {};
    vm.getSwitchMerchantStore = function () {
        var deferred = $q.defer();
        $resource('./merchantStore/getSwitch').get({}, function (resp) {
            if (resp.rows.length > 0) {
                $rootScope.storeInfo = vm.thisMerchantStore = resp.rows[0];
            }
            deferred.resolve(vm.thisMerchantStore);
        });
        return deferred.promise;
    }

    //从后台拿到省列表
    vm.provinces = $resource('./province/list4Select').query();

    //根据省从后台拿市列表
    vm.citys = [];
    vm.getCitysByProvince = function (provinceId) {
        if (provinceId) {
            $resource('./city/listByProvince').get({id: provinceId}, function (resp) {
                var length = resp.rows.length;
                if (length > 0) {
                    vm.citys.splice(0, vm.citys.length);
                    for (var j = 0; j < length; j++) {
                        vm.citys.push({name: resp.rows[j].name, value: resp.rows[j].id});
                    }
                }
            });
        }
    }

    //根据市从后台拿区县列表
    vm.districts = [];
    vm.getDistrictsByCity = function (cityId) {
        if (cityId) {
            $resource('./district/listByCity').get({id: cityId}, function (resp) {
                var length = resp.rows.length;
                if (length > 0) {
                    vm.districts.splice(0, vm.districts.length);
                    for (var j = 0; j < length; j++) {
                        vm.districts.push({name: resp.rows[j].name, value: resp.rows[j].id});
                    }
                }
            });
        }
    }

    //根据区县拿商圈
    vm.circles = [];
    //....

    //根据商圈拿商场
    vm.malls = [];
    //....


    //有关列表页 tables&formly事件
    vm.initNgMgrCtrl = function (mgrOpts, scope) {


    };


}

/**
 * cTables
 * */
function cTablesService($resource, NgTableParams, cAlerts, toaster) {
    var vm = this, iDatatable = 0, iEditor = 1;


    vm.initAttrNgMgr = function(mgrData,scope){
        scope.cancelAttr = function (product, attr) {
            var index = product.attributes.indexOf(attr);
            product.attributes.splice(index, 1);
        };

        scope.deleteAttr = function (product, attr) {
            cAlerts.confirm('确定删除?',function(){
                //点击确定回调
                var xhr = $resource(mgrData.api.deleteAttr);
                xhr.save({id: product.id}, attr).$promise.then(function (result) {
                    if (0 != result.status) {
                        scope.toasterManage(scope.toastError,result);
                        return;
                    }
                    scope.toasterManage(scope.toastDeleteSucc);
                    var index = product.attributes.indexOf(attr);
                    product.attributes.splice(index, 1);
                });
            },function(){
                //点击取消回调
            });

        };

        scope.updateAttr = function (product, attr) {
            var xhr = $resource(mgrData.api.updateAttr);
            xhr.save({id: product.id}, attr).$promise.then(function (result) {
                if (0 != result.status) {
                    scope.toasterManage(scope.toastError,result);
                    return;
                }
                scope.toasterManage(scope.toastOperationSucc);
                attr.editing = false;
            });
        };

        scope.insertAttr = function (product) {
            if (!product.attributes) {
                product.attributes = [];
            }
            product.attributes.push({name: '', value: '', editing: true});
        };
    }

    vm.initNgMgrCtrl = function (mgrOpts, scope) {
        scope.toastError = 0, scope.toastOperationSucc = 1, scope.toastDeleteSucc = 2, scope.toastSearchSucc = 3, scope.toastUploadSucc = 4;
        //初始化搜索配置
        scope.where = {};

        //formly配置项
        scope.formData = {
            fields: mgrOpts.fields
        };

        //formly返回
        scope.goDataTable = function () {
            scope.activeTab = iDatatable;
        };

        //提交失败预留
        scope.saveFailed = function (response) {
            scope.toasterManage(scope.toastError, response);
        }

        //弹提示
        scope.toasterManage = function (type, response) {
            switch (type) {
                case scope.toastError://错误
                    toaster.error({body: "出错啦！" + response.statusMessage});
                    break;
                case scope.toastOperationSucc://操作成功
                    toaster.success({body: "操作成功"});
                    break;
                case scope.toastDeleteSucc://删除成功
                    toaster.success({body: "删除成功"});
                    break;
                case scope.toastSearchSucc://查询成功
                    toaster.success({body: "查询成功"});
                    break;
                case scope.toastUploadSucc://查询成功
                    toaster.success({body: "上传成功"});
                    break;
                default :
                    toaster.error({body: response.statusMessage});
            }

        }

        //formly提交
        scope.processSubmit = function () {
            var formly = scope.formData;
            if (formly.form.$valid) {
                formly.options.updateInitialValue();
                var xhr = $resource(mgrOpts.api.update);
                xhr.save({}, formly.model).$promise.then(scope.saveSuccess, scope.saveFailed);
            }
        };

        //初始化配置tabs的show or hide
        scope.activeTab = iDatatable;

        //点击编辑
        scope.goEditor = function (rowIndex) {
            if (rowIndex > -1) {
                var data = scope.tableOpts.data[rowIndex];
                scope.formData.model = angular.copy(data);
                scope.rowIndex = rowIndex;
            } else {
                scope.formData.model = {};
                scope.rowIndex = -1;
            }
            scope.activeTab = iEditor;
        };

        //点击删除
        scope.doDelete = function (rowIndex) {
            cAlerts.confirm('确定删除?', function () {
                //点击确定回调
                if (mgrOpts.api.delete && rowIndex > -1) {
                    var data = scope.tableOpts.data[rowIndex];
                    $resource(mgrOpts.api.delete).save({}, data, function deleteSuccess(response) {
                        if (0 != response.status) {
                            scope.toasterManage(scope.toastError, response);
                            return;
                        }
                        scope.tableOpts.data.splice(rowIndex, 1);//更新数据表
                        scope.toasterManage(scope.toastDeleteSucc);
                    }, scope.saveFailed);
                }
            }, function () {
                //点击取消回调
            });

        };

        //增删改查后处理tables数据
        scope.saveSuccess = function (response) {
            if (0 != response.status) {
                scope.toasterManage(scope.toastError, response);
                return;
            }
            var data = response.dataMap.updateResult;//scope.formData.model;//response.rows[0].updateResult;//

            if (scope.rowIndex < 0) {
                //scope.tableOpts.data.unshift(data);
                scope.tableOpts.data.splice(0, 0, data);
            } else {
                scope.tableOpts.data.splice(scope.rowIndex, 1, data);
            }
            scope.toasterManage(scope.toastOperationSucc);
            scope.goDataTable();
        }

        //tables获取数据
        scope.tableOpts = new NgTableParams({}, {
            counts: [],
            getData: function (params) {
                //params.count(20);
                if (!scope.loadByInit) {
                    return [];
                }
                var xhr = $resource(mgrOpts.api.read);
                var args = angular.extend(params.url(), scope.where);

                return xhr.get(args).$promise.then(function (data) {
                    if (0 != data.status) {
                        scope.toasterManage(scope.toastError, data);
                        return;
                    }
                    scope.toasterManage(scope.toastSearchSucc);
                    params.total(data.recordsTotal);
                    return data.rows ? data.rows : [];
                });
            }
        });

        //搜索tables的数据
        scope.search = function () {
            scope.loadByInit = true;
            scope.tableOpts.page(1);
            scope.tableOpts.reload();
        };
    }
}

/*
 * cfromly
 * */
function cfromlyService(formlyConfig, $window,$q, toaster, $filter,$timeout,formlyValidationMessages,uploads) {
    formlyConfig.extras.errorExistsAndShouldBeVisibleExpression = 'fc.$touched || form.$submitted';
    formlyValidationMessages.addStringMessage('required', '此字段必填');

    //自定义formly Label&input一行显示
    formlyConfig.setWrapper({
        name: 'lineLabel',
        template: [
            '<label ng-hide="hide" for="{{::id}}" class="col-sm-2 control-label">',
            '{{to.label}} {{to.required ? "*" : ""}}',
            '</label>',
            '<div ng-hide="hide" class="col-sm-8">',
            '<formly-transclude></formly-transclude> <div class="validation" ng-if="showError" ng-messages="fc.$error">',
            '<div ng-message="required">此字段必填</div>',
            '<div ng-message="email">无效的邮件地址</div>',
            '<div ng-message="minlength">太短</div>',
            '<div ng-message="maxlength">太长</div>',
            '<div ng-message="{{::name}}" ng-repeat="(name, message) in ::options.validation.messages">',
            '{{message(fc.$viewValue, fc.$modelValue, this)}}',
            '</div></div></div>',
        ].join(' ')
    });

    /*以下 使用forEach*/

    //input
    formlyConfig.setType({
        name: 'c_input',
        extends: 'input',
        wrapper: ['lineLabel', 'bootstrapHasError'],
        template: '<input type="{{options.templateOptions.type}}" class="form-control" id="{{id}}" formly-dynamic-name="id" formly-custom-validation="options.validators" placeholder="{{options.templateOptions.placeholder}}" aria-describedby="{{id}}_description" ng-required="options.templateOptions.required" ng-disabled="options.templateOptions.disabled" ng-model="model[options.key]">'
    });

    //select
    formlyConfig.setType({
        name: 'c_select',
        extends: 'select',
        wrapper: ['lineLabel', 'bootstrapHasError']
    });

    //textarea
    formlyConfig.setType({
        name: 'c_textarea',
        extends: 'textarea',
        wrapper: ['lineLabel', 'bootstrapHasError']
    });

    //checkbox
    formlyConfig.setType({
        name: 'c_checkbox',
        extends: 'checkbox',
        wrapper: ['lineLabel', 'bootstrapHasError']
    });

    //radio
    formlyConfig.setType({
        name: 'c_radio',
        extends: 'radio',
        wrapper: ['lineLabel', 'bootstrapHasError']
    });

    //images
    formlyConfig.setType({
        name: 'c_images',
        template: [
            '<label ng-hide="hide" for="{{::id}}" class="col-sm-2 control-label">',
            '{{to.label}}',
            '</label>',
            '<div ng-hide="hide" class="col-sm-8">',
            '<img ng-if="!Multi"  ng-src="{{model[options.key]}}" src="http://cdn.myee7.com/FuMJj5jpAK8_wd2c0KvdwEmCaATt?imageView2/1/w/150/h/95" />',
            '<span ng-if="to.on == true" ng-click="on()">修改</span>',
            '</div>'
        ].join(' ')
    });

    //ngImgCrop
    formlyConfig.setType({
        name: 'c_img_crop',
        template: [
            '<label for="{{::id}}" class="col-sm-2 control-label">',
            '{{to.label}}',
            '</label>',
            //'<div><img ng-src="{{myCroppedImage}}"/></div>',
            '<div class="col-sm-8"><div style="display: none"><input type="file" id="fileInput" onchange="angular.element(this).scope().handleFileSelect(this)" /></div>',
            '<div class="cropArea">',
            '<img-crop area-type="square" image="myImage" result-image="myCroppedImage"></img-crop>',
            '</div></div>'
        ].join(' '),
        link: function (scope, el, attrs) {
            scope.myImage='';
            scope.myCroppedImage= '';
            scope.handleFileSelect = function(evt) {
                var file = evt.files[0];
                var reader = new FileReader();
                reader.onload = function (evt) {
                    scope.$apply(function($scope){
                        scope.myImage = evt.target.result;
                    });

                };
                reader.readAsDataURL(file);
            };
        }
    });

    /*
     *file
     *多上传fromly配置
     * templateOptions: {
        upAttr:{//添加实时上传属性
                name: 'img',//这个name是用来判断上传文件的类型，不判断为空('') || null 格式：[img|video|txt]
                upType:4,//{2:线上3:七牛4:(2+3)}
                url:'./files/create',//上传后端接口
                param:{//这是url参数
                    type:'file',//自定义属性
                    path:'logo'//自定义属性
            },
            upMore:2//多上传的数量
        }
    }
    *
    *删除某一个资源fromly配置
    *controller:['$scope', function ($scope) {
    * //资源回显
         $scope.editFile = function(call){
             if($scope.model.smallPic){
                call({url:baseUrl.pushUrl+ $scope.model.smallPic})
             }
         };
         //删除资源
         $scope.upRemove = function(index){
            console.log(index);
         }
    */
    formlyConfig.setType({
        name: 'upload',
        extends: 'input',
        wrapper: ['bootstrapLabel', 'bootstrapHasError'],
        template: [
            '<div ng-class="{true: \'uploadFileT\',false: \'uploadFileF\'}[upClass]" ><ul>',
            '<li ng-if="thumbnail && upClass && upType ==\'video\'" ng-repeat="t in thumbnail"><c-video ng-show="t.url"></c-video><i class="btn-icon fa fa-trash-o" ng-click="remove($index)"></i></li>',
            '<li ng-if="thumbnail && upClass && upType ==\'img\'"" ng-repeat="t in thumbnail"><img ng-src="{{t.url}}?imageView2/1/w/200/h/126" /><i class="btn-icon fa fa-trash-o" ng-click="remove($index)"></i></li>',
            '<li ng-if="len"><input ng-model="model[options.key]" />',
            '<img ng-if="upClass" src="http://7xl2nm.com2.z0.glb.qiniucdn.com/FoDIQTAe1QiceIgWPgngRvJaGkeq?imageView2/1/w/200/h/126" />',
            '<span ng-if="progress">{{progress}}%</span></li>',
            '</ul></div>',
        ].join(' '),
        defaultOptions: {
            templateOptions: {
                type: 'file',
                required: true
            }
        },
        link: function (scope, el, attrs) {
            //初始化 [缩略图|多图长度|样式]
            scope.thumbnail = [];
            scope.len = true;
            scope.upClass = false;
            //判断是否upAttr
            if(scope.to.upAttr){
                var t = scope.to.upAttr;

                //判断是否是编辑
                if(scope.model.id){
                    scope.editFile(function(arr){
                        scope.thumbnail.push(arr);
                        ;(scope.thumbnail.length>=scope.to.upAttr.upMore) && (scope.len = false);
                    });
                }

                //判断属性是否完整
                if(!t.param && !t.upType && !t.url){
                    console.error('请完整设置upType[number] url[str] param[obj]');
                    return false;
                }
                scope.upType = t.name;
                scope.upClass = true;
            }
            //上传事件及回调
            el.on("change", function (changeEvent) {
                var file = changeEvent.target.files[0], name = file.name.replace(/.+\./, ""),to = scope.to,ajaxAll = [];
                if(file && file.size > 0 && file.size <= 100000000){
                    //初始化需要上传的资源
                    var fd = new FormData();
                    fd.append('file', file);

                    //判断上传到哪里
                    if(t){
                        //判断类型是否合法
                        var types = $filter('inputType')(name, t.name);
                        if(types.state < 0){
                            $timeout(function () {toaster.error({body: types.error})}, 0);
                            return;
                        }

                        //type{2:线上3:七牛4:(2+3)}
                        switch(t.upType) {
                            case 2:
                                ajaxAll = [uploads.myeeUpload(fd,t,progress)];
                                break;
                            case 3:
                                ajaxAll = [uploads.qnUpload(file,progress)];
                                break;
                            case 4:
                                ajaxAll = [uploads.qnUpload(file,progress),uploads.myeeUpload(fd,t,progress)];
                                break;
                            default:
                                ajaxAll = null;
                        }
                        //上传
                        if(ajaxAll){
                            $q.all(ajaxAll).then(function(res){
                                scope.progress = null;
                                if(t.upMore && t.upMore>0){
                                    switch(t.upType) {
                                        case 2:
                                            scope.thumbnail.push({url:baseUrl.pushUrl+res[0].dataMap.tree.downloadPath})
                                            break;
                                        case 3:
                                            scope.thumbnail.push({url:baseUrl.qiniuCdn+res[0].key})
                                            break;
                                        case 4:
                                            scope.thumbnail.push({url:baseUrl.qiniuCdn+res[0].key})
                                            break;
                                        default:
                                    }
                                }
                                //判断thumbnail长度显示隐藏上传按钮
                                if(scope.thumbnail.length>=t.upMore){
                                    scope.len = false;
                                }
                                angular.element('#'+scope.id)[0].value = '';//初始input value
                                scope.$emit('fileToUpload', res);
                            })
                        }else{
                            console.error('不能上传非法服务器！');
                        }
                        //进度回调
                        function progress(p){
                            scope.progress = parseInt(t.upType == 3?p:(p+50)/2);
                        }
                    }else{
                        scope.model.name = file.name;//资源管理需要同步文件名到name字段的
                        //广播fd开始
                        scope.$emit('fileToUpload', fd);
                        var fileProp = {};
                        for (var properties in file) {
                            if (!angular.isFunction(file[properties])) {
                                fileProp[properties] = file[properties];
                            }
                        }
                        scope.fc.$setViewValue(fileProp);
                        //广播fd结束
                    }
                }else{
                    scope.fc.$setViewValue(undefined);
                    $timeout(function () {toaster.error({body: "不能上传空文件或文件大小限制(100M)！"})}, 0);
                    angular.element('#file')[0].value = '';//清空input[type=file]value[ 垃圾方式 建议不要使用]
                }

            });
            //
            el.on("focusout", function (focusoutEvent) {
                if ($window.document.activeElement.id === scope.id) {
                    scope.$apply(function (scope) {
                        scope.fc.$setUntouched();
                    });
                } else {
                    scope.fc.$validate();
                }
            });

            //删除素材
            scope.remove = function(index){
                scope.thumbnail.splice(index,1);
                scope.len = true;
                scope.upRemove(index)
            }
        }
    });

    //datepicker
    formlyConfig.setType({
        name: 'datepicker',
        template: [
            '<div class="col-sm-8"><p class="input-group">',
            '<input  type="text" id="{{::id}}" name="{{::id}}" ng-model="model[options.key]" class="form-control" ng-click="datepicker.open($event)" uib-datepicker-popup="{{to.datepickerOptions.format}}" is-open="datepicker.opened" datepicker-options="to.datepickerOptions" />',
            '<span class="input-group-btn">',
            '<button type="button" class="btn btn-default" ng-click="datepicker.open($event)" ng-disabled="to.disabled"><i class="fa fa-calendar"></i></button>',
            '</span></p></div>'
        ].join(' '),
        wrapper: ['bootstrapLabel', 'bootstrapHasError'],
        defaultOptions: {
            ngModelAttrs: {},
            className: 'c_datepicker',
            templateOptions: {
                datepickerOptions: {
                    format: 'yyyy.MM.dd',
                    initDate: new Date()
                }
            }
        },
        controller: ['$scope', function ($scope) {
            $scope.datepicker = {};

            $scope.datepicker.opened = false;

            $scope.datepicker.open = function ($event) {
                $scope.datepicker.opened = !$scope.datepicker.opened;
            };
        }]
    });
}
/*
 * cAlerts
 * */

function cAlerts($uibModal) {
    return {
        confirm: function (titile, ok, cancel) {
            $uibModal.open({
                animation: false,
                template: '<alerts data-title="' + titile + '"></alerts>',
                controller: function ($scope, $uibModalInstance) {
                    $scope.ok = function () {
                        $uibModalInstance.close();
                        ok();
                    }

                    $scope.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                        cancel();
                    }
                },
                size: 'sm'
            });
        }
    }
}
/*
 * uploads
 * */
function uploads($qupload,$resource) {
    var token = $resource('./superman/picture/tokenAndKey').get();
    return {
        qnUpload: function (file,progress) {
            //七牛上传
            var files = $qupload.upload({file: file, token: token.dataMap.uptoken})
            files.then(function (res) {

            }, function (res) {

            }, function (evt) {
                progress(Math.floor(100 * evt.loaded / evt.totalSize))
            });
            return files
        },
        myeeUpload: function (file,attr,progress) {
            //木爷服务器上传
            //qiniuPath:qn?qn:
            //attr.param.qiniuPath = 'key'
            var files = $resource(attr.url).save(attr.param, file).$promise;
            files.then(function (res) {
                if (0 != res.status) {
                    $scope.toasterManage($scope.toastError, res);
                    return false;
                }else{
                    return res;
                }
            });
            progress(0)
            return files;
        }
    }
}

/*
* cResource
*
* */
function cResource(){
    //$resource(url)
    return {
        query:function(){

        },
        get:function(){

        },
        save:function(){

        },
        remove:function(){

        }
    }
}

angular
    .module('myee')
    .service('Constants', constServiceCtor)
    .service('cTables', cTablesService)
    .service('cfromly', cfromlyService)
    .factory('uploads', uploads)
    .factory('cAlerts', cAlerts)
    .factory('cResource', cResource)
