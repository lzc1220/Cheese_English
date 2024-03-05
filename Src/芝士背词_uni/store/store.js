import {
	observable,
	action
} from '@/static/miniprogram_npm/mobx-miniprogram'; // 数据仓库

export const store = observable({
	userInfo: {},
	worker: {},
	openID: '',
	mode: 'day',
	setOpenID: action(function(openID) {
		this.openID = openID;
	}),
	setUserInfo: action(function(userInfo) {
		this.userInfo = userInfo;
	}),
	setWorker: action(function(worker) {
		this.worker = worker;
	}),
	setMode: action(function(mode) {
		this.mode = mode;
		console.log(this.mode);
	}),
	getMode: action(function() {
		return this.mode;
	}),
	getUserInformation: action(function() {
		return this.userInfo;
	}),
	getOpenID: action(function() {
		return this.openID;
	})
});
