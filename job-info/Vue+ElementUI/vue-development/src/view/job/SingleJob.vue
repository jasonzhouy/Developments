<template>
  <div v-loading="listLoading">
    <el-row>
      <img src="../../assets/test.gif" style="width:100px;height:75px margin-top:0px"/>
    </el-row>
    <el-row>
      <el-col :span="18">
        <div style="float:left">
          <p>关于<font size=6 color="#B22222" face="Arial">{{message}}</font>,一共有<font size=6 color="#BEBEBE">{{currentCount}}</font>条相关数据,其中各个地区的分布信息如下:</p>
      </div>
      </el-col>
      </el-row>
    <el-row>
      <el-col :span="8">
        <div id="cityChartColumn" class="div-demo" style="margin: 0px;"></div>
      </el-col>
      <el-col :span="8" style="width:600px">
        <div style="margin-left:220px ">
          <p>关于<font size=6 color="#B22222" face="Arial">{{message}}</font>,一共有<font size=6 color="#BEBEBE">{{currentCount}}</font>条相关数据,其中各个地区的分布信息如下:</p>
      </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { requestSearch } from "../../api/api";
import echarts from "echarts";
export default {
  props: ["messageToJob"],
  data() {
    return {
      message: this.messageToJob,
      listLoading: false,
      currentCount: 0,

      chart1:{
        name: [],
        nums: []
      },
      chart1Column: null
    };
  },
  methods: {
    getCurrentJobNums() {
      let para = {
        query: {
          match: {
            name: this.message
          }
        },
        aggs: {
          aggsByJobName: {
            terms: {
              field: "address.keyword"
            }
          }
        }
      }

      requestSearch(para).then(res => {
        console.log(res);
         var result = res.aggregations.aggsByJobName.buckets;
         this.currentCount = res.hits.total.value;
        for (var i = 0; i < result.length; i++) {
          this.chart1.name[i] = result[i].key;
          this.chart1.nums[i] = result[i].doc_count;
        }

        this.chart1Column = echarts.init(
          document.getElementById("cityChartColumn")
        );
        this.chart1Column.setOption({
          title: { text: "", x: "center" },
          tooltip: {},
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            data: this.chart1.name
          },
          yAxis: {},
          series: [
            {
              name: "数量",
              type: "bar",
              data: this.chart1.nums,
              itemStyle: {
                normal: {
                  label: {
                    show: true, //开启显示
                    position: "top", //在上方显示
                    textStyle: {
                      //数值样式
                      color: "grey",
                      fontSize: 12
                    }
                  }
                }
              }
            }
          ]
        });
      });
    },

    getJobInfo() {
      this.getCurrentJobNums();
    },

  },
  mounted() {
    // this.listLoading = true;
    this.getJobInfo();
  }
};
</script>

<style>

.el-col {
  margin-bottom: 20px;
}
.div-demo {
  position: relative;
  overflow: hidden;
  width: 450px;
  height: 300px;
  padding: 0px;

  border-width: 0px;
  cursor: default;
}
</style>

