/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($filter, $compile, $resource, $state, $q, NgTableParams) {
    var vm = this;

    vm.sizeFormatter = function (value, opts, row) {
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
    };

    vm.dateFormatter = function (value, opts, row) {
        if (value) {
            return $filter('date')(new Date(value), 'yyyy-MM-dd HH:mm:ss');
        }
        return '-';
    };

    vm.productOpts = $resource('/product/type/productOpts').query();

    //从后台拿商户类型
    vm.merchantType = $resource('/admin/merchant/typeList4Select').query();

    //切换门店
    vm.thisMerchant = {};
    vm.getSwitchMerchant = function(){
        var deferred = $q.defer();
        $resource('/admin/merchant/getSwitch').get({}, function (resp) {
            if (resp.rows.length > 0) {
                //vm.flushThisMerchant(resp.rows[0].id, vm.merchants);
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
            //console.log(resp)
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

    vm.initNgMgrCtrl = function(mgrOpts, scope) {
        scope.where = {};
        scope.formData = {
            fields: mgrOpts.fields
        };
        scope.showDataTable = true;
        scope.showEditor = false;

        scope.goDataTable = function () {
            scope.showDataTable = true;
            scope.showEditor = false;
        };

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

        scope.doDelete = function (rowIndex) {
            if (mgrOpts.api.delete && rowIndex > -1) {
                var data = scope.tableOpts.data[rowIndex];
                $resource(mgrOpts.api.delete).save({}, data, saveSuccess, saveFailed);
            }
        };

        function saveSuccess(response) {
            if (0 != response.status) {
                return;
            }
            var data = scope.formData.model;
            if (scope.rowIndex < 0)  {
                scope.tableOpts.data.splice(0, 0, data);
            } else {
                scope.tableOpts.data.splice(scope.rowIndex, 1, data);
            }
            scope.goDataTable();
        }

        function saveFailed(response) {
        }

        scope.processSubmit = function () {
            var formly = scope.formData;
            if (formly.form.$valid) {
                formly.options.updateInitialValue();
                var xhr = $resource(mgrOpts.api.update);
                xhr.save({}, formly.model).$promise.then(saveSuccess, saveFailed);
            }
        };

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

        scope.search = function () {
            scope.loadByInit = true;
            scope.tableOpts.reload();
        };
    };
}

angular
    .module('inspinia')
    .service('Constants', constServiceCtor);