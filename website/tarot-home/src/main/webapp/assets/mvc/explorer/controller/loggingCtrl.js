/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('loggingCtrl', loggingCtrl);

/**
 * loggingCtrl - controller
 */
loggingCtrl.$inject = ['$scope', 'cTables', 'cfromly'];

function loggingCtrl($scope, cTables, cfromly) {
    var mgrData = {
        fields: [
            {
                key: 'startDate',
                type: 'datepicker',
                templateOptions: {
                    label: '开始日期',
                    type: 'text',
                    required:true ,
                    datepickerPopup: 'yyyy-MM-dd',
                    datepickerOptions: {
                        format: 'yyyy-MM-dd'
                    }
                }
            },
            {
                key: 'endDate',
                type: 'datepicker',
                templateOptions: {
                    label: '结束日期',
                    type: 'text',
                    required:true ,
                    datepickerPopup: 'yyyy-MM-dd',
                    datepickerOptions: {
                        format: 'yyyy-MM-dd'
                    }
                }
            }],
        api: {
            read: '../admin/pushLog/paging'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
    $scope.where.queryObj={};

}