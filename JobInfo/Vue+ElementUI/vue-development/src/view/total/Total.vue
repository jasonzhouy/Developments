<template>
  <el-container>
    <toolBar></toolBar>
    <el-main>
      <el-row>
        <el-col :span="18">
          <img src="../../assets/test.gif" style="width:100px;height:75px margin-top:0px" />
          <h3 >概览</h3>
          <div style="font-size:60%;">(基于{{total}}条数据)</div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <div
            id="wordsChartColumn"
            style="position: relative; overflow: hidden; width: 100%; height: 300px; padding: 0px; text-align:center; border-width: 0px; cursor: default;"
          ></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <div id="cityChartColumn" class="div-demo" style="margin: 0px;"></div>
        </el-col>
        <el-col :span="8">
          <div id="companyChartColumn" class="div-demo" style="margin-left: 120px;"></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <div id="salaryMinChartColumn" class="div-demo" style="margin: 0px;"></div>
        </el-col>
        <el-col :span="8">
          <div id="salaryMaxChartColumn" class="div-demo" style="margin-left: 120px;"></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <div id="experienceChartColumn" class="div-circle" style="margin: 0px;"></div>
        </el-col>
        <el-col :span="8">
          <div id="degreeChartColumn" class="div-circle" style="margin-left: 120px;"></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <div
            id="chartLine"
            style="position: relative; overflow: hidden; width: 100%; height: 300px; padding: 0px; margin-left: 0px; border-width: 0px; cursor: default;"
          ></div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="18">
          <div
            id="experienceChartLine"
            style="position: relative; overflow: hidden; width: 100%; height: 300px; padding: 0px; margin-left: 0px; border-width: 0px; cursor: default;"
          ></div>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import ToolBar from "../../components/ToolBar";
