<template>
	<view class="thoughts">
		<view class="notify">
			<van-notify id="van-notify" />
		</view>
		<!-- <van-field model:value="{{ title }}" type="textarea" placeholder="请输入标题" wx:if="{{type===1}}"/> -->
		<van-dialog id="van-dialog" />
		<view class="clear" v-if="message.length" @tap="clear">×</view>
		<view class="input" @tap="onFocus">
			<van-field v-model="message" type="textarea" placeholder="请输入内容" autosize
				custom-style="min-height:100%;overflow:scroll" :focus="isFocus" />
		</view>
		<view class="choose">
			<van-button slot="button" size="small" type="primary" @tap.native="quit">退出</van-button>
			<van-button slot="button" size="small" type="primary" @tap.native="keepAlive">保存草稿</van-button>
			<van-button slot="button" size="small" type="primary" @tap.native="post">确认发表</van-button>
		</view>
	</view>
</template>

<script>
	// import vanField from '@vant/weapp/field/index';
	// import vanButton from '@vant/weapp/button/index';
	// import vanNotify from '@vant/weapp/notify/index';
	// import vanDialog from '@vant/weapp/dialog/index';
	// pages/thoughts/index.js
	import getUserInfo from '../../../utils/getUser';
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../../store/store';
	import Notify from '@/static/miniprogram_npm/@vant/weapp/notify/notify';
	import Dialog from '@/static/miniprogram_npm/@vant/weapp/dialog/dialog';
	import {
		postIdea
	} from '../../../service/idea.service';
	export default {
		components: {},
		data() {
			return {
				isFocus: false,
				message: '',
				type: 0,
				userInfo: {},
				openID: ''
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			const openID = options.openID;
			const type = options.type;
			console.log('tyoe', type);
			this.setData({
				type,
				openID
			});

			if (type == 0) {
				this.storeBindings = createStoreBindings(this, {
					store,
					field: ['userInfo'],
					actions: ['getUserInformation', 'setUserInfo']
				});
				const userInfo = this.getUserInformation();

				if (!userInfo.nickName) {
					const that = this;
					getUserInfo(that);
				} else {
					this.setData({
						userInfo
					});
				}
			}

			let a = uni.getStorageSync('thoughts');

			if (a.length) {
				Notify({
					message: '已自动填充上一次的草稿',
					color: '#ad0000',
					background: '#ffe1e1',
					duration: 900
				});
				this.setData({
					message: a
				});
			}
		},
		methods: {
			onFocus() {
				let isFocus = this.isFocus;
				isFocus = !isFocus;
				this.setData({
					isFocus
				});
			},

			onUnLoad() {
				this.storeBindings.destroyStoreBindings();
			},

			keepAlive() {
				const message = this.message;
				uni.setStorageSync('thoughts', message);
				Notify({
					message: '草稿保存成功',
					color: '#ad0000',
					type: 'success',
					duration: 800
				});
			},

			clear() {
				this.setData({
					message: ''
				});
			},

			post() {
				const message = this.message;
				Dialog.confirm({
						title: '确认发表',
						message: '确认内容无误'
					})
					.then(() => {
						if (message.trim().length == 0) {
							Notify({
								message: '标题或内容不能为空！',
								type: 'danger',
								duration: 800
							});
							return;
						}

						if (!this.userInfo.nickName && this.type == 0) {
							const that = this;
							getUserInfo(that);
							return;
						} //发起网络请求

						this.newPost();
					})
					.catch(() => {
						// on cancel
					});
			},

			async newPost() {
				const type = this.type;
				const openID = this.openID; // 0 :动态   1:感想

				const url = type == 0 ? '/user/pub_dynamic' : '/user/pub_secret';
				let res;

				if (type == 0) {
					res = await postIdea(url, {
						content: this.message,
						image: this.userInfo.avatarUrl,
						name: this.userInfo.nickName,
						openId: openID
					});
				} else {
					res = await postIdea(url, {
						content: this.message,
						openId: openID
					});
				}

				console.log(res);

				if (res.data.message == 'success') {
					Notify({
						message: '发布成功',
						type: 'success',
						duration: 800
					});
				}
			},

			quit() {
				Dialog.confirm({
						title: '确认返回',
						message: '系统将不会自动保存草稿'
					})
					.then(() => {
						// on confirm
						uni.navigateBack();
					})
					.catch(() => {
						// on cancel
					});
			}
		}
	};
</script>
<style>
	.choose {
		display: flex;
		justify-content: space-around;
		align-items: center;
		height: 175rpx;
	}

	.input {
		flex: 1;
		overflow: scroll;
	}

	.thoughts {
		display: flex;
		height: 100vh;
		flex-direction: column;
		position: relative;
	}

	.notify {
		position: absolute;
		top: 0;
	}

	.clear {
		border-radius: 999rpx;
		width: 70rpx;
		height: 70rpx;
		position: absolute;
		bottom: 180rpx;
		right: 10rpx;
		z-index: 10;
		background-color: rgb(211, 209, 209);
		text-align: center;
		line-height: 70rpx;
		font-size: 50rpx;
	}
</style>
