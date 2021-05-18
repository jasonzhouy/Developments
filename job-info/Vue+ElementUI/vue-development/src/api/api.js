
import axios from 'axios';

axios.defaults.headers.post['Content-Type'] = 'application/json';

let base = '/api/boss/job/';

// total里用到的表格

export const requestSearch = params => {
    var url = base + "_search"
    
    return axios.post(url, params).then(res => res.data);
};

export const requestCount = () => {
    // 7.1不推荐在count查询时在index中指定type，此处最好把base中的type（boss）去掉
    var url = base + "_count"
    
    return axios.post(url).then(res => res.data);
};
