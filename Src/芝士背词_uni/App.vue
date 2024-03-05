<script>
	// app.js
	import {
		createStoreBindings
	} from '@/static/miniprogram_npm/mobx-miniprogram-bindings';
	import {
		store
	} from './store/store';
	import {
		getLoginCode,
		codeToToken
	} from './service/login.service';
	export default {
		data() {
			return {};
		},
		onLaunch() {
			this.globalData.login();
			this.globalData.cloudinit();
			this.storeBindings = createStoreBindings(this, {
				store,
				field: ['worker', 'openID'],
				actions: ['setWorker', 'setOpenID']
			});
			// const worker = this.globalData.newWorker();
			// worker.postMessage({
			// 	msg: 'hello worker'
			// });
			// this.setWorker(worker);
		},
		globalData: {
			cloudinit() {
				if (!wx.cloud) {
					console.error('请升级基础库版本');
				} else {
					wx.cloud.init({
						env: 'cloud1-8gx3rydc63cb4461',
						traceUser: true
					});
				}
			},

			login: async function() {
				try {
					const code = await getLoginCode();
					const res = await codeToToken(code);
					this.setOpenID(res.data.data.openid);
					console.log('hererererer', res.data.data.openid);
				} catch (error) {
					console.log(error);
				}
			},

			// newWorker() {
			// 	const worker = uni.createWorker('workers/request/index.js'); // 文件名指定 worker 的入口文件路径，绝对路径

			// 	return worker;
			// }
		}
	};
</script>
<style>
	/**app.wxss**/
	page {
		background-color: #fafafa;
		box-sizing: border-box;
		font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
	}

	.img {
		border-radius: 20rpx;
	}

	@media screen and (min-width: 320px) {
		page {
			font-size: 30rpx;
		}
	}
</style>
