<template>
  <div id="app">
    <el-container>
      <toolBar></toolBar>
      <el-main>
        <el-row>
          <el-col :span="18">
            <img src="../../assets/test.gif" style="width:100px;height:75px margin-top:0px" />
            <h3>找我想找</h3>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-select
              v-model="value"
              placeholder="请选择"
              style="width:120px"
              @change="handleSelect2"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-autocomplete
              style="margin-left:20px;width:450px"
              v-model="state"
              :fetch-suggestions="querySearchAsync"
              placeholder="请输入内容"
              :disabled="value == ''?true:false"
              @select="handleSelect"
            ></el-autocomplete>
            <el-button icon="el-icon-search" circle style="margin-left:20px"></el-button>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>



<script>
import ToolBar from "../../components/ToolBar";
import { requestSearch } from "../../api/api";

export default {
  components: {
    toolBar: ToolBar
  },
  data() {
    return {
      candidates: [],
      state: "",
      options: [
        {
          value: "company",
          label: "公司"
        },
        {
          value: "address",
          label: "地区"
        },
        {
          value: "name",
          label: "职位"
        }
      ],
      value: ""
    };
  },
  methods: {
    loadAll() {
      let para = {
        aggs: {
          info: {
            terms: {
              field: this.value + ".keyword"
            }
          }
        },
        size: 10
      };
      requestSearch(para).then(res => {
        console.log(res);
        var results = res.aggregations.info.buckets;
        this.candidates = [];
        for (var i = 0; i < results.length; i++) {
          let temp = {
            value: results[i].key
          };
          this.candidates[i] = temp;
        }
        console.log(this.candidates);
      });
    },
    querySearchAsync(queryString, cb) {
      if (queryString == null || queryString == "") {
        var candidates = this.candidates;
        cb(candidates);
        console.log(queryString);
      } else {
        let name = this.value;
        let value = queryString;
        let para = {
          query: {
            bool: {
              must: [
                {
                  match_phrase_prefix: {
                    // name: value
                  }
                }
              ]
            }
          },
          aggs: {
            info: {
              terms: {
                field: name + ".keyword"
              }
            }
          }
        };
        // 设置JSON的KEY为变量
        para.query.bool.must[0].match_phrase_prefix[name] = value;
        console.log(para);
        requestSearch(para).then(res => {
          console.log(res);
          var results = res.aggregations.info.buckets;
          this.candidates = [];
          for (var i = 0; i < results.length; i++) {
            let temp = {
              value: results[i].key
            };
            this.candidates[i] = temp;
          }
          console.log(this.candidates);
          cb(this.candidates);
        });
      }
    },
    createStateFilter(queryString) {
      return state => {
        return (
          state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    handleSelect(item) {
      console.log(item);
    },
    handleSelect2(item) {
      console.log(item);
      (this.state = ""), this.loadAll();
    }
  },
  mounted: function() {}
};
</script>


<style>
.el-main {
  color: #333;
  text-align: center;
  line-height: 40px;
}

.el-col {
  margin-bottom: 100px;
}
</style>