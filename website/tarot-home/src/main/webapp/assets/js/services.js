/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($filter, $compile, $resource, $state, $q) {
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
    vm.storeOpts = $resource('/admin/merchantStore/storeOpts').query();

    vm.defaultOptions = {
        info: true,
        searching: false,
        lengthChange: false,
        serverSide: true,
        pagingType: "simple_numbers",
        pageLength: 10,
        deferLoading: 0,
        dom: 'Brtip',
        buttons: [
            {extend: 'csv', text: '导出CSV'}
        ],
        statusCode: {
            302: function () {
                $state.go('/');
            }
        },
        language: {
            info: "显示 _START_ - _END_条，共 _TOTAL_ 条",
            infoEmpty: "显示 0 - 0条，共 0 条",
            emptyTable: "没有查找到数据",
            loadingRecords: "加载中...",
            processing: "处理中...",
            zeroRecords: "没找到匹配的记录",
            paginate: {
                first: "第一页",
                last: "最后页",
                next: "下一页>",
                previous: "<上一页"
            },
            aria: {
                sortAscending: ": 升序",
                sortDescending: ": 降序"
            }
        }
    };

    vm.compile4Row = function (row, data, dataIndex) {
        $compile(angular.element(row).contents())($scope);
    };

    vm.buildOption = function (url, bindWhere, compile4Row) {
        return angular.extend(
            {
                ajax: {
                    url: url,
                    dataSrc: "rows",
                    data: bindWhere
                },
                createdRow: compile4Row
            }, vm.defaultOptions);
    };

    //从后台拿商户类型
    vm.merchantType = $resource('/admin/merchant/typeList4Select').query();

    //从后台拿商户列表
    vm.merchants = [];
    vm.getMerchants = function () {
        var deferred = $q.defer();
        $resource('/admin/merchant/list4Select').query({}, function (resp) {
            vm.merchants = resp;
            deferred.resolve(vm.merchants);
        });
        return deferred.promise;
    };


    vm.thisMerchant = {};
    vm.getSwitchMerchant = function(){
        var deferred = $q.defer();
        vm.getMerchants().then(function () {
            $resource('/admin/merchant/getSwitch').get({}, function (resp) {
                if (resp.rows.length > 0) {
                    vm.flushThisMerchant(resp.rows[0].id, vm.merchants);
                    deferred.resolve(vm.thisMerchant);
                }
            });
        });
        return deferred.promise;
    }

    vm.flushThisMerchant = function (value, merchants) {
        var length = merchants.length;
        for (var i = 0; i < length; i++) {
            if (merchants[i].id == value) {
                break;
            }
        }
        vm.thisMerchant = merchants[i];
    }

    //从后台拿店铺列表
    vm.merchantStores = [];
    vm.getMerchantStores = function () {
        var deferred = $q.defer();
        $resource('/admin/merchantStore/listByMerchantForSelect').query({}, function (resp) {
            vm.merchantStores = resp;
            deferred.resolve(vm.merchantStores);
        });
        return deferred.promise;
    };


    vm.thisMerchantStore = {};
    vm.getSwitchMerchantStore = function(){
        var deferred = $q.defer();
        vm.getMerchantStores().then(function () {
            $resource('/admin/merchantStore/getSwitch').get({}, function (resp) {
                if (resp.rows.length > 0) {
                    vm.flushThisMerchantStore(resp.rows[0].id, vm.merchantStores);
                    deferred.resolve(vm.thisMerchantStore);
                }
            });
        });
        return deferred.promise;
    }

    vm.flushThisMerchantStore = function (value, merchantStores) {
        var length = merchantStores.length;
        for (var i = 0; i < length; i++) {
            if (merchantStores[i].value == value) {
                break;
            }
        }
        vm.thisMerchantStore = merchantStores[i];
    }

    //从后台拿到省列表
    vm.provinces = $resource('/admin/province/list4Select').query();

    //根据省从后台拿市列表
    vm.citys = [];
    vm.getCitysByProvince = function (provinceId, scope) {
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

    //自定义店铺模块初始化处理,新增门店显示商户名
    var merchantStoreInit = function (scope) {
        //console.log(vm.thisMerchant)
        if (vm.thisMerchant) {
            scope.formData.model={merchant: {name: vm.thisMerchant.name}};
        }
    };

    vm.initMgrCtrl = function (mgrData, scope, custom) {
        $.fn.dataTable.ext.errMode = 'none';

        scope.where = {};

        scope.formData = {
            fields: mgrData.fields
        };

        scope.formDetailData = {
            detailFields: mgrData.detailFields
        };

        scope.showDataTable = true;
        scope.showEditor = false;

        scope.showInfoEditor = false;
        scope.showDetailEditor = false;

        scope.goDataTable = function () {
            scope.showDataTable = true;
            scope.showEditor = false;
        };

        scope.goEditor = function (rowIndex) {
            var api = this.dtInstance;
            if (api) {
                scope.dtApi = api;
            }
            scope.addNew = true;
            if (scope.dtApi && rowIndex > -1) {
                var data = scope.dtApi.DataTable.row(rowIndex).data();
                console.log(data)
                scope.formData.model = data;
                scope.addNew = false;
                scope.rowIndex = rowIndex;
            } else {
                scope.formData.model = {};
            }
            scope.showDataTable = false;
            scope.showEditor = true;
            scope.showInfoEditor = true;
            scope.showDetailEditor = false;
        };

        scope.goEditorCustom = function (custom, rowIndex) {
            scope.goEditor(rowIndex);

            if (custom) {
                switch (custom) {
                    case "merchantStore":
                        merchantStoreInit(scope);
                        break;
                    case "default":
                        break;
                }
            }
        };

        scope.goDetailEditor = function (index, rowIndex, parentId) {
            scope.addNew = true;
            if (index >= 0 && scope.infoDetail.length > 0) {
                //var data = scope.dtApi.DataTable.row(rowIndex).data();
                scope.formDetailData.model = scope.infoDetail[index];
                scope.addNew = false;
                scope.rowIndex = rowIndex;
            } else {
                scope.rowIndex = rowIndex;
                scope.formDetailData.model = {index: -1, parentId: parentId, id: null, name: null, value: null}
            }
            scope.showDataTable = false;
            scope.showEditor = true;
            scope.showInfoEditor = false;
            scope.showDetailEditor = true;
        };

        scope.doDelete = function (rowIndex) {
            var api = this.dtInstance;
            if (api) {
                scope.dtApi = api;
            }
            scope.addNew = true;
            if (scope.dtApi && mgrData.api.delete && rowIndex > -1) {
                var data = scope.dtApi.DataTable.row(rowIndex).data();
                $resource(mgrData.api.delete).save({}, data, saveSuccess, saveFailed);
            }
        };

        //异步查询详细
        scope.infoDetail = [];
        scope.getInfoDetail = function (parentId, rowIndex) {
            var content = "";
            var deferred = $q.defer();
            if (parentId) {
                //$resource('/product/attribute/listByProductId').get({productId: parentId}, function (resp) {
                $resource(mgrData.api.attributeList).get({parentId: parentId}, function (resp) {
                    var length = resp.rows.length;
                    if (length > 0) {
                        scope.infoDetail.splice(0, scope.infoDetail.length);
                        for (var j = 0; j < length; j++) {
                            scope.infoDetail.push({index: j, parentId: parentId, id: resp.rows[j].id, name: resp.rows[j].name, value: resp.rows[j].value});
                        }
                    }
                    content = scope.format(rowIndex, parentId);
                    console.log(content)
                    deferred.resolve(content)
                });
            }
            return deferred.promise;
        }

        //拼接详细html
        scope.format = function (rowIndex, parentId) {
            // `d` is the original data object for the row
            var tables = "";
            tables += '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
            //tables += "<tr><td>参数名</td> <td>参数值</td><td>编辑</td> <td>删除</td></tr>";
            tables += "<tr><td><a ng-click='goDetailEditor(-1, \"" + rowIndex + "\", \"" + parentId + "\")'><i class='fa fa-plus'></i></a></td></tr>";
            angular.forEach(scope.infoDetail, function (value) {
                tables += "<tr><td>" + value.name + "</td> <td>" + value.value + "</td>";
                tables += "<td><a ng-click='goDetailEditor(\"" + value.index + "\", \"" + rowIndex + "\", \"" + parentId + "\")'><i class='fa fa-pencil'></i></a></td>";
                tables += "<td><a ng-click='goDetailsDelete(\"" + value.id + "\", \"" + rowIndex + "\")'><i class='fa fa-remove'></i></a></td>";
                tables += "</tr>";
            });
            tables += '</table>';
            console.log(tables)
            //编译angular
            var elem = angular.element(tables);
            var content = elem.contents();
            $compile(content)(scope);

            return content;
        }

        //删除详细
        scope.goDetailsDelete = function (subId, rowIndex) {
            //$resource('/product/attribute/delete').delete({id: subId}, function (resp) {
            if (confirm("确定要删除此条数据吗？")) {
                $resource(mgrData.api.attributeDelete).delete({id: subId}, function (resp) {
                    scope.goDetailsRefresh(rowIndex);
                });
            }
        };

        //刷新详细
        scope.goDetailsRefresh = function (rowIndex) {
            console.log(rowIndex)
            if (scope.dtApi && rowIndex > -1) {
                var row = scope.dtApi.DataTable.row(rowIndex);
                scope.infoDetail = [];
                scope.getInfoDetail(row.data().id, rowIndex).then(function (res) {
                    if (scope.infoDetail.length > 0) {
                        row.child(res).show();
                    } else {
                        row.child(res).hide();
                    }
                });
            }
        };

        //显示详细
        scope.goDetails = function (rowIndex) {
            if (scope.dtApi && rowIndex > -1) {
                var row = scope.dtApi.DataTable.row(rowIndex);
                if (row.child.isShown()) {
                    // This row is already open - close it
                    row.child.hide();
                    //tr.removeClass('btn-icon fa fa-minus-circle bigger-130');
                } else {
                    // Open this row
                    //console.log(row.data().attributeList.length)
                    scope.infoDetail = [];
                    scope.getInfoDetail(row.data().id, rowIndex).then(function (res) {
                        //if (scope.infoDetail.length > 0) {
                            row.child(res).show();
                        //} else {
                        //    row.child(res).hide();
                        //}
                    });
                }
            }
        };

        scope.dtInstance = null;
        scope.dtColumns = mgrData.columns;
        scope.dtOptions = vm.buildOption(mgrData.api.read, function (data) {
            angular.extend(data, scope.where);
        }, function (row, data, dataIndex) {
            var content = angular.element(row).contents();
            $compile(content)(scope);
        });

        function saveSuccess(response) {
            if (0 != response.status) {
                return;
            }
            var data = scope.formData.model;
            if (!scope.addNew) {
                scope.dtApi.DataTable.row(scope.rowIndex).remove();
            }
            scope.dtApi.DataTable.rows.add(data).draw(false);
            scope.goDataTable();
        }

        function saveDetailSuccess(response) {
            if (0 != response.status) {
                return;
            }
            //if (!scope.addNew) {
                //scope.dtApi.DataTable.row(scope.rowIndex).remove();
                scope.goDetailsRefresh(scope.rowIndex);
            //}
            scope.goDataTable();
        }

        function saveFailed(response) {
            console.log(response);
        }

        scope.processSubmit = function () {
            var formly = scope.formData;
            if (formly.form.$valid) {
                formly.options.updateInitialValue();
                $resource(mgrData.api.update).save({}, formly.model, saveSuccess, saveFailed);
            }
        };

        scope.processDetailSubmit = function () {
            var formly = scope.formDetailData;
            if (formly.form.$valid) {
                console.log(formly.options)
                formly.options.updateInitialValue();
                $resource(mgrData.api.updateDetail).save({}, formly.model, saveDetailSuccess, saveFailed);
            }
        };

        scope.search = function () {
            var api = this.dtInstance;
            if (api) {
                scope.dtApi = api;
                api.reloadData();
            }
        };

    }
    ;
}

angular
    .module('inspinia')
    .service('Constants', constServiceCtor);