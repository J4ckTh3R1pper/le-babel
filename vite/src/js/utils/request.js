import axios from 'axios'
import {ElLoading, ElMessage, ElMessageBox, ElNotification} from "element-plus";
import {blobValidate, tansParams} from "./utils.js";
import errCodes from "../errCodes.js";
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

let downloadLoadingInstance;
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: '/api',
    // 超时
    timeout: 10000,
    withCredentials: true,
    headers: {
        'Authorization': sessionStorage.getItem("access_token")
    }
})

service.interceptors.response.use(res => {
        // 未设置状态码则默认成功状态
        const code = res.data.code || 200;
        const token = res.headers['Authorization']
        if ( token ) sessionStorage.setItem("access_token", token)
        // 获取错误信息
        const msg = errCodes[code] || res.data.msg || errCodes['default']
        // 二进制数据则直接返回
        if (res.request.responseType ===  'blob' || res.request.responseType ===  'arraybuffer') {
            return res.data
        }
        if (code === 401) {
            return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
        } else if (code === 500) {
            ElMessage({ message: msg, type: 'error' })
            return Promise.reject(new Error(msg))
        } else if (code === 601) {
            ElMessage({ message: msg, type: 'warning' })
            return Promise.reject(new Error(msg))
        } else if (code !== 200) {
            ElNotification.error({ title: msg })
            return Promise.reject('error')
        } else {
            return Promise.resolve(res.data)
        }
    },
    error => {
        console.log('err' + error)
        let { message } = error;
        if (message === "Network Error") {
            message = "后端接口连接异常";
        } else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        } else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substring(message.length - 3, message.length) + "异常";
        }
        ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
        return Promise.reject(error)
    }
)

// 通用下载方法
export function download(url, params, filename, config) {
    downloadLoadingInstance = ElLoading.service({ text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)", })
    return service.post(url, params, {
        transformRequest: [(params) => { return tansParams(params) }],
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        responseType: 'blob',
        ...config
    }).then(async (data) => {
        const isBlob = blobValidate(data);
        if (isBlob) {
            const blob = new Blob([data])
            saveAs(blob, filename)
        } else {
            const resText = await data.text();
            const rspObj = JSON.parse(resText);
            const errMsg = errCodes[rspObj.code] || rspObj.msg || errCodes['default']
            ElMessage.error(errMsg);
        }
        downloadLoadingInstance.close();
    }).catch((r) => {
        console.error(r)
        ElMessage.error('下载文件出现错误，请联系管理员！')
        downloadLoadingInstance.close();
    })
}

export default service