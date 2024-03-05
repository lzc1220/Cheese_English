<template>
    <view class="no1-detail" :style="'color:' + fontColor">
        <view class="rank">{{ rank }}</view>
        <view class="avatar">
            <avatar :size="size"></avatar>
        </view>
        <view class="count">
            <view>背词数</view>
            <view>{{ wordCount }}</view>
        </view>
        <view></view>
    </view>
</template>

<script>
import avatar from '../avatar/index';
// component/rank-item/index.js
export default {
    components: {
        avatar
    },
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        wordCount: {
            type: Number
        },
        rank: {
            type: String
        },
        fontColor: {
            type: String,
            default: 'black'
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
.rank {
    padding-left: 20rpx;
}
.count {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
