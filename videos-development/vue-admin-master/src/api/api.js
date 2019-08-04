import axios from 'axios';

let base = 'http://www.baidu.com';

export const requestLogin = params => {
    console.log(params.username);
    var url = `${base}/login?username=` + params.username + `&password=` + params.password;
    console.log(url);
    return axios.post(url, params).then(res => res.data);
};



export const getUserList = params => {
    console.log(params);
    var url = `${base}/user/getUserInfo?page=` + params.page + `&username=` + params.name;
    console.log(url);
    return axios.get(url, { params: params });
};

export const getUserListPage = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    console.log(params);
    var url = `${base}/user/getUserInfo?page=` + params.page + `&username=` + params.name;
    console.log(url);
    return axios.get(url, { params: params });
};

export const getBanUserListPage = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    console.log(params);
    var url = `${base}/banUser/list`;
    console.log(url);
    return axios.get(url, { params: params });
};

export const removeUser = params => {
    console.log(params);
    return axios.get(`${base}/user/remove`, { params: params });
};


export const addUser = params => { return axios.get(`${base}/user/add`, { params: params }); };

export const submitBan = params => {
    return axios.get(`${base}/user/forbid`, { params: params });
};

export const cancelBan = params => {
    console.log(params);
    return axios.get(`${base}/banUser/cancelBan`, { params: params });
};

export const getBgmListPage = params => {
    console.log(params);
    var url = `${base}/bgm/getBgmList?page=` + params.page;
    console.log(url);
    return axios.get(url, { params: params });
};



export const addBgm = params => {

    let config = {
        headers: { 'Content-Type': 'multipart/form-data' }
    };
    console.log("文件是" + params.file);
    var url = `${base}/bgm/addBgm`;
    console.log(url);
    return axios.post(url, params, config).then(res => res.data);
};

export const removeBgm = params => {

    return axios.get(`${base}/bgm/remove`, { params: params });
};


export const getVideoListPage = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    console.log(params);
    var url = `${base}/video/getAll`;
    console.log(url);
    return axios.get(url, { params: params });
};


export const batchRemoveVideos = params => {
    console.log(params);
    return axios.get(`${base}/video/batchRemove`, { params: params });
};

export const getReportsList = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    console.log(params);
    var url = `${base}/video/reportList`;
    console.log(url);
    return axios.get(url, { params: params });
};

export const offlineReport = params => {
    console.log(params);
    return axios.get(`${base}/video/offlineReport`, { params: params });
};

export const cancelReport = params => {
    console.log(params);
    return axios.get(`${base}/video/cancelReport`, { params: params });
};

export const getChart1 = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    var url = `${base}/charts/chart1`;
    console.log(url);
    return axios.get(url);
};

export const getChart2 = params => {
    // return axios.get(`${base}/user/listpage`, { params: params }); 
    var url = `${base}/charts/chart2`;
    console.log(url);
    return axios.get(url);
};