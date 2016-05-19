/**
 * Created by Martin on 2016/4/12.
 */
function constServiceCtor($filter) {
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
        lengthChange: false,
        pagingType: "simple_numbers",
        pageLength: 20,
        serverSide: true,
        deferLoading: 0,
        processing: true,
        dom: 'Brtip',
        buttons: [
            {extend: 'csv', text: '导出CSV'}
        ],
        language: {
            sInfo: "显示 _START_ - _END_条，共 _TOTAL_ 条",
            sInfoEmpty: "显示 0 - 0条，共 0 条",
            sEmptyTable: "没有查找到数据",
            sLoadingRecords: "加载中...",
            sProcessing: "处理中...",
            sZeroRecords: "没找到匹配的记录",
            oPaginate: {
                sFirst: "第一页",
                sLast: "最后页",
                sNext: "下一页>",
                sPrevious: "<上一页"
            },
            oAria: {
                sSortAscending: ": 升序",
                sSortDescending: ": 降序"
            }
        }
    };

    vm.buildOption = function (url, bindWhere) {
        return angular.extend({
            ajax: {
                url: url,
                dataSrc: "rows",
                data: bindWhere
            }
        }, vm.defaultOptions);
    }
}

angular
    .module('inspinia')
    .service('Constants', constServiceCtor);