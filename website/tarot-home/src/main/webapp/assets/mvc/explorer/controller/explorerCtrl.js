/**
 * Created by Martin on 2016/6/27.
 */
angular.module('inspinia', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * roleCtrl - controller
 */
explorerCtrl.$inject = ['$scope', '$resource', '$uibModal'];

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

    var vm = $scope.vm = {
        treeConfig:{
            plugins : ['types']
        },
        filies:[],
        addFolder:function(){
            $uibModal.open({
                templateUrl: 'addFolder.html',
                size: 'sm',
                controller: function($scope,$uibModalInstance){
                    $scope.fileName = '';
                    $scope.save = function () {
                        ref = $('#folderTree').jstree(true),
                            sel = ref.get_selected();
                        if (!sel.length) {
                            return false;
                        }
                        ref.create_node(sel[0], {"type": "default", "text": $scope.fileName});
                        $uibModalInstance.close();
                    };

                    $scope.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    };
                }
            });
            //var name = prompt("请输入新建的文件夹名", "新建文件夹"),
        },
        addFile:function(){
            var name = prompt("请输入新建的文件名", "新建文件.txt"),
                ref = $('#folderTree').jstree(true),
                sel = ref.get_selected();
            if (!sel.length) {
                return false;
            }
            ref.create_node(sel[0], {"type": "file", "text": name});
        },
        delete:function(){
            if (confirm("确认删除吗？")) {
                var ref = $('#folderTree').jstree(true),
                    sel = ref.get_selected();
                if (!sel.length) {
                    return false;
                }
                ref.delete_node(sel);
            }
        },
        rename:function(){
            var ref = $('#folderTree').jstree(true),
                sel = ref.get_selected();
            if (!sel.length) {
                return false;
            }
            ref.edit(sel[0]);
        }
    }

    $('#folderTree')
        .jstree({
            core : {
                data:{
                    url: '/admin/files/list',
                    'data' : function (node) {
                        return {'id' : node.id};
                    }
                },
                check_callback: true,
            },
            checkbox: {},
            types: {default: {icon: 'fa fa-folder'}, file: {icon: 'fa fa-file', valid_children: []}},
            plugins: ['types', 'checkbox', 'dnd', 'unique', 'search']
        })
        .on('delete_node.jstree', function (e, data) {
            $.get('/admin/files/change?operation=delete_node', {'id': data.node.id})
                .fail(function () {
                    data.instance.refresh();
                });
        }).on('create_node.jstree', function (e, data) {
            console.log(data)
            $.get('/admin/files/change?operation=create_node', {
                'type': data.node.type,
                'id': data.node.parent,
                'text': data.node.text
            })
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
            $resource('/admin/files/list').query({'id': data.node.id}).$promise.then(function(res){
                vm.filies = res;
            });
        })
}