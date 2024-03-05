import wRequest from './index';
export function getLoginCode() {
    return new Promise((resolve, reject) => {
        uni.login({
            timeout: 1000,
            success: (res) => {
                const code = res.code;
                resolve(code);
            },
            fail: (err) => {
                console.log(err);
                reject(err);
            }
        });
    });
}
export function codeToToken(code) {
    return wRequest.post('/user/log_in', {
        code
    });
}
