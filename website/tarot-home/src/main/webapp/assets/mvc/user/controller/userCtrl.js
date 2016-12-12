/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('userMgrCtrl', userMgrCtrl);

/**
 * roleCtrl - controller
 */
userMgrCtrl.$inject = ['$scope', 'cTables', 'cfromly','$rootScope','$q','cResource','NgTableParams', '$filter'];

function userMgrCtrl($scope, cTables, cfromly, $rootScope, $q, cResource, NgTableParams, $filter) {
    iBindStore = 2;
    // $scope.treeControl = {};
    // // $scope.treeData = [{id:1,name:'所有权限',define:'PERMIT_ALL'}];
    // $scope.treeData = [];
    // $scope.expandField = {field: 'name'};
    // $scope.bindType = 0;

    var mgrData = $scope.mgrData = {
        fields: [
            {key: 'name', type: 'c_input', templateOptions: {label: '昵称', required: true, placeholder: '昵称,20字以内', maxlength: 20,isSearch:true}},
            {
                key: 'login',
                type: 'c_input',
                templateOptions: {label: '登录名', required: true, placeholder: '登录名,40字以内', maxlength: 40,isSearch:true}
            },
            {
                key: 'phoneNumber',
                type: 'c_input',
                templateOptions: {
                    label: '电话号码',
                    placeholder: '电话号码,11位手机',
                    maxlength: 11,
                    pattern: '^(1[3578][0-9]|14[0-7])[0-9]{8}$|(^((1[3578][0-9]|14[0-7])[0-9]{8},)*(1[3578][0-9]|14[0-7])[0-9]{8}$)',
                    isSearch:true
                }
            },
            {
                key: 'email',
                type: 'c_input',
                templateOptions: {type: 'email', label: '电子邮件', required: true, placeholder: '电子邮件,60字以内', maxlength: 60,isSearch:true}
            },
            {
                key: 'activeStatusFlag',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '激活账号', placeholder: '激活账号',type: 'checkbox'},
                defaultValue: false
            }
        ],
        api: {
            read: '../admin/users/paging',
            update: '../admin/users/save',
            delete: '../admin/users/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);
    $scope.tips = "*新建的管理员账号将绑定当前所切换的门店";
    $scope.userName = $rootScope.baseUrl.userName;

    //点击编辑
    // $scope.assignPermission = function (rowIndex) {
    //     var data = $scope.treeData;
    //     iEditor = 4;
    //     if (rowIndex > -1) {
    //         // var data = $scope.tableOpts.data[rowIndex];
    //         // $scope.formData.model = angular.copy(data);
    //         $scope.rowIndex = rowIndex;
    //         cResource.save('../listPermission/list',{isFriendly: true}, {}).then(function(resp){
    //             $scope.treeData = resp.rows;
    //         });
    //     } else {
    //         $scope.formData.model = {};
    //         $scope.rowIndex = -1;
    //     }
    //     $scope.activeTab = iEditor;
    // };
    //
    // //分配权限
    // $scope.submitPermission = function () {
    //
    // }

    //设置可操作门店相关-----------------------------------------------------------------------------------------
    //绑定相关参数
    var vm = $scope.showCase = {};
    vm.selected = [];
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;

    function toggleAll(selectAll, selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                selectedItems[id] = selectAll;
            }
        }
    }

    function toggleOne(selectedItems) {
        for (var id in selectedItems) {
            if (selectedItems.hasOwnProperty(id)) {
                if (!selectedItems[id]) {
                    vm.selectAll = false;
                    return;
                }
            }
        }
        vm.selectAll = true;
    }

    $scope.formBindData = {};
    $scope.showCase.currentRowIndex = 0;

    $scope.filterBindOptions = function () {
        var deferred = $q.defer();
        //tables获取数据,获取可绑定的所有门店
        $scope.tableBindOpts = new NgTableParams({}, {
            counts: [],
            dataset: $filter('filter')($scope.initalBindProductList, $scope.showCase.nameFilter || "")//根据搜索字段过滤数组中数据
        });
        $scope.loadByInit = true;
        $scope.tableOpts.page(1);
        $scope.tableBindOpts.reload();
        deferred.resolve($scope.tableBindOpts);
        return deferred.promise;
    }

    function initalBindProduct() {
        if ($scope.initalBindProductList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindProductList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表，且只返回前10个数据
            return cResource.get('./merchantStore/getAllStoreExceptSelf').then(function(data){
                //初始化showCase.selected数组，给全选框用，让它知道应该全选哪些
                angular.forEach(data.rows, function (indexData, index, array) {
                    //indexData等价于array[index]
                    $scope.showCase.selected[indexData.id] = false;
                });
                $scope.initalBindProductList = data.rows;

                return data.rows;
            });
        }
    }

    $scope.goBindStorePermit = function(rowIndex){
        initalBindProduct().then(function () {
            $scope.filterBindOptions().then(function () {
                $scope.addNew = true;

                if ($scope.tableOpts && rowIndex > -1) {
                    $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据

                    var data = $scope.tableOpts.data[rowIndex];
                    $scope.formBindData.model = data;
                    console.log(data)
                    $scope.formBindData.model.bindShowName = '昵称:' + (data.name || "") + ' | 登录名:' + (data.login || "");

                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.productUsedList) {
                        //console.log("value"+value)
                        var productId = data.productUsedList[value].id;
                        $scope.showCase.selected[productId] = true;
                        $scope.showCase.toggleOne($scope.showCase.selected);//判断全选框是否要被checked
                    }

                    $scope.addNew = false;
                    $scope.rowIndex = rowIndex;
                } else {
                    $scope.formBindData.model = {};
                }
                $scope.activeTab = iBindStore;
            });
        });
    }

    /**
     * 提交用户门店绑定
     * @param type 区分管理员绑定还是普通用户type =1 普通用户，type=0 管理员
     */
    $scope.processBindSubmit = function () {
        var result = [];

        angular.forEach($scope.showCase.selected, function (data, index, array) {
            //data等价于array[index]
            if (data == true) {
                result.push(index);
            }
        });

        cResource.save('./users/bindMerchantStore',{
            'bindString': JSON.stringify(result),
            'userId': $scope.formBindData.model.id
        }, {}).then(function(respSucc){
            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].productUsedList = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    var length = $scope.initalBindProductList.length;
                    for (i = 0; i < length; i++) {
                        var data2 = $scope.initalBindProductList[i];
                        if (data2.id == index) {
                            $scope.tableOpts.data[$scope.showCase.currentRowIndex].productUsedList.push({
                                id: index,
                                code: data2.code,
                                name: data2.name,
                                productNum: data2.productNum
                            });
                            break;
                        }
                    }
                }
            });
            $scope.goDataTable();
        });
    };

    /*$scope.toggleOne = function(data) {
        console.log(data)
        //如果data被选中了，则
        if(data.selected == true) {
            //递归遍历其子权限
            diguiSelected(data);
        }
    }

    function diguiSelected(data) {
        for (var id in data) {
            if (data.hasOwnProperty(id)) {
                if (!data[id]) {
                    vm.selectAll = false;
                    data.selected = true;
                    return;
                }
            }
        }
        vm.selectAll = true;
    }*/

}