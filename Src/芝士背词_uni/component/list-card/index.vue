<template>
    <!-- component/list-card/list-card.wxml -->
    <view class="list-card-item">
        <text :decode="true">{{ word }} &nbsp; &nbsp; {{ mean }}</text>
    </view>
</template>

<script>
// component/list-card/list-card.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        word: {
            type: String,
            default: 'apple'
        },
        mean: {
            type: String
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {}
};
</script>
<style>
/* component/list-card/list-card.wxss */
.list-card-item {
    height: 70rpx;
    margin: 4rpx 0;
    line-height: 70rpx;
    padding-left: 30rpx;
    border-radius: 8%;
    margin-right: 90rpx;
    background-color: rgb(219, 241, 245);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
</style>
