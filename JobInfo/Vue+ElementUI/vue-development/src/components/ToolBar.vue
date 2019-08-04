<template>
  <div id="app">
    <el-container style="width: 100%;height: 100%;">
      <el-aside width="150px">
        <el-menu
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          :collapse="isCollapse"
        >
          <el-menu-item @click="doChange">
            <i :class="toobar.currentClass"></i>
            <span slot="title">{{toobar.currentValue}}</span>
          </el-menu-item>
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span slot="title">浏览</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="1-1">工作</el-menu-item>
              <el-menu-item index="1-2">公司</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="2">
            <i class="el-icon-pie-chart" @click="showTotal"></i>
            <span slot="title">概览</span>
          </el-menu-item>
          <el-menu-item index="3">
            <i class="el-icon-document" @click="showSearch"></i>
            <span slot="title">搜索</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-setting"></i>
            <span slot="title">高级功能</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-present"></i>
            <span slot="title">打赏-联系我</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
         
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>
export default {
  data() {
    return {
      isCollapse: true,
      toobar: {
        currentClass: "el-icon-bottom-right",
        currentValue: "打开"
      },
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    showTotal(){
        this.$router.push({path:"/total"});

    },
    showSearch(){
        this.$router.push({path:"/search"});

    },


    doChange() {
      console.log(this.isCollapse);
      if (this.isCollapse) {
        this.isCollapse = false;
        this.toobar.currentClass = "el-icon-bottom-left";
        this.toobar.currentValue = "关闭";
      } else {
        this.isCollapse = true;
        this.toobar.currentClass = "el-icon-bottom-right";
        this.toobar.currentValue = "打开";
      }
    }
  }
};
</script>

<style>
.el-aside {
  color: #333;
  text-align: center;
  line-height: 200px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 150px;
  min-height: 600px;
}
</style>