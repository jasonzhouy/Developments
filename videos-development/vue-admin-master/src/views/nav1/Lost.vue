<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-input v-model="filters.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="getUsers">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="users" highlight-current-row v-loading="listLoading" style="width: 100%;">
      <el-table-column type="index" v-if="false"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="faceImage" label="头像" width="120" align="center">
        <template slot-scope="scope">
          <img :src="baseUrl + scope.row.faceImage" min-width="70" height="70">
        </template>
      </el-table-column>
      <el-table-column prop="fansCounts" label="粉丝数" min-width="100" sortable></el-table-column>
      <el-table-column prop="followCounts" label="关注数" min-width="100" sortable></el-table-column>
      <el-table-column prop="receiveLikeCounts" label="被赞数" min-width="100" sortable></el-table-column>
      <el-table-column prop="creditPoints" label="信用分" min-width="100" sortable></el-table-column>
      <el-table-column prop="endTime" label="账号状态" :formatter="formatStatus" min-width="120"></el-table-column>
      <el-table-column prop="reason" label="封禁原因" min-width="180" ></el-table-column>
      <el-table-column prop="dealUser" label="处理人" min-width="100" ></el-table-column>
      <el-table-column prop="endTime" label="剩余时长" :formatter="formatTime" min-width="120" sortable></el-table-column>

      <el-table-column label="操作" width="150">
        <template scope="scope">
          <el-button type="danger" size="small" @click="handleBan(scope.$index, scope.row)">提前解禁</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-col :span="24" class="toolbar">
      <el-pagination
        layout="prev, pager, next"
        style="float:right;"
        @current-change="handleCurrentChange"
        :page-size="10"
        :total="total*10"
      ></el-pagination>
    </el-col>

    
  </section>
</template>

<script>
import util from "../../common/js/util";
//import NProgress from 'nprogress'
import { getUserListPage, getBanUserListPage, cancelBan } from "../../api/api";

export default {
  data() {
    return {
      filters: {
        name: ""
      },
      users: [],
      baseUrl: "https://www.zymonster.com/v",
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], //列表选中列

    };
  },
  methods: {
    formatStatus: function(row, column) {
      return row.endTime == undefined
        ? "正常"
        : row.endTime == ""
        ? "正常"
        : "禁止";
    },

    formatTime: function(row, column) {
      return parseInt((new Date(row.endTime) - Date.now()) / 3600000) +'小时';
    },


    //获取用户列表
    getUsers() {
      let para = {
        page: this.page,
        name: this.filters.name
      };
      this.listLoading = true;
      //NProgress.start();
      getBanUserListPage(para).then(res => {
        
        this.total = res.data.data.total;
        this.users = res.data.data.rows;
        this.listLoading = false;
        console.log(this.users);
        //NProgress.done();
      });
    },

   

    

   

    handleBan: function(index, row) {
      this.$confirm("确认提前解禁该用户吗?", "提示", {
        type: "warning"
      })
        .then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = { id: row.id};
          cancelBan(para).then(res => {
            this.listLoading = false;
            //NProgress.done();
            this.$message({
              message: "解禁成功",
              type: "success"
            });
            this.getUsers();
          });
        })
        .catch(() => {});
    },

    handleCurrentChange(val) {
      this.page = val;
      this.getUsers();
    }
  },

  mounted() {
    this.getUsers();
  }
};
</script>

<style scoped>
</style>