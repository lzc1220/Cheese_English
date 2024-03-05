<template>
    <view>
        <view class="idea" v-if="type == 2">
            <view>
                <image :src="info.avatarUrl" class="article-list-cover-img"></image>
            </view>
            <view class="idea-text">
                {{ info.content }}
            </view>
        </view>

        <view class="article-list" @tap="onClick" v-else>
            <view class="article-title">
                {{ info.title }}
            </view>
            <view class="article-digest">
                {{ info.abstract }}
            </view>
            <view :class="type === 0 ? 'article-content' : ''">
                {{ info.content }}
            </view>
        </view>
    </view>
</template>

<script>
// component/article-card/index.js
export default {
    data() {
        return {};
    },
    /**
     * 组件的属性列表
     */
    props: {
        info: {
            type: Object
        },
        type: {
            type: Number,
            default: 0
        }
    },
    /**
     * 组件的方法列表
     */
    methods: {
        onClick() {
            this.$emit('click', {
                detail: this.info
            });
        }
    }
};
</script>
<style>
.article-list-cover-img {
    width: 150rpx;
    height: 150rpx;
    border-radius: 40rpx;
}

.article-list {
    /* display: flex; */
    gap: 20rpx;
    padding-left: 40rpx;
    padding-right: 20rpx;
}
.article-digest {
    white-space: nowrap;
    text-overflow: ellipsis;
    width: 100%;
    overflow: hidden;
    color: rgb(139, 136, 136);
}
.article-title {
    font-size: larger;
    white-space: nowrap;
    text-overflow: ellipsis;
    width: 100%;
    overflow: hidden;
}
.article-content {
    white-space: nowrap;
    text-overflow: ellipsis;
    width: 100%;
    overflow: hidden;
}
.idea-text {
    flex: 1;
}
.idea {
    display: flex;
    gap: 30rpx;
    padding: 10rpx 20rpx;
}

/*
.article-list-text {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
  padding-top:15rpx;
}
.strong{
  font-weight: 600;
}
.article-list-text-detail {
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  overflow: hidden;
}
.tags{
  display: flex;
  position: absolute;
  bottom:20rpx;
  gap:20rpx;
}
.tags-item{
  border-radius: 40rpx;
  border:5rpx solid rgb(119, 226, 77);
  color:rgb(119, 226, 77);
  font-weight: 600;
  width: 100rpx;
  text-align: center;
  box-sizing: border-box;
  font-size: 25rpx;
  height: 60rpx;
  line-height: 60rpx;
}
.cn{
  color:rgb(139, 136, 136)
} */
</style>
