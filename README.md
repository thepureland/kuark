#  <center> Kuark快速开发平台简介


## 1. 概述
&emsp;&emsp;Kuark是一个用kotlin语言实现的、主要以java生态主流技术作为技术栈的快速开发平台，内置丰富的工具、组件、服务，能够帮忙kotlin或java开发者快速、轻松地开发各类系统和功能。
既能用于开发简单小工具，又能用于开发复杂的大型互联网微服务应用。


## 2. 特点
- 开箱即用：源码下载完成，无须作任何配置与环境搭建，即可直接运行
- 按需搭配：可根据系统的复杂度，自由选择所需依赖的组件、服务、中间件
- 灵活自由：支持多种数据库、多种web容器、多种配置中心、多种缓存策略等等
- 高度开放：预留大量扩展点，方便二次开发


## 3. 总体架构
### 3.1 逻辑架构
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bdb664c1045848aba6aa8edbfff40341~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)
### 3.2 部署架构
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/aa8cccf19bed45a6bf59246e873081e2~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)
### 3.3 包说明


### 4. 关键特性
#### 4.1 透明的两级联动分布式缓存
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/eb4469652446453c9a9e86bea39af202~tplv-k3u1fbpfcp-zoom-in-crop-mark:1304:0:0:0.awebp)
#### 4.2 可拆可合的服务
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7d4bcf713ec34c108c7bd4a2b42bca08~tplv-k3u1fbpfcp-watermark.image)
#### 4.3 强大的Bean校验

#### 4.4 表单校验前后端统一规则


## 5. 按需搭配典型场景举例
### 5.1 最简单的应用，它不依赖数据库、中间件等，也不依赖于spring
#### 5.1.1 逻辑架构图
![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/118ae45de792452a8dd9e4ad36b14893~tplv-k3u1fbpfcp-watermark.image)
#### 5.1.2 部署架构图
![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ca2b61eb1d774b7f8c4e062c240e9a3c~tplv-k3u1fbpfcp-watermark.image)
### 5.2 简单的spring管理的应用，它不依赖数据库、中间件等
#### 5.2.1 逻辑架构图
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/06315873beff4c5fb1f864b0cc0ebf81~tplv-k3u1fbpfcp-watermark.image)
#### 5.2.2 部署架构图
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/20c153431d9b4913b1ad6c102fa6afce~tplv-k3u1fbpfcp-watermark.image)
### 5.3 需要缓存的应用
#### 5.3.1 逻辑架构图
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ac4074408b0a400a9039a384409b2664~tplv-k3u1fbpfcp-watermark.image)
#### 5.3.2 部署架构图
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9d65247a3ad94743af119c9dda29e625~tplv-k3u1fbpfcp-watermark.image)
### 5.4 基于关系型数据库的应用
#### 5.4.1 逻辑架构图
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/44306b988ea64b28b42c13a1cc17099e~tplv-k3u1fbpfcp-watermark.image)
#### 5.4.2 部署架构图
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2230fe98060745348654112c1e5155d1~tplv-k3u1fbpfcp-watermark.image)
### 5.5 web应用
#### 5.5.1 逻辑架构图
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a34d6baae78641e5bd9abe66a13c1572~tplv-k3u1fbpfcp-watermark.image)
#### 5.5.2 部署架构图
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/83ab15b11ca44df18497c7b7c0712ba9~tplv-k3u1fbpfcp-watermark.image)
### 5.6 基于本地用户服务的web应用
#### 5.6.1 逻辑架构图
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/927f3c0432924a0f8b883561570ff199~tplv-k3u1fbpfcp-watermark.image)
#### 5.6.2 部署架构图
![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bf2351f6f8254dbeb41df20c1e6dfa21~tplv-k3u1fbpfcp-watermark.image)

### 5.7 基于远程用户服务的web应用
#### 5.7.1 逻辑架构图
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/00e8c4ccc08741eda811f57c3a52a874~tplv-k3u1fbpfcp-watermark.image)
#### 5.7.2 部署架构图
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2d726571cee34a23948b06ba13cdca3c~tplv-k3u1fbpfcp-watermark.image)