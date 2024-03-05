export default async function getUserInfo(that) {
    uni.showModal({
        title: '提示',
        content: '是否允许获取微信昵称和头像？',

        success(res) {
            if (res.confirm) {
                uni.getUserProfile({
                    desc: '用于完善用户资料',
                    // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
                    success: (res) => {
                        that.setData({
                            userInfo: res.userInfo
                        });
                        uni.setStorageSync('userInfo', res.userInfo);
                        console.log(res.userInfo);
                        that.setData({
                            userInfo: res.userInfo
                        });
                        that.setUserInfo(res.userInfo);
                    },
                    fail: function (err) {
                        console.log(err);
                    }
                });
            }
        }
    });
}
