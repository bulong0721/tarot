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
            {title: '动作', width: 20, render: actionsHtml, orderable: false, className: 'center'}
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

function userMgrCtrl($scope, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    function statusHtml(data, type, full, meta) {
        var text = "冻结", style = "label-danger";
        if (full && full['activeStatusFlag']) {
            text = "激活";
            style = "label-primary";
        }
        return '<span class="label ' + style + '">' + text + '</span>';
    }

    var mgrData = {
        columns: [
            {data: 'id', visible: false},
            {data: 'name', title: '昵称', width: 85, orderable: false},
            {data: 'login', title: '登录名', width: 60, orderable: false},
            {data: 'lastLogin', title: '最后登录时间', width: 70, orderable: false},
            {data: 'loginIP', title: '最后登录IP', width: 70, orderable: false},
            {data: 'phoneNumber', title: '电话号码', width: 65, orderable: false},
            {data: 'email', title: '电子邮件', width: 100, orderable: false},
            {data: 'activeStatusFlag', title: '状态', width: 40, orderable: false, render: statusHtml, className: 'center'},
            {title: '操作', width: 35, render: actionsHtml, orderable: false, className: 'center'}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '昵称', required: true, 'placeholder': '昵称'}},
            {'key': 'login', 'type': 'input', 'templateOptions': {'label': '登录名', required: true, 'placeholder': '登录名'}},
            {'key': 'phoneNumber', 'type': 'input', 'templateOptions': {'label': '电话号码', 'placeholder': '电话号码'}},
            {'key': 'email', 'type': 'input', 'templateOptions': {type: 'email', 'label': '电子邮件', required: true, 'placeholder': '电子邮件'}},
            {'key': 'activeStatusFlag', 'type': 'checkbox', 'templateOptions': {'label': '状态', 'placeholder': '状态'}}
        ],
        api: {
            read: '/admin/users/paging',
            update: '/admin/users/save'
        }

    };

    Constants.initMgrCtrl(mgrData, $scope);
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
            {title: '动作', width: 35, render: actionsHtml, orderable: false, className: 'center'}
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

    Constants.initMgrCtrl(mgrData, $scope);

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

function switchMerchantCtrl($scope, $resource, $compile, Constants){
    $scope.merchants = Constants.merchants;
    $scope.merchantSelect = Constants.thisMerchant;

    $scope.switchMerchant = function (){
        console.log($scope.merchantSelect)
        $.post('/admin/merchant/switch', { 'id':$scope.merchantSelect.value})
            .done(function (resp) {
                Constants.flushThisMerchant($scope.merchantSelect.value);
                $scope.merchantSelect = Constants.thisMerchant;
            })
            .fail(function () {
            });
    };
}

function merchantShopCtrl($scope, $resource, $compile, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    //var key = ['id','name','address.province','address.city','address.county','address.circle','address.mall','address.address', 'phone','code','active'];
    //var title = ['门店ID','门店名称', '省份','城市','区县','商圈','商场','地址','联系电话','门店码', '操作','动作'];
    var mgrData = {
        columns: [
            {data: 'id', title: '门店ID', width: 85, orderable: true},
            {data: 'name', title: '门店名称', width: 85, orderable: false},
            {data: 'address.province.name', title: '省份', width: 60, orderable: true},
            {data: 'address.city.name', title: '城市', width: 55, orderable: true},
            {data: 'address.county.name', title: '区县', width: 70, orderable: false, align: 'center'},
            {data: 'address.circle.name', title: '商圈', width: 70, orderable: false},
            {data: 'address.mall.name', title: '商场', width: 65, orderable: false},
            {data: 'address.address.name', title: '地址', width: 100, orderable: false},
            {data: 'phone', title: '联系电话', width: 40, orderable: false},
            {data: 'code', title: '门店码', width: 40, orderable: false},
            {data: 'active', title: '操作', width: 40, orderable: false},
            {title: '动作', width: 35, render: actionsHtml}
        ],
        fields: [
            {'key': 'merchant.name', 'type': 'input', 'templateOptions': {'disabled':true,'label': '商户名称', 'placeholder': '商户名称'}},
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '门店名称', 'placeholder': '门店名称'}},
            {'key': 'address.province.id', 'type': 'select', 'templateOptions': {'label': '省份','options': Constants.provinces}},
            {'key': 'address.city.id', 'type': 'select', 'templateOptions': {'label': '城市','options': Constants.merchantType}},
            {'key': 'address.county.id', 'type': 'select', 'templateOptions': {'label': '区县','options': Constants.merchantType}},
            {'key': 'address.circle.id', 'type': 'select', 'templateOptions': {'label': '商圈','options': Constants.merchantType}},
            {'key': 'address.mall.id', 'type': 'select', 'templateOptions': {'label': '商场','options': Constants.merchantType}},
            {'key': 'address.address.id', 'type': 'input', 'templateOptions': {'label': '地址', 'placeholder': '地址'}},
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


    $scope.$watch('model.address.province.id', function (newValue, oldValue, thisScope) {
        alert(123);
        console.log(newValue);
        console.log(oldValue);
        //if(newValue !== oldValue) {
        //    // logic to reload this select's options asynchronusly based on state's value (newValue)
        //    console.log('new value is different from old value');
        //    if($scope.model[$scope.options.key] && oldValue) {
        //        // reset this select
        //        $scope.model[$scope.options.key] = '';
        //    }
        //    // Reload options
        //    $scope.to.loading = DataService.players(newValue).then(function (res) {
        //        $scope.to.options = res;
        //    });
        //}
    });
}

