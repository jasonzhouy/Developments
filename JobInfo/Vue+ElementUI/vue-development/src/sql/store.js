import { constants } from "os";

// 聚合全国地区前五的平均最低薪资以及最高薪资
export const salaryAggByAddresspara = {
    "aggs": {
        "jobInfo": {
            "terms": {
                "field": "name.keyword",
                "size": 10
            },
            "aggs": {
                "minSalaryAvg": {
                    "avg": {
                        "field": "min_salary"
                    }
                }
            }
        },
        "jobInfo2": {
            "terms": {
                "field": "name.keyword",
                "size": 10
            },
            "aggs": {
                "maxSalaryAvg": {
                    "avg": {
                        "field": "max_salary"
                    }
                }
            }
        }
    },

    "from": 0,
    "size": 5
};


// 统计城市信息
export const addressTotalPara = {
    aggs: {
        address: {
            terms: {
                field: "address.keyword"
            },
            aggs: {
                company: {
                    terms: {
                        field: "company.keyword"
                    }
                }
            }
        }
    }
};

// 统计招聘信息最多的公司
export const companyInfoMaxPara = {
    aggs: {
        company: {
            terms: {
                field: "company.keyword",
                size: "5"
            }
        }
    }
};

// 起薪占比图
export const salaryMinRangePara = {
    aggs: {
        salaryRange: {
            histogram: {
                field: "min_salary",
                interval: 10
            }
        }
    }
};

// 顶薪分布图
export const salaryMaxRangePara = {
    aggs: {
        salaryRange: {
            histogram: {
                field: "max_salary",
                interval: 10
            }
        }
    }
};

// 工作经验需求对比
export const experiencePara = {
    aggs: {
        experienceRequire:{
            terms:{
                field:"experience"
            }

        }
    }
};

// 工作学历需求对比
export const degreePara = {
    aggs: {
        degreeRequire:{
            terms:{
                field:"degree"
            }

        }
    }
};



// 职位字符云
export const wordsPara = {
    aggs: {
        words:{
            terms:{
                field:"name.keyword",
                size:30
            }

        }
    }
};

export const salaryAggByExperiencePara = {
    "aggs": {
        "experienceInfo": {
            "terms": {
                "field": "experience",
                "order" : { "_term" : "asc" }
            },
            "aggs": {
                "minSalaryAvg": {
                    "avg": {
                        "field": "min_salary"
                    }
                }
            }
        },
        "experienceInfo2": {
            "terms": {
                "field": "experience",
                "order" : { "_term" : "asc" }
            },
            "aggs": {
                "maxSalaryAvg": {
                    "avg": {
                        "field": "max_salary"
                    }
                }
            }
        }
    },

};

export const getPrefix = {
    "query": {
        "multi_match": {
          "type": "phrase", 
          "query": "",
          "fields": ["name","address.keyword"]
        }
      }
}