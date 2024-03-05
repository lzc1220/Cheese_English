<template>
    <view class="rank">
        <view class="no1">
            <image src="/static/assets/img/swiper/5E63E5C92BCC78AAE5F98B4764AB7236.jpg" class="no1-bg"></image>
            <rank-item :wordCount="999" rank="No.1" fontColor="white" :size="150"></rank-item>
        </view>
        <view class="common"></view>
    </view>
</template>

<script>
import avatar from '../../../component/avatar/index';
import rankItem from '../../../component/rank-item/index';
// pages/rank/index.js
export default {
    components: {
        avatar,
        rankItem
    },
    data() {
        return {};
    },
    /**
     * 生命周期函数--监听页面加载
     */
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
    methods: {}
};
</script>
<style>
.no1-bg {
    width: 100%;
    filter: blur(2px);
    z-index: -1;
    position: absolute;
    height: 350rpx;
}
.no1 {
    height: 350rpx;
    position: relative;
}
.no1-detail {
    display: flex;
    height: 100%;
    align-items: center;
    color: white;
    gap: 40rpx;
}
.avatar {
    flex: 1;
}
</style>
