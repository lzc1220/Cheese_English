<template>
	<view class="idea">
		<view class="idea-title">
			<view class="idea-title-text">{{ title }}</view>
			<slot name="top"></slot>
		</view>
		<idea-item :info="item" :type="type" @click="onClick" v-for="(item, index) in ideaList" :key="index">
		</idea-item>
		<slot name="bottom"></slot>
	</view>
</template>

<script>
	import ideaItem from '@/component/idea-item/index';
	// import vanIcon from './@vant/weapp/icon/index';
	// component/idea/index.js
	export default {
		components: {
			ideaItem,
			// vanIcon
		},
		data() {
			return {};
		},
		/**
		 * 组件的属性列表
		 */
		props: {
			title: {
				type: String,
				default: ''
			},
			ideaList: {
				type: Object
			},
			type: {
				type: Number
			}
		},
		options: {
			multipleSlots: true // 在组件定义时的选项中启用多 slot 支持
		},
		/**
		 * 组件的方法列表
		 */
		methods: {
			onClick(e) {
				this.$emit('click', {
					detail: e.detail
				});
			}
		}
	};
</script>
<style>
	.idea {
		display: flex;
		flex-direction: column;
		gap: 30rpx;
		margin: 10rpx auto;
		border-radius: 50rpx;
		border: 3rpx solid rgb(158, 158, 158);
		box-shadow: 2px 2px rgb(158, 158, 158);
		padding: 20rpx 0;
		width: 92%;
	}

	.idea-title {
		padding: 0 40rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.idea-title-text {
		font-size: 1.1rem;
	}
</style>
