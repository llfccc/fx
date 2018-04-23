<template>
    <div>
        <!--<h1 class="title" style="align:center;">周报分析</h1>-->

        <div>
            <!--<button @click="analysis_type_project">test</button>-->
            <el-row :gutter="24">
                <el-form :inline="true" :model="filters">

                    <span class="demonstration">按周数:</span>
                    <el-date-picker v-model="filters.natural_week" type="week" format="yyyy-WW 周" @change="weekChange"
                                    placeholder="选择周" :picker-options="weekOption">
                    </el-date-picker>
                    <el-form-item>
                        <span class="demonstration">按日期：</span>
                        <el-date-picker v-model="filters.filter_date" value-format="yyyy-MM-dd" type="daterange" align="right"
                                        placeholder="选择日期范围" @change='filterDateChange'
                                        :picker-options="pickerOptions2">
                        </el-date-picker>
                    </el-form-item>                    部门：
                    <el-select v-model="filters.analysis_department_id" placeholder="部门" filterable clearable>
                        <el-option v-for="item in department_list" :key="item.id" :label="item.department_name"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                    <el-button type="primary" @click="filter">分析</el-button>
                    <el-button type="primary" @click="export_employee_devtype">导出Excel</el-button>

                </el-form>
            </el-row>

        </div>
        </br>
        <el-row :gutter="24">
            <el-col :span="14">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">部门情况</span>
                        <!--<el-button style="float: right;" type="primary">操作按钮</el-button>-->
                    </div>
                    <el-col :span="24">
                        <div id="load" style="width: 100%;height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
            <el-col :span="10">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">部门情况</span>
                    </div>
                    <el-col :span="24">
                        <div id="department" style="height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>

        </el-row>

        <el-row :gutter="24">
            <el-col :span="14">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">个人情况</span>
                        <!--<el-button style="float: right;" type="primary">操作按钮</el-button>-->
                    </div>
                    <el-col :span="24">
                        <div id="everyday" style="height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
            <el-col :span="10" v-if="!type_project_visable">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">个人情况</span>
                    </div>
                    <el-col :span="24">
                        <div id="personal" style="height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
            <el-col :span="10" v-if="type_project_visable">

                <el-card class="box-card">
                    <div slot="header" class="clearfix">
            <span style="line-height
                  : 10px;">项目情况</span>
                    </div>
                    <el-col :span="24">
                        <div id="type_project" style="height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <!-- <el-col :span="16">
                    <el-card class="box-card">
                      <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">暂无内容</span>
                      </div>
                      <el-col :span="24">
                      </el-col>
                    </el-card>

                  </el-col> -->
            <!-- <el-col :span="8">
                    <el-card class="box-card">
                      <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">项目情况</span>
                      </div>
                      <el-col :span="24">
                        <div id="type_project" style="height: 350px;" > </div>
                      </el-col>
                    </el-card>
                  </el-col> -->
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

                type_project_visable: false,
                filters: {
                    project_name: '',
                    employee_name: '',
                    project_id: '',
                    filter_date: '',
                    natural_week: '',
                    analysis_department_id: '',
                },
                department_list: [],
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
                echartsEmployeeEveryday: {
                    charts: '',
                    date_list: [],
                    time_count_list: []
                },
                echartsPerson: {
                    charts: '',
                    opinion: [],
                    opinionData: []
                },
                echartsDepart: {
                    charts: '',
                    opinion: [],
                    opinionData: []
                },
                echartsProject: {
                    charts: '',
                    x_data: [],
                    y_data: []
                },
                echartsLoad: {
                    charts: '',
                    name_data: [],
                    avg_time_data: [],
                    sum_time_data: [],
                },
                echartsPosition: {
                    charts: '',
                    x_data: [],
                    y_data: [],
                    y_change_data: [],
                },
                echartsTypeProject: {
                    charts: '',
                    project_list: [],
                    count: [],
                },
            }
        },
        computed: {
            filter_user_list() {
                var self = this;
                let department_id = localStorage.getItem('department_id');
                let ms_username = localStorage.getItem('ms_username');

                var filter_user_list = new Array();
                for (var i in self.user_list) {
                    if (self.user_list[i].department == department_id & self.user_list[i].chinese_name != ms_username) {
                        filter_user_list.push(self.user_list[i])
                    }
                }
                return filter_user_list
            }
        },
        methods: {
            get_users: function (params) {
                var self = this;
                this.$axios.get('/accounts/get_username/')
                    .then(function (response) {
                            self.user_list = eval(response.data.content);
                        }
                    );
            },
            get_projects: function (params) {
                var self = this;
                this.$axios.get('/works/get_projects/')
                    .then(function (response) {
                            self.project_list = eval(response.data.content);

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
                    self.department_list = eval(res.data.content);
                    console.log(self.department_list)
                });
            },
            drawDepartPie(id) {
                var self = this;
                this.DepartmentCharts = echarts.init(document.getElementById('department'))

                this.DepartmentCharts.setOption({
                    title: {
                        text: '部门耗时类型分布',
                        subtext: '',
                        x: 'left'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        // orient: 'vertical',
                        bottom: 'bottom',
                        data: this.echartsDepart.opinion
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '45%'],
                            data: this.echartsDepart.opinionData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                })
                this.DepartmentCharts.on('click', function (params) {
                    const type_name = params.name;
                    self.type_project_visable = true;
                    self.analysis_type_project(params, type_name)
                });
            },
            drawPersonPie(id, employee_name) {
                var self = this;
                this.PersonCharts = echarts.init(document.getElementById('personal'))

                this.PersonCharts.setOption({
                    title: {
                        text: employee_name + '--耗时类型分布',
                        subtext: '',
                        // textStyle: {
                        //   fontSize: 14
                        // },
                        x: 'left'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        // orient: 'vertical',
                        // left: 'right',
                        // top: 35,
                        bottom: 'bottom',
                        data: this.echartsPerson.opinion
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '50%'],
                            data: this.echartsPerson.opinionData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                })
            },
            drawTypeProject(id, type_name) {
                var self = this;
                this.TypeProjectCharts = echarts.init(document.getElementById('type_project'))

                this.TypeProjectCharts.setOption({
                    title: {
                        text: type_name + '类型--项目耗时分布',
                        subtext: '',
                        // textStyle: {
                        //   fontSize: 14
                        // },
                        x: 'left'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        // orient: 'vertical',
                        // left: 'right',
                        bottom: 'bottom',
                        // top: 35,
                        data: this.echartsTypeProject.project_list
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '50%'],
                            data: this.echartsTypeProject.count,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                })

            },
            drawProject(id) {
                var self = this;
                this.ProjectCharts.setOption({
                    title: {
                        text: '"' + self.filters.project_name + '"' + '项目--耗时分布'
                    },
                    color: ['#3398DB'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: this.echartsProject.x_data,
                            barWidth: 80,
                            axisTick: {
                                alignWithLabel: true
                            },
                            axisLabel: {
                                'interval': 0,
                                rotate: -20
                            },
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: '项目耗时',
                            type: 'bar',
                            barWidth: 100,
                            data: this.echartsProject.y_data
                        }
                    ]
                })
            },
            drawLoad(id) {
                var self = this;
                this.LoadCharts = echarts.init(document.getElementById('load'))
                this.LoadCharts.setOption({
                    title: {
                        text: '部门人员负荷图'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            crossStyle: {
                                color: '#999'
                            }
                        }
                    },
                    // toolbox: {
                    //   feature: {
                    //     dataView: { show: true, readOnly: false },
                    //     magicType: { show: true, type: ['line', 'bar'] },
                    //     restore: { show: true },
                    //     saveAsImage: { show: true }
                    //   }
                    // },
                    legend: {
                        data: ['总工作时间', '平均工作时间'],
                        // right : 'right'
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: this.echartsLoad.name_data,
                            axisTick: {
                                alignWithLabel: true
                            }, axisLabel: {
                                'interval': 0,
                                rotate: -20
                            },
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '总工作时间（H）',
                            // min: 0,
                            // max: 250,
                            // interval: 50,
                            axisLabel: {
                                formatter: '{value} '
                            },

                        },
                        {
                            type: 'value',
                            name: '平均工作时间（H）',
                            min: 0,
                            max: 25,
                            interval: 5,
                            axisLabel: {
                                formatter: '{value} '
                            }
                        }
                    ],
                    series: [
                        {
                            name: '总工作时间',
                            type: 'bar',
                            barWidth: '70%',
                            data: this.echartsLoad.sum_time_data,
                            itemStyle: {
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'inside'
                                    }
                                },
                            },
                        },
                        {
                            name: '平均工作时间',
                            type: 'line',
                            barWidth: '70%',
                            data: this.echartsLoad.avg_time_data
                        }
                    ]
                })
                this.LoadCharts.on('click', function (params) {
                    const employee_name = params.name;
                    self.type_project_visable = false;
                    self.analysis_employee_devtype(params, employee_name)
                    self.analysis_employee_everyday(params, employee_name)
                });
            },
            drawEveryday(id, employee_name) {
                var self = this;
                this.EverydayCharts = echarts.init(document.getElementById('everyday'))

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
                        data: self.echartsEmployeeEveryday.date_list,
                        axisLabel: {
                            'interval': 0,
                            rotate: -20
                        },
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: []
                }
                var time_count_list = self.echartsEmployeeEveryday.time_count_list;
                var legend = [];
                for (var value in time_count_list) {
                    legend.push(value)
                    var d = {
                        name: value,
                        type: 'bar',
                        stack: '总量',
                        barWidth: 80,
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data: time_count_list[value]
                    }
                    option.series.push(d);
                }
                option.legend.data = legend
                this.EverydayCharts.clear();
                this.EverydayCharts.setOption(option);
                // this.EverydayCharts.on('click', function (params) {
                //   // const employee_name = params.name;
                //   // self.analysis_employee_devtype(params, employee_name)
                //   // self.analysis_employee_everyday(params, employee_name)
                // });
            },
              filterDateChange(val) {
                this.filters.filterDate = JSON.stringify(val);
            },
            weekChange(val) {
                var self = this;

                if (val) {
                    self.filters.naturalWeek = val.getFullYear() + '-' + val.getDateWeek() + '周'
                }
                if (self.filters.naturalWeek) {
                    self.filters.filter_date = ''
                    self.filters.filterDate = ''
                }
            },
            filter: function (params) {
                this.analysis_department_type();
                // this.analysis_project();
                this.analysis_load();
            },
            analysis_department_type: function (params) {
                /*
                分析整个部门的工作类型分布
                */
                var self = this;
                this.$axios.get('/analysis/analysis_department_type/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        analysis_department_id: self.filters.analysis_department_id,

                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsDepart.opinionData = responseContent.type_count
                            self.echartsDepart.opinion = responseContent.type_list
                            self.drawDepartPie('department');
                        }
                    );
            },
            analysis_type_project: function (params, type_name) {
                /*
                分析整个部门的工作类型分布
                */
                var self = this;
                this.$axios.get('/analysis/analysis_type_project/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        type_name: type_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsTypeProject.project_list = responseContent.project_list
                            self.echartsTypeProject.count = responseContent.count
                            self.drawTypeProject('type_project', type_name)
                        }
                    );
            },

            analysis_employee_everyday: function (params, employee_name) {
                var self = this;
                self.echartsEmployeeEveryday.date_list = []
                self.echartsEmployeeEveryday.time_count_list = []
                self.$axios.get('/analysis/analysis_employee_everyday/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        employee_name: employee_name,
                        natural_week: self.filters.naturalWeek,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsEmployeeEveryday.date_list = responseContent.date_list
                            self.echartsEmployeeEveryday.time_count_list = responseContent.time_count_list
                            self.drawEveryday('everyday', employee_name);
                        }
                    );
            },
            export_employee_devtype: function (params, employee_name) {
                var self = this;
                if (self.filters.filterDate || self.filters.naturalWeek) {

                    window.location.href = '/analysis/export_employee_devtype/?filter_date=' + self.filters.filterDate + '&&natural_week=' + self.filters.naturalWeek
                } else {
                    alert("没有选择时间段")
                }
            },
            analysis_employee_devtype: function (params, employee_name) {
                var self = this;
                self.$axios.get('/analysis/analysis_employee_devtype/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        employee_name: employee_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsPerson.opinionData = responseContent.type_count
                            self.echartsPerson.opinion = responseContent.type_list
                            self.drawPersonPie('personal', employee_name);
                        }
                    );
            },
            analysis_load: function (params) {
                var self = this;
                this.$axios.get('/analysis/analysis_load/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        analysis_department_id: self.filters.analysis_department_id,

                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsLoad.name_data = responseContent.name_data
                            self.echartsLoad.avg_time_data = responseContent.avg_time_data
                            self.echartsLoad.sum_time_data = responseContent.sum_time_data
                            self.drawLoad('load');
                        }
                    );
            },
        },
        //调用
        mounted() {
            this.$nextTick(function () {
                // this.filter();
                this.getDepartment();
                this.get_users();
                this.get_projects();

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
