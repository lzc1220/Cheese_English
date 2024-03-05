<template>
	<!-- <image src="../../../assets/img/swiper/B6F947C3A70824727639B21A78C9B1E5.jpg" class="bg-img"></image> -->

	<view class="article-detail">
		<view class="header">
			{{ data.title }}
			<!-- <van-icon name="like-o" size="60rpx" color="red" wx:if="isFork"></van-icon>
    <van-icon name="like" size="60rpx" color="red" /> -->
		</view>
		<view class="bg"></view>
		<view class="article-detail-content">
			{{ data.content }}
		</view>
		<view class="comment">
			<view class="comment-item" v-for="(item, index) in commentList" :key="index">
				<view>{{ item }}</view>
			</view>
		</view>
		<view class="input">
			<van-field v-model="value" placeholder="发表评论" :border="false" @change="onChange">
				<van-button slot="button" size="small" type="primary" @tap.native="postComment">确认</van-button>
			</van-field>
		</view>
	</view>
</template>

<script>
	// import vanField from '@/miniprogram_npm/@vant/weapp/field/index';
	// import vanIcon from '@/miniprogram_npm/@vant/weapp/icon/index';
	// pages/article-detail/index.js
	import {
		createComment,
		fetchComment
	} from '../../../service/article.service';
	export default {
		components: {},
		data() {
			return {
				data: {
					content: ''
				},
				commentList: [],
				value: '',
				type: ''
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			const eventChannel = this.getOpenerEventChannel();
			const that = this;
			eventChannel.on('initData', function(data) {
				that.setData({
					data
				});
			});
			this.getComment(this.data.num);
		},
		methods: {
			async getComment(num) {
				// const res = await fetchComment({num})
				const res = await fetchComment({
					num: this.data.num
				});
				this.setData({
					commentList: res.data.data
				});
				console.log(this.commentList);
			},

			async postComment() {
				const res = await createComment({
					content: this.value,
					num: this.data.num
				});
				console.log(res.data.code);

				if (res.data.code == 200) {
					console.log(res.data, 'here');
					this.value = '';
					this.getComment(this.data.num);
				}
			},

			onChange() {
				console.log('占位：函数 onChange 未声明');
			}
		}
	};
</script>
<style>
	.bg-img {
		filter: blur(2.5px);
		z-index: -1;
		position: fixed;
	}

	.bg {
		height: 200rpx;
		/* position: sticky; */
	}

	.article-detail-content {
		width: 88%;
		margin: 0 auto;
		box-sizing: border-box;
		background-color: rgb(196, 240, 229);
		border-radius: 50rpx;
		padding: 3%;
		flex: 1;
	}

	.header {
		top: 0;
		position: sticky;
		z-index: 1;
		width: 68%;
		margin: 0 auto;
		border-radius: 50rpx;
		font-size: x-large;
	}

	.input {
		position: fixed;
		bottom: 10rpx;
		left: 10rpx;
		right: 10rpx;
	}

	page {
		padding-bottom: 100rpx;
	}

	.comment-item {
		font-size: 40rpx;
		border: 2rpx solid rgb(168, 204, 198);
	}
</style>
