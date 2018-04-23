<template>
    <div>
        <el-row>
            <span class="demonstration"> 筛选时间: </span>
            <el-date-picker v-model="filters.filter_date" type="week"
                            format="yyyy-WW 周" placeholder="选择周" @change="filterDateChange"
                            :picker-options="weekOption"> >
            </el-date-picker>
            <el-button type="primary" v-on:click="filter">查询</el-button>
            <el-button type="success" @click="handleAdd">新增</el-button>
        </el-row>
        </br>
        </br>
        <el-table :data="summary_list">
            <el-table-column prop="natural_week" label="自然周数" min-width="90">
            </el-table-column>
            <el-table-column prop="summary" label="总结" min-width="200">
            </el-table-column>
            <el-table-column prop="self_evaluation" label="自我评价" min-width="200">
            </el-table-column>
            <el-table-column prop="plan" label="计划" min-width="200">
            </el-table-column>

            <el-table-column label="操作" min-width="160">
                <template slot-scope="scope">
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="SubmitWholeWeek(scope.$index, scope.row)">提交周报
                    </el-button>
                    <i class="el-icon-check" v-if="scope.row.closed_status==1"></i>
                    <i class="el-icon-close" v-if="scope.row.closed_status==0"></i>
                    <!-- <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button> -->
                </template>
            </el-table-column>
        </el-table>
        <br/>
        <br/>
        <div>
            <p>** 请注意选择筛选日期才可以看到周总结</p>
            <p>** 请注意“提交周报”后上级才能看到你已交周报，否则只有你自己才能看到 </p>
        </div>
        <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false" size="small">
            <el-form :model="insertForm" :rules="rules" ref="insertForm" label-width="110px">
                <el-form-item label="周数:" prop="filter_date">
                    <div class="block">
                        <span class="demonstration"></span>
                        <el-date-picker v-model="insertForm.filter_date" type="week" format="yyyy-WW 周"
                                        :picker-options="weekOption" @change="insertDateChange"
                                        placeholder="选择周">
                        </el-date-picker>
                    </div>
                </el-form-item>
                <el-form-item label="总结：" prop="summary">
                    <el-input type="textarea" class="form-control" id="summary" placeholder="总结"
                              v-model="insertForm.summary">
                        总结
                    </el-input>
                </el-form-item>
                <el-form-item label="自我评价：" prop="self_evaluation">
                    <el-input type="textarea" class="form-control" id="self_evaluation" placeholder="自我评价"
                              v-model="insertForm.self_evaluation">
                        总结
                    </el-input>
                </el-form-item>
                <el-form-item label="计划：" prop="plan">
                    <el-input type="textarea" class="form-control" id="plan" placeholder="计划" v-model="insertForm.plan">
                        总结
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" prop="filter_date">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="insertSummary('insertForm')" :loading="addLoading">保存
                </el-button>
            </div>
            <div>
                <p>**每个内容最多60000字符** </p>
            </div>
        </el-dialog>

        <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false" size="small">
            <el-form ref="editForm" :model="editForm" :rules="rules" label-width="110px">
                <el-form-item label="周数：">
                    <span class="demonstration"></span>
                    <p>{{editForm.natural_week}} 周</p>
                </el-form-item>

                <el-form-item label="总结：" prop="summary">
                    <el-input type="textarea" class="form-control" id="summary" placeholder="总结"
                              v-model="editForm.summary">
                        总结
                    </el-input>
                </el-form-item>
                <el-form-item label="自我评价：" prop="self_evaluation">
                    <el-input type="textarea" class="form-control" id="self_evaluation" placeholder="自我评价"
                              v-model="editForm.self_evaluation">
                        总结
                    </el-input>
                </el-form-item>
                <el-form-item label="计划：" prop="plan">
                    <el-input type="textarea" class="form-control" id="plan" placeholder="计划" v-model="editForm.plan">
                        总结
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit('editForm')" :loading="editLoading">保存</el-button>
            </div>
            <p>**每个内容最多60000字符** </p>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                name: '标题',
                addFormVisible: false,
                editFormVisible: false,
                summary_list: [],
                filters: {
                    project_name: '',
                    filter_date: '',
                    natural_week: '',
                },

                addLoading: false,
                insertForm: {
                    filter_date: '',
                    natural_week: '',
                    summary: '',
                    plan: '',
                    self_evaluation: '',
                },
                weekOption: {
                    firstDayOfWeek: 1,
                },
                editLoading: false,
                editForm: {
                    filter_date: '',
                    natural_week: '',
                    summary: '',
                    plan: '',
                    self_evaluation: '',
                },
                rules: {
                    natural_week: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    summary: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    self_evaluation: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    plan: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                },
            }
        },
        created() {
            this.get_summary()
        },
        methods: {
            calcDialogSize: function () {
                let size = 'full';
                const width = window.innerWidth;

                // if (width < 1024) {
                //     size = 'full';
                // }
                // if (width >= 1360) {
                //     size = 'large';
                // }
                // else if (width < 1920) {
                //     size = 'large';
                // }
                return size;
            },
            filterDateChange(val) {
                if (val) {
                    this.filters.natural_week = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }
            },
            insertDateChange(val) {
                if (val) {
                    this.insertForm.natural_week = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }
            },
            // editDateChange(val) {
            //     //编辑界面的日期变更后调用
            //     var self = this;
            //     self.editForm.natural_week = val
            // },
            filter: function (params) {
                this.summary_list = [];
                this.get_summary();
            },
            get_summary: function (params) {
                var self = this;
                this.$axios.get('/works/get_weekly_summary/', {
                    params: {
                        natural_week: self.filters.natural_week,
                    }
                })
                    .then(function (response) {
                            self.summary_list = eval(response.data.content);
                        }
                    );
            },
            insertSummary(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var self = this;
                        let str = 'natural_week=' + self.insertForm.natural_week + '&summary=' + self.insertForm.summary + '&self_evaluation=' + self.insertForm.self_evaluation + '&plan=' + self.insertForm.plan;
                        this.$axios.post('/works/insert_summary/', str).then(function (response) {
                            if (response.data.code == 1) {
                                // self.filters.filter_date =self.insertForm.natural_week
                                self.get_summary();

                                self.$message({
                                    message: response.data.msg,
                                    type: 'success'
                                });
                            } else {
                                self.$message({
                                    message: response.data.msg,
                                    type: 'error'
                                });
                            }
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                    this.addFormVisible = false;
                });
            },

            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);
            },
            handleAdd: function () {
                this.addFormVisible = true;
                this.insertForm = {
                    filter_date: '',
                    natural_week: '',
                    summary: '',
                    plan: '',
                    self_evaluation: '',
                };
            },
            handleDelete: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                    var self = this;
                    this.$axios.get('/works/del_summary/', {
                        params: {
                            delID: row.id,
                        }
                    })
                        .then(function (response) {
                                if (response.data.code == 1) {
                                    self.get_summary()
                                    self.$message({
                                        message: response.data.msg,
                                        type: 'success'
                                    });
                                } else {
                                    self.$message({
                                        message: response.data.msg,
                                        type: 'error'
                                    });
                                }
                            }
                        );
                })
            },
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        // let para = Object.assign({}, this.editForm);
                        var self = this;
                        let str = 'id=' + this.editForm.id + '&summary=' + self.editForm.summary + '&plan=' + self.editForm.plan + '&self_evaluation=' + self.editForm.self_evaluation;
                        this.$axios.post('/works/update_summary/', str).then(function (response) {
                            if (response.data.code == 1) {
                                self.get_summary();
                                self.$message({
                                    message: response.data.msg,
                                    type: 'success'
                                });
                            } else {
                                self.$message({
                                    message: response.data.msg,
                                    type: 'error'
                                });
                            }
                        });
                        this.editFormVisible = false;
                    }
                })
            },
            SubmitWholeWeek: function (index, row) {
                this.$confirm('确认提交当周事件和总结吗?提交后无法修改已提交的总结和周报事件', '提示', {
                    type: 'warning'
                }).then(() => {
                    var self = this;
                    let str = 'submitID=' + row.id;
                    this.$axios.post('/works/submit_whole_week/', str)
                        .then(function (response) {
                                if (response.data.code == 1) {
                                    self.get_summary()
                                    self.$message({
                                        message: response.data.msg,
                                        type: 'success'
                                    });
                                } else {
                                    self.$message({
                                        message: response.data.msg,
                                        type: 'error'
                                    });
                                }
                            }
                        );
                })
            },
        },
    }
</script>
<style scoped>
    .title {
        text-align: center;
        margin: 5px 0 40px 0;
    }
</style>
