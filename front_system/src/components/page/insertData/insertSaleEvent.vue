<template>
    <div>
    
        <!--<h1 class="title"  style="align:center;">登记拜访</h1>
                                                <!--</br>-->
    
        </br>
        <el-form :inline="true" :model="filters">
            <el-col :span="16" class="toolbar" style="padding-bottom: 0px;">
                <span class="demonstration">筛选客户</span>
                <el-select v-model="filters.customer_id" clearable filterable placeholder="客户名">
                    <el-option v-for="item in customer_list" :key="item.id" :label="item.short_name" :value="item.id">
                    </el-option>
                </el-select>
                <span class="demonstration">筛选时间</span>
                <el-date-picker v-model="filters.filter_date" type="daterange" align="right" placeholder="选择日期范围" @change='filterDateChange' :picker-options="pickerOptions2">
                </el-date-picker>
                <el-form-item>
                    <el-button type="primary" v-on:click="filter">查询</el-button>
                </el-form-item>
            </el-col>
            <el-col :span="8" class="toolbar" style="padding-bottom: 0px;">
                <el-form-item>
                    <el-button type="success" @click="handleAddVisit">新增拜访</el-button>
                </el-form-item>
            </el-col>
        </el-form>
        </br>
        </br>
        </br>
        <el-dialog title="新增客户" v-model="addCustomerVisible" :close-on-click-modal="false" size="calcDialogSize">
            <el-form ref="insertCustomerForm" :model="insertCustomerForm" label-width="90px">
                <el-form-item label="客户全称">
                    <el-col :span="24">
                        <el-input type="text" class="form-control" id="full_name" placeholder="客户全称" v-model="insertCustomerForm.full_name">客户全称
                        </el-input>
                    </el-col>
    
                </el-form-item>
                <el-form-item label="客户简称">
                    <el-col :span="24">
                        <el-input type="text" class="form-control" id="short_name" placeholder="客户简称" v-model="insertCustomerForm.short_name">客户简称
                        </el-input>
                    </el-col>
                </el-form-item>
    
                <el-form-item label="联系人">
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="contact_name" placeholder="主要联系人姓名" v-model="insertCustomerForm.contact_name">主要联系人姓名
                        </el-input>
                    </el-col>
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="contact_post" placeholder="主要联系人职位" v-model="insertCustomerForm.contact_post">主要联系人职位
                        </el-input>
                    </el-col>
                </el-form-item>
    
                <el-form-item label="联系方式">
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="contact_mdn" placeholder="主要联系人手机号码" v-model="insertCustomerForm.contact_mdn">主要联系人手机号码
                        </el-input>
                    </el-col>
    
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="contact_tel_num" placeholder="主要联系人电话号码" v-model="insertCustomerForm.contact_tel_num">主要联系人电话号码
                        </el-input>
                    </el-col>
    
                </el-form-item>
    
                <el-form-item label="备注：">
                    <el-col :span="24">
                        <el-input type="textarea" class="form-control" id="sale_customer_remark" placeholder="备注" v-model="insertCustomerForm.sale_customer_remark">
                            备注
                        </el-input>
                    </el-col>
    
                </el-form-item>
            </el-form>
    
            <div slot="footer" class="dialog-footer">
                <el-col :span="16">
                    <el-button @click.native="addCustomerVisible = false">取消</el-button>
                    <el-button type="primary" @click.native="addCustomer" :loading="addLoading">提交</el-button>
                </el-col>
                </br>
                </br>
                </br>
            </div>
    
        </el-dialog>
        <el-dialog title="新增拜访" v-model="addEventVisible" :close-on-click-modal="false">
            <el-form ref="insertForm" :model="insertForm" label-width="90px">
                <!--<el-form-item label="拜访时间">
            
                                        </el-form-item>-->
                <el-form-item label="拜访对象">
                    <el-select v-model="insertForm.sale_customer_id" clearable filterable placeholder="客户名">
                        <el-option v-for="item in customer_list" :key="item.id" :label="item.short_name" :value="item.id">
                        </el-option>
                    </el-select>
                    <el-button type="primary" @click="handleAddCustomer">新增客户</el-button>
                </el-form-item>
    
                <el-form-item label="拜访信息">
                    <el-date-picker v-model="insertForm.visit_date" type="date" placeholder="拜访时间" @change="dateChange" :picker-options="dateOption">
                    </el-date-picker>
                    <el-select v-model="insertForm.active_type_id" clearable filterable placeholder="拜访类型">
                        <el-option v-for="item in sale_event_type_list" :key="item.id" :label="item.active_type_name" :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="insertForm.sale_phase_id" clearable filterable placeholder="阶段">
                        <el-option v-for="item in sale_phase_list" :key="item.id" :label="item.phase_name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
    
                <el-form-item label="客户信息">
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="cus_con_post" placeholder="客户职位" v-model="insertForm.cus_con_post">主要联系人电话号码
                        </el-input>
                    </el-col>
    
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="cus_con_wechart" placeholder="客户的微信号" v-model="insertForm.cus_con_wechart">客户的微信号
                        </el-input>
                    </el-col>
    
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="cus_con_mdn" placeholder="手机号码" v-model="insertForm.cus_con_mdn">手机号码
                        </el-input>
                    </el-col>
                    <el-col :span="12">
                        <el-input type="text" class="form-control" id="cus_con_tel_num" placeholder="客户电话号码" v-model="insertForm.cus_con_tel_num">客户联系方式
                        </el-input>
                    </el-col>
    
                </el-form-item>
                <el-form-item label="沟通成果：">
                    <el-input type="textarea" class="form-control" id="communicate_record" placeholder="沟通成果" v-model="insertForm.communicate_record">沟通成果
                    </el-input>
                </el-form-item>
                <el-form-item label="故障：">
                    <el-input type="textarea" class="form-control" id="break_down_content" placeholder="出现的故障" v-model="insertForm.break_down_content">沟通成果
                    </el-input>
                </el-form-item>
                <el-form-item label="拜访地址：">
                    <el-input type="textarea" class="form-control" id="visit_address" placeholder="拜访地址" v-model="insertForm.visit_address">沟通成果
                    </el-input>
                </el-form-item>
                <el-form-item label="下次时间：">
                    <el-date-picker v-model="insertForm.expect_next_visit_time" type="date" placeholder="预计下次拜访时间" @change="dateChange2">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="备注：">
                    <el-input type="textarea" class="form-control" id="sale_event_remark" placeholder="备注" v-model="insertForm.sale_event_remark">
                        备注
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addEventVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addWork" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
    
        <el-table :data="sale_list" border style="width: 100%">
            <el-table-column prop="visit_date" label="拜访时间" width="150" fixed='left' sortable>
            </el-table-column>
            <el-table-column prop="short_name" label="客户简称" width="150" sortable>
            </el-table-column>
            <el-table-column prop="phase_name" label="拜访阶段" width="150" sortable>
            </el-table-column>
            <el-table-column prop="cus_con_post" label="客户职位" width="150" sortable>
            </el-table-column>
    
            <el-table-column prop="cus_con_mdn" label="手机号码" width="160">
            </el-table-column>
            <el-table-column prop="cus_con_tel_num" label="客户联系方式" width="190">
            </el-table-column>
            <el-table-column prop="cus_con_wechart" label="客户的微信号" width="190">
            </el-table-column>
    
            <el-table-column prop="communicate_record" label="沟通成果" width='170' sortable>
            </el-table-column>
    
            <el-table-column prop="break_down_content" label="出现的故障" width='170' sortable>
            </el-table-column>
    
            <el-table-column prop="expect_next_visit_time" label="预计下次拜访时间" width='170' sortable>
            </el-table-column>
    
            <el-table-column prop="visit_address" label="拜访地址" width='170' sortable>
            </el-table-column>
    
            <el-table-column prop="sale_event_remark" label="备注" width="150">
            </el-table-column>
            <el-table-column prop="sale_event_id" label="id" width="100">
            </el-table-column>
            <el-table-column label="操作" width="140" fixed="right">
                <template slot-scope="scope">
                    <!--<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>-->
                    <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form ref="form" :model="editForm" label-width="80px">
                <el-form-item label="工作内容">
                    <el-input type="textarea" class="form-control" id="description" placeholder="工作内容" v-model="editForm.description">工作内容
                    </el-input>
                </el-form-item>
    
                <el-form-item label="工作时间">
                    <el-col :span="11" class="block">
                        <el-date-picker v-model="editForm.start_time" type="datetime" placeholder="选择日期时间">
                        </el-date-picker>
                    </el-col>
                    <el-col class="line" :span="2">-</el-col>
                    <el-col :span="11" class="block">
                        <el-date-picker v-model="editForm.end_time" type="datetime" placeholder="选择日期时间">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="项目情况：">
    
                    <el-col :span="8">
                        <el-input type="text" class="form-control" id="up_reporter_id" placeholder="上游汇报人" v-model="editForm.up_reporter_id">
                            工作负责人
                        </el-input>
                    </el-col>
                    <el-col :span="8">
                        <el-input type="text" class="form-control" id="down_reporter_ids" placeholder="下游汇报人" v-model="editForm.down_reporter_ids">下游汇报人
                        </el-input>
    
                    </el-col>
                    <el-col :span="8">
                        <el-input type="text" class="form-control" id="fin_percentage" placeholder="进度" v-model="editForm.fin_percentage">进度
                        </el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addWork" :loading="addLoading">提交修改</el-button>
            </div>
    
        </el-dialog>
    
    </div>
