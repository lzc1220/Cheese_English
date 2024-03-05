<template>
	<view :class="mode_ === 'night' ? 'night' : ''">
		<van-cell-group inset>
			<van-cell icon="user-o" is-link title="退出账号" value="" label="并清除本地登录记录" @tap.native="unLogin" />
			<van-cell icon="points" title="清除本地缓存" is-link value="" label="包括打卡信息及登录记录" @tap.native="deleteLocal" />
			<van-cell icon="revoke" title="返回" @tap.native="navigateBack" is-link></van-cell>
		</van-cell-group>
	</view>
</template>

<script>
	// import vanCell from '@/miniprogram_npm/@vant/weapp/cell/index';
	// import vanCellGroup from '@/miniprogram_npm/@vant/weapp/cell-group/index';
	// pages/config/index.js
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../../store/store';
	export default {
		components: {},
		data() {
			return {
				qingchu: false,
				mode_: 'day',
				userInfo: {}
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['mode'],
				actions: ['getMode']
			});
			this.setData({
				mode_: this.getMode()
			});
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
		onShow() {},
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
			unLogin() {
				//  移除token
				// wx.removeStorageSync('token')
				// console.log("用户已退出登录")
				uni.showToast({
					title: '用户已退出登录',
					icon: 'none',
					duration: 1000,
					mask: true
				});
				this.setData({
					qingchu: true
				});
			},

			deleteLocal() {
				// console.log("清除本地缓存")
				uni.showToast({
					title: '成功清除本地缓存',
					icon: 'none',
					duration: 1000,
					mask: true
				});
				let {
					keys
				} = uni.getStorageInfoSync();

				for (let item of keys) {
					uni.removeStorageSync(item);
				}

				this.setData({
					qingchu: true
				});
				var pages = getCurrentPages();
				var prevPage = pages[pages.length - 2]; //上一个页面

				prevPage.setData({
					userInfo: {}
				});
			},

			navigateBack() {
				uni.navigateBack({});
			}
		}
	};
</script>
<style>
	.night {
		background-color: black !important;
		color: #fff !important;
		filter: brightness(0.7);
		height: 100vh;
	}
</style>
