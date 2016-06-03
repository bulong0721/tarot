/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($filter, $compile, $resource) {
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
        return $filter('date')(new Date(value), 'yyyy-MM-dd HH:mm:ss');
    };

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

    //先写死，后面再从后台拿
    vm.merchantType = [
        {name: '商场', value: 'AL'},
        {name: '餐饮', value: 'AK'},
        {name: '零售', value: 'AZ'},
        {name: '其他', value: 'AR'},
        {name: '商圈', value: 'CA'}];

    vm.initMgrCtrl = function (mgrData, scope) {
        var vm = scope;
        scope.where = {};
        scope.dtInstance = null;
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

        function saveSuccess(response) {
            if (0 != response.status) {
                return;
            }
            var data = scope.formData.model;
            if(!scope.addNew) {
                scope.dtApi.DataTable.row(scope.rowIndex).remove();
            }
            scope.dtApi.DataTable.rows.add(data).draw();
            scope.goDataTable();
        }

        function saveFailed(response) {
            console.log(response);
            alert('saveFailed');
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

    };
}

angular
    .module('inspinia')
    .service('Constants', constServiceCtor);