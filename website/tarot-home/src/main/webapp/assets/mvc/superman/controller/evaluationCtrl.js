angular.module('myee', [])
    .controller('evaluationCtrl', evaluationCtrl);

evaluationCtrl.$inject = ['$scope','cTables'];
function evaluationCtrl($scope,cTables) {
    //获取数据配置
    var mgrOpts = {
        api: {
            read: './superman/evaluation/list'
        }
    };

    cTables.initNgMgrCtrl(mgrOpts, $scope);
}