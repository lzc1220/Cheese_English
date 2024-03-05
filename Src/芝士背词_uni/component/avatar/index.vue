<template>
    <view>
        <image :src="avatarUrl" class="avatarUrl" :style="'width:' + size + 'rpx;height: ' + size + 'rpx;'"></image>
    </view>
</template>

<script>
// component/avator/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        avatarUrl: {
            type: String,
            default: '/static/assets/img/noPic.jpg'
        },
        size: {
            type: Number,
            default: 100
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {}
};
</script>
<style>
.avatarUrl {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
}
</style>
