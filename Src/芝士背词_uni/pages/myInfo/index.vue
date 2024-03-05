<template>
	<view>
		<van-notify id="van-notify" />
		<view :class="'myinfo ' + (mode_ === 'night' ? 'night' : '')">
			<view class="header">
				<view v-if="userInfo.avatarUrl">
					<avatar :avatarUrl="userInfo.avatarUrl"></avatar>
					<view>{{ userInfo.nickName }}</view>
				</view>
				<view @tap="onGetUserInfo" v-if="userInfo.avatarUrl === undefined">
					<image src="/static/assets/img/noPic.jpg" class="header-img"></image>
					<button>获取用户信息</button>
				</view>
			</view>

			<view class="container">
				<ec-canvas id="mychart-dom-line" canvas-id="mychart-line" :ec="ec"></ec-canvas>
				<view class="text">本周学习单词999个，再接再厉！</view>
			</view>
			<view class="info">
				<view @tap="onDisplay" class="info-item">每月打卡</view>
				<view class="info-item" @tap="toConfig">设置</view>
				<view class="info-item" @tap="setMode_">
					<text v-if="mode_ === 'day'">夜间模式</text>
					<text v-else>白天模式</text>
				</view>
				<view class="info-item" @tap="onDeveloperClick">关于我们</view>
				<van-calendar :show="show" confirm-text="打卡" readonly @close="onClose" @confirm="onConfirm"
					:dakaDate="dakaDays" />
			</view>
		</view>
	</view>
</template>

<script>
	// import ecCanvas from '../../component/ec-canvas/ec-canvas';

	import avatar from '@/component/avatar/index';
	import Notify from '@/static/miniprogram_npm/@vant/weapp/notify/notify.js';
	import * as echarts from '@/static/ec-canvas/echarts';
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../store/store';

	function initChart(canvas, width, height, dpr) {
		const chart = echarts.init(canvas, null, {
			width: width,
			height: height,
			devicePixelRatio: dpr // new
		});
		canvas.setChart(chart);
		var option = {
			title: {
				text: '本周学习曲线',
				left: 'center'
			},
			xAxis: {
				type: 'category',
				boundaryGap: false,
				data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] // show: false
			},
			yAxis: {
				x: 'center',
				type: 'value',
				splitLine: {
					lineStyle: {
						type: 'dashed'
					}
				} // show: false
			},
			series: [{
				name: 'A',
				type: 'line',
				smooth: true,
				data: [18, 36, 65, 30, 78, 40, 33]
			}]
		};
		chart.setOption(option);
		return chart;
	}

	export default {
		components: {
			// ecCanvas,
			avatar,
		},
		data() {
			return {
				ec: {
					onInit: initChart
				},

				show: false,
				date: 0,
				dakaDays: [],
				isAuth: false,

				userInfo: {
					avatarUrl: '',
					nickName: ''
				},

				qingchu: false,
				mode_: '',
				undefined: ''
			};
		},
		onShareAppMessage: function(res) {
			return {
				title: 'ECharts 可以在微信小程序中使用啦！',
				path: '/pages/index/index',
				success: function() {},
				fail: function() {}
			};
		},
		onLoad() {
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['userInfo', 'mode'],
				actions: ['setUserInfo', 'setMode', 'getMode']
			}); // wx.setStorageSync('daka', [])

			this.setData({
				mode_: this.getMode()
			}); // console.log(this.data.mode_)

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
				// console.log("本月第一次打开该页面")
				uni.setStorageSync('daka', []);
				dakaDays = [];
				uni.setStorageSync('toMonth', toMonth_);
			}

			this.setData({
				dakaDays
			}); // console.log("here", this.data.dakaDays)
		},
		onShow() {},
		onReady() {},
		onUnload() {
			this.storeBindings.destroyStoreBindings();
		},
		methods: {
			onDeveloperClick() {
				uni.navigateTo({
					url: '../../page-myinfo/pages/aboutAs/index'
				});
			},

			toConfig() {
				uni.navigateTo({
					url: '../../page-myinfo/pages/config/index'
				});
			},

			onGetUserInfo() {
				uni.getUserProfile({
					desc: '获取您的头像',
					success: (res) => {
						this.setData({
							userInfo: res.userInfo
						});
						console.log(this.userInfo.avatarUrl);
						this.setUserInfo(res.userInfo);
					},
					fail: (err) => {
						console.log(err);
					}
				});
			},

			onDisplay() {
				this.setData({
					show: true
				});
			},

			onClose() {
				this.setData({
					show: false
				});
			},

			onConfirm(event) {
				// console.log(this.data.dakaDays)
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

			formatDate(date) {
				date = new Date(date);
				return date.getDate();
			},

			setMode_() {
				const nextMode = this.mode_ === 'day' ? 'night' : 'day';
				this.setMode(nextMode);
				this.setData({
					mode_: nextMode
				});
				Notify({
					type: 'success',
					message: `模式已切换至:${this.mode_ === 'day' ? '白天' : '夜间'}`,
					duration: 800
				});
			}
		}
	};
</script>
<style>
	.header {
		margin-top: 50rpx;
		background-color: white;
		width: 100%;
		height: 100rpx;
		display: flex;
		background: rgba(0, 0, 0, 0);
		justify-content: center;
		align-items: center;
		text-align: center;
	}

	.header-img {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
	}

	/**index.wxss**/
	ec-canvas {
		width: 100%;
		height: 500rpx;
	}

	.text {
		position: absolute;
		bottom: 20rpx;
	}

	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-between;
		box-sizing: border-box;
		position: relative;
		margin-top: 50rpx;
	}

	.info {
		margin-top: 60rpx;
		border-radius: 30rpx;
		display: flex;
		justify-content: space-around;
		flex: 1;
		flex-wrap: wrap;
		/* align-items: center; */
		align-content: flex-start;
	}

	.info-item {
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.logo {
		width: 35%;
		position: fixed;
		right: 10%;
		bottom: 5%;
	}

	.myinfo {
		display: flex;
		flex-direction: column;
		height: 100vh;
		transition: all 0.5s;
	}

	.night {
		background-color: black !important;
		color: #fff !important;
		filter: brightness(0.7);
	}
</style>
