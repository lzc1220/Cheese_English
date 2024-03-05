<template>
    <view class="word-book">
        <view class="top-bar">
            <view :class="'top-bar-item ' + (activeType === 1 ? 'active' : '')" @tap="toToday">今日生词</view>
            <view :class="'top-bar-item ' + (activeType === 1 ? '' : 'active')" @tap="toAll">所有生词</view>
        </view>
        <view class="word-book-list">
            <list-card
                :word="item.wordContent"
                :mean="item.meaning[0]"
                @tap.native="onWordClick($event, { word: item })"
                :data-word="item"
                v-for="(item, index) in wordList"
                :key="index"
            ></list-card>
            <view class="diction-list">
                <view class="diction-list-item" v-for="(item, index) in dictionaryList" :key="index">
                    <text>{{ item }}</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import listCard from '../../../component/list-card';
import { fetchForkWords } from '../../../service/word.service';
export default {
    components: {
        listCard
    },
    data() {
        return {
            wordList: [],
            dictionaryOffset: [],
            dictionaryList: [],
            activeType: 1,
            openID: ''
        };
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        const openID = options.openID;
        this.setData({
            openID
        });
    },
    onShow() {
        this.getForkWords(this.openID);
        const dictionaryList = [];

        for (var i = 65; i < 91; i++) {
            dictionaryList.push(String.fromCharCode(i));
        }

        this.setData({
            dictionaryList
        });
    },
    methods: {
        getForkWords: async function (openID) {
            const res = await fetchForkWords(openID);
            console.log(res);
            let data = [];
            let data_ = res.data.data;

            for (let key in data_) {
                data[key] = data_[key];
            }

            data.sort((a, b) => {
                return a.wordContent.localeCompare(b.wordContent);
            });

            for (let i = 0; i < data.length; i++) {
                let first = data[i].wordContent[0];

                if (this.dictionaryOffset[first] == undefined) {
                    this.dictionaryOffset[first] = i;
                }
            }

            this.setData({
                wordList: data
            });
        },

        onWordClick(event, _dataset) {
            /* ---处理dataset begin--- */
            this.datasetHandle(event, _dataset);
            /* ---处理dataset end--- */
            const openID = this.openID;
            const word = event.currentTarget.dataset.word;
            uni.navigateTo({
                url: `../word/index?wordContent=${word.wordContent}&openID=${openID}`
            });
        },

        toAll() {
            this.setData({
                activeType: 0
            });
        },

        toToday() {
            this.setData({
                activeType: 1
            });
        }
    }
};
</script>
<style>
.diction-list {
    width: 70rpx;
    position: fixed;
    padding: 2% 0;
    right: 5rpx;
    top: 80rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    height: 90%;
    font-size: 30rpx;
    margin-right: 10rpx;
    background-color: rgb(219, 241, 245);
    border-radius: 35rpx;
}

.diction-list-item {
    width: 50rpx;
    height: 50rpx;
    text-align: center;
    border-radius: 50%;
}

.diction-list-item:active {
    background-color: rgb(159, 204, 226);
}

.top-bar {
    height: 70rpx;
    display: flex;
    justify-content: space-around;
    align-items: center;
}
.top-bar-item {
    position: relative;
}

.active::after {
    content: ' ';
    display: inline-block;
    position: absolute;
    bottom: -5rpx;
    left: 6rpx;
    right: 6rpx;
    border-bottom: rgb(138, 207, 138) 6rpx solid;
    /* opacity: 1; */
    transition: all 0.5s;
}
</style>
