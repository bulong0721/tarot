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

function datatablesCtrl($scope, $compile, Constants) {
    $scope.where = {};
    $scope.dtInstance = null;

    $scope.dtColumns = [
        {data: 'name', title: '名称', width: 85, orderable: false},
        {data: 'login', title: '用户名', width: 60, orderable: false},
        {data: 'type', title: '账号类型', width: 55, orderable: false},
        {data: 'dateLeast', title: '最后登录时间', width: 70, orderable: false, align: 'center'},
        {data: 'ipLeast', title: '最后登录IP', width: 70, orderable: false},
        {data: 'phone', title: '电话号码', width: 65, orderable: false},
        {data: 'email', title: '电子邮件', width: 100, orderable: false},
        {data: 'active', title: '状态', width: 40, orderable: false},
        {title: '动作', width: 35, render: actionsHtml}
    ];

    $scope.formData = {
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'login', 'type': 'input', 'templateOptions': {'label': '用户名', 'placeholder': '用户名'}},
            {'key': 'type', 'type': 'input', 'templateOptions': {'label': '类型', 'placeholder': '类型'}}
        ]
    };

    $scope.linkClick = function(full) {
        console.log("full:" + full);
    };

    function actionsHtml(data, type, full, meta) {
        return '<a class="btn btn-sm btn-primary" ng-click="linkClick('+meta.row+')" ui-sref="user.user.editor"><i class="fa fa-edit"></a>';
    }

    $scope.dtOptions = Constants.buildOption("/admin/users/paging", function (data) {
        angular.extend(data, $scope.where);
    }, function (row, data, dataIndex) {
        var elem = angular.element(row);
        var content = elem.contents();
        var scope = $scope;
        $compile(content)(scope);
    });

    $scope.search = function () {
        var api = this.dtInstance;
        if (api) {
            api.reloadData();
        }
    };
}

function uiGridCtrl($scope) {
    $scope.where = {};
}


function explorerCtrl($scope, $resource, $uibModal) {

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
                url: '/admin/files/list.html',
                data: function (node) {
                    return {id: node.id};
                }
            },
            check_callback: true,
        },

        plugins: ['state', 'types', 'checkbox', 'dnd', 'unique', 'search', 'contextmenu'],
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
        $.get('/admin/files/change.html?operation=delete_node', {'id': data.node.id})
            .fail(function () {
                data.instance.refresh();
            });
    }).on('create_node.jstree', function (e, data) {
        $.get('/admin/files/change.html?operation=create_node', {'type': data.node.type, 'id': data.node.parent, 'text': data.node.text})
            .done(function (d) {
                data.instance.set_id(data.node, d.id);
            })
            .fail(function () {
                data.instance.refresh();
            });
    }).on('rename_node.jstree', function (e, data) {
        $.get('/admin/files/change.html?operation=rename_node', {'id': data.node.id, 'text': data.text})
            .done(function (d) {
                data.instance.set_id(data.node, d.id);
            })
            .fail(function () {
                data.instance.refresh();
            });
    }).on('select_node.jstree', function (e, data) {
        $scope.details = $resource('/admin/files/list.html').query({'id': data.node.id});
    })

    $scope.addFolder = function () {

        var name = prompt("请输入新建的文件夹名", "新建文件夹");
        var ref = $('#folderTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = sel[0];
        //alert(JSON.stringify(sel));
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

function basicEditorCtrl($scope, $state, $stateParams) {
    $scope.vm = {
        model: {},
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'login', 'type': 'input', 'templateOptions': {'label': '用户', 'placeholder': '用户'}},
            {'key': 'type', 'type': 'input', 'templateOptions': {'label': '类型', 'placeholder': '类型'}}
        ],
        options: {}
    };
    console.log($stateParams);
}

angular
    .module('inspinia')
    .controller('basicEditorCtrl', basicEditorCtrl)
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('explorerCtrl', explorerCtrl)
    .controller('uiGridCtrl', uiGridCtrl)
    .controller('MainCtrl', MainCtrl);