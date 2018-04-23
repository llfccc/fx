<template>
    <div>

        <!--<h1 class="title" style="align:center;">查看员工周报</h1>-->
        <div>
            <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
                <el-form :inline="true" :model="filters">
                    <el-col :span="24">
                        <span class="demonstration">周数:</span>
                        <el-date-picker v-model="filters.filter_date" type="week" format="yyyy-WW 周"
                                        @change="weekChange" placeholder="选择周" :picker-options="weekOption">
                        </el-date-picker>
                        部门：
                        <el-select v-model="filters.analysis_department_id" placeholder="部门" filterable clearable>
                            <el-option v-for="item in department_list" :key="item.id" :label="item.department_name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                        <el-button type="primary" v-on:click="filter">查询</el-button>
                    </el-col>
                    <el-col :span="4">

                    </el-col>
                </el-form>
            </el-col>
            </br>
            </br>
            </br>
        </div>
        <el-table :data="weekly_data" style="width: 100%">
            <el-table-column prop="chinese_name" label="名字" sortable>
            </el-table-column>
            <el-table-column prop="mark" class="search" label="提交情况" sortable>
            </el-table-column>
            <el-table-column prop="sum_time" label="工作时长" sortable>
            </el-table-column>
            <el-table-column prop="load_percent" label="工作负荷">
            </el-table-column>
            <el-table-column prop="promtness_rate" label="周报及时率" sortable>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
                <template slot-scope="scope">
                    <!--<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>-->
                    <el-button size="small" type="danger" @click="get_employee_weekly(scope.row)">查看周报</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="查看周报" :visible.sync="EmployeeWeeklyVisible" size="large">
            <table border="1" class="table table-responsive table-bordered" width="100%" v-show="weekly_dict">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>星期</th>
                    <th>项目名称</th>
                    <th>类型</th>
                    <th>工时</th>
                    <th>事件描述</th>
                    <th>工时</th>
                    <th>上游对接人</th>
                    <th>下游对接人</th>
                    <th>完成比</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in weekly_dict">
                    <td width="5%" :rowspan="item.span" :class="{hidden: item.dis}">{{item.event_date}}</td>
                    <td width="5%" :rowspan="item.span" :class="{hidden: item.dis}">{{item.which_day}}</td>
                    <td width="8%">{{item.project_name}}</td>
                    <td width="5%">{{item.event_type_name}}</td>
                    <td width="4%" :rowspan="item.span" :class="{hidden: item.dis}">{{item.daily_sum_time |
                        num_round}}
                    </td>
                    <td width="18%">{{item.description}}</td>
                    <td width="3%">{{item.duration_time |num_round}}</td>
                    <td width="5%">{{item.up_reporter_name}}</td>
                    <td width="5%">{{item.down_reporter_name}}</td>
                    <td width="3%">{{item.fin_percentage}}</td>
                    <td width="12%">{{item.dev_event_remark}}</td>
                </tr>
                <tr height="40">
                    <td colspan="1">
                        <strong>汇总</strong>
                    </td>
                    <!--<td colspan="2" >{{summary.summary}}</td>-->
                    <td colspan="3"></td>
                    <td>
                        <strong>{{weekly_time | num_round}}</strong>
                    </td>
                    <td colspan="6"></td>
                </tr>
                </tbody>
            </table>
            </br>
            </br>

            <el-card class="box-card">
                <div slot="header" class="clearfix">
                    <span style="line-height: 36px;">工作总结-----{{summary.natural_week}}周</span>

                </div>
                <div class="text item">
                    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
                        <el-col :span="8">
                            <el-card class="box-card">
                                <div slot="header" class="clearfix">
                                    <span style="line-height: 16px;">周总结</span>
                                </div>
                                {{summary.summary}}
                            </el-card>
                        </el-col>
                        <el-col :span="8">
                            <el-card class="box-card">
                                <div slot="header" class="clearfix">
                                    <span style="line-height: 16px;">自我评价</span>
                                </div>
                                {{summary.self_evaluation}}
                            </el-card>
                        </el-col>
                        <el-col :span="8">
                            <el-card class="box-card">
                                <div slot="header" class="clearfix">
                                    <span style="line-height: 16px;">计划</span>
                                </div>
                                {{summary.plan}}
                            </el-card>
                        </el-col>
                    </el-col>
                </div>
            </el-card>
        </el-dialog>
    </div>
