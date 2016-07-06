/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($resource, $q) {
    var vm = this;

    //获取产品类型
    vm.productOpts = $resource('/product/type/productOpts').query();

    //从后台拿商户类型
    vm.merchantType = $resource('/admin/merchant/typeList4Select').query();

    //切换门店
    vm.thisMerchant = {};
    vm.getSwitchMerchant = function(){
        var deferred = $q.defer();
        $resource('/admin/merchant/getSwitch').get({}, function (resp) {
            if (resp.rows.length > 0) {
                vm.thisMerchant = resp.rows[0];
            }
            deferred.resolve(vm.thisMerchant);
        });
        return deferred.promise;
    }

    //从后台拿店铺列表
    vm.merchantStores = [];
    vm.getMerchantStores = function () {
        var deferred = $q.defer();
        $resource('/admin/merchantStore/list').get({}, function (resp) {
            vm.merchantStores = resp.rows;
            deferred.resolve(vm.merchantStores);
        });
        return deferred.promise;
    };

    //切换门店
    vm.thisMerchantStore = {};
    vm.getSwitchMerchantStore = function(){
        var deferred = $q.defer();
        $resource('/admin/merchantStore/getSwitch').get({}, function (resp) {
            if (resp.rows.length > 0) {
                vm.thisMerchantStore = resp.rows[0];
            }
            deferred.resolve(vm.thisMerchantStore);
        });
        return deferred.promise;
    }

    //从后台拿到省列表
    vm.provinces = $resource('/admin/province/list4Select').query();

    //根据省从后台拿市列表
    vm.citys = [];
    vm.getCitysByProvince = function (provinceId) {
        if (provinceId) {
            $resource('/admin/city/listByProvince').get({id: provinceId}, function (resp) {
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
            $resource('/admin/district/listByCity').get({id: cityId}, function (resp) {
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
    vm.initNgMgrCtrl = function(mgrOpts, scope) {



    };
}

/**
 * cTables
 * */
function cTablesService($resource,NgTableParams,cAlerts){
    var vm = this;
    vm.initNgMgrCtrl = function(mgrOpts, scope) {
        //初始化搜索配置
        scope.where = {};

        //formly配置项
        scope.formData = {
            fields: mgrOpts.fields
        };

        //formly返回
        scope.goDataTable = function () {
            scope.showDataTable = true;
            scope.showEditor = false;
        };

        //提交成功预留
        function saveFailed(response) {
        }

        //formly提交
        scope.processSubmit = function () {
            var formly = scope.formData;
            console.log(formly)
            if (formly.form.$valid) {
                formly.options.updateInitialValue();
                var xhr = $resource(mgrOpts.api.update);
                xhr.save({}, formly.model).$promise.then(saveSuccess, saveFailed);
            }
        };

        //初始化配置tabs的show or hide
        scope.showDataTable = true;
        scope.showEditor = false;

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
            scope.showDataTable = false;
            scope.showEditor = true;
        };

        //点击删除
        scope.doDelete = function (rowIndex) {
            cAlerts.confirm('确定删除?',function(){
                //点击确定回调
                if (mgrOpts.api.delete && rowIndex > -1) {
                    var data = scope.tableOpts.data[rowIndex];
                    $resource(mgrOpts.api.delete).save({}, data, saveSuccess, saveFailed);
                }
            },function(){
                //点击取消回调
            });

        };

        //增删改查后处理tables数据
        function saveSuccess(response) {
            if (0 != response.status) {
                return;
            }
            var data = response.dataMap.updateResult;//scope.formData.model;//response.rows[0].updateResult;//
            if (scope.rowIndex < 0) {
                scope.tableOpts.data.splice(0, 0, data);
            } else {
                scope.tableOpts.data.splice(scope.rowIndex, 1, data);
            }
            scope.goDataTable();
        }

        //tables获取数据
        scope.tableOpts = new NgTableParams({}, {
             counts: [],
             getData: function (params) {
             if (!scope.loadByInit) {
             return [];
             }
             var xhr = $resource(mgrOpts.api.read);
             var args = angular.extend(params.url(), scope.where);
             return xhr.get(args).$promise.then(function (data) {
             params.total(data.recordsTotal);
             return data.rows;
             });
             }
             });

             //搜索tables的数据
             scope.search = function () {
             scope.loadByInit = true;
             scope.tableOpts.reload();
         };
    }
}

/*
* cfromly
* */
function cfromlyService(formlyConfig,$window){
    //自定义formly Label&input一行显示
    formlyConfig.setWrapper({
        name: 'lineLabel',
        template: [
            '<label ng-hide="hide" for="{{::id}}" class="col-sm-2 control-label">',
            '{{to.label}} {{to.required ? "*" : ""}}',
            '</label>',
            '<div ng-hide="hide" class="col-sm-8">',
            '<formly-transclude></formly-transclude>',
            '</div>'
        ].join(' ')
    });

    /*以下 使用forEach*/

    //input
    formlyConfig.setType({
        name: 'c_input',
        extends: 'input',
        wrapper: ['lineLabel', 'bootstrapHasError'],
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

    //file
    formlyConfig.setType({
        name: 'upload',
        extends: 'input',
        wrapper: ['bootstrapLabel', 'bootstrapHasError'],
        defaultOptions: {
            templateOptions: {
                type: 'file',
                required: true
            }
        },
        link: function(scope, el, attrs) {
            el.on("change", function(changeEvent) {
                var file = changeEvent.target.files[0];
                if (file) {
                    var fd = new FormData();
                    fd.append('file', file);
                    scope.$emit('fileToUpload', fd);
                    var fileProp = {};
                    for (var properties in file) {
                        if (!angular.isFunction(file[properties])) {
                            fileProp[properties] = file[properties];
                        }
                    }
                    scope.fc.$setViewValue(fileProp);
                } else {
                    scope.fc.$setViewValue(undefined);
                }
            });
            el.on("focusout", function(focusoutEvent) {
                if ($window.document.activeElement.id === scope.id) {
                    scope.$apply(function(scope) {
                        scope.fc.$setUntouched();
                    });
                } else {
                    scope.fc.$validate();
                }
            });
        }
    });

    //datepicker
    formlyConfig.setType({
        name: 'datepicker',
        template: [
            '<p class="input-group">',
            '<input  type="text" id="{{::id}}" name="{{::id}}" ng-model="model[options.key]" class="form-control" ng-click="datepicker.open($event)" uib-datepicker-popup="{{to.datepickerOptions.format}}" is-open="datepicker.opened" datepicker-options="to.datepickerOptions" />',
            '<span class="input-group-btn">',
            '<button type="button" class="btn btn-default" ng-click="datepicker.open($event)" ng-disabled="to.disabled"><i class="fa fa-calendar"></i></button>',
            '</span></p>'
        ].join(' '),
        wrapper: ['bootstrapLabel', 'bootstrapHasError'],
        defaultOptions: {
            ngModelAttrs: {},
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

function cAlerts($uibModal){
    return {
        confirm:function(titile,ok,cancel){
            $uibModal.open({
                animation: false,
                template: '<alerts data-title="'+titile+'"></alerts>',
                controller: function($scope,$uibModalInstance){
                    $scope.ok = function(){
                        $uibModalInstance.close();
                        ok();
                    }

                    $scope.cancel = function(){
                        $uibModalInstance.dismiss('cancel');
                        cancel();
                    }
                },
                size: 'sm'
            });
        }
    }
}

angular
    .module('myee')
    .service('Constants', constServiceCtor)
    .service('cTables', cTablesService)
    .service('cfromly', cfromlyService)
    .factory('cAlerts', cAlerts)