# ThisMoment短视频分享App+管理系统 版本V1.5.0


这是This Moment短视频分享小程序的开发源码，其中包括了：

    基于wxml +  Java Web的小程序的App端代码
    &
    基于vue.js + Element UI + Java Web的短视频后台管理系统端的源代码
    
    App后台技术栈包括：
    
      zookeeper
      pagehelper
      redis
      ffmpeg
      ssm
      Spring Boot
      swagger2
      mysql
      activeMQ
      javaMail
      ...
    
    后台管理系统包括:
      echarts
      axios
      ...
      
 
系统架构于三台服务器之上：小程序App端的代码运行于腾讯云，小程序后台以及后台管理系统运行于阿里云上，数据存储则放在了腾讯云实例上。

redis做无状态session保存（后台验证用户登录状态）
mysql存储业务数据
使用zookeeper作为文件更新的通知机制（消息中心）
ffmpeg（Linux Shell调用 负责视频音频流的合成&截图的生成）
activeMQ完成和JavaMail系统之间的异步消息传递
SpirngBoot用来自动配置所用到的web框架
......




App欢迎页+首页：

![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/welcome.jpg)
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/index.jpg)

登录页面:
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/login.jpg)

小程序管理页面:
数据展示:
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/data.jpg)
首页:
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/admin1.jpg)
举报处理页面:
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/report.jpg)
邮件相关:
![Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/mail1.jpg)
[Image text](https://github.com/jasonzhouy/Videos-Development/raw/master/example/mail2.jpg)





    
   
    

