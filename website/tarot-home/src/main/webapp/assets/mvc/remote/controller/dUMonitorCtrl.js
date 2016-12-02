/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('dUMonitorMgrCtrl', dUMonitorMgrCtrl);

/**
 * dUMonitorMgrCtrl - controller
 */
dUMonitorMgrCtrl.$inject = ['$scope','cResource','$uibModal','$interval'];

function dUMonitorMgrCtrl($scope,cResource,$uibModal,$interval) {
    var ms = 3600000,
        vm = $scope.vm = {
            oldOrNow:'0',
            listByStoreId:[],
            summaryVal:'',
            metricVal:'',
            period:[{ts:ms,name:'1 小时'}, {ts:ms*2,name:'2 小时'}, {ts:ms*4,name:'4 小时'}, {ts:ms*12,name:'12 小时'}, {ts:ms*24,name:'24 小时'}, {ts:ms*168,name:'1 周'}, {ts:ms*720,name:'1 月'}, {ts:ms*8760,name:'1 年'}],
            //periodSelect:'',
            gets:{
                deviceUsedId:'',
                period:ms,
                metricsKeyString:'',
                type:0
            },
            getMetrics:function(gets,call){
                //type == 1 大图
                var url = this.oldOrNow=='0'?
                    '../admin/remoteMonitor/deviceUsed/queryMetricPointsByRange':
                    '../admin/remoteMonitor/deviceUsed/metrics';

                cResource.get(url,gets).then(function(resp){
                    if(!resp){//如果获取失败则情况绘图
                        vm.metricVal = '';
                    }
                    else if(resp.rows && resp.rows.length>0) {
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
                vm.gets.metricsKeyString = '';
                this.getMetrics(this.gets,function(data){
                    vm.metricVal = data;
                });
            },
            summarys:function(){
                //Summary 获取静态指标
                cResource.get('../admin/remoteMonitor/deviceUsed/summary',{deviceUsedId:vm.gets.deviceUsedId}).then(function(resp){
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
                cResource.get('../admin/device/used/listByStoreId',{}).then(function(resp){
                    if(resp.rows && resp.rows.length>0){
                        vm.listByStoreId = resp.rows;
                        vm.gets.deviceUsedId = resp.rows[0].id;
                        vm.summarys();
                        vm.metrics();
                    }
                });
            },
            refresh:function(){ vm.metrics()},
            periodNow:function(val,type){
                //切换当前的机器或时间段 byStore(店铺机器)
                if(type ===2){
                    //当前机器
                    this.gets.deviceUsedId = val;
                    vm.summarys();
                }else if(type ===1){
                    //时间段
                    this.gets.period = val;
                }else if(type ===3){
                    //快照或实时
                    this.oldOrNow = val;
                    this.gets.period = vm.period[0].ts;
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
                        $scope.periods = vm.period;
                        vm.gets.metricsKeyString = "['"+val+"']";
                        vm.gets.type = 1;
                        //type == 1;//大图
                        function getMetrics(){
                            vm.getMetrics(vm.gets,function(data){
                                $scope.metricBigVal =  data;
                            });
                        }
                        //
                        getMetrics();

                        //切换时间段
                        $scope.periodNow = function(val){
                            vm.gets.period = val;
                            getMetrics();
                        }
                    }
                });
            }
        }

    //初始化
    vm.listByStoreId();
    //$interval(function () {
    //    vm.metrics();
    //}, 60000);
}