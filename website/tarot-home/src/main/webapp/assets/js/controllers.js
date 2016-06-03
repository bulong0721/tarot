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

function merchantShopCtrl($scope, $resource, $compile, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    var key = ['id','name','address.province','address.city','address.county','address.circle','address.mall','address.address', 'phone','code','active'];
    var title = ['门店ID','门店名称', '省份','城市','区县','商圈','商场','地址','联系电话','门店码', '操作','动作'];
    var mgrData = {
        columns: [
            {data: 'id', title: '门店ID', width: 85, orderable: true},
            {data: 'name', title: '门店名称', width: 85, orderable: false},
            {data: 'address.province', title: '省份', width: 60, orderable: true},
            {data: 'address.city', title: '城市', width: 55, orderable: true},
            {data: 'address.county', title: '区县', width: 70, orderable: false, align: 'center'},
            {data: 'address.circle', title: '商圈', width: 70, orderable: false},
            {data: 'address.mall', title: '商场', width: 65, orderable: false},
            {data: 'address.address', title: '地址', width: 100, orderable: false},
            {data: 'phone', title: '联系电话', width: 40, orderable: false},
            {data: 'code', title: '门店码', width: 40, orderable: false},
            {data: 'active', title: '操作', width: 40, orderable: false},
            {title: '动作', width: 35, render: actionsHtml}
        ],
        fields: [
            {'key': 'merchant.name', 'type': 'input', 'templateOptions': {'disabled':true,'label': '商户名称', 'placeholder': '商户名称'}},
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '门店名称', 'placeholder': '门店名称'}},
            {'key': 'address.province', 'type': 'input', 'templateOptions': {'label': '省份', 'placeholder': '省份'}},
            {'key': 'address.city', 'type': 'input', 'templateOptions': {'label': '城市', 'placeholder': '城市'}},
            {'key': 'address.county', 'type': 'input', 'templateOptions': {'label': '区县', 'placeholder': '区县'}},
            {'key': 'address.circle', 'type': 'input', 'templateOptions': {'label': '商圈', 'placeholder': '商圈'}},
            {'key': 'address.mall', 'type': 'input', 'templateOptions': {'label': '商场', 'placeholder': '商场'}},
            {'key': 'address.address', 'type': 'input', 'templateOptions': {'label': '地址', 'placeholder': '地址'}},
            {'key': 'phone', 'type': 'input', 'templateOptions': {'label': '联系电话', 'placeholder': '联系电话'}},
            {'key': 'code', 'type': 'input', 'templateOptions': {'label': '门店码', 'placeholder': '门店码'}},
        ],
        api: {
            read: '/admin/merchantStore/listByMerchant',
            update: '/admin/merchantStore/save'
        }

    };

    Constants.initMgrCtrl(mgrData, $scope, $resource, $compile);

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

function merchantCtrl($scope, $resource, $compile, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    //var type={};
    //$resource('/admin/merchant/typeList').get({},function(resp){type = resp.data;console.log(type);});
    //console.log(type);

    console.log("aaaaaa"+$scope.merchantType);
    var mgrData = {
        columns: [
            {data: 'id', title: '商户ID', width: 85, orderable: true},
            {data: 'name', title: '商户名称', width: 85, orderable: true},
            {data: 'businessType', title: '商户类型', width: 60, orderable: true},
            {data: 'cuisineType', title: '商户菜系', width: 55, orderable: true},
            {data: 'imgFile', title: '商户图标', width: 70, orderable: false, align: 'center'},
            {data: 'description', title: '商户描述', width: 70, orderable: false},
            {title: '动作', width: 35, render: actionsHtml}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'type':'text','label': '商户名称', 'placeholder': '商户名称'}},
            {'key': 'businessType', 'type': 'select',
                'templateOptions': {
                    'label': '商户类型',
                    'options': Constants.merchantType
                }
            },
            {'key': 'cuisineType', 'type': 'input', 'templateOptions': {'type':'text','label': '商户菜系', 'placeholder': '商户菜系'}},
            {'key': 'imgFile', 'type': 'input', 'templateOptions': {'type':'file','label': '商户图标', 'placeholder': '商户图标'}},
            {'key': 'description', 'type': 'textarea', 'templateOptions': {'label': '商户描述', 'placeholder': '商户描述',"rows": 10}}
        ],
        api: {
            read: '/admin/merchant/list',
            update: '/admin/merchant/save'
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

function deviceCtrl($scope, $compile, $resource, Constants) {
    $scope.where = {};
    $scope.dtInstance = null;

    $scope.dtColumns = [
        {data: 'name', title: '名称', width: 40, orderable: false},
        {data: 'versionNum', title: '版本号', width: 40, orderable: false},
        {data: 'description', title: '描绘', width: 60, orderable: false},
        {title: '动作', width: 35, render: actionsHtml}
    ];

    $scope.formData = {
        fields: [
            {'key': 'id', 'type': 'input',  'templateOptions': {/*'type':'hidden',*/'label': 'id', 'placeholder': 'id'}},
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'versionNum', 'type': 'input', 'templateOptions': {'label': '版本号', 'placeholder': '版本号'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ]
    };

    $scope.linkClick = function(full) {
        console.log("full:" + full);
    };

    function actionsHtml(data, type, full, meta) {
        return '<a class="btn btn-sm btn-primary" ng-click="linkClick('+meta.row+')" ui-sref="device.type.editor"><i class="fa fa-edit"></a>';
    }

    $scope.dtOptions = Constants.buildOption("/device/paging", function (data) {
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

    $scope.submit = function(){
        var vm = $scope.formData;
        $resource('/device/update').save({}, vm.model, function(resp){

        },function(error){
            alert(error.statusMessage);
        });
    }
}

angular
    .module('inspinia')
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('roleMgrCtrl', roleMgrCtrl)
    .controller('merchantCtrl', merchantCtrl)
    .controller('merchantShopCtrl', merchantShopCtrl)
    .controller('explorerCtrl', explorerCtrl)
    .controller('uiGridCtrl', uiGridCtrl)
    .controller('MainCtrl', MainCtrl)
    .controller('deviceCtrl',deviceCtrl);
