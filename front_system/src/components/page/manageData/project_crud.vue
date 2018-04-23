<template>
    <section>
        <!--工具条-->

        <el-row :gutter="24">
            <el-col :span="11">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 36px;">项目分类表 </span>
                        <el-button style="float: right;" type="primary" @click="handleAdd">新增项目分类</el-button>
                    </div>

                    <!--列表-->
                    <el-table :data="project_type_list" highlight-current-row v-loading="listLoading"
                              @row-click="handleClickProjectType" @selection-change="selsChange" style="width: 100%;">

                        <el-table-column prop="project_type_name" label="项目类型名称" min-width="120" sortable>
                        </el-table-column>

                        <el-table-column prop="project_type_remark" label="项目类型介绍" min-width="120" sortable>
                        </el-table-column>

                        <el-table-column prop="closed_status" label="是否激活" :formatter="formatActive" min-width="100"
                                         sortable>
                        </el-table-column>
                        <el-table-column label="操作" min-width="80">
                            <template slot-scope="scope">
                                <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                                <!-- <el-button type="danger" size="small" @click="handleDelRole(scope.$index, scope.row)">禁用</el-button> -->
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="2">
            </el-col>
            <!--新增角色界面-->
            <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false">
                <el-form :model="addForm" label-width="110px" :rules="addFormRules" ref="addForm">
                    <el-form-item label="项目类型名称" prop="project_type_name">
                        <el-input v-model="addForm.project_type_name" auto-complete="off"></el-input>
                    </el-form-item>

                    <el-form-item label="项目类型介绍" prop="project_type_remark">
                        <el-input v-model="addForm.project_type_remark" auto-complete="off"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click.native="addFormVisible = false">取消</el-button>
                    <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
                </div>
            </el-dialog>
            <!--编辑项目类型界面-->
            <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
                <el-form :model="editForm" label-width="120px" :rules="editFormRules" ref="editForm">
                    <el-form-item label="项目类型名称" prop="project_type_name">
                        <el-input v-model="editForm.project_type_name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="项目类型介绍" prop="project_type_remark">
                        <el-input v-model="editForm.project_type_remark" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="启用">
                        <el-radio-group v-model="editForm.closed_status">
                            <el-radio class="radio" :label="0">启用</el-radio>
                            <el-radio class="radio" :label="1">禁用</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click.native="editFormVisible = false">取消</el-button>
                    <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
                </div>
            </el-dialog>

            <!--项目列表-->
            <el-col :span="11">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 36px;">{{this.current_project_type_name}}组---项目表 </span>
                        <el-button style="float: right;" type="primary" @click="handleAddProject">添加项目</el-button>
                    </div>
                    <el-table :data="project_list" highlight-current-row v-loading="listLoading"
                              @selection-change="selsChange" style="width: 100%;">

                        <el-table-column prop="project_name" label="项目名称" min-width="160" sortable>
                        </el-table-column>
                        <el-table-column prop="dev_project_remark" label="项目备注" min-width="160" sortable>
                        </el-table-column>

                        <el-table-column prop="closed_status" label="是否激活" :formatter="formatActive" min-width="100"
                                         sortable>
                        </el-table-column>
                        <el-table-column label="操作" width="150">
                            <template slot-scope="scope">
                                <el-button type="danger" size="small"
                                           @click="handleEditPorject(scope.$index, scope.row)">编辑
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>

        </el-row>
        <!--新增项目界面-->
        <el-dialog title="新增" :visible.sync="addProjectFormVisible" :close-on-click-modal="false">
            <el-form :model="addProjectForm" label-width="100px" :rules="addFormRules" ref="addForm">
                <el-form-item label="项目名称" prop="project_name">
                    <el-input v-model="addProjectForm.project_name" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="项目介绍" prop="dev_project_remark">
                    <el-input v-model="addProjectForm.dev_project_remark" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addProjectFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addProjectSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
        <!--编辑项目界面-->
        <el-dialog title="编辑" v-model="editProjectFormVisible" :close-on-click-modal="false">
            <el-form :model="editProjectForm" label-width="100px" :rules="editFormRules" ref="editForm">
                <el-form-item label="项目名称" prop="project_name">
                    <el-input v-model="editProjectForm.project_name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="项目介绍" prop="dev_project_remark">
                    <el-input v-model="editProjectForm.dev_project_remark" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="启用">
                    <el-radio-group v-model="editProjectForm.closed_status">
                        <el-radio class="radio" :label="0">启用</el-radio>
                        <el-radio class="radio" :label="1">禁用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editProjectFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editProjectSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>

    </section>
</template>

