<template>
    <div>
        <div class="filter" style="margin-bottom:20px">
            <el-row :gutter="24">
                <span class="demonstration">项目名：</span>
                <el-select v-model="filters.project_id" clearable filterable placeholder="项目名">
                    <el-option v-for="item in project_item_list" :key="item.id" :label="item.project_name"
                               :value="item.id">
                    </el-option>
                </el-select>
                <span class="demonstration">时间：</span>
                <el-date-picker v-model="filters.filter_date" type="daterange" value-format="yyyy-MM-dd" align="right"
                                placeholder="选择日期范围" @change='filterDateChange' :picker-options="pickerOptions2">
                </el-date-picker>
                <el-button type="primary" v-on:click="get_dev_event">查询</el-button>
                <el-button type="success" @click="handleAdd">新增</el-button>
            </el-row>
        </div>

        <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false" size='calcDialogSize'>
            <el-form :model="insertForm" :rules="rules" ref="insertForm" label-width="90px">
                <el-row :gutter="24">
                    <el-col :span="12" :xs="24">
                        <el-form-item label="项目类型" prop="dev_event_project_type_id">
                            <el-select v-model="insertForm.dev_event_project_type_id" clearable filterable
                                       placeholder="项目类型" @change="get_type_projects">
                                <el-option v-for="item in project_type_list" :key="item.id"
                                           :label="item.project_type_name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="项目名" prop="dev_event_project_id">
                            <el-select v-model="insertForm.dev_event_project_id" clearable filterable
                                       placeholder="项目类型">
                                <el-option v-for="item in project_item_list" :key="item.id" :label="item.project_name"
                                           :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>

                </el-row>

                <el-row :gutter="24">

                    <el-col :span="12" :xs="24">
                        <el-form-item label="工作类型" prop="dev_event_type_id">
                            <el-select v-model="insertForm.dev_event_type_id" clearable filterable placeholder="类型"
                                       @change="check_project">
                                <el-option v-for="item in event_type_list" :key="item.id" :label="item.event_type_name"
                                           :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="日期" prop="event_date">
                            <el-date-picker v-model="insertForm.event_date" type="date" format="yyyy-MM-dd"
                                            value-format="yyyy-MM-dd" placeholder="选择日期" @change="addEventDateChange"
                                            :picker-options="weekOption">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-col :span="12" :xs="24">

                        <el-form-item label="起始时间" prop="startTime">
                            <el-time-picker v-model="insertForm.start_time" value-format='HH:mm' format='HH:mm'
                                            @change="addEventStartTimeChange" placeholder="起始时间"></el-time-picker>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12" :xs="24">

                        <el-form-item label="结束时间" prop="endTime">
                            <el-time-picker v-model="insertForm.end_time" value-format='HH:mm' format='HH:mm'
                                            @change="addEventEndTimeChange" placeholder="结束时间"></el-time-picker>
                        </el-form-item>
                    </el-col>

                </el-row>

                <el-row :gutter="24">
                    <el-form-item label="工作描述" prop="description">
                        <el-col :span="24">
                            <el-input type="textarea" class="form-control" id="description" placeholder="工作内容"
                                      v-model="insertForm.description">工作内容
                            </el-input>
                        </el-col>
                    </el-form-item>
                </el-row>

                <el-row :gutter="24">

                    <el-col :span="12" :xs="24">
                        <el-form-item label="对接人" prop="up_reporter_id">
                            <el-select v-model="insertForm.up_reporter_id" clearable filterable placeholder="上游">
                                <el-option v-for="item in user_list" :key="item.user_id" :label="item.chinese_name"
                                           :value="item.user_id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="" prop="up_reporter_id">
                            <el-select v-model="insertForm.down_reporter_ids" multiple clearable filterable
                                       placeholder="下游">
                                <el-option v-for="item in user_list" :key="item.user_id" :label="item.chinese_name"
                                           :value="item.user_id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="完成比" prop="fin_percentage">
                            <el-input-number id="fin_percentage" default="100" v-model="insertForm.fin_percentage"
                                             :step="10" :min="0" :max="100">进度
                            </el-input-number>
                        </el-form-item>
                    </el-col>

                </el-row>

                <el-form-item label="备注">
                    <el-input type="textarea" class="form-control" id="dev_event_remark" placeholder="备注"
                              v-model="insertForm.dev_event_remark">
                        备注
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" :disabled="!allow_submit" @click="submitForm('insertForm')"
                           :loading="addLoading">创建
                </el-button>

            </div>
            <div>
                <el-alert :title="notification_info" type="warning" show-icon v-show="notification_show">
                </el-alert>
                <p>**每个内容最多500字符** </p>
                <p>**原则上选定了具体项目名称后，工作类型一般不要选择“其他”项** </p>
            </div>
        </el-dialog>

        <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false" size="calcDialogSize">
            <el-form :model="editForm" :rules="rules" ref="editForm" label-width="90px">
                <el-row :gutter="24">
                    <el-col :span="12" :xs="24">
                        <el-form-item label="项目类型" prop="dev_event_project_type_id">
                            <el-select v-model="editForm.dev_event_project_type_id" clearable filterable
                                       placeholder="项目类型" @change="get_type_projects">
                                <el-option v-for="item in project_type_list" :key="item.id"
                                           :label="item.project_type_name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="项目名" prop="dev_event_project_id">
                            <el-select v-model="editForm.dev_event_project_id" clearable filterable placeholder="项目类型">
                                <el-option v-for="item in project_item_list" :key="item.id" :label="item.project_name"
                                           :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-col :span="12" :xs="24">
                        <el-form-item label="工作类型" prop="dev_event_type_id">
                            <el-select v-model="editForm.dev_event_type_id" clearable filterable placeholder="类型">
                                <el-option v-for="item in event_type_list" :key="item.id" :label="item.event_type_name"
                                           :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12" :xs="24">
                        <el-form-item label="日期" prop="event_date">
                            <el-date-picker v-model="editForm.event_date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                                            type="date" placeholder="选择日期" @change="editEventDateChange"
                                            :picker-options="weekOption">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="工作描述" prop="description">
                    <el-col :span="24">
                        <el-input type="textarea" class="form-control" id="description" placeholder="工作内容"
                                  v-model="editForm.description">工作内容
                        </el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="完成比" prop="fin_percentage">
                    <el-input-number id="fin_percentage" default="100" v-model="editForm.fin_percentage" :step="10"
                                     :min="0" :max="100">进度
                    </el-input-number>
                </el-form-item>
                <el-row :gutter="24">
                    <el-col :span="12">
                        <el-form-item label="起始时间" prop="start_time">
                            <el-time-picker v-model="editForm.start_time" value-format='HH:mm' format='HH:mm'
                                            @change="editEventStartTimeChange" placeholder="起始时间"></el-time-picker>
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="结束时间" prop="end_time">
                            <el-time-picker v-model="editForm.end_time" value-format='HH:mm' format='HH:mm'
                                            @change="editEventEndTimeChange" placeholder="结束时间"></el-time-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="24">
                    <el-col :span="12">
                        <el-form-item label="对接人" prop="up_reporter_id">
                            <el-select v-model="editForm.up_reporter_id" clearable filterable placeholder="上游">
                                <el-option v-for="item in user_list" :key="item.user_id" :label="item.chinese_name"
                                           :value="item.user_id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="" prop="up_reporter_id">
                            <el-select v-model="editForm.down_reporter_ids" multiple clearable filterable
                                       placeholder="下游">
                                <el-option v-for="item in user_list" :key="item.user_id" :label="item.chinese_name"
                                           :value="item.user_id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="备注">
                    <el-input type="textarea" class="form-control" id="dev_event_remark" placeholder="备注"
                              v-model="editForm.dev_event_remark">
                        备注
                    </el-input>
                </el-form-item>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" :disabled="!allow_submit" @click.native="editSubmit('editForm')"
                           :loading="editLoading">保存
                </el-button>
            </div>
            <div>
                <el-alert :title="notification_info" type="warning" show-icon v-if="notification_show">
                </el-alert>
                <p>**每个内容最多500字符** </p>
                <p>**原则上选定了具体项目名称后，工作类型一般不要选择“其他”项** </p>

            </div>

        </el-dialog>

        <template>
            <el-table :data="filter_work_list" style="width: 100%" border>
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item label="事件日期：">
                                <span>{{ props.row.event_date }}</span>
                            </el-form-item>
                            <el-form-item label="项目名称：">
                                <span>{{ props.row.project_name }}</span>
                            </el-form-item>
                            <el-form-item label="工作类型：">
                                <span>{{ props.row.event_type_name }}</span>
                            </el-form-item>
                            <el-form-item label="工作描述：">
                                <span>{{ props.row.description }}</span>
                            </el-form-item>
                            <el-form-item label="开始时间：">
                                <span>{{ props.row.start_time }}</span>
                            </el-form-item>
                            <el-form-item label="结束时间：">
                                <span>{{ props.row.end_time }}</span>
                            </el-form-item>
                            <el-form-item label="上游对接人：">
                                <span>{{ props.row.up_reporter_name }}</span>
                            </el-form-item>
                            <el-form-item label="下游对接人：">
                                <span>{{ props.row.down_reporter_name }}</span>

                            </el-form-item>

                            <el-form-item label="备注：">
                                <span>{{ props.row.dev_event_remark }}</span>
                            </el-form-item>
                            <el-form-item label="完成比：">
                                <span>{{ props.row.fin_percentage }}</span>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column label="日期" prop="event_date" width="160">
                </el-table-column>
                <el-table-column label="开始" prop="start_time" width="110">
                </el-table-column>
                <el-table-column label="结束" prop="end_time" width="110">
                </el-table-column>
                <el-table-column label="项目名称" prop="project_name" min-length="120">
                </el-table-column>
                <el-table-column label="工作描述" prop="description" min-length="350">
                </el-table-column>
                <el-table-column label="完成比" prop="fin_percentage" width="120">
                </el-table-column>
                <el-table-column label="操作" prop="dev_closed_status">

                    <template slot-scope="scope">
                        <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
                                   v-if="scope.row.dev_closed_status==0"> 编辑
                        </el-button>
                        <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)"
                                   v-if="scope.row.dev_closed_status==0">删除
                        </el-button>
                        <i class="el-icon-check" v-if="scope.row.dev_closed_status==1"></i>
                    </template>
                </el-table-column>
            </el-table>

        </template>
        </br>
        </br>
        </br>
        <p>不选日期则取默认当周内容</p>
        </br>
        <!-- <el-button type="primary" ><a href='/works/get_event_excel/'>下载excel(缺参数)</a></el-button>             -->
    </div>
