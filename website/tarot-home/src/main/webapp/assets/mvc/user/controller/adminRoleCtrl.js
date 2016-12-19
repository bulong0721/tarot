/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('adminRoleMgrCtrl', adminRoleMgrCtrl);

/**
 * roleCtrl - controller
 */
adminRoleMgrCtrl.$inject = ['$scope', 'cTables','cfromly','cResource','$filter','$q'];

function adminRoleMgrCtrl($scope, cTables,cfromly, cResource, $filter,$q) {
    var iDatatable = 0, iEditor = 1, iBindStore = 2;
    var vm = $scope.showCase = {};
    $scope.treeControl = {};
    $scope.treeData = [];
    $scope.expandField = {field: 'name'};
    $scope.formBindData = {};
    $scope.showCase.currentRowIndex = 0;

    var mgrOpts = {
        fields: [
            {
                key: 'name',
                type: 'c_input',
                templateOptions: {label: '角色名', required: true, placeholder: '角色名,20字以内', maxlength: 20}
            },
            {
                key: 'description',
                type: 'c_textarea',
                ngModelAttrs: {
                    style: {attribute: 'style'}
                },
                templateOptions: {label: '描述', placeholder: '描述,255字以内', rows: 10, style: 'max-width:500px',maxlength:255}
            }
        ],
        api: {
            read: '../admin/adminRoles/paging',
            update: '../admin/adminRoles/save',
            delete: '../admin/adminRoles/delete'
        }
    };

    cTables.initNgMgrCtrl(mgrOpts, $scope);
    $scope.tips = "*管理所有角色，不受切换门店影响";

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

    /* 提交权限绑定*/
    $scope.submitPermission = function () {
        var arraySelected = [];
        var data = $scope.treeData;
        recursionTree(data, arraySelected);
        cResource.save('./adminRoles/bindPermissions',{
            'bindString': JSON.stringify(arraySelected),
            'roleId': $scope.formBindData.model.id
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
                isFriendly: true
            }).then(function(data){
                $scope.treeData = data.rows;
                $scope.initalBindPermissionList = data.rows;
                return data.rows;
            });
        }
    }
}