# ShortVideoAssistant
短视频辅助器，语音控制手机刷抖音，运用到百度语音识别的语音唤醒和安卓的无障碍服务，懒人神器

解决现实生活中吃零食看抖音弄脏手的问题

抖音视频演示：

https://v.douyin.com/rbSn7sv/


**仅供学习交流,请勿它用**  **仅供学习交流,请勿它用**  **仅供学习交流,请勿它用**

开发环境：
Android Studio

minSdk 26
targetSdk 32


运用到**百度语音识别的语音唤醒**和**安卓的无障碍服务**

--------------------

**如果你是下载运行打包的话，请看这里（前提是你懂得使用Android Studio，会基本的开发操作）**

--------------------------


![image](https://user-images.githubusercontent.com/57706599/194303365-231ef422-5d52-4fbc-b093-d3ce47acb0ee.png)

因为需要授权ID和密钥授权，且SDK包已在本项目源码里面，所以直接说明如何获取百度语音的AppID,  API Key,  Secret Key，



访问此网站，

https://login.bce.baidu.com/?account=

用百度账号登录，然后选择**语音能力引擎**


![image](https://user-images.githubusercontent.com/57706599/194304172-50ad3c7b-134a-43bf-8499-d1b318452cf3.png)

-----------------------

点击免费尝鲜的**去领取**

![image](https://user-images.githubusercontent.com/57706599/194304725-5e5b9dd9-6cc4-4f86-950a-33d1cc7bc734.png)

如图勾选中想要的功能，点**0元领取**

（语音唤醒唤醒貌似不会限制使用次数，不过有使用时间限制吧，要是过了很久不能用了，可以回来重新配置）

![image](https://user-images.githubusercontent.com/57706599/194305449-d636bd54-3ad7-4bcc-8ba8-0bb892aab10e.png)

返回到原来的页面，点击**创建应用**

![image](https://user-images.githubusercontent.com/57706599/194306539-7b47aade-f532-4e5f-b00d-a87142fccbe6.png)


填写创建应用的信息，应用名称：**短视频辅助器**，语音包名：**com.bunny.shortvideoassist**，应用归属：**个人**，应用描述：随便写写，

（名字什么的你不喜欢的话，你懂安卓开发的话，可以改名字还有包名，若不知道，还是用我的包名得了，包名记得对应项目的包名喔）

然后**立即创建**

![image](https://user-images.githubusercontent.com/57706599/194306018-1191bd86-6063-4aa0-803c-b8caed900a25.png)

在应用列表，查看AppID,  API Key,  Secret Key,复制好后面用到

![image](https://user-images.githubusercontent.com/57706599/194306907-723c28c1-6af9-449f-962b-56a14d7ca9a5.png)

在获取到百度语音的AppID,  API Key,  Secret Key后,下载本项目的源码，解压后，用Android Studio打开项目,填入信息到下图中core模块->AndroidManifest.xml->三个meta-data的value值

![image](https://user-images.githubusercontent.com/57706599/194301124-62e279ce-9126-44ec-824d-da502e0f337f.png)

后面运行看是否有错误，若无就可以打包了

如果想改唤醒词，请看这里（不过百度语音唤醒可自定义性低）

https://ai.baidu.com/tech/speech/wake#tech-demo


![image](https://user-images.githubusercontent.com/57706599/194304500-b325ede0-c772-403a-a81c-414bf4649f9c.png)

完成修改后的唤醒词文件下载后导入到这里，替换WakeUp.bin文件即可。

-------------------------------------



1.运用到百度语音识别的语音唤醒

详细请看百度AI官网：https://ai.baidu.com/tech/speech/wake

技术文档：https://ai.baidu.com/ai-doc/SPEECH/Vk38lyr75

具体如何接入该功能，请查看百度官方的接入手册


2.安卓的无障碍服务

实现刷手机的常用手势，点击，双击，向下滑，向上滑


**效果动图展示**

![Video_20220914_012331_818](https://user-images.githubusercontent.com/57706599/190066354-7bcb078e-4eba-4666-b25c-0112a715b9b6.gif)


![IMG_20220914_133015](https://user-images.githubusercontent.com/57706599/190067394-1d4a4a67-a9c2-4d20-86d7-94f3ee8c4502.jpg)


