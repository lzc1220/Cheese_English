<template>
	<view>
		<image src="/static/assets/img/swiper/B6F947C3A70824727639B21A78C9B1E5.jpg" class="solo-top-bg"
			mode="aspectFill"></image>
		<view class="solo">
			<view class="solo-top">
				<view class="top-avatar l">
					<avatar></avatar>
					<view class="readyStatus-item l" v-if="isPlayer && (!isReady || !isReady_fr)">
						{{ isReady ? '√' : '×' }}
					</view>
				</view>
				<view class="top-avatar c">
					<avatar avatarUrl="../../assets/img/vs.jpg"></avatar>
					<view><text :decode="true">&nbsp;</text></view>
				</view>
				<view class="top-avatar r">
					<avatar></avatar>
					<view class="readyStatus-item r" v-if="isPlayer && (!isReady || !isReady_fr)">
						{{ isReady_fr ? '√' : '×' }}
					</view>
				</view>
			</view>
			<view class="solo-noplayer" v-if="!isPlayer">
				<view class="title">需要好友加入才能开始游戏哦</view>
				<view class="title">点击按钮,转发链接给好友,开始游戏</view>
				<view class="share-btn"><button class="button" open-type="share">转发链接</button></view>
			</view>
			<view class="wait-for-ready" v-if="isPlayer && (!isReady || !isReady_fr)">
				<view>等待双方玩家准备</view>
				<van-button color="linear-gradient(to right, #4bb0ff, #6149f6)" round @tap.native="getReady">
					<text v-if="!isReady" :decode="true">准备&nbsp;</text>
					<text v-if="isReady">已准备</text>
				</van-button>
			</view>
			<view class="solo-player" v-if="isPlayer && isReady && isReady_fr">
				<view class="solo-title">{{ game[gameMode] }}</view>
				<view class="time" :animation="animationData">
					{{ leftTime }}
				</view>
				<view class="scroll">
					<scroll-view scroll-y class="scroll_">
						<view class="content">
							<list-item :content="item.word" height="60rpx" fontSize="40rpx" bgColor="#e6f8f7"
								v-for="(item, index) in answerList" :key="index"></list-item>
						</view>
					</scroll-view>
				</view>

				<van-search left-icon="none" :placeholder="doing ? '在这里输入吧~' : '它必须干点什么...'" class="van-search"
					@search="onSearch" :value="inputValue" :disabled="!doing"></van-search>
			</view>
		</view>
		<van-overlay :show="showQuit">
			<view class="wrapper">
				<view class="quit-card">
					<view class="quit-card-title">游戏结束啦，XX胜利</view>
					<view class="quit-card-goback" @tap="goback">回到首页</view>
					<view class="chacha" @tap="chacha">×</view>
				</view>
			</view>
		</van-overlay>
	</view>
</template>

<script>
	import avatar from '../../../component/avatar/index';
	// import vanSearch from '@/miniprogram_npm/@vant/weapp/search/index';
	import listItem from '../../../component/list-item/index';
	// import vanOverlay from '@/miniprogram_npm/@vant/weapp/overlay/index';
	// import vanButton from '@/miniprogram_npm/@vant/weapp/button/index';
	// pages/solo1/index.js
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from '../../../store/store';
	export default {
		components: {
			avatar,
			// vanSearch,
			listItem,
			// vanOverlay,
			// vanButton
		},
		data() {
			return {
				isPlayer: true,
				leftTime: 7,
				animationData: {},
				answerList: [],
				inputValue: '',
				timer: -1,
				game: ['形容词词缀:ful', '名词词缀:ment', '否定前缀:un', '否定前缀:dis', '表示预先，提前:pre', '形容词词缀:able'],
				gameMode: 0,
				showQuit: false,
				doing: true,
				isReady: false,
				isReady_fr: false
			};
		},
		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad(options) {
			this.setData({
				gameMode: parseInt(Math.random() * this.game.length)
			});
			console.log(this.game[this.gameMode]);
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['worker'],
				actions: ['setWorker']
			});
			console.log(this.worker);
		},
		onUnload() {
			this.storeBindings.destroyStoreBindings();
		},
		/**
		 * 用户点击右上角分享
		 */
		onShareAppMessage: function(res) {
			var that = this; //如果用户是点击按钮进行分享的

			if (res.from == 'button') {
				return {
					title: '进，懂？',
					//分享出去的标题
					imageUrl: '/static/assets/img/swiper/1.jpg',
					//分享时显示的图片
					path: 'pages/solo/index?player=1' //别人点击链接进来的页面及传递的参数
				};
			} else {}
		},
		onShow(options) {
			// this.startAnimation()
		},
		methods: {
			startAnimation() {
				if (this.timer != -1) {
					clearInterval(this.timer);
					this.setData({
						timer: -1
					});
				}

				var animation = uni.createAnimation({
					duration: 1000,
					timingFunction: 'ease'
				});
				const timer = setInterval(() => {
					animation.scale(2, 2).step({
						duration: 200
					});
					animation.scale(1, 1).step();
					let leftTime = this.leftTime;

					if (leftTime == 0) {
						clearInterval(timer);
						this.endOfGame();
						this.setData({
							timer: -1
						});
						return;
					}

					leftTime -= 1;
					this.setData({
						leftTime,
						animationData: animation.export()
					});
				}, 1000);
				this.setData({
					timer
				});
			},

			onSearch(e) {
				const answerList = this.answerList;
				const answer = {};
				answer.side = 0;
				answer.word = e.detail; //向服务器请求，验证答案是否正确

				this.judge(answer.word);
				answerList.push(answer);
				this.setData({
					answerList,
					inputValue: ''
				});
			},

			judge(word) {
				const reg = /:(w+)^/i;
				this.game[this.gameMode].match(/:(\w+)$/g);
				const gameRule = RegExp.$1;
				const res = word.includes(gameRule);

				if (res === true) {
					console.log('PASS'); //给后端

					this.setData({
						leftTime: 7
					});
					this.startAnimation();
				}
			},

			endOfGame() {
				this.setData({
					showQuit: true,
					doing: false
				});
			},

			chacha() {
				this.setData({
					showQuit: false
				});
			},

			goback() {
				console.log('run!');
				uni.reLaunch({
					url: '../home/index'
				});
			},

			getReady() {
				//发起网络请求，本玩家准备了
				this.setData({
					isReady: true
				});
			}
		}
	};
