angular.module('angular-jqgrid', ['ng']).directive('ngJqgrid', function ($window) {
    return {
        restrict: 'A',
        replace: true,
        scope: {
            config: '=',
            api: '=?'
        },
        link: function (scope, element, attrs) {
            var table, div;
            scope.$watch('config', function (value) {
                element.children().empty();
                table = element;
                value.pager = attrs.pager;
                $(window).on('resize.jqGrid', function () {
                    table.jqGrid( 'setGridWidth', $(".page-content").width() );
                })
                table.jqGrid(value).navGrid(attrs.pager, {
                    edit: true,
                    editicon: 'ace-icon fa fa-pencil blue',
                    add: true,
                    addicon: 'ace-icon fa fa-plus-circle purple',
                    del: true,
                    delicon: 'ace-icon fa fa-trash-o red',
                    search: true,
                    searchicon: 'ace-icon fa fa-search orange',
                    refresh: true,
                    refreshicon: 'ace-icon fa fa-refresh green',
                    view: true,
                    viewicon : 'ace-icon fa fa-search-plus grey'
                });
                scope.vapi = function () {
                    var args = Array.prototype.slice.call(arguments, 0);
                    return table.jqGrid.apply(table, args);
                };
                scope.api = {
                    loadData: function (rows) {
                        if (rows) {
                            table.jqGrid('setGridParam', {data: rows})
                                .trigger('reloadGrid');
                        }
                    }
                };
            });
            scope.$watch('data', function (value) {
                table.jqGrid('setGridParam', {data: value})
                    .trigger('reloadGrid');
            });
        }
    };
})
