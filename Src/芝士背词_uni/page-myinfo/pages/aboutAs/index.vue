<template>
    <view>
        <!-- pages/developer/index.wxml -->
        <view class="text-area">
            <view>亲爱的用户，您好</view>
            <view>感谢您使用我们的产品</view>
            <view>使用过程中有任何问题、建议请随时联系我们，感谢您的支持</view>
            <view>希望我们的产品对您学习英语有所帮助</view>
        </view>
        <view class="text-area">
            <view>我们来自华中科技大学计卓2001班</view>
            <view>这是我们的软件工程大作业</view>
            <p>项目开源仓库</p>
            <p class="a" @tap.native="gitAddress">点击复制到粘贴板</p>
        </view>
        <!-- <view class="text-area">
  <view>联系我们：</view>
  <image src="/assets/img/myWx.png" mode="widthFix" class="myWx"></image>
</view> -->
    </view>
</template>

<script>
// pages/developer/index.js
export default {
    data() {
        return {};
    }
    /**
     * 生命周期函数--监听页面加载
     */,
    onLoad(options) {},
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {},
    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {},
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {},
    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {},
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {},
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {},
    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {},
    methods: {
        gitAddress() {
            uni.setClipboardData({
                data: `https://gitee.com/wyx2002/hust_program-work`
            });
        }
    }
};
</script>
<style>
.text-area {
    padding: 40rpx;
    background-color: rgb(238, 240, 149);
    border-radius: 10rpx;
    margin: 30rpx;
}
.a {
    color: cornflowerblue;
    text-decoration: underline;
}
.myWx {
    width: 300rpx;
}
</style>