function merchantCtrl($scope, $resource, $compile, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }



    var mgrData = {
        columns: [
            {data: 'id', title: '商户ID', width: 85, orderable: true},
            {data: 'name', title: '商户名称', width: 85, orderable: true},
            {data: 'businessTypeKey', title: '商户类型', width: 60, orderable: true},
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

function tableTypeMgrCtrl($scope, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    var mgrData = {
        columns: [
            {data: 'id', visible: false},
            {data: 'name', title: '名称', width: 85, orderable: false},
            {data: 'description', title: '描述', width: 60, orderable: false},
            {data: 'capacity', title: '容纳人数', width: 70, orderable: false},
            {data: 'minimum', title: '最小就坐', width: 70, orderable: false},
            {title: '操作', width: 35, render: actionsHtml, orderable: false, className: 'center'}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', required: true, 'placeholder': '描述'}},
            {'key': 'capacity', 'type': 'input', 'templateOptions': {'label': '容纳人数', 'placeholder': '容纳人数'}},
            {'key': 'minimum', 'type': 'input', 'templateOptions': {'label': '最小就坐', required: true, 'placeholder': '最小就坐'}}
        ],
        api: {
            read: '/admin/catering/type/paging',
            update: '/admin/catering/type/save'
        }
    };

    Constants.initMgrCtrl(mgrData, $scope);
}

function tableZoneMgrCtrl($scope, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    var mgrData = {
        columns: [
            {data: 'id', visible: false},
            {data: 'name', title: '名称', width: 85, orderable: false},
            {data: 'description', title: '描述', width: 60, orderable: false},
            {title: '操作', width: 35, render: actionsHtml, orderable: false, className: 'center'}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', required: true, 'placeholder': '描述'}},
        ],
        api: {
            read: '/admin/catering/zone/paging',
            update: '/admin/catering/zone/save'
        }
    };

    Constants.initMgrCtrl(mgrData, $scope);
}

function tableMgrCtrl($scope, $resource, Constants) {
    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    var typeOpts = $resource('/admin/catering/type/options').query();

    var zoneOpts = $resource('/admin/catering/zone/options').query();

    var mgrData = {
        columns: [
            {data: 'id', visible: false},
            {data: 'name', title: '名称', width: 85, orderable: false},
            {data: 'description', title: '描述', width: 120, orderable: false},
            {data: 'tableType.name', title: '桌型', width: 75, orderable: false},
            {data: 'tableZone.name', title: '区域', width: 75, orderable: false},
            {title: '操作', width: 35, render: actionsHtml, orderable: false, className: 'center'}
        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', required: true, 'placeholder': '名称'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', required: true, 'placeholder': '描述'}},
            {'key': 'tableType.id', 'type': 'select', 'templateOptions': {'label': '桌型', valueProp: 'id', options: typeOpts, required: true, 'placeholder': '桌型'}},
            {'key': 'tableZone.id', 'type': 'select', 'templateOptions': {'label': '区域', valueProp: 'id', options: zoneOpts, 'placeholder': '区域'}},
        ],
        api: {
            read: '/admin/catering/table/paging',
            update: '/admin/catering/table/save'
        }
    };

    Constants.initMgrCtrl(mgrData, $scope);
}


