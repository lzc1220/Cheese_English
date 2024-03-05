<template>
    <view class="history-item">{{ data }}</view>
</template>

<script>
// component/search-history/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        data: {
            type: String,
            default: '默认值'
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {}
};
</script>
<style>
.history-item {
    background-color: rgb(239, 241, 237);
    margin: 10rpx 20rpx;
    border-radius: 20rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 5rpx 10rpx;
}
</style>
