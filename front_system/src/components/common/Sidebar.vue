<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" theme="dark" unique-opened router background-color="#545c64"  text-color="#fff"
      active-text-color="#ffd04b">
            <el-menu-item index="readme">
                <i></i>说明
            </el-menu-item>

            <template v-for="(item,index)  in module_list">
                <!--//定义本子系统id=1-->
                    <el-submenu :index="index+''">
                    <template slot="title">
                        <i class="el-icon-menu"></i>{{item.module_name}}</template>
                    <template v-for="(node,node_index)  in item.children">
                        <el-menu-item :index="node.module_url">{{node.module_name}}</el-menu-item>

                    </template>
                </el-submenu>

            </template>

            <!--
                <el-submenu  v-for="permission.summary_perm">
                    <template slot="title">
                        <i class="el-icon-menu"></i>周报管理</template>
                    <el-menu-item   index=" " v-show="permission.tech_perm">事件管理</el-menu-item>
                    <el-menu-item  index="insertSaleEvent" v-show="permission.sale_perm">拜访管理</el-menu-item>
                    <el-menu-item index="insertWeekSummary" v-show="permission.summary_perm">周总结管理</el-menu-item>
                    <el-menu-item index="viewWeekly" v-show="permission.tech_perm">查看周报</el-menu-item>
                    <el-menu-item index="viewSaleEvent" v-show="permission.sale_perm">查看拜访</el-menu-item>
                </el-submenu> -->

            <!-- <el-submenu index="3" v-show="permission.tech_mang_perm">
                    <template slot="title">
                    <i class="el-icon-star-on"></i>周报分析</template>
                    <el-menu-item index="analysisWeekly">员工周报</el-menu-item>
                    <el-menu-item index="analysisTrend">趋势分析</el-menu-item>
                    <el-menu-item index="analysisDevDepart">部门分析</el-menu-item>
                    <el-menu-item index="analysisDevProject">项目分析</el-menu-item>
                </el-submenu> -->
            <!--<el-submenu index="4">
                        <template slot="title">
                            <i class="el-icon-menu"></i>销售拜访</template>

                        <el-menu-item index="downloadExcel">导出Excel</el-menu-item>
                    </el-submenu>-->

            <!-- <el-submenu index="5" v-show="permission.sale_mang_perm">
                    <template slot="title">
                        <i class="el-icon-star-on"></i>销售分析</template>
                    <el-menu-item index="analysisSaleEvent">拜访分析</el-menu-item>
                    <el-menu-item index="analysisSalePerformance">拜访业绩</el-menu-item>
                </el-submenu> -->

            <!--<el-submenu index="5">
                        <template slot="title">
                            <i class="el-icon-star-on"></i>文件下载</template>
                        <el-menu-item index="rulesDownload">制度下载</el-menu-item>
                        <el-menu-item index="notice">通知</el-menu-item>
                        <!--<el-menu-item index="basecharts">基础图表</el-menu-item>-->
            <!--<el-menu-item index="mixcharts">混合图表</el-menu-item>-->
            <!-- </el-submenu>
                <el-submenu index="6"  v-show="permission.sale_mang_perm">
                    <template slot="title">
                        <i class="el-icon-setting"></i>基础数据</template>
                    <el-menu-item index="projectManage">数据管理</el-menu-item>
                    <el-menu-item index="saleTarget">制作中</el-menu-item>

                </el-submenu>
                                <el-menu-item index="saleCustmoer">测试websocket</el-menu-item> -->
        </el-menu>
    </div>
</template>

<script>
export default {
    data() {
        return {
              module_list: [],
        }
    },
    created() {
        this.module_list = JSON.parse(localStorage.getItem('module_list'));

    },
    computed: {
        onRoutes() {
            return this.$route.path.replace('/', '');
        }
    }
}
</script>

<style >
.sidebar {
    display: block;
    position: absolute;
    width: 200px;
    left: 0;
    top: 70px;
    bottom: 0;
    background: #2E363F;
}

.sidebar>ul {
    height: 100%;
}
</style>
