<template>
    <view class="find-btn" :style="'background-color:' + bgc" @tap="onClick">
        <view class="find-btn-title">{{ title }}</view>
        <view class="find-btn-digest">{{ digest }}</view>
    </view>
</template>

<script>
// component/find-btn/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        title: {
            type: String,
            default: '默认文本'
        },
        digest: {
            type: String,
            default: '默认文本'
        },
        bgc: {
            type: String,
            default: 'skyblue'
        },
        flag: {
            type: Number
        },
        url: {
            type: String,
            default: ''
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {
        onClick() {
            const flag = this.flag;
            const url = this.url;
            this.$emit('listener', {
                detail: {
                    flag,
                    url
                }
            });
        }
    }
};
</script>
<style>
.find-btn {
    border-radius: 50rpx;
    width: 300rpx;
    height: 200rpx;
    padding: 20rpx;
    box-sizing: border-box;
}
.find-btn-title {
    font-size: 1.3rem;
}
.find-btn-digest {
    color: rgb(77, 76, 76);
}
</style>
