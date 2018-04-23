<template>
    <div>
        <!--<h1 class="title" style="align:center;">周报分析</h1>-->

        <div>
            <el-form :inline="true" :model="filters">
                <el-col :span="24">
                    <span class="demonstration">按周数:</span>
                    <el-date-picker v-model="filters.natural_week" type="week" format="yyyy-WW 周" @change="weekChange"
                                    placeholder="选择周" :picker-options="weekOption">
                    </el-date-picker>
                    <el-form-item>
                        <span class="demonstration">时间：</span>
                        <el-date-picker v-model="filters.filter_date" type="daterange" align="right"
                                        placeholder="选择日期范围" value-format="yyyy-MM-dd"  @change="filterDateChange"
                                        :picker-options="pickerOptions2">
                        </el-date-picker>
                    </el-form-item>
                    项目类型：
                    <el-select v-model="filters.project_type_id" placeholder="项目类型：" filterable clearable>
                        <el-option v-for="item in project_type_list" :key="item.id" :label="item.project_type_name"
                                   :value="item.id">
                        </el-option>
                    </el-select>
                    <el-button type="primary" @click="filter">分析</el-button>
                    <el-button type="primary" @click="export_project_timeTaken">导出Excel</el-button>
                    <el-button type="primary" @click="export_finance_project_timeTaken">导出财务项目分析表</el-button>


                </el-col>
            </el-form>
        </div>
        <br>
        </br>

        <el-row :gutter="24">
            <el-col :span="8">
            </el-col>
            <el-col :span="24">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">项目总耗时</span>
                    </div>
                    <el-col :span="24">
                        <div id="projectTiemTaken" style="width: 100%;height: 400px"></div>
                    </el-col>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="24">
            <el-col :span="10">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">岗位耗时</span>
                    </div>
                    <el-col :span="24">
                        <div id="position" style="width: 100%;height: 400px"></div>
                    </el-col>
                </el-card>
            </el-col>

            <el-col :span="14">

                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">人员耗时</span>
                    </div>
                    <el-col :span="24">
                        <div id="project" style="width: 100%;height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="24">
            <el-col :span="24">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span style="line-height: 10px;">人员耗时</span>
                    </div>
                    <el-col :span="24">
                        <div id="ProjectEveryday" style="width: 100%;height: 400px;"></div>
                    </el-col>
                </el-card>
            </el-col>
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
                filters: {
                    project_name: '',
                    employee_name: '',
                    project_id: '',
                    filter_date: '',
                    natural_week: '',
                    project_type_id: '',
                    // end_date: '',
                },
                project_type_list: '',
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
                echartsProjectTimeTaken: {
                    charts: '',
                    x_data: [],
                    y_data: []
                },
                echartsProjectEveryday: {
                    charts: '',
                    date_list: [],
                    time_count_list: []
                },
                echartsLoad: {
                    charts: '',
                    x_data: [],
                    y_data: []
                },
                echartsPosition: {
                    charts: '',
                    x_data: [],
                    y_data: [],
                    y_change_data: [],
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
            get_project_type: function (params) {
                var self = this;
                this.$axios.get('/works/get_project_type/')
                    .then(function (response) {
                            self.project_type_list = eval(response.data.content);
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
            drawDepartPie(id) {
                this.DepartmentCharts.setOption({
                    title: {
                        text: '部门耗时类型分布',
                        subtext: '',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.echartsDepart.opinion
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
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
            },
            drawPersonPie(id, employee_name) {
                var self = this;
                this.PersonCharts.setOption({
                    title: {
                        text: '"' + employee_name + '"' + '--耗时类型分布',
                        subtext: '',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.echartsPerson.opinion
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
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
            drawProjectTimeTaken(id) {
                var self = this;

                this.ProjectTimeTakenCharts = echarts.init(document.getElementById('projectTiemTaken'))

                this.ProjectTimeTakenCharts.setOption({
                    title: {
                        text: '项目耗时分布'
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
                        bottom: '10%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: self.echartsProjectTimeTaken.x_data,
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
                            barWidth: '60%',
                            data: self.echartsProjectTimeTaken.y_data
                        }
                    ]
                })
                this.ProjectTimeTakenCharts.on('click', function (params) {
                    const project_name = params.name;
                    self.analysis_project_employee(params, project_name);
                    self.analysis_position(params, project_name);
                    self.analysis_project_everyday(params, project_name);
                });
            },

            drawProject(id, project_name) {
                var self = this;
                this.ProjectCharts = echarts.init(document.getElementById('project'))

                this.ProjectCharts.setOption({
                    title: {
                        text: '"' + project_name + '"' + '项目--具体人员耗时'
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
                            barWidth: '60%',
                            data: this.echartsProject.y_data
                        }
                    ]
                })
            },
            drawPorjectEveryday(id, project_name) {
                var self = this;
                this.ProjectEverydayCharts = echarts.init(document.getElementById('ProjectEveryday'))

                var option = {
                    title: {
                        text: project_name + '项目时间堆叠区域图'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#6a7985'
                            }
                        }
                    },
                    legend: {
                        data: []
                    },
                    // toolbox: {
                    //   feature: {
                    //     saveAsImage: {}
                    //   }
                    // },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: self.echartsProjectEveryday.date_list
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: []
                }
                var time_count_list = self.echartsProjectEveryday.time_count_list;
                var legend = [];
                for (var value in time_count_list) {
                    legend.push(value)
                    for (var k in time_count_list[value]) {
                        if (time_count_list[value][k] == 0) {
                            time_count_list[value][k] = {
                                value: time_count_list[value][k],
                                label: {
                                    normal: {
                                        show: false
                                    }
                                }
                            }
                        }
                    }
                    var d = {
                        name: value,
                        type: 'line',
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'top'
                            }
                        },
                        // areaStyle: { normal: {} },
                        data: time_count_list[value]
                    }

                    option.series.push(d);
                }
                option.legend.data = legend
                this.ProjectEverydayCharts.clear();
                this.ProjectEverydayCharts.setOption(option);
            },
            drawPosition(id, employee_name) {
                var self = this;
                this.PositionCharts = echarts.init(document.getElementById('position'))

                this.PositionCharts.setOption({
                    title: {
                        text: '"' + employee_name + '"' + '--各岗位耗时分布',
                        subtext: '',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        // orient: 'vertical',
                        // left: 'left',
                        bottom: 'bottom',
                        data: this.echartsPosition.position_list
                    },
                    series: [
                        {
                            name: '时间占比（H） ',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '50%'],
                            data: this.echartsPosition.count,
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
            filterDateChange(val) {
                this.filters.filterDate = JSON.stringify(val);
                console.log(this.filters.filterDate)
            },
            filter: function (params) {
                this.analysis_project_timeTaken();
                //      this.analysis_project_everyday();
            },
            export_project_timeTaken: function (params, employee_name) {
                var self = this;
                if (self.filters.filterDate || self.filters.naturalWeek) {
                    window.location.href = '/analysis/export_project_timeTaken/?filter_date=' + self.filters.filterDate + '&&natural_week=' + self.filters.naturalWeek + self.filters.filterDate + '&&project_type_id=' + self.filters.project_type_id
                } else {
                    alert("没有选择时间段")
                }
            },
            export_finance_project_timeTaken: function (params, employee_name) {
                var self = this;
                if (self.filters.filterDate || self.filters.naturalWeek) {
                    window.location.href = '/analysis/export_finance_project_timeTaken/?filter_date=' + self.filters.filterDate + '&&natural_week=' + self.filters.naturalWeek + '&&project_type_id=' + self.filters.project_type_id
                } else {
                    alert("没有选择时间段")
                }
            },
            analysis_position: function (params, project_name) {
                var self = this;
                this.$axios.get('/analysis/analysis_position/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        project_name: project_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsPosition.position_list = responseContent.position_list
                            self.echartsPosition.count = responseContent.count
                            // self.echartsPosition.y_change_data = responseContent.y_change_data
                            self.drawPosition('position', project_name);
                        }
                    );
            },
            analysis_employee: function (params) {
                var self = this;
                this.$axios.get('/analysis/analysis_employee/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        employee_name: self.filters.employee_name,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsPerson.opinionData = responseContent.type_count
                            self.echartsPerson.opinion = responseContent.type_list
                            self.drawPersonPie('personal');
                        }
                    );
            },
            analysis_project_everyday: function (params, project_name) {
                var self = this;
                this.$axios.get('/analysis/analysis_project_everyday/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        project_name: project_name,
                        natural_week: self.filters.naturalWeek,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsProjectEveryday.date_list = responseContent.date_list
                            self.echartsProjectEveryday.time_count_list = responseContent.time_count_list
                            self.drawPorjectEveryday('ProjectEveryday', project_name);
                        }
                    );
            },
            analysis_project_timeTaken: function (params) {
                var self = this;
                this.$axios.get('/analysis/analysis_project_timeTaken/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        natural_week: self.filters.naturalWeek,
                        project_type_id: self.filters.project_type_id,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsProjectTimeTaken.x_data = responseContent.x_data
                            self.echartsProjectTimeTaken.y_data = responseContent.y_data
                            self.drawProjectTimeTaken('projectTimeTaken')
                        }
                    );
            },
            analysis_project_employee: function (params, project_name) {
                /*
                分析每个项目中具体人员的耗时
                */
                var self = this;
                self.$axios.get('/analysis/analysis_project_employee/', {
                    params: {
                        filter_date: self.filters.filterDate,
                        project_name: project_name,
                        natural_week: self.filters.naturalWeek,
                    }
                })
                    .then(function (response) {
                            var responseContent = JSON.parse(response.data.content);
                            self.echartsProject.x_data = responseContent.x_data
                            self.echartsProject.y_data = responseContent.y_data
                            self.drawProject('project', project_name);
                        }
                    );
            },
        },
        //调用
        mounted() {
            // this.PersonCharts = echarts.init(document.getElementById('personal'))

            // this.PositionCharts = echarts.init(document.getElementById('position'))
            // this.DepartmentCharts = echarts.init(document.getElementById('department'))
            this.$nextTick(function () {
                // this.filter();
                this.get_users();
                this.get_projects();
                this.get_project_type();
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