<script>
    //	import util from '../../common/js/util'
    //import NProgress from 'nprogress'
    //	import { getUserListPage, removeUser, batchRemoveUser, editUser, addUser } from '../../api/api';
    export default {
        data() {
            return {
                filters: {
                    username: '',
                },
                project_type_list: [],
                user_list: [],
                project_list: [],
                current_type_id: '',
                current_project_type_name: '',
                total: 0,
                page: 1,
                listLoading: false,
                sels: [],//列表选中列
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editProjectFormVisible: false,
                editFormRules: {
                    project_type_name: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ]
                },
                editUserFormRules: {
                    type_id: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ]
                },
                //编辑界面数据
                editForm: {
                    project_type_name: '',
                    project_type_remark: '',
                },
                editProjectForm: {
                    project_name: '',
                    dev_project_remark: '',
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    project_type_name: [
                        {required: true, message: '必填项', trigger: 'blur'}
                    ]
                },
                addProjectFormRules: {
                    project_name: [
                        {required: true, message: '必填项', trigger: 'blur'}
                    ]
                },
                //新增界面数据
                addForm: {
                    project_type_name: '',
                    project_type_remark: '',
                },
                addProjectFormVisible: false,
                addProjectForm: {
                    project_name: '',
                    dev_project_remark: '',

                }
            }
        },
        methods: {
            //激活显示转换
            formatActive: function (row, column) {
                return row.closed_status == 1 ? '关闭' : row.closed_status == 0 ? '启用' : '未知';
            },
            handleCurrentChange(val) {
                this.page = val;
                this.GetProjectType();
            },
            //获取项目类型列表
            GetProjectType() {
                // let para = {
                // 	page: this.page,
                // };

                //NProgress.start();
                this.$axios.get('/works/get_project_type/', {
                    params: {
                        show_all: 1
                    }
                }).then((res) => {
                    this.total = res.data.total;
                    this.project_type_list = eval(res.data.content);
                });
            },
            handleClickProjectType(row, event, column) {
                this.current_type_id = row.id
                this.current_project_type_name = row.project_type_name
                this.getProject()
            },
            // //获取用户列表
            // getUser() {
            // 	let para = {
            // 		page: this.page
            // 	};
            // 	this.$axios.get('/handle_auth/retrieve_user/', {
            // 		params: {
            // 			//                    username: this.filters.username
            // 		}
            // 	}).then((res) => {
            // 		this.total = res.data.total;
            // 		this.user_list = res.data.content;
            // 	});
            // },
            getProject() {
                var self = this;
                this.$axios.get('/works/get_projects/', {
                        params: {
                            project_type_id: this.current_type_id,
                            show_all: true,
                        }
                    }
                ).then((res) => {
                    self.project_list = eval(res.data.content);
                    //NProgress.done();
                });
            },
            //获取部门列表
            //			getDepartment() {
            ////				let para = {
            ////					page: this.page,
            ////					name: this.filters.name
            ////				};
            ////				this.listLoading = true;
            //				//NProgress.start();
            //                this.$axios.get('/manage_data/retrieve_department/', {
            //                params: {
            ////                    filter_date: this.filters.filterDate,
            ////                    project_id: this.filters.project_id
            //                }
            //            }).then((res) => {
            //					this.department_list = res.data.content;
            //					//NProgress.done();
            //				});
            //			},
            // //删除角色
            // handleDelRole: function (index, row) {
            // 	this.$confirm('确认禁用该类型吗?', '提示', {
            // 		type: 'warning'
            // 	}).then(() => {
            // 		//NProgress.start();
            // 		let para = {
            // 			del_id: row.id,
            // 		};
            // 		let params = this.makePostReady(para)
            // 		this.$axios.post('/works/del_project_type/', params)
            // 			.then((res) => {
            // 				if (res.data.code == 1) {
            // 					this.$message({
            // 						message: res.data.msg,
            // 						type: 'success'
            // 					});
            // 					this.GetProjectTypeType();
            // 				}
            // 				else {
            // 					this.$message({
            // 						message: res.data.msg,
            // 						type: 'error'
            // 					});
            // 				}
            // 			});
            // 	}).catch(() => {
            // 	});

            // },
            //禁用
            // handleDelUserRole: function (index, row) {
            // 	this.$confirm('确认从该角色下删除该该用户吗?', '提示', {
            // 		type: 'warning'
            // 	}).then(() => {
            // 		console.log(row)
            // 		let para = {
            // 			del_id: row.user_type_id,
            // 		};
            // 		let params = this.makePostReady(para)
            // 		console.log(params)
            // 		this.$axios.post('/works/delete_user_role/', params)
            // 			.then((res) => {
            // 				if (res.data.code == 1) {
            // 					this.$message({
            // 						message: res.data.msg,
            // 						type: 'success'
            // 					});
            // 					this.getProject();
            // 				}
            // 				else {
            // 					this.$message({
            // 						message: res.data.msg,
            // 						type: 'error'
            // 					});
            // 				}
            // 			});
            // 	}).catch(() => {
            // 	});

            // },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
                console.log(this.editForm)
            },
            //显示编辑界面
            handleEditPorject: function (index, row) {
                this.editProjectFormVisible = true;
                this.editProjectForm = Object.assign({}, row);
            },
            // handleEditPorject
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    project_type_name: '',
                    project_type_remark: '',
                }
            },

            //显示新增用户界面
            handleAddProject: function () {
                if (this.current_type_id) {
                    this.addProjectFormVisible = true;
                    this.addProjectForm = {
                        project_name: '',
                        dev_project_remark: '',
                    }
                } else {
                    alert("请先在左表点选一个项目类型")
                }
            },
            //编辑
            editSubmit: function () {
                var self = this;
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {

                            //NProgress.start();
                            let para = Object.assign({}, this.editForm);
                            para.update_id = para.id
                            let params = this.makePostReady(para)
                            //							para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            this.$axios.post('/works/update_project_type/', params)
                                .then((res) => {
                                    if (res.data.code == 1) {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'success'
                                        });
                                        self.GetProjectType();
                                    }
                                    else {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'error'
                                        });
                                    }
                                    this.$refs['editForm'].resetFields();
                                    this.editFormVisible = false;

                                });
                        });
                    }
                });
            },
            //编辑项目
            editProjectSubmit: function () {
                var self = this;
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            this.editProjectFormVisible = false;

                            //NProgress.start();
                            let para = Object.assign({}, this.editProjectForm);
                            para.update_id = para.id

                            let params = this.makePostReady(para)
                            //							para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
                            this.$axios.post('/works/update_project/', params)
                                .then((res) => {
                                    if (res.data.code == 1) {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'success'
                                        });
                                        self.getProject();
                                    }
                                    else {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'error'
                                        });
                                    }
                                    // this.$refs['editProjectForm'].resetFields();

                                });
                        });
                    }
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {

                            //NProgress.start();
                            let para = Object.assign({}, this.addForm);
                            let params = this.makePostReady(para)
                            this.$axios.post('/works/create_project_type/', params)
                                .then((res) => {
                                    if (res.data.code == 1) {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'success'
                                        });
                                        this.GetProjectType();
                                    }
                                    else {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'error'
                                        });
                                    }

                                    this.$refs['addForm'].resetFields();
                                    this.addFormVisible = false;
                                });
                        });
                    }
                });
            },
            addProjectSubmit: function () {
                var self = this;
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {

                            //NProgress.start();
                            let para = Object.assign({}, this.addProjectForm);
                            para.project_type_id = self.current_type_id

                            let params = this.makePostReady(para)
                            this.$axios.post('/works/create_project/', params)
                                .then((res) => {
                                    if (res.data.code == 1) {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'success'
                                        });
                                        this.getProject();
                                    }
                                    else {
                                        this.$message({
                                            message: res.data.msg,
                                            type: 'error'
                                        });
                                    }
                                    // self.$refs['addProjectForm'].resetFields();
                                    self.addProjectFormVisible = false;
                                });
                        });
                    }
                });
            },
            // addUserSubmit: function () {
            // 	this.$refs.addForm.validate((valid) => {
            // 		if (valid) {
            // 			this.$confirm('确认提交吗？', '提示', {}).then(() => {
            // 				//NProgress.start();
            // 				let para = Object.assign({}, this.addProjectForm);
            // 				let params = this.makePostReady(para)
            // 				this.$axios.post('/manage_data/create_user_role/', params)
            // 					.then((res) => {
            // 						this.$message({
            // 							message: '提交成功',
            // 							type: 'success'
            // 						});
            // 						this.addProjectFormVisible = false;
            // 						this.getProject();
            // 						// this.$refs['addProjectForm'].resetFields();
            // 					});
            // 			});
            // 		}
            // 	});
            // },
            selsChange: function (sels) {
                this.sels = sels;
            },
            //批量删除
            //			batchRemove: function () {
            //				var ids = this.sels.map(item => item.id).toString();
            //				this.$confirm('确认删除选中记录吗？', '提示', {
            //					type: 'warning'
            //				}).then(() => {
            //					this.listLoading = true;
            //					//NProgress.start();
            //					let para = { ids: ids };
            //					batchRemoveUser(para).then((res) => {
            //						this.listLoading = false;
            //						//NProgress.done();
            //						this.$message({
            //							message: '删除成功',
            //							type: 'success'
            //						});
            //						this.GetProjectType();
            //					});
            //				}).catch(() => {
            //				});
            //			}
        },
        mounted() {
            this.GetProjectType();
            // this.getUser();

        }
    }
</script>

<style scoped>

</style>
