<template>
	<view :class="'container  ' + (mode_ === 'night' ? 'night' : '')">
		<view class="banner" @tap="toPost">
			<div class="top-banner">
				<image src="/static/assets/img/swiper/B6F947C3A70824727639B21A78C9B1E5.jpg" mode="center"
					class="banner-img"></image>
				<text class="banner-text bottom left">Keep on going never give up.</text>
				<text class="banner-text top right">{{ month }}.{{ year }}</text>
			</div>
		</view>
		<view class="w-search">
			<input class="w-input" placeholder="搜索单词" @tap="toSearchDetail" :disabled="true" />
			<van-icon name="search" class="w-search-icon" @tap.native="toSearchDetail" />
		</view>
		<view class="w-devider"></view>
		<view class="wordBtn" @tap="toWords">开始背词</view>
		<view class="nav">
			<view class="nav-btn" @tap="toWordBook">
				<text>单词本</text>
			</view>
			<view class="nav-btn">
				<text>复习</text>
			</view>
			<view class="nav-btn" @tap="toSolo">
				<text>好友比拼</text>
			</view>
		</view>
		<idea title="好句推荐" :ideaList="sentenceInfo" :type="1"></idea>
		<idea title="今日阅读" :ideaList="articleInfo" @click="toArticleDetail">
			<view @tap="toArticle">更多></view>
		</idea>
	</view>
</template>

<script>
	// import vanIcon from '@/miniprogram_npm/@vant/weapp/icon/index';
	import idea from '@/component/idea/index';
	import jumpTo from '../../utils/jumpTo';
	import {
		getArticle
	} from '../../service/article.service';
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../store/store';
	import {
		getSentence
	} from '../../service/article.service';
	export default {
		components: {
			// vanIcon,
			idea
		},
		data() {
			return {
				swiper_pic: [],
				articleInfo: [],
				year: 2000,
				month: 1,
				date: 1,
				mode_: 'day',
				articleInfo: {},
				sentenceInfo: {}
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['mode', 'openID'],
				actions: ['getMode', 'getOpenID']
			}); // setTimeout(() => {
			//   const openID = this.getOpenID()
			//   console.log("openid in home",openID)
			//   this.setData({openID})
			// }, 500);

			const now = new Date();
			const month = now.toDateString().split(' ')[1];
			const date = now.getDate();
			const year = now.getFullYear();
			this.setData({
				month,
				year,
				date
			});
			this.fetchArticle();
			this.fetchSentence();
		},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow() {
			this.setData({
				mode_: this.getMode()
			});
		},
		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide() {},
		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload() {
			this.storeBindings.destroyStoreBindings();
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh() {},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom() {},
		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage() {},
		methods: {
			async fetchSentence() {
				const res = await getSentence();
				this.setData({
					sentenceInfo: res.data.data
				});
				console.log(this.sentenceInfo);
			},

			async fetchArticle() {
				const res = await getArticle();
				this.setData({
					articleInfo: res.data.data
				});
				console.log(this.articleInfo);
			},

			toSearchDetail(e) {
				const openID = this.getOpenID(); // jumpTo("../searchDetail/index?openID="+openID)

				jumpTo('../../page-home/pages/searchDetail/index?openID=' + openID);
			},

			toPost() {
				const month = this.month;
				const year = this.year;
				const date = this.date;
				uni.navigateTo({
					// url: '../post/index',
					url: '../../page-home/pages/post/index',
					success: function(res) {
						// 通过 eventChannel 向被打开页面传送数据
						res.eventChannel.emit('formateDate', {
							month,
							date,
							year
						});
					}
				});
			},

			toArticle() {
				jumpTo('../../page-home/pages/article/index');
			},

			toWords() {
				const openID = this.getOpenID();
				jumpTo(`../../page-home/pages/word/index?openID=${openID}`);
			},

			toSolo() {
				jumpTo('../../page-home/pages/solo/index');
			},

			toWordBook() {
				const openID = this.getOpenID();
				jumpTo('../../page-home/pages/wordBook/index?openID=' + openID);
			},

			toArticleDetail(e) {
				// const openID = this.getOpenID()
				uni.navigateTo({
					// url: '../article-detail/index',
					url: '../../page-home/pages/article-detail/index',
					success: function(res) {
						// 通过 eventChannel 向被打开页面传送数据
						res.eventChannel.emit('initData', e.detail);
					}
				});
			}
		}
	};
</script>
<style>
	.top-banner {
		height: 200rpx;
		width: 92%;
		margin-left: 4%;
		position: absolute;
		border-radius: 40rpx;
		overflow: hidden;
		/* clip:rect(20px,0px,20px,0px); */
	}

	.banner {
		width: 86%;
		margin: 0 auto;
		position: relative;
		height: 200rpx;
	}

	.banner-img {
		width: 100%;
		height: 100%;
	}

	.container {
		width: 100vw;
		position: relative;
		transition: all 0.5s;
	}

	.banner-text {
		position: absolute;
		color: white;
		z-index: 10;
	}

	.top {
		top: 20rpx;
	}

	.bottom {
		bottom: 20rpx;
	}

	.left {
		left: 40rpx;
	}

	.right {
		right: 20rpx;
	}

	.w-search {
		display: flex;
		margin-top: 20rpx;
		gap: 40rpx;
		justify-content: center;
		align-items: center;
	}

	.w-search-searchDetail {
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		left: 20rpx;
	}

	.w-search-icon {
		border-radius: 50%;
		width: 2rem;
		height: 2rem;
		background-color: rgba(109, 235, 83, 0.747);
	}

	.w-input {
		border-radius: 2rem;
		width: 75%;
		height: 3rem;
		line-height: 3rem;
		background-color: rgb(231, 230, 230);
		box-sizing: border-box;
		padding: 0 1.5rem;
	}

	.w-devider {
		margin-top: 20rpx;
		border-top: solid 2px #c4c9c8;
		width: 80%;
		margin: 30rpx auto 0 auto;
		box-sizing: border-box;
	}

	.wordBtn {
		width: 12rem;
		margin: 20rpx auto 0 auto;
		text-align: center;
		border-radius: 2rem;
		background-color: rgb(136, 210, 211);
		height: 3rem;
		line-height: 3rem;
		color: #fff;
		font-size: 1.5rem;
	}

	.nav {
		display: flex;
		align-items: center;
		height: 300rpx;
	}

	.nav-btn {
		flex: 1;
		width: 25%;
		font-size: 0.8rem;
		text-align: center;
	}

	.night {
		background-color: black !important;
		color: #fff !important;
		filter: brightness(0.7);
	}
</style>
