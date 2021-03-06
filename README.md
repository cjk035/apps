Android Apps 是什么?
====================
H5 APP 框架应用 ( 别名：小程序 ) ，提供JS API 供开发者使用，开发者可以做出流畅的H5 APP，实现即用即开，无需要额外安装

![演示界面](https://github.com/haowei4032/apps/raw/master/preview/1797827030.jpg)
![演示界面](https://github.com/haowei4032/apps/raw/master/preview/728371208.jpg)
![演示界面](https://github.com/haowei4032/apps/raw/master/preview/664023026.jpg)

### 测试JS API
```java

void apps.setNavigatorTitle(String text); // 设置状态栏标题
apps.setNavigatorBarColor(string color); // 设置状态栏颜色
apps.open(string url); // 打开新页面
apps.vibrate(); // 震动
apps.fullScreen( boolean enable ); // 全屏
apps.startWifi(); // 开启wifi
apps.stopWifi(); // 关闭wifi
apps.showToast(string text); // 显示 tips 消息
apps.setHome( string url ) // 设置首页 【仅调试模式使用，非生产环境接口】
apps.navigatorBackEnable(boolean enable); // 开启、关闭后退按钮
apps.phoneCall(string number, boolean action); //打电话
apps.setCopyText(string text); //写入剪贴板
apps.getCopyText(); //读取剪贴板
apps.geoLocation(); //返回定位信息
apps.getWifiList(); //获取wifi列表
apps.sendNotificationText(string text); // 设置通知栏消息文本
apps.getDeviceID(); //获取device id
apps.startRecord(); //开启录音
apps.playRecord(); //播放录音
apps.getNetworkType(); //获取网络类型
apps.layoutFullscreen(); // 布局全屏
apps.setColorPrimary(string color); // 设置主颜色
```

### 有问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮箱 boss#haowei.me

### 捐助开发者
这是一个`开源项目`，你可以做非商业用途，你可以参与编写，你也可以捐助此项目。

![支付宝红包](https://github.com/haowei4032/apps/raw/master/preview/1776602611.jpg)

