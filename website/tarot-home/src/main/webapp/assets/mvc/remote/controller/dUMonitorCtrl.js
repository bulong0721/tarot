/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('dUMonitorMgrCtrl', dUMonitorMgrCtrl);

/**
 * dUMonitorMgrCtrl - controller
 */
dUMonitorMgrCtrl.$inject = ['$scope','$resource','$uibModal','$interval'];

function dUMonitorMgrCtrl($scope,$resource,$uibModal,$interval) {
    var ms = 3600000,
        vm = $scope.vm = {
            listByStoreId:[],
            summaryVal:'',
            metricVal:'',
            period:[{ts:ms,name:'1 小时'}, {ts:ms*2,name:'2 小时'}, {ts:ms*4,name:'4 小时'}, {ts:ms*12,name:'12 小时'}, {ts:ms*24,name:'24 小时'}, {ts:ms*168,name:'1 周'}, {ts:ms*720,name:'1 月'}, {ts:ms*8760,name:'1 年'}],
            gets:{
                deviceUsedId:'',
                period:ms,
                metricsKeyString:'',
                type:0
            },
            getMetrics:function(gets,call){
                //type == 1 大图
                $resource('../admin/remoteMonitor/deviceUsed/metrics').get(gets, function (resp) {
                    if(resp.rows && resp.rows.length>0) {
                        var r = resp.rows[0];
                        call(
                            vm.gets.type == 1?
                            r.metricInfoList[0]:
                            {
                                deviceUsed: r.deviceUsed,
                                metricInfoList: r.metricInfoList,
                                appInfoList: r.appInfoList[0],
                                summaryUsed: r.summaryUsed
                            }
                        )
                    }
                });
            },
            metrics:function(){
                //Metrics 获取动态指标
                vm.gets.type = 0;
                this.getMetrics(this.gets,function(data){
                    vm.metricVal = data;
                });
            },
            summarys:function(){
                //Summary 获取静态指标
                $resource('../admin/remoteMonitor/deviceUsed/summary').get({
                    deviceUsedId:vm.gets.deviceUsedId
                }, function (resp) {
                    if(resp.rows && resp.rows.length>0) {
                        var r = resp.rows[0];
                        vm.summaryVal = {
                            deviceUsed: r.deviceUsed,
                            appInfoList:r.appInfoList[0],
                            metricInfoList: r.metricInfoList
                        }
                    }
                });
            },
            listByStoreId:function(){
                //listByStoreId 获取机器列表
                $resource('../admin/device/used/listByStoreId').get({}, function (resp) {
                    if(resp.status == '0'){
                        if(resp.rows && resp.rows.length>0){
                            vm.listByStoreId = resp.rows;
                            vm.gets.deviceUsedId = resp.rows[0].id;
                            vm.summarys();
                            vm.metrics();
                        }
                    }
                });
            },
            periodNow:function(val,byStore){
                //切换当前的机器或时间段 byStore(店铺机器)
                if(byStore){
                    //当前机器
                    this.gets.deviceUsedId = val;
                    vm.summarys();
                }else{
                    //时间段
                    this.gets.period = val;
                }
                vm.metrics();
            },
            metricInfoListApp:function(title,res,show){
                //运行的服务 运行的进程
                $uibModal.open({
                    templateUrl: 'metricInfoListApp.html',
                    size: 'lg',
                    controller: function ($scope) {
                        $scope.show = show;
                        $scope.title = title;
                        $scope.apps = res;
                    }
                });
            },
            metricsBig:function(val){
                //获取动态详细指标
                $uibModal.open({
                    templateUrl: 'metricsBig.html',
                    size: 'lg',
                    controller: function ($scope) {
                        $scope.metricBigVal = '';
                        $scope.period = vm.gets.period;
                        vm.gets.metricsKeyString = "['"+val+"']";
                        vm.gets.type = 1;
                        //type == 1;//大图
                        vm.getMetrics(vm.gets,function(data){
                            $scope.metricBigVal =  data;
                        });
                    }
                });
            }
        }

    //初始化
    vm.listByStoreId();
    /*$interval(function () {
        vm.metrics();
    }, 60000);*/
}