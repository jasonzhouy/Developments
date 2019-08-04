<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true" :model="filters">
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增背景音乐</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="bgms" highlight-current-row v-loading="listLoading" style="width: 100%;">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="index" width="60"></el-table-column>
      <el-table-column prop="name" label="音乐名" width="120" sortable></el-table-column>
      <el-table-column prop="author" label="歌手" width="100" sortable></el-table-column>
      <el-table-column prop="path" label="内容" width="100" sortable>
        <template slot-scope="scope">
          <a :href="baseUrl + scope.row.path">点我播放</a>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template scope="scope">
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <el-col :span="24" class="toolbar">
			<el-pagination layout="prev, pager, next" style="float:right;" @current-change="handleCurrentChange" :page-size= "10" :total= "total*10" >
			</el-pagination>
		</el-col>

    <!--新增界面-->
    <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
        <el-form-item label="歌曲名" prop="name">
          <el-input v-model="addForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="addForm.author"></el-input>
        </el-form-item>
        <el-form-item label="文件">
          <el-upload
            ref="upload"
            action="/api/bgm/addBgm"
            :with-credentials="true"
            accept=".mp3"
            :file-list="fileList"
            :auto-upload="false"
            :before-upload="beforeUploadFile"
            :on-change="fileChange"
            :on-exceed="exceedFile"
            :on-success="handleSuccess"
            :on-error="handleError"
          >
            <el-button size="small" type="primary" plain>选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="uploadFile">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import util from "../../common/js/util";
//import NProgress from 'nprogress'
import { getBgmListPage, removeBgm, addBgm } from "../../api/api";

export default {
  data() {
    return {
      filters: {
        name: ""
      },
      bgms: [],
      baseUrl: "https://www.zymonster.com/mvc",
      total: 0,
      page: 1,
      listLoading: false,
      sels: [], //列表选中列



      limitNum:1,
      addFormVisible: false, //新增界面是否显示
      addLoading: false,
      addFormRules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }]
      },

      //新增界面数据
      addForm: {
        name: "",
        file: ''
      },
      fileList: []
    };
  },
  methods: {
    exceedFile(files, fileList) {
      this.$notify.warning({
        title: "警告",
        message: `只能选择 ${
          this.limitNum
        } 个文件，当前共选择了 ${files.length + fileList.length} 个`
      });
    },
    // 文件状态改变时的钩子
    fileChange(file, fileList) {
      console.log("change");
       this.addForm.file = file.raw
      console.log(this.addForm.file);
      console.log(fileList);
    },
    // 上传文件之前的钩子, 参数为上传的文件,若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFile(file) {
      console.log("before upload");
      console.log(file);
      this.addForm.file = file;
      let extension = file.name.substring(file.name.lastIndexOf(".") + 1);
      let size = file.size / 1024 / 1024;
      if (extension !== "mp3") {
        this.$notify.warning({
          title: "警告",
          message: `只能上传mp3的文件`
        });
      }
      if (size > 20) {
        this.$notify.warning({
          title: "警告",
          message: `文件大小不得超过20M`
        });
      }
    },
    // 文件上传成功时的钩子
    handleSuccess(res, file, fileList) {
      this.$notify.success({
        title: "成功",
        message: `文件上传成功`
      });
    },
    // 文件上传失败时的钩子
    handleError(err, file, fileList) {
      this.$notify.error({
        title: "错误",
        message: `文件上传失败`
      });
    },
    uploadFile() {
      this.addFormVisible = true;
      this.$refs.upload.submit();
      let para = new FormData();

      para.append('name',this.addForm.name);
      para.append('author',this.addForm.author);
      para.append('file',this.addForm.file);

      

      addBgm(para).then(res => {
              this.addLoading = false;
              //NProgress.done();
              this.$message({
                message: "提交成功",
                type: "success"
              });
      })
        

      /*
      let formData = new FormData()
      formData.append('file', this.form.file)
      axios.post('https://jsonplaceholder.typicode.com/posts/', 
        formData,
        { "Content-Type": "multipart/form-data" }
      )
      .then(res => {
        console.log('res')
        console.log(res)
      })
      .catch(err => {

      })
      */
    },

    handleCurrentChange(val) {
      this.page = val;
      this.getUsers();
    },
    //获取用户列表
    getUsers() {
      let para = {
        page: this.page
      };
      this.listLoading = true;
      //NProgress.start();
      getBgmListPage(para).then(res => {
        this.total = res.data.data.total;
        this.bgms = res.data.data.rows;
        this.listLoading = false;
        //NProgress.done();
      });
    },
    //删除
    handleDel: function(index, row) {
      this.$confirm("确认删除该记录吗?", "提示", {
        type: "warning"
      })
        .then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = { id: row.id };
          removeBgm(para).then(res => {
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

    //显示新增界面
    handleAdd: function() {
      this.addFormVisible = true;
      this.addForm = {
        name: "",
        file: ''
      };
    },
    


    selsChange: function(sels) {
      this.sels = sels;
    },
  
  },
  mounted() {
    this.getUsers();
  }
};
</script>

<style scoped>
</style>