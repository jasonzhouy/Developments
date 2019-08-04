<template>
  <section>
    <!--列表-->
    <el-table
      :data="reports"
      highlight-current-row
      v-loading="listLoading"
      @selection-change="selsChange"
      style="width: 100%;"
    >
      <el-table-column type="index" v-if="false"></el-table-column>
      <el-table-column prop="id" label="id" min-width="100"></el-table-column>
      <el-table-column prop="videoId" label="视频ID" min-width="100"></el-table-column>
      <el-table-column prop="reportVideoCoverPath" label="视频" min-width="100">
        <template slot-scope="scope">
          <img :src="baseUrl + scope.row.reportVideoCoverPath" min-width="100" height="100">
        </template>
      </el-table-column>
      <el-table-column prop="reportFrom" label="举报人" min-width="100"></el-table-column>
      <el-table-column prop="reportTitle" label="分类" min-width="100"></el-table-column>
      <el-table-column prop="reportReason" label="原因" min-width="100"></el-table-column>
      <el-table-column prop="reportTo" label="被举报人" min-width="100"></el-table-column>
      <el-table-column
        prop="reportTime"
        label="举报时间"
        :formatter="formatTime"
        min-width="180"
        sortable
      ></el-table-column>
      <el-table-column label="操作" width="200">
        <template scope="scope">
          <el-button type="success" size="small" @click="visit(scope.row,scope.$index)">查看</el-button>
          <el-button type="danger" size="small" @click="handleOffline(scope.row,scope.$index)">下线</el-button>
          <el-button type="warning" size="small" @click="handleCancel(scope.row,scope.$index)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination
        layout="prev, pager, next"
        style="float:right;"
        @current-change="handleCurrentChange"
        :page-size="5"
        :total="total*5"
      ></el-pagination>
    </el-col>
  </section>
</template>

<script>
import util from "../../common/js/util";
//import NProgress from 'nprogress'
import { getReportsList,offlineReport,cancelReport } from "../../api/api";
export default {
  data() {
    return {
      reports: [],
      baseUrl: "https://www.zymonster.com/v",
      total: 50,
      page: 1,
      listLoading: false,
      sels: [] //列表选中列
    };
  },
  methods: {
    formatTime: function(row, column) {
      return util.formatDate.format(
        new Date(row.reportTime),
        "yyyy-MM-dd hh:mm"
      );
    },

    visit: function(row, column) {
      // location.href = this.baseUrl + param;
      console.log(row.reportVideoPath);
      window.open(this.baseUrl + row.reportVideoPath);
    },

    handleOffline: function(row, column) {
      // location.href = this.baseUrl + param;
      this.$confirm("确认下线吗？", "提示", {
        type: "warning"
      })
        .then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = {
            username: row.reportTo,
            videoId: row.videoId,
            id: row.id
          };
		  console.log(para);
		  offlineReport(para).then(res => {
			  this.listLoading = false;
            //NProgress.done();
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.getReports();

		  })
          
        })
        .catch(() => {});
    },

    handleCancel: function(row, column) {
      // location.href = this.baseUrl + param;
      this.$confirm("确认取消投诉吗？", "提示", {
        type: "warning"
      })
        .then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = {
             username: row.reportTo,
            videoId: row.videoId,
            id: row.id
          };
		  console.log(para);
		  cancelReport(para).then(res => {
			  this.listLoading = false;
            //NProgress.done();
            this.$message({
              message: "取消成功",
              type: "success"
            });
            this.getReports();

		  })
          
        })
        .catch(() => {});
    },

    //获取用户列表
    getReports() {
      let para = {
        page: this.page
      };
      this.listLoading = true;
      //NProgress.start();
      console.log("1");
      getReportsList(para).then(res => {
        console.log(res.data.data.rows.reportTime);
        this.total = res.data.data.total;
        this.reports = res.data.data.rows;
        this.listLoading = false;
        //NProgress.done();
      });
    },

    selsChange: function(sels) {
      this.sels = sels;
    },

    handleCurrentChange(val) {
      this.page = val;
      this.getReports();
    }
  },

  mounted() {
    this.getReports();
  }
};
</script>

<style scoped>
</style>