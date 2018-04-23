<template>
    <div>
        <!--<h1 class="title" style="align:center;">周报分析</h1>-->

        <div>
            <el-form :inline="true" :model="filters">
                <span class="demonstration">起始周数</span>
                <el-date-picker v-model="filters.start_week" type="week" format="yyyy-WW 周" @change="startWeekChange"
                                placeholder="选择周" :picker-options="weekOption">
                </el-date-picker>
                <span class="demonstration">结束周数</span>
                <el-date-picker v-model="filters.end_week" type="week" format="yyyy-WW 周" @change="endWeekChange"
                                placeholder="选择周" :picker-options="weekOption">
                </el-date-picker>
                部门：
                <el-select v-model="filters.analysis_department_id" placeholder="部门" filterable clearable>
                    <el-option v-for="item in department_list" :key="item.id" :label="item.department_name"
                               :value="item.id">
                    </el-option>
                </el-select>

                <el-button type="primary" @click="filter">分析</el-button>
            </el-form>
        </div>
        <br>
        </br>
        </br>
        </br>

        <el-row :gutter="24">
            <el-col :span="8">
            </el-col>
            <el-col :span="24">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">部门情况</span>
                    </div>
                    <el-col :span="24">
                        <div id="departmentTrend" style="width: 100%;height: 300px"></div>
                    </el-col>
                </el-card>
            </el-col>
        </el-row>
        </br>

        <el-row :gutter="24">
            <el-col :span="24">

                </br>
                <!--
                    <template v-for="item in filter_user_list">
                      <el-button type="success" @click="analysis_employee_load($event)">{{item.chinese_name}}</el-button>
                      <!--<el-tag type="danger" @click="analysis_employee_load(param)">{{item.chinese_name}}</el-tag>
                    </template>-->
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        </span>
                        <el-select v-model="employee_name" placeholder="姓名" filterable clearable>
                            <el-option v-for="item in filter_user_list" :key="item.chinese_name"
                                       :label="item.chinese_name" :value="item.chinese_name">
                            </el-option>
                        </el-select>
                        <el-button type="success" @click="analysis_employee_load">个体分析</el-button>
                        <span style="line-height: 10px;"></span>
                    </div>
                    <el-col :span="24">
                        <div id="employee_trend" style="width: 100%;height: 350px;"></div>
                    </el-col>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="24">
        </el-row>
        <el-row :gutter="24">

            <el-col :span="8">
                <div id="project" style="width: 100%;height: 350px"></div>
            </el-col>
            <el-col :span="8">

            </el-col>
        </el-row>
        <el-row :gutter="24">
        </el-row>

    </div>
