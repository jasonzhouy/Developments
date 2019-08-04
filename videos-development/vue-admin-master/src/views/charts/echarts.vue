<template>
  <section class="chart-container">
    <el-row>
      <el-col :span="24">
        <div id="chartColumn" style="width:100%; height:400px;"></div>
      </el-col>
      <el-col :span="24">
        <div id="chartPie" style="width:100%; height:400px;"></div>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import echarts from "echarts";
import { getChart1, getChart2 } from "../../api/api";

export default {
  data() {
    return {
      chart1: {
        name: [],
        nums: []
      },
      chartColumn: null,
      chartPie: null
    };
  },

  methods: {
    drawColumnChart() {
      getChart1().then(res => {
        console.log(res.data.data);
        this.chart1.name = res.data.data.name;
        this.chart1.nums = res.data.data.nums;
        this.listLoading = false;
        //NProgress.done();
        this.chartColumn = echarts.init(document.getElementById("chartColumn"));
        this.chartColumn.setOption({
          title: { text: "当前视频池中视频数" },
          tooltip: {},
          xAxis: {
            data: this.chart1.name
          },
          yAxis: {},
          series: [
            {
              name: "数量",
              type: "bar",
              data: this.chart1.nums
            }
          ]
        });
      });
    },

    drawPieChart() {
      getChart2().then(res => {
        this.chartPie = echarts.init(document.getElementById("chartPie"));
        this.chartPie.setOption({
          title: {
            text: "观看比重",
            subtext: "实时更新",
            x: "center"
          },
          tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: "vertical",
            left: "left",
            data: res.data.data.name
          },
          series: [
            {
              name: "访问来源",
              type: "pie",
              radius: "55%",
              center: ["50%", "60%"],
              data: res.data.data.result,
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: "rgba(0, 0, 0, 0.5)"
                }
              }
            }
          ]
        });
      });
    },
    drawCharts() {
      this.drawColumnChart();
      this.drawPieChart();
    }
  },

  mounted: function() {
    this.drawCharts();
  },
  updated: function() {
    this.drawCharts();
  }
};
</script>

<style scoped>
.chart-container {
  width: 100%;
  float: left;
}
/*.chart div {
        height: 400px;
        float: left;
    }*/

.el-col {
  padding: 30px 20px;
}
</style>
