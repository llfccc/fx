import Vue from 'vue';
import App from './App';
import router from './router';
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
// import '../static/css/theme-green/index.css';       // 浅绿色主题
import "babel-polyfill";
// import './mock/index.js';
// //bootstrap
// import 'admin-lte/bootstrap/js/bootstrap.min.js'
// import 'admin-lte/bootstrap/css/bootstrap.min.css'
// // //AdminLTE
// import 'admin-lte/dist/css/AdminLTE.min.css'
// import 'admin-lte/dist/css/skins/_all-skins.min.css'
// import 'admin-lte/dist/js/app.min.js'
// //font-awesome
// import 'font-awesome/css/font-awesome.min.css'
Vue.use(ElementUI);
Vue.prototype.$axios = axios;
//
Vue.prototype.makePostReady = function makePostReady(object) {
    var sentence = "";
    for (var key in object) {
        var sentenceAdd = key + '=' + object[key] + '&';
        sentence = sentence + sentenceAdd;
    }
    //console.log( 'sentence: ' + sentence );
    return sentence;
}
//根据日期获取周数
/**
 * Returns the week number for this date.  dowOffset is the day of week the week
 * "starts" on for your locale - it can be from 0 to 6. If dowOffset is 1 (Monday),
 * the week returned is the ISO 8601 week number.
 * @param int dowOffset
 * @return int
 */
Date.prototype.getDateWeek = function (dowOffset) {
    /*getWeek() was developed by Nick Baicoianu at MeanFreePath: http://www.meanfreepath.com */
    dowOffset = typeof(dowOffset) == 'int' ? dowOffset : 0; //default dowOffset to zero
    var newYear = new Date(this.getFullYear(), 0, 1);
    var day = newYear.getDay() - dowOffset; //the day of week the year begins on
    day = (day >= 0 ? day : day + 7);
    var daynum = Math.floor((this.getTime() - newYear.getTime() -
        (this.getTimezoneOffset() - newYear.getTimezoneOffset()) * 60000) / 86400000) + 1;
    var weeknum;
    //if the year starts before the middle of a week
    if (day < 4) {
        weeknum = Math.floor((daynum + day - 1) / 7) + 1;
        if (weeknum > 52) {
            nYear = new Date(this.getFullYear() + 1, 0, 1);
            nday = nYear.getDay() - dowOffset;
            nday = nday >= 0 ? nday : nday + 7;
            /*if the next year starts before the middle of
              the week, it is week #1 of that year*/
            weeknum = nday < 4 ? 1 : 53;
        }
    }
    else {
        weeknum = Math.floor((daynum + day - 1) / 7);
    }
    return weeknum;
};
// http请求拦截器
import {Loading, Message} from 'element-ui'

axios.defaults.timeout = 5000
axios.interceptors.response.use(data => { // 响应成功关闭loading
    //  loadinginstace.close()
    var error_code = data.data.code
    if (error_code == -10) {
        Message.error({message: '登陆失效，请重新登陆'})
        router.push({path: '/login'})
    }
    return data
})

//获取cookie 中的csrf token，并用拦截器写入
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

// Vue.prototype.$axios.defaults.headers.post['X-CSRFToken'] = csrftoken;
Vue.prototype.$axios.interceptors.request.use(
    config => {
        var csrftoken = getCookie("csrftoken");
        config.headers.post['X-CSRFToken'] = csrftoken;
        return config;
    },
    err => {
        return Promise.reject(err);
    });
new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
