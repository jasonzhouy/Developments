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
          <el-button type="success" @click="handleAdd">新增</el-button>
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
      <el-table-column prop="mailAddress" label="邮箱地址" min-width="180"></el-table-column>

      <el-table-column label="操作" width="150">
        <template scope="scope">
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
          <el-button
            type="warning"
            size="small"
            @click="handleBan(scope.$index, scope.row)"
            v-if="!scope.row.endTime"
          >封禁</el-button>
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

    <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="mailAddress">
          <el-input v-model="addForm.mailAddress"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadUser">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="封禁" v-model="banFormVisible" :close-on-click-modal="false">
      <el-form :model="banForm" label-width="80px" ref="banForm">
        <el-form-item label="封禁到" prop="banTime">
          <el-date-picker
            v-model="banForm.valueTime"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="pickerOptions0"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="封禁时长:" prop="lastTime" v-if="banForm.valueTime != ''">
          <p>{{calculate(banForm.valueTime)}}</p>
        </el-form-item>
        <el-form-item label="封禁原因:" prop="reason" v-if="banForm.valueTime != ''">
          <el-input v-model="banForm.reason"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native="banFormVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadBan">确认</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import util from "../../common/js/util";
//import NProgress from 'nprogress'
import { getUserListPage, removeUser, addUser ,submitBan } from "../../api/api";

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

      addFormVisible: false, //新增界面是否显示
      addLoading: false,
      addFormRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        mailAddress: [
          { required: true, message: "请输入邮箱", trigger: "blur" }
        ]
      },
      //新增界面数据
      addForm: {
        username: "",
        password: "",
        mailAddress: ""
      },


      banFormVisible: false, //封禁界面是否显示
      banLoading: false,
      //封禁界面数据
      banForm: {
        valueTime: "",
        lastTime: "",
        reason:"",
        id:''
      },
      pickerOptions0: {
        disabledDate: time => {
          return time.getTime() < Date.now();
          // return   time.getTime() > Date.now()
        }
      }
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

    calculate: function(time) {
      console.log(time);

      return  parseInt((new Date(this.banForm.valueTime) - Date.now()) / 3600000) +'小时';

    },

    //获取用户列表
    getUsers() {
      let para = {
        page: this.page,
        name: this.filters.name
      };
      this.listLoading = true;
      //NProgress.start();
      getUserListPage(para).then(res => {
        console.log(res.data.data);
        this.total = res.data.data.total;
        this.users = res.data.data.rows;
        this.listLoading = false;
        //NProgress.done();
      });
    },

    handleAdd: function() {
      this.addFormVisible = true;
    },

    handleBan: function(index, row) {
      this.banFormVisible = true;
      this.banForm.id = row.id;
    },

    uploadUser: function() {
      this.addFormVisible = false;
      let para = {
        username: this.addForm.username,
        password: this.addForm.password,
        mailAddress: this.addForm.mailAddress
      };
      this.listLoading = true;

      addUser(para).then(res => {
        this.listLoading = false;

        this.$message({
          message: "增加成功",
          type: "success"
        });
        this.getUsers();
      });
    },

    uploadBan: function() {
      this.banFormVisible = false;
      let para = {
        id: this.banForm.id,
        endTime: util.formatDate.format(this.banForm.valueTime,"yyyy-MM-dd hh:mm:ss"),
        dealTime: util.formatDate.format(new Date(),"yyyy-MM-dd hh:mm:ss"),
        reason: this.banForm.reason
      };
      this.listLoading = true;
      console.log(para);
      submitBan(para).then(res => {
        this.listLoading = false;
        this.$message({
          message: "封禁成功",
          type: "success"
        });
        this.getUsers();
      });
      
    },

    handleDel: function(index, row) {
      this.$confirm("确认删除该记录吗?", "提示", {
        type: "warning"
      })
        .then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = { username: row.username };
          removeUser(para).then(res => {
            this.listLoading = false;
            //NProgress.done();
            this.$message({
              message: "删除成功",
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