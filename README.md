Android Apps 是什么?
====================
H5 APP 框架应用 ( 别名：小程序 ) ，提供JS API 供开发者使用，开发者可以做出流畅的H5 APP，实现即用即开，无需要额外安装


### 测试JS API
* apps.setNavigatorTitle(); // 设置状态栏标题
* apps.setNavigatorBarColor(); // 设置状态栏颜色
* apps.load(); // 加载 URL
* apps.vibrate(); // 震动
* apps.fullscreen( [ boolean off = false ] ); // 全屏

### 有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮箱 boss#haowei.me

### 捐助开发者
这是一个`开源项目`，你可以做非商业用途，你可以参与编写，你也可以捐助此项目。


### HTML代码
```javascript

const apps = {

	fn: null,

	addEventListener: function(type, fn) {
		if (!this.fn) this.fn = {};
		if (!this.fn[type]) this.fn[type] = [];
		this.fn[type].push(fn);
	},

	dispatchEvent: function(event) {
		if (!this.fn[event.type]) return;
		var listener = this.fn[event.type];
		for (var i = 0; i < listener.length; i++) {
			if (typeof listener[i] == "function") listener[i].call(this, event);
		}
	}

};

```
