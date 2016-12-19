/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('customerMgrCtrl', customerMgrCtrl);

/**
 * roleCtrl - controller
 */
customerMgrCtrl.$inject = ['$scope', 'cTables', 'cResource','$filter','$q','cfromly','NgTableParams'];

function customerMgrCtrl($scope, cTables, cResource,$filter,$q,cfromly,NgTableParams) {
    var iDatatable = 0, iEditor = 1, iBindStore = 2, iBindRole = 5;
    $scope.treeControl = {};
    $scope.treeData = [];
    $scope.expandField = {field: 'name'};
    $scope.bindType = 0;

    var mgrData = $scope.mgrData = {
        fields: [
            {key: 'firstName', type: 'c_input', templateOptions: {label: '姓氏', required: false, placeholder: '20字以内', maxlength: 20}},
            {key: 'lastName', type: 'c_input', templateOptions: {label: '名字', required: true, placeholder: '20字以内', maxlength: 20}},
            {
                key: 'username',
                type: 'c_input',
                templateOptions: {label: '登录名', required: true, placeholder: '登录名,40字以内', maxlength: 40,isSearch:true}
            },
            //{key: 'phoneNumber', type: 'c_input', templateOptions: {label: '电话号码', placeholder: '电话号码'}},
            {
                key: 'emailAddress',
                type: 'c_input',
                templateOptions: {type: 'email', label: '电子邮件', required: false, placeholder: '电子邮件,60字以内', maxlength: 60,isSearch:true}
            },
            {
                key: 'receiveEmail',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '接收邮件', placeholder: '接收邮件',type: 'checkbox'}
            },
            {
                key: 'deactivated',
                type: 'c_input',
                className: 'formly-min-checkbox',
                templateOptions: {label: '冻结账号', placeholder: '冻结账号',type: 'checkbox'}
            }
        ],
        api: {
            read: '../admin/customers/paging',
            update: '../admin/customers/save',
            delete: '../admin/customers/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrData, $scope);

    $scope.tips = "*请切换不同门店，来管理不同门店的普通用户账号";

    //打开分配权限界面
    $scope.goAssignPermission = function (rowIndex) {
        var data = $scope.treeData;
        iPermission = 4;
        if (rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.formBindData.model = data;
            initalBindPermissions().then(function (resp) {
                var idsArr = [];
                $scope.showCase.currentRowIndex = rowIndex;
                data = $scope.tableOpts.data[$scope.showCase.currentRowIndex];
                if (data.checkedPermissionList != undefined) {
                    for (var i = 0; i < data.checkedPermissionList.length; i++) {
                        idsArr.push(data.checkedPermissionList[i].id);
                    }
                    recursionPermissionList(resp[0], idsArr);
                } else {
                    data = $scope.initalBindPermissionList;
                    recursionAllUnchecked(data[0]);
                }
            });
        }
        $scope.activeTab = iPermission;
    };

    /* 递归遍历所有权限置为未选中 */
    function recursionAllUnchecked(branch) {
        branch.checked = false;
        if (branch.children != undefined) {
            for (var ele = 0; ele < branch.children.length; ele++) {
                recursionAllUnchecked(branch.children[ele]);
            }
        }
    }

    /* 递归所有权限节点，如果在后台传过来的选中list里，就置为选中*/
    function recursionPermissionList(branch, arrayId) {
        if ($.inArray(branch.id, arrayId) != -1) {
            branch.checked = true;
        } else {
            branch.checked = false;
        }
        if (branch.children != undefined) {
            for (var ele = 0; ele < branch.children.length; ele++) {
                recursionPermissionList(branch.children[ele], arrayId);
            }
        }
    }

    /* 递归遍历将选中的权限放array*/
    function recursionAllCheckedPermission(branch, list) {
        if (branch.checked == true) {
            list.push(branch);
        }
        if (branch.children != undefined) {
            for (var ele = 0; ele < branch.children.length; ele++) {
                recursionAllCheckedPermission(branch.children[ele], list);
            }
        }
    }

    //设置可操作门店相关-----------------------------------------------------------------------------------------
    //绑定相关参数
    var vm = $scope.showCase = {};
    vm.selected = [];
    vm.selectAll = false;
    vm.toggleAll = toggleAll;
    vm.toggleOne = toggleOne;
    $scope.thisUserDefaultStoreName = '';//当前用户所属门店，绑定时排除

    function initalBindProduct() {
        if ($scope.initalBindProductList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindProductList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表
            return cResource.get('./merchantStore/getAllStoreExceptSelf').then(function(data){
                //初始化showCase.selected数组，给全选框用，让它知道应该全选哪些
                var result = []; //排除当前用户所属门店
                angular.forEach(data.rows, function (indexData, index, array) {
                    //indexData等价于array[index]
                    $scope.showCase.selected[indexData.id] = false;
                    if( indexData.name != $scope.thisUserDefaultStoreName ) {
                        result.push(indexData);
                    }
                });
                $scope.initalBindProductList = result;

                return result;
            });
        }
    }

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

    $scope.goBindStorePermit = function(rowIndex){
        //进入绑定页面前，赋值用户所属门店
        if ($scope.tableOpts && rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.thisUserDefaultStoreName = data.storeName;
        }

        initalBindProduct().then(function () {
            $scope.filterBindOptions().then(function () {
                $scope.addNew = true;

                if ($scope.tableOpts && rowIndex > -1) {
                    $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据
                    $scope.formBindData.model = data;
                    $scope.formBindData.model.bindShowName = '登录名:' + (data.username || "") + ' | 姓氏:' + (data.firstName || "") + ' | 名字:' + (data.lastName || "");

                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.storeList) {
                        //console.log("value"+value)
                        var productId = data.storeList[value].id;
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
     */
    $scope.processBindSubmit = function () {
            var result = [];

            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    result.push(index);
                }
            });

            cResource.save('./customers/bindMerchantStore',{
                'bindString': JSON.stringify(result),
                'userId': $scope.formBindData.model.id
            }, {}).then(function(respSucc){
                //用js离线刷新表格数据
                $scope.tableOpts.data[$scope.showCase.currentRowIndex].storeList = [];//先清空
                angular.forEach($scope.showCase.selected, function (data, index, array) {
                    //data等价于array[index]
                    if (data == true) {
                        var length = $scope.initalBindProductList.length;
                        for (i = 0; i < length; i++) {
                            var data2 = $scope.initalBindProductList[i];
                            if (data2.id == index) {
                                $scope.tableOpts.data[$scope.showCase.currentRowIndex].storeList.push({
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

    /* 提交权限绑定*/
    $scope.submitPermission = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        recursionTree(data, arraySelected);
        cResource.save('./customer/bindPermissions',{
            'bindString': JSON.stringify(arraySelected),
            'customerId': $scope.formBindData.model.id
        }, {}).then(function(resp){
            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].checkedPermissionList = [];//先清空
            var checkedArr = [];
            recursionAllCheckedPermission($scope.treeData[0], checkedArr);
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].checkedPermissionList = checkedArr;
            if (0 != resp.status) {
                $filter('toasterManage')(5, "绑定失败!",false);
            } else {
                $filter('toasterManage')(5, "绑定成功!",true);
            }
        });
        $scope.goDataTable();
    }

    //递归出所有选中的文件
    function recursionTree(data, arraySelected) {
        angular.forEach(data, function (d) {
            if (d.checked == true && d.children.length == 0) {
                arraySelected.push(d.id);
            }
            if (d.children.length > 0) {
                recursionTree(d.children, arraySelected);
            }
        });
    }

    /* 初始化绑定权限 */
    function initalBindPermissions() {
        if ($scope.initalBindPermissionList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindPermissionList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表，且只返回前10个数据
            return cResource.get('./listPermission/list',{
                isFriendly: false
            }).then(function(data){
                $scope.treeData = data.rows;
                $scope.initalBindPermissionList = data.rows;
                return data.rows;
            });
        }
    }

    //打开分配角色界面
    $scope.goAssignRole = function (rowIndex) {
        //进入绑定页面前，赋值用户所属角色
        if ($scope.tableOpts && rowIndex > -1) {
            var data = $scope.tableOpts.data[rowIndex];
            $scope.thisUserDefaultRoleName = data.roleName;
        }
        initalBindRole().then(function () {
            $scope.filterRoleBindOptions().then(function () {
                $scope.addNew = true;

                if ($scope.tableOpts && rowIndex > -1) {
                    $scope.showCase.currentRowIndex = rowIndex;//记录当前选择的行，以备后续更新该行数据
                    $scope.formBindData.model = data;
                    $scope.formBindData.model.bindShowName = '登录名:' + (data.username || "") + ' | 姓氏:' + (data.firstName || "") + ' | 名字:' + (data.lastName || "");
                    //根据已关联的产品去勾选对应的checkbox
                    $scope.showCase.selectAll = false;
                    $scope.showCase.toggleAll(false, $scope.showCase.selected);//先取消所有checkbox的勾选状态
                    for (var value in data.roleList) {
                        //console.log("value"+value)
                        var roleId = data.roleList[value].id;
                        $scope.showCase.selected[roleId] = true;
                        $scope.showCase.toggleOne($scope.showCase.selected);//判断全选框是否要被checked
                    }

                    $scope.addNew = false;
                    $scope.rowIndex = rowIndex;
                } else {
                    $scope.formBindData.model = {};
                }
                $scope.activeTab = iBindRole;
            });
        });
    }

    function initalBindRole() {
        if ($scope.initalBindRoleList) {//如果已经从后台读取过数据了，则不再访问后台获取列表
            var deferred = $q.defer();
            deferred.resolve($scope.initalBindRoleList);
            return deferred.promise;
        } else {//第一次需要从后台读取列表
            return cResource.get('./role/listCustomerRole').then(function(data){
                //初始化showCase.selected数组，给全选框用，让它知道应该全选哪些
                var result = []; //排除当前用户所属门店
                angular.forEach(data.rows, function (indexData, index, array) {
                    //indexData等价于array[index]
                    $scope.showCase.selected[indexData.id] = false;
                    if( indexData.roleName != $scope.thisUserDefaultRoleName ) {
                        result.push(indexData);
                    }
                });
                $scope.initalBindRoleList = result;
                return result;
            });
        }
    }

    $scope.filterRoleBindOptions = function () {
        var deferred = $q.defer();
        //tables获取数据,获取可绑定的所有门店
        $scope.tableBindOpts = new NgTableParams({}, {
            counts: [],
            dataset: $filter('filter')($scope.initalBindRoleList, $scope.showCase.nameFilter || "")//根据搜索字段过滤数组中数据
        });
        $scope.loadByInit = true;
        $scope.tableOpts.page(1);
        $scope.tableBindOpts.reload();
        deferred.resolve($scope.tableBindOpts);
        return deferred.promise;
    }

    /**
     * 提交用户角色绑定
     * @param type 区分管理员绑定还是普通用户type =1 普通用户，type=0 管理员
     */
    $scope.processBindRoleSubmit = function () {
        var result = [];

        angular.forEach($scope.showCase.selected, function (data, index, array) {
            //data等价于array[index]
            if (data == true) {
                result.push(index);
            }
        });

        cResource.save('./customers/bindRole',{
            'bindString': JSON.stringify(result),
            'userId': $scope.formBindData.model.id
        }, {}).then(function(resp){
            //用js离线刷新表格数据
            $scope.tableOpts.data[$scope.showCase.currentRowIndex].roleList = [];//先清空
            angular.forEach($scope.showCase.selected, function (data, index, array) {
                //data等价于array[index]
                if (data == true) {
                    var length = $scope.initalBindRoleList.length;
                    for (i = 0; i < length; i++) {
                        var data2 = $scope.initalBindRoleList[i];
                        if (data2.id == index) {
                            $scope.tableOpts.data[$scope.showCase.currentRowIndex].roleList.push({
                                id: index,
                                description: data2.description,
                                name: data2.name
                            });
                            break;
                        }
                    }
                }
            });
            $scope.goDataTable();
        });
    };
}