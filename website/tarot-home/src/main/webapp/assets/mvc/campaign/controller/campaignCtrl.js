angular.module('myee', [])
    .controller('campaignCtrl', campaignCtrl);

/**
 * campaignCtrl - controller
 */
campaignCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster'];
function campaignCtrl($scope, Constants,cTables,cfromly,toaster) {
    var mgrData = {
        api: {
            read: './api/info/findHistoryInfoByStoreToday'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function() {
        var data= $scope.where;
        if(data.checkCode==null){
            toaster.warning({ body:"请输入验证码！"});
            return;
        }
        $.post("../api/info/checkCode",{"checkCode":data.checkCode},function(data){
            if(data.status==0){
                toaster.success({ body:"验证成功"});
                $scope.loadByInit = true;
                $scope.tableOpts.reload();
            }else{
                toaster.error({ body:data.statusMessage});
            }
        })

    }
}
