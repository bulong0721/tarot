angular.module('myee', [])
    .controller('campaignCtrl', campaignCtrl);

/**
 * campaignCtrl - controller
 */
campaignCtrl.$inject = ['$scope', 'Constants','cTables','cfromly','toaster','$timeout'];
function campaignCtrl($scope, Constants,cTables,cfromly,toaster,$timeout) {
    var mgrData = {
        api: {
            read: './api/info/findHistoryInfoByStoreToday'
        }
    };
    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.checkCode = function(e) {
        //回车(enter)判断开始
        if(e){if((window.event?e.keyCode:e.which)!=13){return false;}}
        //回车(enter)判断结束

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
                $timeout(function () {toaster.error({ body:data.statusMessage})}, 0);
            }
        })

    }
}
