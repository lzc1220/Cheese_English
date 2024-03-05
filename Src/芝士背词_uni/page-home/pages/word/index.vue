<template>
	<view class="words-container">
		<van-notify id="van-notify" />
		<view class="top-banner">
			<view class="top-banner-item">
				<view>今日背词</view>
				<view>15/60</view>
			</view>
			<view class="top-banner-item">
				<view>学习时间</view>
				<view>12min</view>
			</view>
		</view>
		<word-list :wordList="wordList" :offsetX="offsetX" style="flex: 1" :mode_="mode_"></word-list>
		<view class="tab-bar">
			<view class="tab-bar-item" @tap="preWord">
				<van-icon name="arrow-left" />
			</view>

			<view class="tab-bar-item" @tap="onFork">
				<van-icon name="star-o" size="50rpx" v-if="!wordList[offset].isForked" />
				<van-icon name="star" size="50rpx" v-else color="rgb(234, 238, 4)" />
				<view>收藏</view>
			</view>
			<view class="tab-bar-item" @tap="onUnfamiliar">
				<van-icon name="question-o" size="50rpx" v-if="!wordList[offset].isUnfamiliar" />
				<van-icon name="question" size="50rpx" v-else color="rgb(234, 238, 4)" />
				<view>陌生</view>
			</view>
			<view class="tab-bar-item" @tap="onMaster">
				<!-- <van-icon name="flag-o" size="50rpx"/> -->
				<van-icon name="diamond-o" size="50rpx" v-if="!wordList[offset].isMaster" />
				<van-icon name="diamond" size="50rpx" v-else color="rgb(234, 238, 4)" />
				<view>掌握</view>
			</view>
			<view class="tab-bar-item" @tap="nextWord">
				<van-icon name="arrow" />
			</view>
		</view>
		<!-- <word-list wordList="{{wordList}}"></word-list> -->
	</view>
</template>

<script>
	// import vanIcon from '@/miniprogram_npm/@vant/weapp/icon/index';
	import wordList from '../../../component/wordList/index';
	// import vanNotify from '@/miniprogram_npm/@vant/weapp/notify/index';
	// pages/word/index.js
	import {
		fetchWord,
		cancelFork
	} from '../../../service/word.service';
	import {
		searchWord
	} from '../../../service/search.service';
	import Notify from '@/static/miniprogram_npm/@vant/weapp/notify/notify.js';
	import {
		addFork
	} from '../../../service/word.service';
	export default {
		components: {
			// vanIcon,
			wordList,
			// vanNotify
		},
		data() {
			return {
				wordList: [],
				offsetX: 0,
				mode_: '',
				flag: true,
				offset: 0,
				openID: '',
				isForked: false,
				isUnfamiliar: false,
				isMaster: ''
			};
		},
		onLoad(options) {
			const wordContent = options.wordContent;
			console.log(wordContent);

			if (wordContent) {
				this.getWord(this, {
					wordContent,
					idx: -1
				});
			}

			if (!wordContent) {
				this.fetchWord();
			}

			const openID = options.openID;
			this.setData({
				openID
			});
		},
		onUnload() {},
		methods: {
			/**
			 * 生命周期函数--监听页面加载
			 */
			async getWord(that, param) {
				try {
					const res = await searchWord(param);
					res.data.data.isForked = true;
					that.setData({
						wordList: [res.data.data]
					});
				} catch (error) {
					uni.showToast({
						title: '搜索结果为空！',
						icon: 'error'
					});
				}
			},

			async fetchWord() {
				const result = await fetchWord();
				this.setData({
					wordList: result.data.data
				});
			},

			preWord() {
				this.setData({
					isForked: false
				});
				let offsetX = this.offsetX;

				if (offsetX === 0) {
					return;
				}

				offsetX += 100;
				this.setData({
					offsetX
				});
				this.setData({
					offset: this.offset - 1
				});
			},

			nextWord() {
				this.setData({
					isForked: false,
					isUnfamiliar: false
				});
				let offsetX = this.offsetX;

				if (offsetX == -(this.wordList.length - 1) * 100) {
					return;
				}

				offsetX -= 100;
				this.setData({
					offsetX
				});
				this.setData({
					offset: this.offset + 1
				});
			},

			async onFork() {
				if (this.flag === false) {
					Notify({
						type: 'warning',
						message: '请求频繁，请稍后再试'
					});
					return;
				}

				this.setData({
					flag: false
				});
				const wordList = this.wordList;
				const offset = this.offset;
				const isForked = wordList[offset].isForked;

				if (isForked) {
					wordList[offset].isForked = false;
				} else {
					wordList[offset].isForked = true;
				}

				this.setData({
					wordList
				});
				const openID = this.openID;
				const wordContent = this.wordList[offset].wordContent;
				let res;

				if (!isForked) {
					res = await addFork({
						wordContent,
						idx: -1,
						openId: openID
					});
				} else {
					res = await cancelFork({
						wordContent,
						idx: -1,
						openId: openID
					});
				}

				console.log(res);
				this.setData({
					flag: true
				});
			},

			async onUnfamiliar() {
				const wordList = this.wordList;
				const offset = this.offset;

				if (this.flag === false) {
					Notify({
						type: 'warning',
						message: '请求频繁，请稍后再试'
					});
					return;
				}

				const isUnfamiliar = wordList[offset].isUnfamiliar;

				if (isUnfamiliar) {
					wordList[offset].isUnfamiliar = false;
				} else {
					wordList[offset].isUnfamiliar = true;
				}

				this.setData({
					flag: false
				});
				this.setData({
					wordList
				});
				this.setData({
					flag: true
				});
			},

			async onMaster() {
				const wordList = this.wordList;
				const offset = this.offset;

				if (this.flag === false) {
					Notify({
						type: 'warning',
						message: '请求频繁，请稍后再试'
					});
					return;
				}

				const isMaster = wordList[offset].isMaster;

				if (isMaster) {
					wordList[offset].isMaster = false;
				} else {
					wordList[offset].isMaster = true;
				}

				this.setData({
					flag: false
				});
				this.setData({
					wordList
				});
				this.setData({
					flag: true
				});
			}
		}
	};
</script>
<style>
	.wordList {
		display: flex;
		overflow: hidden;
		width: 2000vw;
		flex: 1;
		transition: transform 0.5s;
	}

	.words {
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		font-weight: 500;
		box-sizing: border-box;
		position: relative;
	}

	.top-banner {
		border-radius: 30rpx;
		border: 3rpx solid gray;
		width: 100%;
		display: flex;
		box-sizing: border-box;
		justify-content: space-around;
		padding: 10rpx 30rpx;
	}

	.top-banner-item {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.tab-bar {
		display: flex;
		justify-content: space-around;
		align-items: center;
		height: 150rpx;
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

	.word-info-sentence {
		align-self: start;
		margin-left: 40rpx;
	}

	.tab-bar-item {
		width: 100rpx;
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
	}

	.words-container {
		display: flex;
		width: 100vw;
		height: 100vh;
		flex-direction: column;
	}
</style>
