<template>
    <view>
        <image src="/static/assets/img/swiper/B6F947C3A70824727639B21A78C9B1E5.jpg" class="post-img"></image>
        <view class="post-text">
            <view class="d">
                <text class="date">{{ date }}</text>
                <text class="month" :decode="true">{{ month }} &nbsp;</text>
                <text class="year">{{ year }}</text>
            </view>
            <view>Keep on going never give up</view>
            <view>一往无前，永不言弃</view>
        </view>
    </view>
</template>

<script>
// pages/post/index.js
export default {
    data() {
        return {
            month: 1,
            year: 2000,
            date: 1
        };
    }
    /**
     * 生命周期函数--监听页面加载
     */,
    onLoad(options) {
        const eventChannel = this.getOpenerEventChannel();
        let that = this;
        eventChannel.on('formateDate', function ({ month, year, date }) {
            that.setData({
                month,
                year,
                date
            });
        });
    },
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
.post-img {
    width: 100vw;
    height: 100vh;
    position: fixed;
    z-index: -1;
}
.post-text {
    color: white;
    position: absolute;
    bottom: 10vh;
    left: 10vw;
}
.date {
    font-size: 1.5rem;
}
.year {
    font-size: 1.1rem;
}
.d {
    color: yellow;
    margin-bottom: 0.8rem;
}
</style>