function deviceCtrl($scope, $compile, $resource, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }
    var mgrData = {
        columns: [
            {data: 'name', title: '名称', width: 40, orderable: false},
            {data: 'versionNum', title: '版本号', width: 40, orderable: false},
            {data: 'description', title: '描绘', width: 60, orderable: false},
            {title: '动作', width: 35, render: actionsHtml,className: 'center'}

        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'versionNum', 'type': 'input', 'templateOptions': {'label': '版本号', 'placeholder': '版本号'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}}
        ],
        api: {
            read: '/device/paging',
            update: '/device/update'
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

function deviceUsedCtrl($scope, $compile, $resource, Constants) {

    function actionsHtml(data, type, full, meta) {
        return '<a ng-click="goEditor(' + meta.row + ')"><i class="btn-icon fa fa-pencil bigger-130"></a>';
    }

    function getDeviceList() {
        var data = $resource("/device/list").query();
        return data;
    }

    function getStoreList() {
        var data = $resource("/admin/merchantStore/listByMerchantForSelect").query();
        return data;
    }

    var mgrData = {
        columns: [
            {data: 'name', title: '名称', width: 40, orderable: false},
            {data: 'heartbeat', title: '心跳', width: 40, orderable: false},
            {data: 'boardNo', title: '牌号', width: 60, orderable: false},
            {data: 'deviceNum', title: '设备号', width: 60, orderable: false},
            {data: 'description', title: '描绘', width: 60, orderable: false},
            {data: 'device.name', title: '设备名', width: 60, orderable: false},
            {data: 'store.name', title: '商户名', width: 60, orderable: false},
            {title: '动作', width: 35, render: actionsHtml,className: 'center'}

        ],
        fields: [
            {'key': 'name', 'type': 'input', 'templateOptions': {'label': '名称', 'placeholder': '名称'}},
            {'key': 'heartbeat', 'type': 'input', 'templateOptions': {'label': '心跳', 'placeholder': '心跳'}},
            {'key': 'boardNo', 'type': 'input', 'templateOptions': {'label': '牌号', 'placeholder': '牌号'}},
            {'key': 'deviceNum', 'type': 'input', 'templateOptions': {'label': '设备号', 'placeholder': '设备号'}},
            {'key': 'description', 'type': 'input', 'templateOptions': {'label': '描述', 'placeholder': '描述'}},
            {'key': 'device.id', 'type': 'select', 'templateOptions': {'label': '选择设备','options': getDeviceList()}},
            {'key': 'store.id', 'type': 'select', 'templateOptions': {'label': '门户名', 'options': getStoreList()}}
        ],
        api: {
            read: '/deviceUsed/paging',
            update: '/deviceUsed/update'
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

angular
    .module('inspinia')
    .controller('deviceCtrl', deviceCtrl)
    .controller('deviceUsedCtrl', deviceUsedCtrl)
    .controller('datatablesCtrl', datatablesCtrl)
    .controller('tableTypeMgrCtrl', tableTypeMgrCtrl)
    .controller('tableZoneMgrCtrl', tableZoneMgrCtrl)
    .controller('tableMgrCtrl', tableMgrCtrl)
    .controller('userMgrCtrl', userMgrCtrl)
    .controller('roleMgrCtrl', roleMgrCtrl)
    .controller('merchantCtrl', merchantCtrl)
    .controller('merchantShopCtrl', merchantShopCtrl)
    .controller('switchMerchantCtrl', switchMerchantCtrl)
    .controller('explorerCtrl', explorerCtrl)
    .controller('uiGridCtrl', uiGridCtrl)
    .controller('MainCtrl', MainCtrl);