</template>

<script>
export default {
    data() {
        return {
            filters: {
                customer_id: '',
                filter_date: '',

            },
            pickerOptions2: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            dateOption: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7 * 6;
                }
            },
            sale_list: [],
            sale_phase_list: [],
            customer_list: [],
            sale_event_type_list: [],
            user_list: [],
            insertCustomerForm: {
                full_name: '',
                short_name: '',
                contact_post: '',
                contact_name: '',
                contact_mdn: '',
                contact_tel_num: '',
                sale_customer_remark: '',
            },
            insertForm: {
                cus_con_post: '',
                visit_date: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
                cus_con_mdn: '',
                cus_con_tel_num: '',
                cus_con_wechart: '',
                communicate_record: '',
                sale_event_remark: '',
                active_type_id: '',
                sale_customer_id: '',
                sale_phase_id: '',
                break_down_content: '',
                expect_next_visit_time: '',
                visit_address: '',
            },
            editForm: {
                description: '',
                start_time: '',
                end_time: '',
                fin_percentage: '',
                up_reporter_ids: '',
                down_reporter_ids: '',
                dev_event_remark: '',
            },
            addEventVisible: false,//新增拜访界面是否显示
            addCustomerVisible: false,//新增客户界面是否显示
            addLoading: false,
            editFormVisible: false,//编辑界面是否显示
            editLoading: false,
            //新增界面数据
            // addForm: {
            //     name: '',
            //     sex: -1,
            //     age: 0,
            //     birth: '',
            //     addr: ''
            // },
        }
    },
    created() {
        // 组件创建完后获取数据，这里和1.0不一样，改成了这个样子
        this.get_sale_event()
        this.get_customers()
        this.get_sale_phases()
        this.get_sale_event_types()
    },
    methods: {
        filterDateChange(val) {
            var self = this;
            self.filters.filterDate = val;
        },
        dateChange(val) {
            var self = this;
            self.insertForm.visit_date = val
        },
        dateChange2(val) {
            var self = this;
            self.insertForm.expect_next_visit_time = val
        },
        filter(val) {
            this.get_sale_event();
        },
        calcDialogSize: function () {
            let size = 'normal';
            const width = window.innerWidth;

            if (width < 1024) {
                size = 'full';
            } else if (width < 13600) {
                size = 'normal';
            } else if (width < 1920) {
                size = 'large';
            }
            return size;
        },
        get_sale_event_types: function (params) {
            var self = this;
            this.$axios.get('/works/get_sale_event_types/')
                .then(function (response) {
                    self.sale_event_type_list = eval(response.data.content);
                }
                );
        },
        get_sale_phases: function (params) {
            var self = this;
            this.$axios.get('/works/get_sale_phases/')
                .then(function (response) {
                    self.sale_phase_list = eval(response.data.content);

                }
                );
        },
        get_sale_event: function (params) {
            var self = this;
            this.$axios.get('/works/get_saleevents/', {
                params: {
                    filter_date: self.filters.filterDate,
                    customer_id: self.filters.customer_id
                }
            })
                .then(function (response) {
                    self.sale_list = eval(response.data.content);
                }
                );
        },
        get_customers: function (params) {
            var self = this;
            this.$axios.get('/works/get_customers/')
                .then(function (response) {
                    self.customer_list = eval(response.data.content);
                }
                );
        },
        addCustomer: function () {
            var self = this;
            let str = 'full_name=' + self.insertCustomerForm.full_name + '&short_name=' + self.insertCustomerForm.short_name + '&contact_post=' + self.insertCustomerForm.contact_post + '&contact_name=' + self.insertCustomerForm.contact_name + '&contact_mdn=' + self.insertCustomerForm.contact_mdn + '&contact_tel_num=' + self.insertCustomerForm.contact_tel_num + '&sale_customer_remark=' + self.insertCustomerForm.sale_customer_remark;
            this.$axios.post('/works/insert_customer/', str).then(function (response) {
                if (response.data.code == 1) {

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
            // this.insertCustomerForm = Object.assign({},insertCustomerForm);
            this.get_customers();
            this.addCustomerVisible = false;
            var clean_field = ['full_name', 'short_name', 'contact_post', 'contact_name', 'contact_mdn', 'contact_tel_num', 'sale_customer_remark']
            for (var i = 0; i < clean_field.length; i++) {
                self.insertForm[clean_field[i]] = '';
            }

        },

        addWork: function () {
            var self = this;
            let str = 'visit_date=' + self.insertForm.visit_date + '&break_down_content=' + self.insertForm.break_down_content + '&expect_next_visit_time=' + self.insertForm.expect_next_visit_time + '&visit_address=' + self.insertForm.visit_address + '&cus_con_post=' + self.insertForm.cus_con_post + '&cus_con_mdn=' + self.insertForm.cus_con_mdn + '&cus_con_tel_num=' + self.insertForm.cus_con_tel_num + '&cus_con_wechart=' + self.insertForm.cus_con_wechart + '&communicate_record=' + self.insertForm.communicate_record + '&sale_event_remark=' + self.insertForm.sale_event_remark + '&sale_event_owner_id=' + self.insertForm.sale_event_owner_id + '&active_type_id=' + self.insertForm.active_type_id + '&sale_customer_id=' + self.insertForm.sale_customer_id + '&sale_phase_id=' + self.insertForm.sale_phase_id;
            this.$axios.post('/works/insert_sale_event/', str).then(function (response) {

                if (response.data.code == 1) {

                    self.$message({
                        message: response.data.msg,
                        type: 'success'
                    });
                    self.get_sale_event()
                } else {
                    self.$message({
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            });
            //this.$refs['addForm'].resetFields();
            this.addEventVisible = false;
            this.get_sale_event();
            var clean_field = ['cus_con_post', 'cus_con_mdn', 'cus_con_tel_num', 'cus_con_wechart', 'communicate_record', 'sale_event_remark', 'active_type_id', 'sale_customer_id', 'sale_phase_id']
            for (var i = 0; i < clean_field.length; i++) {
                self.insertCustomerForm[clean_field[i]] = '';
            }
        },

        handleDelete: function (index, row) {
            var self = this;
            this.$axios.get('/works/del_sale_event/', {
                params: {
                    delID: row.sale_event_id,
                }
            })
                .then(function (response) {
                    if (response.data.code == 1) {

                        self.$message({
                            message: '恭喜你，删除成功',
                            type: 'success'
                        });
                    } else {
                        self.$message({
                            message: '删除失败',
                            type: 'error'
                        });
                    }
                    self.get_sale_event();
                }
                );

        },
        handleEdit: function (index, row) {
            this.editFormVisible = true;
            this.editForm = Object.assign({}, row);
        },
        handleAddVisit: function () {
            this.addEventVisible = true;
            // this.addForm = {
            //     name: '',
            //     sex: -1,
            //     age: 0,
            //     birth: '',
            //     addr: ''
            // };
        },
        handleAddCustomer: function () {
            this.addCustomerVisible = true;
            // this.addForm = {
            //     name: '',
            //     sex: -1,
            //     age: 0,
            //     birth: '',
            //     addr: ''
            // };
        },
    },
}
</script>
<style scoped>
.title {
    margin: 0 auto;
    text-align: center;
    margin: 5px 0
}

.filter {
    margin-top: 40px
}
</style>
