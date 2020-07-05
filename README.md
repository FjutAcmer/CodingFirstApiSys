# 一码当先 | CodingFirst 在线评测系统 Web服务端

## 介绍

一码当先 在线编程教育系统Web服务端（Spring boot版），
在SSM框架搭建的的FjutOnlineJudge 3.0版本上进行了重构

## 软件架构

1. 项目采用JAVA语言编写，版本为JDK1.8；
2. 项目采用Springboot + Mybatis + TKMybatis框架，集成了Swagger, Druid等管理工具；
3. 关系型数据库采用MySQL 8.0.19；
4. 键值数据库采用Redis 3.2.100；
5. Web客户端是采用Vue框架搭建的SPA应用；
6. 评测机是采用容器技术搭建的独立应用。

## 开发教程

1. 下载并安装Idea，配置好开发环境，安装相关的插件；
2. 下载并安装maven (不是必要，如果正确安装Idea会自动配置)；
3. 下载并安装MySQL 8.0.15和MySQL可视化工具，项目运行时保持MySQL链接正常；
4. 运行根目录下sql文件夹内的CodingFirstDBInit.sql文件，创建数据库。
5. 下载并安装Redis 3.2.100，项目运行时保持Redis Server开启；
6. 在 src/main/resources/ 下的application.yml，application-dev.yml文件中修改相关字段保证开发环境的配置正确；
7. 运行 CodingFirstApplication.java。同时系统会自动建立配置文件中定义的所必需的文件夹；
8. 点击控制台中的输出或者在浏览器中输入 http://localhost:[配置文件中的端口]/[配置文件中的项目名]/swagger-ui.html
进入在线接口文档；
9. 点击控制台中的输出或者在浏览器中输入 http://localhost:[配置文件中的端口]/[配置文件中的项目名]/druid/index.html，并输入用户名密码后，进入Druid管理界面；

## 部署教程

1. 在 src/main/resources 文件夹及其子文件夹下寻找文件名包含“-dev”的文件，并根据其内容新增对应的“-prod”文件，并在其中修改生产环境配置；
2. 在项目根目录下运行maven命令，以生产环境打包为jar文件：
 ```
 mvn package -P prod
 ```

3. 在 [项目根目录]/target/文件夹下找到文件 [pom.xml中的打包项目名].jar；
4. 使用JDK1.8或以上版本的JAVA运行该jar，即可部署应用；
5. 文件 [pom.xml中的打包项目名].jar 同级目录下会生成logs文件夹存放日志。

## 参与贡献

1. Fork 本仓库，建立自己的分仓库；
2. 新建分支，编写自己的代码；
3. 提交代码到自己的分仓库中；
4. 新建 Pull Request，提请提交到主仓库中；
5. 主仓库审核完毕，即可并入主仓库主线；
6. 主仓库审核失败，退回。

## 鸣谢
JetBrains 对本开源项目提供了免费激活码，在此表示感谢

![Logo](http://qiniu.fjutcoder.com/icon-intellij-idea.png-pic_resize)

[进入JetBrains官网](https://www.jetbrains.com/?from=CodingFirstOnlineJudgeSystem)