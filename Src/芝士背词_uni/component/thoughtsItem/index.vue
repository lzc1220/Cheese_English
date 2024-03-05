<template>
    <view class="thought-item">
        <view class="thought-item-title">
            {{ content }}
        </view>
    </view>
</template>

<script>
// component/thoughtsItem/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        content: {
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
.thought-item {
    display: flex;
    align-items: center;
    padding: 10rpx 20rpx;
    border-radius: 20rpx;
    background-color: skyblue;
    /* overflow: hidden; */
}

.thought-item-title {
    font-size: 40rpx;
}
</style>
