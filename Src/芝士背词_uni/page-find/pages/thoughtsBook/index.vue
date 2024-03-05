<template>
    <view class="thoughtsBook">
        <thought-item :content="item" v-for="(item, index) in thoughtList" :key="index"></thought-item>
    </view>
</template>

<script>
import thoughtItem from '../../../component/thoughtsItem/index';
// pages/thoughtsBook/index.js
import { fetchThoughts } from '../../../service/thoughts.service';
export default {
    components: {
        thoughtItem
    },
    data() {
        return {
            openID: '',
            thoughtList: []
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
        this.getThoughts(openID);
    },
    methods: {
        async getThoughts(openId) {
            const res = await fetchThoughts({
                openId
            });
            this.setData({
                thoughtList: res.data.data
            });
            console.log(res);
        }
    }
};
</script>
<style>
/* pages/thoughtsBook/index.wxss */
</style>
