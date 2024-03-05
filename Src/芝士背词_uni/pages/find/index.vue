<template>
	<view :class="'find ' + (mode_ === 'night' ? 'night' : '')">
		<swiper :autoplay="true" interval="2500" circular class="swiper">
			<block v-for="(item, index) in swiper_pic" :key="index">
				<swiper-item>
					<view :data-url="item.url">
						<image :src="item" mode="scaleToFill" class="swiper-img"></image>
					</view>
				</swiper-item>
			</block>
		</swiper>
		<view class="blue-bg"></view>
		<view class="find-menu" :style="'height: ' + findMenuHeight + 'rpx;'">
			<find-btn :flag="item.flag" :title="item.title" :digest="item.digest" :url="item.url" @listener="onBtnClick"
				v-for="(item, index) in menuData" :key="index"></find-btn>
			<view class="more" @tap="onMoreClick">. . .</view>
		</view>
		<view class="idea">
			<idea title="好友动态" :ideaList="ideaList" :type="2">
				<view slot="top" class="top-slot" @tap="newIdea">
					<van-icon name="add-o" />
				</view>
				<view slot="bottom" class="bottom-slot" @tap="loadMore">
					<van-icon name="ellipsis" />
				</view>
			</idea>
		</view>
		<van-calendar :show="show" confirm-text="打卡" readonly @close="onClose" @confirm="onConfirm"
			:dakaDate="dakaDays" />
	</view>
</template>