</template>

<script>
    export default {
        data() {
            return {
                filters: {
                    project_id: '',
                    filter_date: '',
                    start_date: '',
                    end_date: '',
                },
                add_notificition: '',
                notification_info: '',
                notification_show: false,
                weekOption: {
                    firstDayOfWeek: 1,
                },
                pickerOptions2: {
                    firstDayOfWeek: 1,
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
                allow_submit: false,
                work_list: [],
                filter_work_list: [],
                project_type_list: [],
                project_item_list: [],
                event_type_list: [],
                user_list: [],
                // dateOption: {
                //     disabledDate(time) {
                //         return time.getTime() < Date.now() - 8.64e7 * 30;
                //     }
                // },
                insertForm: {
                    description: '',
                    event_date: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
                    start_time: '',
                    end_time: '',
                    timestamp1: '',
                    timestamp2: '',
                    fin_percentage: '100',
                    up_reporter_id: '',
                    down_reporter_ids: '',
                    dev_event_remark: '',
                    dev_event_project_type_id: '',
                    dev_event_project_id: '',
                    dev_event_type_id: '',
                    project_name: '',
                },
                lastForm: {},
                editForm: {
                    description: '',
                    event_date: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
                    start_time: '',
                    end_time: '',
                    timestamp1: '',
                    timestamp2: '',
                    fin_percentage: '',
                    up_reporter_id: '',
                    down_reporter_ids: '',
                    dev_event_remark: '',
                    dev_event_project_type_id: '',
                    dev_event_project_id: '',
                    dev_event_type_id: '',
                    project_name: '',
                },
                // ruleForm: {
                //     name: '',
                //     region: '',
                //     date1: '',
                //     date2: '',
                //     delivery: false,
                //     type: [],
                //     resource: '',
                //     desc: ''
                // },
                rules: {
                    dev_event_project_id: [
                        {type: 'number', required: true, message: '必填项', trigger: 'change'}
                    ],
                    // dev_event_project_type_id: [
                    //     { type: 'number', required: true, message: '必填项', trigger: 'change' }
                    // ],
                    dev_event_type_id: [
                        {type: 'number', required: true, message: '必填项', trigger: 'change'}
                    ],
                    event_date: [
                        {type: 'string', required: true, message: '日期为必填项', trigger: 'change'}
                    ],
                    startTime: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    endTime: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    description: [
                        {type: 'string', required: true, message: '必填项', trigger: 'change'}
                    ],
                    fin_percentage: [
                        {type: 'number', required: true, message: '必填项', trigger: 'change'}
                    ],
                },
                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                testValue: '',
            }
        },
        created() {
            // 组件创建完后获取数据
            var self = this;
            this.get_dev_event();
            this.get_project_type();
            this.get_event_types();
            this.get_type_projects(-1)
            this.get_users();
        },
        filters: {},
        computed: {
            // formatActive: function(dev_closed_status) {
            //     return dev_closed_status == 1 ? 'true' : dev_closed_status == 0 ? false : false;
            // },
            calcDialogSize: function () {
                var size = 'normal';
                var width = window.innerWidth;
                if (width < 1200) {
                    size = 'full';
                }
                //  else if (width > 1360) {
                //     size = 'large';
                // } else if (width < 1920) {
                //     size = 'large';
                // }
                return size;
            },
        },
        methods: {

            filter_time: function (input) {
                //筛选时间格式里的小时和分钟数
                var d = new Date(input);
                var hour = d.getHours();
                var minutes = d.getMinutes() < 10 ? '0' + d.getMinutes() : '' + d.getMinutes();
                return hour + ':' + minutes;
            },
            check_project: function () {
                var self = this;
                for (var k in self.project_item_list) {
                    if (self.project_item_list[k].project_name == '其他--项目') {
                        var other_id = self.project_item_list[k].id;
                    }
                }
                for (var k in self.event_type_list) {
                    //将请假等3个类型的id取出来并与选择的类型id比较，如果相同则填入指定内容
                    if (self.event_type_list[k].event_type_name == '病假' || self.event_type_list[k].event_type_name == '事假' || self.event_type_list[k].event_type_name == '调休') {
                        // option_array.push(self.event_type_list[k].id)
                        if (self.insertForm.dev_event_type_id == self.event_type_list[k].id) {
                            this.insertForm.description = self.event_type_list[k].event_type_name
                            this.insertForm.dev_event_project_id = other_id
                            this.insertForm.startTime = "08:30:00";
                            this.insertForm.endTime = "17:30:00";
                            this.insertForm.start_time = "08:30:00";
                            this.insertForm.end_time = "17:30:00";

                            this.checkAddTime();
                            break;
                        }
                    }
                    if (self.event_type_list[k].event_type_name == '午休') {
                        if (self.insertForm.dev_event_type_id == self.event_type_list[k].id) {
                            this.insertForm.description = self.event_type_list[k].event_type_name
                            this.insertForm.dev_event_project_id = other_id
                            this.insertForm.startTime = "11:30:00"
                            this.insertForm.endTime = "13:00:00"

                            this.insertForm.start_time = "11:30:00"
                            this.insertForm.end_time = "13:00:00"
                            this.checkAddTime();
                            break;
                        }
                    }
                    // self.insertForm.description=''
                    // self.insertForm.dev_event_project_id=''
                    // self.insertForm.start_time= ''
                    // self.insertForm.end_time= ''
                }

            },
            filterDateChange(val) {
                this.filters.filterDate = JSON.stringify(val);
            },
            addEventDateChange(val) {
                if (val) {
                    // this.insertForm.event_date = val
                    if (this.insertForm.startTime && this.insertForm.endTime) {
                        this.checkAddTime();
                    }
                }
            },
            addEventStartTimeChange(val) {
                if (val) {
                    this.insertForm.startTime = val + ':00'
                    if (this.insertForm.endTime && this.insertForm.event_date) {
                        this.checkAddTime();
                    }
                }
            },
            addEventEndTimeChange(val) {
                if (val) {
                    this.insertForm.endTime = val + ':00'
                    if (this.insertForm.startTime && this.insertForm.event_date) {
                        this.checkAddTime()
                    }
                }
            },
            editEventDateChange(val) {
                if (val) {
                    this.editForm.event_date = val
                    if (this.editForm.startTime && this.editForm.endTime) {
                        this.checkEditTime();
                    }
                }
            },
            editEventStartTimeChange(val) {
                if (val) {
                    this.editForm.startTime = val + ':00'
                    if (this.editForm.endTime && this.editForm.event_date) {

                        this.checkEditTime();
                    }
                }
            },
            editEventEndTimeChange(val) {
                if (val) {
                    this.editForm.endTime = val + ':00'
                    if (this.editForm.startTime && this.editForm.event_date) {
                        this.checkEditTime()
                    }
                }
            },
            checkTime1: function (timestamp1, timestamp2) {
                //检查时间差是否在0-2小时之间，否则就进行错误提示
                var self = this;
                let time_diff = (timestamp2 - timestamp1);   //时间差
                var legal = true;
                if (time_diff < 5 * 1000 * 60) {
                    // this.$notify({
                    //     title: '错误',
                    //     message: '时间差为负数或者小于5分钟，请修改'
                    // });
                    legal = false;
                    this.notification_info = '时间差小于5分钟，请修改';
                    this.notification_show = true;
                } else if (time_diff > 2 * 1000 * 3600) {
                    // this.$notify({
                    //     title: '错误',
                    //     message: '时间差大于2小时，最好分开填写'
                    // });
                    this.notification_show = true;
                    this.notification_info = '时间差大于2小时，最好分开填写'
                }
                ;
                return legal
            },
            checkTime2: function (timestamp1, timestamp2, current_event_date, exclude_id) {
                //检查当天时间范围是否已经填了，没有使用过的时间范围则返回true,否则返回false
                //work_list为以时间逆序的列表
                //excude_id为排除检查的事件id，主要为了避免更新事件时无法因时间重复无法更新选中的事件
                var self = this;
                var computed_list = new Array()

                for (var i = 0; i < this.work_list.length; i++) {
                    let a = new Date(this.work_list[i].event_date + " " + "00:00:00");
                    let b = new Date(current_event_date + " " + "00:00:00");
                    //此处一定要用getTime，否则变成对象的比较了
                    if (a.getTime() == b.getTime() && this.work_list[i].dev_event_id != exclude_id) {
                        //&& this.work_list[i].dev_event_id !=this.editForm.dev_event_id
                        var temp = new Object;
                        temp.start = new Date(this.work_list[i].event_date + " " + this.work_list[i].start_time + ":00");
                        temp.end = new Date(this.work_list[i].event_date + " " + this.work_list[i].end_time + ":00");
                        computed_list.push(temp)
                    }
                }
                console.log(computed_list)
                var compare = function (prop) {
                    return function (obj1, obj2) {
                        var val1 = obj1[prop];
                        var val2 = obj2[prop];
                        if (val1 < val2) {
                            return -1;
                        } else if (val1 > val2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }

                computed_list.sort(compare("start"))
                let work_length = computed_list.length;
                let legal = false
                let start = timestamp1
                let end = timestamp2

                if (work_length <= 0) {
                    legal = true
                } else {
                    //如果新的时间范围在第一个时间的开始时间之前及最后一个结束时间之后，则必然不重合
                    //如果新的时间范围在某一个时间范围的结束时间之后，且在下一个时间范围的开始时间之前，则必然不重合
                    for (var i = 0; i < work_length; i++) {
                        if ((end <= computed_list[0].start || (start >= computed_list[work_length - 1].end))) {
                            legal = true;
                        } else if (start >= computed_list[i].end && end <= computed_list[i + 1].start) {
                            legal = true;
                        }
                    }
                }
                if (!legal) {
                    // self.$message({
                    //     message: '时间范围重复，请重新填写',
                    //     type: 'error'
                    // });
                    this.notification_show = true;
                    this.notification_info = '时间范围可能重复，请注意'
                }
                return legal
            },
            checkAddTime: function (params) {
                var self = this;
                this.allow_submit = false;
                this.notification_info = '';
                this.notification_show = false;
                //同时检查2种情况，看是否合法
                var timestamp1 = new Date(this.insertForm.event_date + " " + this.insertForm.startTime);
                var timestamp2 = new Date(this.insertForm.event_date + " " + this.insertForm.endTime);


                var exclude_id = -1
                var current_event_date = this.insertForm.event_date
                var v1 = this.checkTime1(timestamp1, timestamp2)
                var v2 = this.checkTime2(timestamp1, timestamp2, current_event_date, exclude_id)
                if (v1 && v2) {
                    this.allow_submit = true;
                }

            },
            checkEditTime: function (params) {
                var self = this;
                this.allow_submit = false;
                this.notification_info = '';
                this.notification_show = false;
                //同时检查2种情况，看是否合法
                var timestamp1 = new Date(this.editForm.event_date + " " + this.editForm.startTime);
                var timestamp2 = new Date(this.editForm.event_date + " " + this.editForm.endTime);

                var exclude_id = this.editForm.dev_event_id

                var current_event_date = this.editForm.event_date
                var v1 = this.checkTime1(timestamp1, timestamp2)
                var v2 = this.checkTime2(timestamp1, timestamp2, current_event_date, exclude_id)
                if (v1 && v2) {
                    this.allow_submit = true;
                }

            },
            get_users: function (params) {
                var self = this;
                this.$axios.get('/accounts/get_username/')
                    .then(function (response) {
                            self.user_list = eval(response.data.content);
                        }
                    );
            },
            filter_dev_event: function () {
                //显示时间范围
                var self = this;
                var filter_work_list = new Array();

                function cloneObject(obj) {
                    if (obj === null) {
                        return
                    }
                    //深度复制一个对象
                    var o = typeof obj == 'object' && obj.length === 'Number' ? [] : {};
                    for (var i in obj) {
                        if (obj.hasOwnProperty(i)) {
                            o[i] = typeof obj[i] === "object" ? cloneObject(obj[i]) : obj[i];
                        }
                    }
                    return o;
                }

                for (var i in self.work_list) {
                    var tmp = cloneObject(self.work_list[i])
                    tmp['startTime'] = this.filter_time(tmp['start_time'])
                    tmp['endTime'] = this.filter_time(tmp['end_time'])
                    if (tmp['up_reporter_id']) {
                        tmp['up_reporter_id'] = parseInt(tmp['up_reporter_id'])
                    }
                    if (tmp['down_reporter_ids']) {
                        tmp['down_reporter_ids'] = tmp['down_reporter_ids'].split(",")
                        for (var i = 0; i < tmp['down_reporter_ids'].length; i++) {
                            tmp['down_reporter_ids'][i] = parseInt(tmp['down_reporter_ids'][i])
                        }
                    }
                    filter_work_list.push(tmp)
                }
                return filter_work_list;
            },

            get_dev_event: function (params) {
                var self = this;
                this.$axios.get('/works/get_dev_events/', {
                    params: {
                        filter_date: this.filters.filterDate,
                        project_id: this.filters.project_id
                    }
                })
                    .then(function (response) {
                            self.work_list = eval(response.data.content);
                            self.filter_work_list = self.filter_dev_event()
                        }
                    );
            },

            get_project_type: function (params) {
                var self = this;
                this.$axios.get('/works/get_project_type/')
                    .then(function (response) {
                            self.project_type_list = eval(response.data.content);
                        }
                    );
            },
            get_type_projects: function (params) {
                var self = this;
                if (params) {
                    var project_type_id = params
                } else {
                    project_type_id = -1
                }
                this.$axios.get('/works/get_projects/', {
                    params: {
                        project_type_id: project_type_id,
                    }
                })
                    .then(function (response) {
                            self.project_item_list = eval(response.data.content);
                            self.insertForm.dev_event_project_id = '';
                            // self.editForm.dev_event_project_id='';

                        }
                    );
            },
            get_event_types: function (params) {
                var self = this;
                this.$axios.get('/works/get_event_types/')
                    .then(function (response) {
                            self.event_type_list = eval(response.data.content);
                        }
                    );
            },

            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {

                        // let para = Object.assign({}, this.editForm);
                        var self = this;
                        let para = Object.assign({}, this.editForm);
                        para.update_id = para.dev_event_id
                        let params = this.makePostReady(para)
                        this.$axios.post('/works/update_devevent/', params).then(function (response) {
                            if (response.data.code == 1) {
                                self.get_dev_event();
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
            handleDelete: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning'
                }).then(() => {
                        var self = this;
                        this.$axios.get('/works/del_devevent/', {
                            params: {
                                delID: row.dev_event_id,
                            }
                        })
                            .then(function (response) {
                                    if (response.data.code == 1) {
                                        self.get_dev_event()
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
                    }
                )
            },
            handleEdit: function (index, row) {

                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);

                this.editForm.startTime = this.editForm.start_time + ':00'
                this.editForm.endTime = this.editForm.end_time + ':00'
                this.checkEditTime();
                // this.edit_dev_event_id=this.editForm

            },
            handleAdd: function () {
                this.addFormVisible = true;

                this.insertForm = {
                    description: '',
                    event_date: new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(),
                    // event_time: new Array(),
                    start_time: '',
                    end_time: '',
                    fin_percentage: '100',
                    up_reporter_id: '',
                    down_reporter_ids: new Array(),
                    dev_event_remark: '',
                    dev_event_project_id: '',
                    dev_event_project_type_id: '',
                    dev_event_type_id: '',
                    project_name: '',
                    event_type_id: '',
                    timestamp1: '',
                    timestamp2: '',
                };
                if (this.lastForm.dev_event_project_id) {
                    this.insertForm.dev_event_project_id = this.lastForm.dev_event_project_id
                    this.insertForm.event_date = this.lastForm.event_date
                    this.insertForm.startTime = this.lastForm.endTime.slice(0, 7)
                    this.insertForm.start_time = this.lastForm.endTime.slice(0, 5)
                    this.checkAddTime()
                }


            },
            get_event_excel: function () {
                var self = this;
                this.$axios.get('/works/get_event_excel /', {
                    params: {
                        filter_date: self.filters.filterDate,
                        project_id: self.filters.project_id
                    }
                })
                    .then(function (response) {
                            self.work_list = eval(response.data.content);
                        }
                    );
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var self = this;
                        this.lastForm.event_date = self.insertForm.event_date
                        this.lastForm.startTime = self.insertForm.startTime
                        this.lastForm.endTime = self.insertForm.endTime
                        this.lastForm.dev_event_project_id = self.insertForm.dev_event_project_id

                        // this.lastForm.startTime = self.insertForm.endTime

                        let str = 'event_date=' + self.insertForm.event_date + '&startTime=' + self.insertForm.startTime + '&endTime=' + self.insertForm.endTime + '&description=' + self.insertForm.description + '&fin_percentage=' + self.insertForm.fin_percentage + '&up_reporter_id=' + self.insertForm.up_reporter_id + '&down_reporter_ids=' + self.insertForm.down_reporter_ids + '&dev_event_remark=' + self.insertForm.dev_event_remark + '&dev_event_project_id=' + self.insertForm.dev_event_project_id + '&dev_event_type_id=' + self.insertForm.dev_event_type_id;
                        this.$axios.post('/works/insert_devevent/', str).then(function (response) {
                            if (response.data.code == 1) {
                                self.addFormVisible = false;
                                // self.filters.filterDate = ''
                                self.filters.project_id = ''
                                self.get_dev_event()
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
                });
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
        margin: 40px 0
    }

    .hidden {
        display: none;
    }

    .demo-table-expand {
        font-size: 0;
    }

    .demo-table-expand label {
        width: 90px;
        /* color: #99a9bf; */
    }

    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 42%;
        padding-left: 66px;
    }

    .el-form--inline span {
        font-weight: 600;
    }
</style>
