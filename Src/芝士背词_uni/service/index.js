const BASE_URL = 'http://www.wangyixuan2002.cn:8079';

class WRequset {
    request(url, method, params) {
        return new Promise((resolve, reject) => {
            uni.request({
                url: BASE_URL + url,
                method,
                header: {
                    //设置请求头
                    'content-Type': 'application/json' // 'Authorization':wx.getStorageSync("key"),//读取本地存储的token//将token去读，请求时会将请求头带过去
                },
                data: params,
                success: (res) => {
                    // console.log(res)
                    resolve(res);
                },
                fail: (err) => {
                    reject(err);
                }
            });
        });
    }

    get(url, params) {
        return this.request(url, 'GET', params);
    }

    post(url, data) {
        return this.request(url, 'POST', data);
    }
}

const wRequest = new WRequset();
export default wRequest;