import wordCloud from "echarts-wordcloud";
import echarts from "echarts";
import { requestSearch, requestCount } from "../../api/api";
import { constants } from "fs";
import {
  salaryAggByAddresspara,
  addressTotalPara,
  companyInfoMaxPara,
  salaryMinRangePara,
  salaryMaxRangePara,
  experiencePara,
  degreePara,
  wordsPara,
  salaryAggByExperiencePara
} from "../../sql/store";
import { fail } from "assert";
export default {
  components: {
    toolBar: ToolBar
  },
  data() {
    return {
      total: 20,
      chart1: {
        name: [],
        nums: []
      },
      chartColumn: null,
      chart2: {
        name: [],
        numsMin: [],
        numsMax: []
      },
      chart2Column: null,
      chart3: {
        name: [],
        nums: []
      },
      chart3Column: null,
      chart4: {
        name: [],
        nums: []
      },
      chart4Column: null,
      chart5: {
        name: [],
        nums: []
      },
      chart5Column: null,
      chart6: {
        objectData: []
      },
      chart6Column: null,
      chart7: {
        objectData: []
      },
      chart7Column: null,
      chart8: {
        objectData: []
      },
      chart8Column: null,
      chart9: {
        name: [],
        numsMin: [],
        numsMax: []
      },
      chart9Column: null
    };
  },
  methods: {
    getTotal() {
      requestCount().then(res => {
        this.total = res.count;
      });
    },
    drawColumnChart() {
      let para = addressTotalPara;
      requestSearch(para).then(res => {
        var result = res.aggregations.address.buckets;
        for (var i = 0; i < result.length; i++) {
          this.chart1.name[i] = result[i].key;
          this.chart1.nums[i] = result[i].doc_count;
        }

        this.chartColumn = echarts.init(
          document.getElementById("cityChartColumn")
        );
        this.chartColumn.setOption({
          title: { text: "热门城市工作机会", x: "center" },
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

    drawLineChart() {
      let para = salaryAggByAddresspara;
      requestSearch(para).then(res => {
        var resultMin = res.aggregations.jobInfo.buckets;
        var resultMax = res.aggregations.jobInfo2.buckets;

        for (var i = 0; i < resultMin.length; i++) {
          this.chart2.name[i] = resultMin[i].key;
          this.chart2.numsMin[i] = Math.floor(resultMin[i].minSalaryAvg.value);
          this.chart2.numsMax[i] = Math.floor(resultMax[i].maxSalaryAvg.value);
        }
        this.chartColumn = echarts.init(document.getElementById("chartLine"));
        this.chartColumn.setOption({
          title: { text: "全国平均薪资比较", x: "center" },
          tooltip: {
            trigger: "axis"
          },
          legend: {
            right: "right",
            orient: "horizontal",
            data: ["最低薪资", "最高薪资"]
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            type: "category",
            axisLabel: {
              interval: 0,
              rotate: 30
            },
            boundaryGap: false,
            axisTick: {
              show: false
            },
            data: this.chart2.name
          },
          yAxis: {
            type: "value"
          },
          series: [
            {
              name: "最低薪资",
              type: "line",
              data: this.chart2.numsMin
            },
            {
              name: "最高薪资",
              type: "line",
              data: this.chart2.numsMax
            }
          ]
        });
      });
    },

    drawCompanyColumnChart() {
      let para = companyInfoMaxPara;
      requestSearch(para).then(res => {
        var result = res.aggregations.company.buckets;
        for (var i = 0; i < result.length; i++) {
          this.chart3.name[i] = result[i].key;
          this.chart3.nums[i] = result[i].doc_count;
        }

        this.chart3Column = echarts.init(
          document.getElementById("companyChartColumn")
        );
        this.chart3Column.setOption({
          title: { text: "在招岗位最多的公司", x: "center" },
          tooltip: {},
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            data: this.chart3.name,
            axisLabel: {
              interval: 0
            },
            boundaryGap: true
          },
          yAxis: {},
          series: [
            {
              name: "数量",
              type: "bar",
              data: this.chart3.nums,
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
    drawsalaryMinColumnChart() {
      let para = salaryMinRangePara;
      requestSearch(para).then(res => {
        var result = res.aggregations.salaryRange.buckets;
        for (var i = 0; i < result.length; i++) {
          this.chart4.name[i] = result[i].key;
          this.chart4.nums[i] = result[i].doc_count;
        }

        this.chart4Column = echarts.init(
          document.getElementById("salaryMinChartColumn")
        );
        this.chart4Column.setOption({
          title: { text: "起薪分布", x: "center" },
          tooltip: {},
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            data: this.chart4.name,
            axisLabel: {
              interval: 0
            },
            boundaryGap: true
          },
          yAxis: {},
          series: [
            {
              name: "数量",
              type: "bar",
              data: this.chart4.nums,
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
    drawsalaryMaxColumnChart() {
      let para = salaryMaxRangePara;
      requestSearch(para).then(res => {
        var result = res.aggregations.salaryRange.buckets;
        for (var i = 0; i < result.length; i++) {
          this.chart5.name[i] = result[i].key;
          this.chart5.nums[i] = result[i].doc_count;
        }

        this.chart4Column = echarts.init(
          document.getElementById("salaryMaxChartColumn")
        );
        this.chart4Column.setOption({
          title: { text: "顶薪分布", x: "center" },
          tooltip: {},
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            data: this.chart5.name,
            axisLabel: {
              interval: 0
            },
            boundaryGap: true
          },
          yAxis: {},
          series: [
            {
              name: "数量",
              type: "bar",
              data: this.chart5.nums,
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

    drawExperienceColumnChart() {
      let para = experiencePara;
      requestSearch(para).then(res => {
        var result = res.aggregations.experienceRequire.buckets;
        for (var i = 0; i < result.length; i++) {
          var temp = {
            value: result[i].doc_count,
            name: this.doFilterExperience(result[i].key)
          };
          this.chart6.objectData[i] = temp;
        }

        this.chart6Column = echarts.init(
          document.getElementById("experienceChartColumn")
        );
        this.chart6Column.setOption({
          title: {
            text: "经验要求",
            subtext: "全国数据",
            left: "center"
          },
          tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          series: [
            {
              name: "数量",
              type: "pie",
              radius: "65%",
              center: ["50%", "50%"],
              selectedMode: "single",
              data: this.chart6.objectData,
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

    drawDegreeColumnChart() {
      let para = degreePara;
      requestSearch(para).then(res => {
        var result = res.aggregations.degreeRequire.buckets;
        for (var i = 0; i < result.length; i++) {
          var temp = {
            value: result[i].doc_count,
            name: this.doFilterDegree(result[i].key)
          };
          this.chart7.objectData[i] = temp;
        }

        this.chart7Column = echarts.init(
          document.getElementById("degreeChartColumn")
        );
        this.chart7Column.setOption({
          title: {
            text: "学历要求",
            subtext: "全国数据",
            left: "center"
          },
          tooltip: {
            trigger: "item",
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          series: [
            {
              name: "数量",
              type: "pie",
              radius: "65%",
              center: ["50%", "50%"],
              selectedMode: "single",
              data: this.chart7.objectData,
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

    drawWordsChart() {
      let para = wordsPara;
      requestSearch(para).then(res => {
        var result = res.aggregations.words.buckets;
        for (var i = 0; i < result.length; i++) {
          var temp = {
            value: result[i].doc_count,
            name: result[i].key
          };
          this.chart8.objectData[i] = temp;
        }
        this.chart8Column = echarts.init(
          document.getElementById("wordsChartColumn")
        );

        this.chart8Column.setOption({
          tooltip: {},
          series: [
            {
              type: "wordCloud",
              gridSize: 2,
              sizeRange: [16, 60],
              rotationRange: [-90, 90],
              shape: "smooth",
              textStyle: {
                normal: {
                  color: function() {
                    return (
                      "rgb(" +
                      [
                        Math.round(Math.random() * 255),
                        Math.round(Math.random() * 255),
                        Math.round(Math.random() * 255)
                      ].join(",") +
                      ")"
                    );
                  }
                },
                emphasis: {
                  shadowBlur: 10,
                  shadowColor: "#333"
                }
              },
              data: this.chart8.objectData
            }
          ]
        });

        // 添加点击事件
        // this.chart8Column.on("click", params => {
        //   console.log(params);
        //   this.$emit("clickFromTotal", params.name);
        // });
      });
    },

    drawExperienceLineChart() {
      let para = salaryAggByExperiencePara;
      requestSearch(para).then(res => {
        console.log(res);
        var resultMin = res.aggregations.experienceInfo.buckets;
        var resultMax = res.aggregations.experienceInfo2.buckets;
        for (var i = 0; i < resultMin.length; i++) {
          this.chart9.name[i] = this.doFilterExperience(resultMin[i].key);
          this.chart9.numsMin[i] = Math.floor(resultMin[i].minSalaryAvg.value);
          this.chart9.numsMax[i] = Math.floor(resultMax[i].maxSalaryAvg.value);
        }
        this.chart9Column = echarts.init(
          document.getElementById("experienceChartLine")
        );
        this.chart9Column.setOption({
          title: { text: "工作经验薪资对比", x: "center" },
          tooltip: {
            trigger: "axis"
          },
          legend: {
            right: "right",
            orient: "horizontal",
            data: ["最低薪资", "最高薪资"]
          },
          grid: {
            left: "3%",
            right: "4%",
            bottom: "3%",
            containLabel: true
          },
          xAxis: {
            type: "category",
            axisLabel: {
              interval: 0,
              rotate: 30
            },
            boundaryGap: false,
            axisTick: {
              show: false
            },
            data: this.chart9.name
          },
          yAxis: {
            type: "value"
          },
          series: [
            {
              name: "平均最低",
              type: "line",
              data: this.chart9.numsMin
            },
            {
              name: "平均最高",
              type: "line",
              data: this.chart9.numsMax
            }
          ]
        });
      });
    },

    doFilterExperience(para) {
      switch (para) {
        case 0:
          return "经验不限";
          break;
        case 1:
          return "1年以内";
          break;
        case 2:
          return "1-3年";
          break;
        case 3:
          return "3-5年";
          break;
        case 4:
          return "5-10年";
          break;
        case 5:
          return "10年以上";
          break;
        case 6:
          return "应届生";
          break;
        default:
          return "其他";
          break;
      }
    },

    doFilterDegree(para) {
      switch (para) {
        case 0:
          return "学历不限";
          break;
        case 1:
          return "大专";
          break;
        case 2:
          return "本科";
          break;
        case 3:
          return "硕士";
          break;
        case 4:
          return "博士";
          break;
        default:
          return "其他";
          break;
      }
    },

    drawCharts() {
      this.drawWordsChart();
      this.drawColumnChart();
      this.drawLineChart();
      this.drawCompanyColumnChart();
      this.drawsalaryMinColumnChart();
      this.drawsalaryMaxColumnChart();
      this.drawExperienceColumnChart();
      this.drawDegreeColumnChart();
      this.drawExperienceLineChart();
    }
  },
  mounted: function() {
    this.getTotal();
    this.drawCharts();
  },
  updated: function() {
    this.getTotal();
    this.drawCharts();
  }
};
</script>

<style >
/*.chart div {
        height: 400px;
        float: left;
    }*/
.el-main {
  color: #333;
  text-align: center;
  line-height: 40px;
}
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
.div-circle {
  position: relative;
  overflow: hidden;
  width: 450px;
  height: 450px;
  padding: 0px;
  border-width: 0px;
  cursor: default;
}
</style>
