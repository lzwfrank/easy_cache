#EASY_CACHE
****
|作者|lzwfrank|
|---|---
|邮箱|835274763@qq.com
****
##前言  
  偷个懒没上传maven,有兴趣的同学可以下到本地打包上传  
##项目背景  
  针对传统软件行业，部署的redis是单节点 哨兵还是cluster都是根据项目情况而变化了。而目前常用的redis连接工具，不同的redis模式都意味着代码层面的变动。所以最初的目的就是提供一个标准的redis封装,使用者不用关注底层的redis
部署方式的变化,此工具通过redis配置的变化进行自动适配。
  之后也是参考JetCache的部分设计思路，实现了基于cglib代理和基于AOP实现的注解方式的缓存  

##使用说明  
#配置说明  
由于默认是在springboot环境下使用，所以在设计之初，配置信息就被认为必须是在application.properties中  
 
| 配置名 | 参考值  | 描述 |
| :------------ |:------------| :------------|
| spring.redis.masterName      | mymaster | 哨兵模式的masterName（非必传，当配置此选项时，则默认开始哨兵模式连接） |
| spring.redis.nodes      | localhost:6379        |   redis连接信息（必传） |
| spring.redis.pwd | admin        |    redis连接密码 (非必传) |






