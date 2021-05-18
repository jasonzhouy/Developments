# jobinfo 版本V1.0.0

技术栈：

  Python爬虫、正则表达式、ElasticSearch、Vue、Echarts、少量Linux（可选）。
  
  
  
数据流向：


  Python获取数据->简单处理&过滤->ElasticSearch->聚合->图标展示
  
![Image text](https://github.com/jasonzhouy/jobinfo/raw/master/exampleImage/1.png)

![Image text](https://github.com/jasonzhouy/jobinfo/raw/master/exampleImage/2.png)

![Image text](https://github.com/jasonzhouy/jobinfo/raw/master/exampleImage/3.png)

![Image text](https://github.com/jasonzhouy/jobinfo/raw/master/exampleImage/4.png)
  
Python版本:Python3
使用Python爬虫获取Boss直聘互联网相关的职位信息。
爬虫相关：
  1. 爬取数据归档保存本地
  2. 爬取信息日志记录
  3. 验证信息反爬手段（2/3）
注:
  1.此处没有使用https代理池，因为Boss直聘在使用代理池访问时，会反向嗅探ip来源，对于服务器他可能有常用端口检测，直接会触发反爬的验证识别。
  2.尽量使用本机爬，如果使用服务器爬取数据，尽量关闭不用端口，增加掩蔽手段，避免被反嗅探。
  


添加Linux Cron可以设置定时爬取任务,指令:
cron -e 
后台任务指令:
nohup python3 Boss.py &




Vue + ElementUI：
作为数据的前端展示以及渲染:
  1. 添加Echarts作为图表的渲染包
  2.添加axios作为数据交互手段
 
 
 
 
ElasticSearch版本:ElasticSearch 7.2.0
ElasticSearch:
1.表结构的有些索引项使用的是iksmart
（注：如果可以，尽量自己重写分词器以及分词过滤器，ik对于中文特定领域的词义分析支持一般
    比如： JAVA开发工程师 ik很有可能会分成J、A、V、A、开发工程师
    再比如： Java开发工程师 ik也许会分成 J、a、v、a、Java、开发工程师）
 2. 建议使用kibana来作为开发过程中的ES浏览/查询工具。非常强大
 
 
 
 
 
 本项目适用于：
    1.愿意对全文搜索引擎ES有一点了解的学生、新手，作为了解ES的入门项目再合适不过了 
    2.对于数据获取、过滤、存储、展示全栈流程愿意熟悉的大数据/Web开发工程师
    3.想熟悉Vue常用第三方插件整合的新手程序猿们
 总而言之，这是一个适合愿意对ES、Vue、Python有一点了解的工程师/学生们的项目。
 
 
 
 
 
 接下来的更新计划：
  在V1.X.X的版本里，会进行具体工作信息的展示&公司信息的展示
  在V2.0.0后的版本，会重写分词器，优化ElasticSearch结构，优化项目架构。（也许会把展示时的数据来源交给Node.js搭建的后台服务）
  ......
  
  
 
 如果有任何建议，可以给我发邮件嗷~非常欢迎
 
 更于2019-7-22



 