<script>
	import findBtn from '@/component/find-btn/index';
	import idea from '@/component/idea/index';
	// import vanCalendar from '@/miniprogram_npm/@vant/weapp/calendar/index';
	// import vanIcon from '@/miniprogram_npm/@vant/weapp/icon/index';
	// pages/find/index.js
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../store/store';
	import {
		loadIdea,
		postIdea,
		forkIdea
	} from '../../service/idea.service';
	export default {
		components: {
			findBtn,
			idea,
			// vanCalendar,
			// vanIcon
		},
		data() {
			return {
				swiper_pic: [],
				menuData: [],
				show: false,
				date: 0,
				dakaDays: [],
				findMenuHeight: 200,
				mode_: '',
				ideaList: [],
				offset: 0,
				flag: 1
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			this.loadMore();
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['mode', 'openID'],
				actions: ['getMode', 'getOpenID']
			});
			let dakaDays = uni.getStorageSync('daka');
			let localToMonth = uni.getStorageSync('toMonth');

			if (localToMonth === '') {
				uni.setStorageSync('toMonth', toMonth);
			}

			if (dakaDays === '') {
				dakaDays = [];
				uni.setStorageSync('daka', dakaDays);
				return;
			}

			let date = new Date(); // let today = new Date().getDate();

			let toMonth = uni.getStorageSync('toMonth');
			let toMonth_ = date.getMonth();

			if (toMonth != toMonth_) {
				console.log('本月第一次打开该页面');
				uni.setStorageSync('daka', []);
				dakaDays = [];
				uni.setStorageSync('toMonth', toMonth_);
			}

			this.setData({
				dakaDays
			});
			const fs = uni.getFileSystemManager();
			let swiper_pic = [];

			try {
				const res = ['1.jpg', '5E63E5C92BCC78AAE5F98B4764AB7236.jpg', '9DE6BB91460456531BA2A6D617216592.jpg',
					'B1B1742E77A34A155602C0985195DEA9.jpg', 'B6F947C3A70824727639B21A78C9B1E5.jpg',
					'u=3541251942,2872259075&fm=253&fmt=auto&app=120&f=JPEG.jpg'
				]

				swiper_pic = res.map((t) => `/static/assets/img/swiper/${t}`);
			} catch (e) {
				console.error(e);
			}

			this.setData({
				swiper_pic
			});
			let menuData = [{
					title: '每日打卡',
					digest: '坚持就是胜利',
					flag: 0
				},
				{
					title: '今日排名',
					digest: '今日学单词数排名',
					url: '../../page-find/pages/rank/index',
					flag: 1
				},
				{
					title: '每日感想',
					digest: '记录每日感悟',
					flag: 1,
					url: '../../page-find/pages/thoughts/index'
				},
				{
					title: '感想本',
					digest: '查看感想本',
					flag: 1,
					url: '../../page-find/pages/thoughtsBook/index'
				}
			];
			this.setData({
				menuData
			});
		},
		onShow() {
			this.setData({
				mode_: this.getMode()
			});
		},
		onUnload() {
			this.storeBindings.destroyStoreBindings();
		},
		methods: {
			onBtnClick(event) {
				let url = event.detail.url;
				const flag = event.detail.flag;
				const openID = this.getOpenID();

				if (flag === 0) {
					this.setData({
						show: true
					});
				} else {
					if (url) {
						url = url + '?openID=' + openID;
					}

					uni.navigateTo({
						url
					});
				}
			},

			onConfirm(event) {
				console.log(this.dakaDays);
				this.setData({
					show: false,
					date: this.formatDate(event.detail)
				});
				let dakaDays = this.dakaDays;
				let date = new Date().getDate();

				if (!dakaDays[date]) {
					dakaDays[date] = true;
					uni.setStorageSync('daka', dakaDays);
				}

				this.setData({
					dakaDays
				});
			},

			onClose() {
				this.setData({
					show: false
				});
			},

			formatDate(date) {
				date = new Date(date);
				return date.getDate();
			},

			onMoreClick() {
				if (this.findMenuHeight === 200) {
					this.setData({
						findMenuHeight: 420
					});
				} else {
					this.setData({
						findMenuHeight: 200
					});
				}
			},

			newIdea() {
				const openID = this.getOpenID();
				uni.navigateTo({
					url: `../../page-find/pages/thoughts/index?type=0&openID=${openID}`
				});
			},

			async loadMore() {
				let offset = this.offset;
				const flag = this.flag;

				if (flag == 1) {
					this.loadData({
						num: 2,
						start: offset
					});
					offset += 2;
					this.setData({
						offset
					});
				} else {
					uni.showToast({
						title: '请求频繁',
						duration: 1000,
						icon: 'error'
					});
				}
			},

			async loadData(param) {
				this.setData({
					flag: 0
				});
				let result;

				try {
					result = await loadIdea(param);
				} catch (error) {
					uni.showToast({
						title: '丢包啦，请重试',
						duration: 1000,
						icon: 'error'
					});
					this.setData({
						flag: 1
					});
					return;
				}

				const res = result.data.data;

				if (res.length === 0) {
					uni.showToast({
						title: '没有更多内容了',
						icon: 'error'
					});
				}

				let ideaList = this.ideaList;

				for (let i = 0; i < res.length; i++) {
					let key = Object.keys(res[i])[0];
					const content = res[i][Object.keys(res[i])[0]];
					key = key.slice(1, key.length - 1);
					key = key.split(',');
					const openID = key[0];
					const avatarUrl = key[1];
					const nickName = key[2];
					ideaList.push({
						openID,
						avatarUrl,
						nickName,
						content
					});
				}

				this.setData({
					ideaList,
					flag: 1
				});
			}
		}
	};
</script>
<style>
	.swiper {
		width: 80%;
		margin-left: 10%;
		border-radius: 60rpx;
		overflow: hidden;
	}

	.swiper-img {
		width: 100%;
	}

	.blue-bg {
		height: 200rpx;
		background-color: rgb(187, 238, 247);
		z-index: -1;
		position: absolute;
		width: 100%;
		top: 0;
	}

	.find {
		position: relative;
		height: 100vh;
		transition: all 0.5s;
	}

	.find-menu {
		display: flex;
		margin-top: 20rpx;
		gap: 20rpx;
		padding: 0 20rpx;
		flex-wrap: wrap;
		height: 200rpx;
		overflow: hidden;
		position: relative;
		transition: height 0.5s;
	}

	.more {
		position: absolute;
		top: 10rpx;
		right: 10rpx;
		font-size: 30rpx;
		flex-wrap: wrap;
		width: 0.6rem;
		background-color: rgb(187, 238, 247);
		border-radius: 60px;
		padding: 0 30rpx;
		text-align: center;
		height: 150rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		/* vertical-align: top; */
		/* overflow: visible; */
	}

	.night {
		background-color: black !important;
		color: #fff !important;
		filter: brightness(0.7) ；;
	}

	.bottom {
		width: 50rpx;
		height: 50rpx;
	}

	.top-slot {
		width: 50rpx;
		height: 100%;
	}

	.bottom-slot {
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>