</script>
<style>
	.solo-top-bg {
		width: 100vw;
		height: 100vh;
		position: fixed;
		z-index: -1;
		filter: blur(4px);
	}

	.solo {
		color: white;
		display: flex;
		flex-direction: column;
		justify-content: center;
		height: 100vh;
	}

	.solo-top {
		position: relative;
		height: 190rpx;
	}

	.readyStatus-item {
		font-size: large;
	}

	.top-avatar {
		position: absolute;
		top: 50%;
		display: flex;
		flex-direction: column;
		height: 100%;
		align-items: center;
		justify-content: flex-end;
		gap: 30rpx;
	}

	.l {
		left: 17%;
		translate: 0 -50%;
	}

	.c {
		left: 50%;
		translate: -50% -50%;
	}

	.r {
		right: 17%;
		translate: 0 -50%;
	}

	.title {
		font-size: larger;
		text-align: center;
		margin-top: 20px;
	}

	.solo-title {
		font-size: larger;
		text-align: center;
	}

	.button {
		padding: 0;
		margin: 0;
		width: 10rem;
		margin: 0 auto;
		border: 3rpx solid rgb(223, 248, 248);
		box-shadow: 2px 2px rgb(185, 219, 218);
		outline: none;
	}

	.share-btn {
		display: flex;
		justify-content: center;
		align-items: center;
		flex: 1;
	}

	.solo-noplayer {
		flex: 1;
		display: flex;
		flex-direction: column;
	}

	.solo-player {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		overflow: auto;
	}

	.van-search {
		bottom: 2rpx;
		width: 100%;
		border-radius: 50px;
	}

	.time {
		font-size: 70rpx;
		text-align: center;
		color: rgb(236, 218, 178);
		overflow: visible;
		padding-top: 20rpx;
	}

	.content {
		display: flex;
		flex-wrap: wrap;
		font-size: 26rpx;
	}

	.scroll {
		flex: 1;
		overflow: hidden;
	}

	.scroll_ {
		height: 100%;
		max-height: 100%;
	}

	.wrapper {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	.quit-card {
		background-color: green;
		width: 500rpx;
		height: 400rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 10%;
		position: relative;
		border-radius: 38rpx;
	}

	.chacha {
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 40px;
		border-radius: 999px;
		width: 40px;
		height: 40px;
		background-color: pink;
		position: absolute;
		top: -10px;
		right: -10px;
	}

	.quit-card-title {
		font-size: 1rem;
		border-radius: 30rpx;
		padding: 10rpx 15rpx;
	}

	.quit-card-goback {
		border-radius: 30rpx;
		padding: 10rpx 15rpx;
		display: inline-block;
		background-color: rgb(224, 159, 159);
	}

	.wait-for-ready {
		flex: 1;
	}

	.wait-for-ready {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
	}
</style>
