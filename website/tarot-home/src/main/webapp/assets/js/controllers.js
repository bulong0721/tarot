/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Martin.Xu';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

}

function roleMgrCtrl($scope, $compile, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    var mgrData = {
        columns: [
            {data: 'id', visible: false},
            {data: 'roleName', title: '角色名', width: 60, orderable: false},
            {data: 'description', title: '描述', width: 100, orderable: false},
            {title: '动作', width: 20, render: actionsHtml, className: 'center'}
        ],
        fields: [
            {'key': 'roleName', 'type': 'input', 'templateOptions': {'label': '角色名', required: true, 'placeholder': '角色名'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        api: {
            read: '/admin/roles/paging',
            update: '/admin/roles/save'
        }
    };
    Constants.initMgrCtrl(mgrData, $scope);

    $scope.dtColumns = mgrData.columns;

    $scope.dtOptions = Constants.buildOption(mgrData.api.read, function (data) {
        angular.extend(data, $scope.where);
    }, function (row, data, dataIndex) {
        var elem = angular.element(row);
        var content = elem.contents();
        var scope = $scope;
        $compile(content)(scope);
    });
}

function datatablesCtrl($scope, $resource, $compile, Constants) {
    var mgrData = {
        columns: [
            {data: 'name', title: '名称', width: 85, orderable: false},
            {data: 'login', title: '用户名', width: 60, orderable: false},
            {data: 'type', title: '账号类型', width: 55, orderable: false},
            {data: 'dateLeast', title: '最后登录时间', width: 70, orderable: false, align: 'center'},
            {data: 'ipLeast', title: '最后登录IP', width: 70, orderable: false},
            {data: 'phone', title: '电话号码', width: 65, orderable: false},
            {data: 'email', title: '电子邮件', width: 100, orderable: false},
            {data: 'active', title: '状态', width: 40, orderable: false},
            {title: '动作', width: 35, render: actionsHtml, className: 'center'}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'login', 'type': 'input', 'templateOptions': {'label': '用户名', 'placeholder': '用户名'}},
            {'key': 'type', 'type': 'input', 'templateOptions': {'label': '类型', 'placeholder': '类型'}}
        ],
        api: {
            read: '/admin/users/paging',
            update: '/admin/users/save'
        }

    };

    Constants.initMgrCtrl(mgrData, $scope, $resource, $compile);

    $scope.dtColumns = mgrData.columns;

    function actionsHtml(data, type, full, meta) {
        return '<a class="red" ng-click="goEditor()"><i class="btn-icon fa fa-trash-o bigger-130"></a>';
    }

    $scope.dtOptions = Constants.buildOption(mgrData.api.read, function (data) {
        angular.extend(data, $scope.where);
    }, function (row, data, dataIndex) {
        var elem = angular.element(row);
        var content = elem.contents();
        var scope = $scope;
        $compile(content)(scope);
    });
}

function editMerchantCtrl($scope, $resource) {
    var vm = $scope;

    //获取一个商户信息请求
    $resource('/admin/merchant/get').get({
        id: '100'//以后从右上角的切换门店取商户ID
    }, function (resp) {
        // 处理响应成功
        vm.list = resp.data[0];
        vm.setModel(vm.list);
        console.log(vm.model);
    }, function (err) {
        // 处理错误

    });

    vm.setModel = function (list) {
        //设置formly的初始值
        vm.model = {
            name: list.name,
            businessType: list.businessType,
            cuisineType: list.cuisineType,
            description: list.description
        };
    };

    vm.options = {};

    vm.fields = [
        {'key': 'name', 'type': 'input', 'templateOptions': {'type': 'text', 'label': '商户名称', 'placeholder': '商户名称'}},
        {
            'key': 'businessType', 'type': 'select',
            'templateOptions': {
                'label': '商户类型',
                'options': [
                    {name: '商场', value: 'AL'},
                    {name: '餐饮', value: 'AK'},
                    {name: '零售', value: 'AZ'},
                    {name: '其他', value: 'AR'},
                    {name: '商圈', value: 'CA'}
                ]
            }
        },
        {'key': 'cuisineType', 'type': 'input', 'templateOptions': {'type': 'text', 'label': '商户菜系', 'placeholder': '商户菜系'}},
        {'key': 'imgFile', 'type': 'input', 'templateOptions': {'type': 'file', 'label': '商户图标', 'placeholder': '商户图标'}},
        {'key': 'description', 'type': 'textarea', 'templateOptions': {'label': '商户描述', 'placeholder': '商户描述', "rows": 10}}
    ];

    //提交到后台修改门店信息
    $scope.onSubmit = function () {
        alert(123);
        console.log(vm.model.id);
        $resource('/admin/merchant/edit').save(
            {
                id: '100',//以后从右上角的切换门店取商户ID
                name: vm.model.name,
                businessType: vm.model.businessType,
                cuisineType: vm.model.cuisineType,
                description: vm.model.description
            },
            {},
            function (response) {
                // 处理响应成功
            }, function (response) {
                // 处理非成功响应
            }
        );
    };

    //重置参数为初始状态
    $scope.onReset = function () {
        vm.setModel(vm.list);
    };


}

function uiGridCtrl($scope) {
    $scope.where = {};
}


function explorerCtrl($scope, $resource) {

    $scope.search = function () {
        var to = false;
        if (to) {
            clearTimeout(to);
        }
        to = setTimeout(function () {
            var v = $('#folderTree_q').val();
            $('#folderTree').jstree(true).search(v);
        }, 250);

    }


    $('#folderTree').jstree({
        core: {
            data: {
                url: '/admin/files/list',
                data: function (node) {
                    return {id: node.id};
                }
            },
            check_callback: true,
        },

        plugins: ['types', 'checkbox', 'dnd', 'unique', 'search'],
        types: {
            default: {
                icon: 'fa fa-folder'
            },
            file: {
                icon: 'fa fa-file',
                valid_children: []
            }
        },
        checkbox: {},
        search: {
            show_only_matches: true,

        }

    }).on('delete_node.jstree', function (e, data) {
        $.get('/admin/files/change?operation=delete_node', {'id': data.node.id})
            .fail(function () {
                data.instance.refresh();
            });
    }).on('create_node.jstree', function (e, data) {
        $.get('/admin/files/change?operation=create_node', {'type': data.node.type, 'id': data.node.parent, 'text': data.node.text})
            .done(function (d) {
                data.instance.set_id(data.node, d.id);
            })
            .fail(function () {
                data.instance.refresh();
            });
    }).on('rename_node.jstree', function (e, data) {
        $.get('/admin/files/change?operation=rename_node', {'id': data.node.id, 'text': data.text})
            .done(function (d) {
                data.instance.set_id(data.node, d.id);
            })
            .fail(function () {
                data.instance.refresh();
            });
    }).on('select_node.jstree', function (e, data) {
        $scope.details = $resource('/admin/files/list').query({'id': data.node.id});
    })

    $scope.addFolder = function () {

        var name = prompt("请输入新建的文件夹名", "新建文件夹");
        var ref = $('#folderTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = sel[0];
        sel = ref.create_node(sel, {"type": "default", "text": name});
    };


    $scope.addFile = function () {
        var name = prompt("请输入新建的文件名", "新建文件.txt");
        var ref = $('#folderTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = sel[0];
        sel = ref.create_node(sel, {"type": "file", "text": name});
    };

    $scope.delete = function () {
        if (confirm("确认删除吗？")) {
            var ref = $('#folderTree').jstree(true),
                sel = ref.get_selected();
            if (!sel.length) {
                return false;
            }
            ref.delete_node(sel);
        }
    };

    $scope.rename = function () {
        var ref = $('#folderTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = sel[0];
        ref.edit(sel);
    };

}

angular
    .module('inspinia')
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('roleMgrCtrl', roleMgrCtrl)
    .controller('editMerchantCtrl', editMerchantCtrl)
    .controller('explorerCtrl', explorerCtrl)
    .controller('uiGridCtrl', uiGridCtrl)
    .controller('MainCtrl', MainCtrl);