</template>
<script>

    export default {
        name: '',
        data() {
            return {
                weekly_dict: [],
                filters: {
                    filter_date: '',
                    analysis_department_id: '',
                    // naturalWeek: '',
                },
                summary: {
                    summary: '',
                    self_evaluation: '',
                    plan: '',
                },
                user_list: '',
                EmployeeWeeklyVisible: false,
                weekly_data: [],
                department_list: [],
                weekOption: {
                    firstDayOfWeek: 1,
                },
            }
        },
        computed: {
            // filter_user_list() {
            //   var self = this;
            //   let department_id = localStorage.getItem('department_id');
            //   let ms_username = localStorage.getItem('ms_username');
            //   var filter_user_list = new Array();
            //   for (var i in self.user_list) {
            //     if (self.user_list[i].department == department_id & self.user_list[i].chinese_name != ms_username) {
            //       filter_user_list.push(self.user_list[i])
            //     }
            //   }
            //   return filter_user_list
            // }
            // fixed_weekly_dict() {
            //   var self = this;
            //   for (var i in this.weekly_dict) {
            //     this.weekly_dict[i]['load_percent'] = this.weekly_dict[i]['load_percent'] / 7.5 / 5
            //   }
            //   console.log(this.weekly_dict)
            //   return this.weekly_dict
            // }
            weekly_time() {
                //计算周总工作时间
                var weekly_time = 0;
                var self = this;
                for (var i in self.weekly_dict) {
                    weekly_time += self.weekly_dict[i]['duration_time']
                }
                // weekly_time=Math.ceil(weekly_time)
                return weekly_time;
            }
        },
        filters: {
            num_round(data) {
                return Math.round(data * 10) / 10
            }
        },
        methods: {
            weekChange(val) {
                var self = this;

                if (val) {
                    self.filters.naturalWeek = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }
            },

            filter: function (params) {
                this.analysis_employee_weekly();
                // this.get_summary();
                // this.get_users();
            },
            get_users: function (params) {
                var self = this;
                this.$axios.get('/accounts/get_username/')
                    .then(function (response) {
                            self.user_list = eval(response.data.content);
                        }
                    );
            },
            //获取部门列表
            getDepartment() {
                var self = this;
                var user_department_id = localStorage.getItem('department_id')

                this.$axios.get('/accounts/get_department/', {
                    params: {
                        department_id: user_department_id,
                    }
                }).then((res) => {
                    self.department_list = res.data.content;
                });
            },
            analysis_employee_weekly: function (params) {
                var self = this;

                function convertNull(input, key) {
                    //如果有这个键，则原样返回，否则返回0
                    if (input[key]) {
                        return input[key]
                    } else {
                        return 0
                    }
                }

                this.$axios.get('/analysis/analysis_employee_weekly/', {
                    params: {
                        natural_week: self.filters.naturalWeek,
                        analysis_department_id: self.filters.analysis_department_id,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            var result = new Array();
                            for (var i = 0; i <= responseContent.length - 1; i++) {
                                responseContent[i]['load_percent'] = Math.ceil(convertNull(responseContent[i], 'sum_time') / 8 / 5 * 100) + " %";
                                responseContent[i]['sum_time'] = convertNull(responseContent[i], 'sum_time') + " h"
                                if (convertNull(responseContent[i], 'total_line') * 100) {
                                    responseContent[i]['promtness_rate'] = Math.ceil(convertNull(responseContent[i], 'right_line') / convertNull(responseContent[i], 'total_line') * 100) + " %"
                                } else {
                                    responseContent[i]['promtness_rate'] = "0 %"
                                }
                                var markID = convertNull(responseContent[i], 'punctual_mark')
                                if (markID == 0) {
                                    responseContent[i]['mark'] = '未写总结'
                                }
                                if (markID == 3) {
                                    responseContent[i]['mark'] = '未交总结'
                                }
                                if (markID == 2) {
                                    responseContent[i]['mark'] = '总结晚交'
                                }
                                if (markID == 1) {
                                    responseContent[i]['mark'] = '√'
                                }
                                result.push(responseContent[i])
                            }
                            // delete responseContent[21]
                            self.weekly_data = result
                        }
                    );
            },
            get_employee_weekly: function (params) {
                var self = this;
                this.weekly_dict = [];

                let chinese_name = params.chinese_name
                this.EmployeeWeeklyVisible = true;
                this.$axios.get('/analysis/analysis_devevent/', {
                    params: {
                        natural_week: this.filters.naturalWeek,
                        employee_name: chinese_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);

                            function combineCell(list) {
                                var k = 0;
                                while (k < list.length) {
                                    // ;
                                    list[k]['span'] = 1;
                                    list[k]['dis'] = false;
                                    list[k]['daily_sum_time'] = list[k]['duration_time'];
                                    for (var i = k + 1; i <= list.length - 1; i++) {
                                        if (list[k]["event_date"] == list[i]["event_date"] && list[k]["event_date"] != '') {
                                            list[k]['span']++;
                                            list[k]['dis'] = false;
                                            list[i]['span'] = 1;
                                            list[i]['dis'] = true;
                                            list[k]['daily_sum_time'] += list[i]['duration_time'];
                                        } else {
                                            break;
                                        }
                                    }
                                    k = i;
                                }
                                //取整
                                // for (var i in list) {

                                //   list[i]['daily_sum_time'] = Math.round(list[i]['daily_sum_time'])
                                // }
                                return list;
                            }

                            self.weekly_dict = combineCell(responseContent)
                        }
                    );
                this.get_summary(chinese_name)
            },
            get_summary: function (chinese_name) {
                var self = this;
                self.summary = {
                    summary: '',
                    self_evaluation: '',
                    plan: '',
                };
                this.$axios.get('/analysis/analysis_week_summary/', {
                    params: {
                        natural_week: self.filters.naturalWeek,
                        employee_name: chinese_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            if (responseContent[0]) {
                                self.summary = responseContent[0]
                            }
                        }
                    );
            },

        },
        //调用
        mounted() {
            this.$nextTick(function () {
                this.getDepartment()
                // this.analysis_employee_weekly()
                // this.get_summary()
            })
        }
    }
</script>
<style scoped>
    .title {
        text-align: center;
        margin: 5px 0 40px 0;
    }

    .hidden {
        display: none;
    }

    table {
        max-width: 100%;
        background-color: transparent;
    }

    th {
        text-align: center;
    }

    td {
        text-align: center;
    }

    .table {
        width: 100%;
        margin-bottom: 20px;
    }

    .table > thead > tr > th,
    .table > tbody > tr > th,
    .table > tfoot > tr > th,
    .table > thead > tr > td,
    .table > tbody > tr > td,
    .table > tfoot > tr > td {
        padding: 8px;
        line-height: 1.428571429;

        border-top: 1px solid #dddddd;
    }

    .table > thead > tr > th {
        vertical-align: bottom;
        border-bottom: 2px solid #dddddd;
    }

    .table > caption + thead > tr:first-child > th,
    .table > colgroup + thead > tr:first-child > th,
    .table > thead:first-child > tr:first-child > th,
    .table > caption + thead > tr:first-child > td,
    .table > colgroup + thead > tr:first-child > td,
    .table > thead:first-child > tr:first-child > td {
        border-top: 0;
    }

    .table > tbody + tbody {
        border-top: 2px solid #dddddd;
    }

    .table .table {
        background-color: #ffffff;
    }

    .table-condensed > thead > tr > th,
    .table-condensed > tbody > tr > th,
    .table-condensed > tfoot > tr > th,
    .table-condensed > thead > tr > td,
    .table-condensed > tbody > tr > td,
    .table-condensed > tfoot > tr > td {
        padding: 5px;
    }

    .table-bordered {
        border: 1px solid #dddddd;
    }

    .table-bordered > thead > tr > th,
    .table-bordered > tbody > tr > th,
    .table-bordered > tfoot > tr > th,
    .table-bordered > thead > tr > td,
    .table-bordered > tbody > tr > td,
    .table-bordered > tfoot > tr > td {
        border: 1px solid #dddddd;
    }

    .table-bordered > thead > tr > th,
    .table-bordered > thead > tr > td {
        border-bottom-width: 2px;
    }

    .table-striped > tbody > tr:nth-child(odd) > td,
    .table-striped > tbody > tr:nth-child(odd) > th {
        background-color: #f9f9f9;
    }

    .table-hover > tbody > tr:hover > td,
    .table-hover > tbody > tr:hover > th {
        background-color: #f5f5f5;
    }
</style>

