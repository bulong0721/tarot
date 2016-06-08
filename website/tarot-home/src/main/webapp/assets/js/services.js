/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($filter, $compile, $resource, $state) {
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

    vm.productOpts = $resource('/product/used/type').query();
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
    vm.merchants = $resource('/admin/merchant/list4Select').query();

    vm.thisMerchant = {};
    $resource('/admin/merchant/getSwitch').get({}, function (resp) {
        //console.log(resp.rows);
        if (resp.rows.length > 0) {
            //console.log("#####merchantsLength:"+vm.merchants.length);
            vm.flushThisMerchant(resp.rows[0].id);//按F5刷新后，vm.merchants为[],导致匹配不成功？？？？？？？？？？？？？？？
        }
        //console.log(vm.thisMerchant)
    });
    vm.flushThisMerchant = function (value) {
        var length = vm.merchants.length;
        for (var i = 0; i < length; i++) {
            if (vm.merchants[i].value == value) {
                break;
            }
        }
        vm.thisMerchant = vm.merchants[i];
        //console.log(vm.thisMerchant);
    }

    //从后台拿到省列表
    vm.provinces = $resource('/admin/province/list4Select').query();

    //根据省从后台拿市列表
    vm.citys = [];
    vm.getCitysByProvince = function (provinceId, scope) {
        if (provinceId) {
            //vm.citys = $resource('/admin/city/listByProvince4Select').query({id:provinceId});//直接query出来的list不能刷新select内容？？？？？？？
            //console.log(vm.citys);
            $resource('/admin/city/listByProvince').get({id: provinceId}, function (resp) {
                var length = resp.rows.length;
                if (length > 0) {
                    vm.citys.splice(0, vm.citys.length);
                    //console.log("clear-citiysLength:" + vm.citys.length);
                    //vm.districts.splice(0,vm.districts.length);//联动会使区出错
                    for (var j = 0; j < length; j++) {
                        vm.citys.push({name: resp.rows[j].name, value: resp.rows[j].id});
                    }
                }
                //console.log("citysLength:" + vm.citys.length);
            });
        }
    }

    //根据市从后台拿区县列表
    vm.districts = [];
    vm.getDistrictsByCity = function (cityId) {
        if (cityId) {
            //vm.districts = $resource('/admin/district/listByCity4Select').query({id:cityId});
            $resource('/admin/district/listByCity').get({id: cityId}, function (resp) {
                var length = resp.rows.length;
                if (length > 0) {
                    vm.districts.splice(0, vm.districts.length);
                    //console.log("clear-districtsLength:"+vm.districts.length);
                    for (var j = 0; j < length; j++) {
                        vm.districts.push({name: resp.rows[j].name, value: resp.rows[j].id});
                    }
                }
                //console.log("districtsLength:"+vm.districts.length);
            });
        }
    }

    //根据区县拿商圈
    vm.circles = [];
    //....

    //根据商圈拿商场
    vm.malls = [];
    //....

    vm.initMgrCtrl = function (mgrData, scope) {
        $.fn.dataTable.ext.errMode = 'none';

        scope.where = {};

        scope.formData = {
            fields: mgrData.fields
        };
        scope.showDataTable = true;
        scope.showEditor = false;

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
                scope.formData.model = data;
                scope.addNew = false;
                scope.rowIndex = rowIndex;
            } else {
                scope.formData.model = {}
            }
            scope.showDataTable = false;
            scope.showEditor = true;
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

        scope.format = function (d) {
            // `d` is the original data object for the row
            var tables = "";
            tables += '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
            angular.forEach(d.productTypeList, function (value) {
                tables += "<tr><td>" + value.type + "</td> <td>" + value.friendlyType + "</td>";
                tables += "<td><a ng-click='goDetailsEditor(\"" + value.type + "\", \"" + value.friendlyType + "\")'><i class='fa fa-pencil'></i></a></td>";
                tables += "<td><a ng-click='goDetailsDelete(\"" + value.type + "\", \"" + d.id + "\")'><i class='fa fa-remove'></i></a></td>";
                tables += "</tr>";
            });
            tables += '</table>';

            var elem = angular.element(tables);
            var content = elem.contents();
            $compile(content)(scope);

            return content;
        }

        scope.goDetailsEditor = function (subId, parentId) {
            alert("subId=" + subId + ", parentId=" + parentId);
        };

        scope.goDetailsDelete = function (subId, parentId) {
            alert("subId=" + subId + ", parentId=" + parentId);
        };

        scope.goDetails = function (rowIndex) {
            if (scope.dtApi && rowIndex > -1) {
                var data = scope.dtApi.DataTable.row(rowIndex).data();
                //var tr = $(this).closest('tr');
                //alert("tr="+tr);
                var row = scope.dtApi.DataTable.row(rowIndex);
                if ( row.child.isShown() ) {
                    // This row is already open - close it
                    row.child.hide();
                    //tr.removeClass('btn-icon fa fa-minus-circle bigger-130');
                }else {
                    // Open this row
                    row.child( scope.format(row.data()) ).show();
                    //tr.addClass('btn-icon fa fa-minus-circle bigger-130');
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