/**
 * Created by Martin on 2016/6/27.
 */
angular.module('myee', [])
    .controller('explorerCtrl', explorerCtrl);

/**
 * roleCtrl - controller
 */
explorerCtrl.$inject = ['$scope', 'cResource', '$uibModal','toaster'];

function explorerCtrl($scope, cResource, $uibModal,toaster) {

    $scope.search = function () {
        var to = false;
        if (to) {
            clearTimeout(to);
        }
        to = setTimeout(function () {
            var v = $('#folderTree_q').val();
            $('#folderTree').jstree(true).search(v);
        }, 250);

    };
    //ng配置/点击
    var vm = $scope.vm = {
        treeConfig:{plugins : ['types']},
        filies:[],
        filePath:'',
        addFolder:function(){
            var ref = $('#folderTree').jstree(true),
                sel = ref.get_selected();
            if (!sel.length) {
                toaster.warning({ body:"请先选择文件夹！"});
                return false;
            }
            $uibModal.open({
                templateUrl: 'addFolder.html',
                size: 'sm',
                controller: function($scope,$uibModalInstance){
                    $scope.fileName = '';
                    $scope.save = function () {
                        ref.create_node(sel[0], {"type": "default", "text": $scope.fileName});
                        $uibModalInstance.close();
                    };

                    $scope.cancel = function () {
                        $uibModalInstance.dismiss('cancel');
                    };
                }
            });
        },
        addFile:function(path){
            var fd = new FormData();
            fd.append("file",path.files[0]);
            fd.append("path",vm.filePath);
            //fd.append("type",'flie');
            cResource.upload('../files/create',{type:'file'}, fd).then(function(res){

            });
        },
        delete:function(){
            if (confirm("确认删除吗？")) {
                var ref = $('#folderTree').jstree(true),
                    sel = ref.get_selected();
                if (!sel.length) {
                    toaster.warning({ body:"请先选择需删除的文件夹！"});
                    return false;
                }
                ref.delete_node(sel);
            }
        },
        rename:function(){
            var ref = $('#folderTree').jstree(true),
                sel = ref.get_selected();
            if (!sel.length) {
                toaster.warning({ body:"请先选择重命名的文件夹！"});
                return false;
            }
            ref.edit(sel[0]);
        },
        choiceFiles:[],
        choiceFile:function(index){
            vm.filies[index].check = vm.filies[index].check == undefined || vm.filies[index].check == false ? true : false;
            this.choiceFiles.push(index);
        }
    }

    //jq配置/点击
    $('#folderTree')
        .jstree({
            core : {
                data:{
                    url: '../admin/files/list',
                    'data' : function (node) {
                        return {'id' : node.id};
                    }
                },
                check_callback: true,
            },
            //checkbox: {},
            types: {default: {icon: 'fa fa-folder'}, file: {icon: 'fa fa-file', valid_children: []}},
            plugins: ['types', 'dnd', 'unique', 'search']
        })
        .on('delete_node.jstree', function (e, data) {
            $.get('../admin/files/change?operation=delete_node', {'id': data.node.id})
                .fail(function () {
                    data.instance.refresh();
                });
        }).on('create_node.jstree', function (e, data) {
            $.get('../admin/files/change?operation=create_node', {
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
            $.get('../admin/files/change?operation=rename_node', {'id': data.node.id, 'text': data.text})
                .done(function (d) {
                    data.instance.set_id(data.node, d.id);
                })
                .fail(function () {
                    data.instance.refresh();
                });
        });
        $('#folderTree').on('select_node.jstree', function (e, data) {
            vm.filePath = data.node.id.split('\\').pop();
            $.get('../admin/files/showList', {'id': data.node.id})
                .done(function (res) {
                    $scope.$apply(function() {
                        vm.filies = res.dataMap.tree;
                    });
                });
        });
}