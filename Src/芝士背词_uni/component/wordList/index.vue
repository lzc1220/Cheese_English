<template>
    <view class="wordList" :style="'transform: translateX(' + offsetX + 'vw);width: ' + wordList.length + '00vw;'">
        <view class="words" v-for="(item, index) in wordList" :key="index">
            <view class="word-info-word">
                <view class="word-info-word-word">{{ item.wordContent }}</view>
                <view>{{ item.pronunciation }}</view>
            </view>

            <view class="word-info-means">
                <view class="word-info-means-item" v-for="(pro, index1) in item.meaning" :key="pro.idx">{{ pro }}</view>
            </view>
        </view>
    </view>
</template>

<script>
// component/wordList/index.js
export default {
    data() {
        return {
            pro: {
                idx: ''
            }
        };
    },
    /**
     * 组件的属性列表
     */
    props: {
        wordList: {
            type: Array,
            default: () => []
        },
        offsetX: {
            type: Number
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {}
};
</script>
<style>
.wordList {
    display: flex;
    overflow: hidden;
    /* flex:1; */
    height: 100%;
    transition: transform 0.5s;
}

.words {
    width: 100%;
    height: 100%;
    display: flex;
    max-width: 100vw;
    flex-direction: column;
    align-items: center;
    font-weight: 500;
    box-sizing: border-box;
    position: relative;
}
.word-info-word {
    flex: 1.5;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    gap: 40rpx;
    font-size: 39rpx;
}
.word-info-word-word {
    font-size: 90rpx;
}
.word-info-means {
    display: flex;
    flex: 1;
    justify-content: space-around;
    width: 100%;
    align-content: flex-start;
    flex-wrap: wrap;
}
.word-info-means-item {
    border: solid 3rpx rgb(187, 206, 203);
    border-radius: 60rpx;
    width: 86%;
    box-sizing: border-box;
    padding: 0.4rem 1rem;
    overflow: hidden;
    background-color: rgb(226, 250, 246);
}
</style>