</template>
<script>
    import echarts from 'echarts'

    export default {
        name: '',
        data() {
            return {
                user_list: '',
                project_list: '',
                employee_name: '',
                department_list: [],
                filters: {
                    project_name: '',
                    employee_name: '',
                    project_id: '',
                    start_week: '',
                    end_week: '',
                    analysis_department_id: '',
                },
                weekOption: {
                    firstDayOfWeek: 1,
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
                    },
                        {
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
                // echartsPerson: {
                //   charts: '',
                //   opinion: [],
                //   opinionData: []
                // },
                // echartsDepart: {
                //   charts: '',
                //   opinion: [],
                //   opinionData: []
                // },
                // echartsProject: {
                //   charts: '',
                //   x_data: [],
                //   y_data: []
                // },
                echartsEmployeeTrend: {
                    charts: '',
                    date_list: [],
                    type_name_data: []
                },
                echartsDepartmentTrend: {
                    charts: '',
                    type_name_data: [],
                    sum_data: [],
                },
            }
        },
        computed: {
            filter_user_list() {
                var self = this;
                // let department_id = localStorage.getItem('department_id');
                let username = localStorage.getItem('username');
                // console.log(department_id)
                var filter_user_list = new Array();
                for (var i in self.user_list) {
                    if (self.user_list[i].chinese_name != username) {
                        filter_user_list.push(self.user_list[i])
                    }
                }
                return filter_user_list
            }
        },
        methods: {
            get_users: function (params) {
                var self = this;
                let department_id = localStorage.getItem('department_id');
                this.$axios.get('/accounts/get_username/', {
                        params: {
                            department_id: department_id,
                        }
                    }
                )
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
            drawDepartmentTrend(id) {
                var self = this;
                var option = {
                    title: {
                        text: '每周工作累积图',
                        // subtext: 'From ExcelHome',
                        // sublink: 'http://e.weibo.com/1341556070/AjQH99che'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: [],
                        bottom: 'bottom'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '10%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: self.departmentTrendCharts.date_list
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: []
                }
                var time_count_list = self.departmentTrendCharts.time_count_list;
                var legend = [];
                for (var value in time_count_list) {
                    legend.push(value)
                    var d = {
                        name: value,
                        type: 'bar',
                        stack: '总量',
                        barWidth: 80,
                        // label: {
                        //   normal: {
                        //     show: true,
                        //     position: 'insideRight'
                        //   }
                        // },

                        data: time_count_list[value]
                    }
                    option.series.push(d);
                }
                option.legend.data = legend
                this.departmentTrendCharts.clear();
                this.departmentTrendCharts.setOption(option);
                // this.EverydayCharts.on('click', function (params) {
                //   // const employee_name = params.name;
                //   // self.analysis_employee_devtype(params, employee_name)
                //   // self.analysis_employee_everyday(params, employee_name)
                // });
            },
            drawEmployeeTrend(id, employee_name) {
                var self = this;
                var option = {
                    title: {
                        text: '"' + employee_name + '"' + '--每日工作累积图',
                        // subtext: 'From ExcelHome',
                        // sublink: 'http://e.weibo.com/1341556070/AjQH99che'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: [],
                        bottom: 'bottom'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '10%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        data: self.echartsEmployeeTrend.date_list
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: []
                }
                var time_count_list = self.echartsEmployeeTrend.time_count_list;
                var legend = [];
                for (var value in time_count_list) {
                    legend.push(value)
                    var d = {
                        name: value,
                        type: 'bar',
                        stack: '总量',
                        barWidth: 80,
                        // label: {
                        //   normal: {
                        //     show: true,
                        //     position: 'insideRight'
                        //   }
                        // },
                        data: time_count_list[value]
                    }
                    option.series.push(d);
                }
                option.legend.data = legend
                this.employeeTrendCharts.clear();
                this.employeeTrendCharts.setOption(option);
                // this.ProjectTimeTakenCharts.on('click', function (params) {
                //     const project_name = params.name;
                //     self.analysis_project_employee(params,project_name);
                //     self.analysis_position(params,project_name);
                // });
            },

            startWeekChange(val) {
                var self = this;

                if (val) {
                    self.filters.startWeek = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }

            },
            endWeekChange(val) {
                var self = this;
                if (val) {
                    self.filters.endWeek = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }
            },
            filter: function (params) {
                this.analysis_department_trend();
                // this.analysis_employee_load();
            },

            analysis_department_trend: function (params) {
                var self = this;
                this.$axios.get('/analysis/analysis_load_trend/', {
                    params: {
                        start_week: self.filters.startWeek,
                        end_week: self.filters.endWeek,
                        analysis_department_id: self.filters.analysis_department_id,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);

                            self.departmentTrendCharts.date_list = responseContent.date_list
                            self.departmentTrendCharts.time_count_list = responseContent.time_count_list
                            self.drawDepartmentTrend('departmentTrend')
                        }
                    );
            },
            analysis_employee_load: function (event) {
                /*
                分析每个项目中具体人员的负载趋势
                */
                var self = this;
                // self.employee_name = event.target.innerText;
                self.$axios.get('/analysis/analysis_load_trend/', {
                    params: {
                        start_week: self.filters.startWeek,
                        end_week: self.filters.endWeek,
                        employee_name: self.employee_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsEmployeeTrend.date_list = responseContent.date_list
                            self.echartsEmployeeTrend.time_count_list = responseContent.time_count_list
                            self.drawEmployeeTrend('employee_trend', self.employee_name);
                        }
                    );
            },
        },
        //调用
        mounted() {
            this.departmentTrendCharts = echarts.init(document.getElementById('departmentTrend'))
            this.employeeTrendCharts = echarts.init(document.getElementById('employee_trend'))

            // // this.PositionCharts = echarts.init(document.getElementById('position'))
            // // this.DepartmentCharts = echarts.init(document.getElementById('department'))
            // this.PositionCharts = echarts.init(document.getElementById('position'))
            // this.ProjectCharts = echarts.init(document.getElementById('project'))

            this.$nextTick(function () {
                // this.filter();
                this.get_users();
                this.getDepartment();
            })
        }
    }
</script>
<style scoped>
    .title {
        text-align: center;
        margin: 5px 0 40px 0;
    }
</style>
