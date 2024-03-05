<template>
	<view :class="mode_ === 'night' ? 'night' : ''">
		<view class="w-search">
			<input class="w-input" :value="value" :focus="isFocus" placeholder="搜索单词" @input="onChange"
				@confirm="onConfirm" type="text" />
			<van-icon name="search" class="w-search-icon" />
		</view>
		<view class="title" v-if="history.length">{{ title }}</view>
		<view class="history">
			<search-history :data="item" @tap.native="onHistoryClick($event, { word: item })" :data-word="item"
				v-for="(item, index) in history" :key="index"></search-history>
		</view>
	</view>
</template>

<script>
	// import vanIcon from '@/miniprogram_npm/@vant/weapp/icon/index';
	import searchHistory from '../../component/search-history/index';
	// pages/searchDetail/index.js
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../../store/store';
	export default {
		components: {
			// vanIcon,
			searchHistory
		},
		data() {
			return {
				history: [],
				value: '',
				isFocus: false,
				title: '搜索历史',
				flag: 1,
				mode_: 'day',
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
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['mode'],
				actions: ['getMode']
			});

			try {
				let history = uni.getStorageSync('history');

				if (!history.length) {
					history = [];
				}

				uni.setStorageSync('history', history);
				this.setData({
					history
				});
			} catch (e) {
				console.error(e);
			}
		},
		onUnload() {
			this.storeBindings.destroyStoreBindings();
		},
		/**
		 * 生命周期函数--监听页面初次渲染完成
		 */
		onReady() {},
		/**
		 * 生命周期函数--监听页面显示
		 */
		onShow() {
			this.setData({
				mode_: this.getMode()
			});
			let that = this;
			setTimeout(function() {
				that.setData({
					isFocus: true
				});
				console.log(that.isFocus);
			}, 100);
		},
		/**
		 * 生命周期函数--监听页面隐藏
		 */
		onHide() {},
		/**
		 * 生命周期函数--监听页面卸载
		 */
		onUnload() {},
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
			onChange(e) {
				this.setData({
					value: e.detail.value
				});
			},

			onConfirm: async function(event) {
				if (this.flag === 0) {
					return;
				}

				let searchValue = this.value;
				searchValue = searchValue.trim();

				if (searchValue == undefined || searchValue == '') {
					return;
				}

				this.searchValue(searchValue);
			},

			searchValue: async function(searchValue) {
				const openID = this.openID;
				this.setData({
					flag: 0
				});
				let history = uni.getStorageSync('history');
				history = history.filter((t) => t != searchValue);

				if (!history) {
					history = [];
				}

				if (history.length == 10) {
					history = history.slice(0, 9);
				}

				history.unshift(searchValue);
				this.setData({
					history
				});
				this.setData({
					value: ''
				});
				uni.setStorageSync('history', history);

				try {
					this.setData({
						flag: 1
					});
					uni.navigateTo({
						url: `../../../page-home/pages/word/index?wordContent=${searchValue}&openID=${openID}`
					});
				} catch (error) {
					this.setData({
						flag: 1
					});
					uni.showToast({
						title: '网络连接超时',
						icon: 'error'
					});
				}
			},

			onHistoryClick(event, _dataset) {
				/* ---处理dataset begin--- */
				this.datasetHandle(event, _dataset);
				/* ---处理dataset end--- */
				if (this.flag === 0) {
					return;
				}

				const word = event.currentTarget.dataset.word;
				this.searchValue(word);
			}
		}
	};
</script>
<style>
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

	.history {
		display: flex;
		flex-wrap: wrap;
		padding: 40rpx;
	}

	.title {
		font-size: 1.2rem;
		margin: 1.3rem 0 0 2rem;
	}

	.night {
		background-color: black !important;
		/* color:#fff!important; */
		filter: brightness(0.7);
		height: 100vh;
	}
</style>
