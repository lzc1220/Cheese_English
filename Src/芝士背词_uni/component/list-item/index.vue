<template>
    <view :class="'content ' + mainStyle" :style="'height: ' + height + '; font-size: ' + fontSize + ';background-color: ' + bgColor + ';'">{{ content }}</view>
</template>

<script>
// component/list-item/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        content: {
            type: null,
            default: ' '
        },
        mainStyle: {
            type: String,
            default: ' '
        },
        height: {
            type: String,
            default: ' '
        },
        fontSize: {
            type: String,
            default: ' '
        },
        bgColor: {
            type: String,
            default: 'white'
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {}
};
</script>
<style>
.content {
    /* background-color: white; */
    margin: 10rpx 20rpx;
    border-radius: 10%rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 10rpx;
    color: rgb(52, 116, 158);
    border-radius: 30rpx;
}
</style>
