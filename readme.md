#网关系统



##目的

我们服务框架使用的是dubbox(这个不用再多描述了），内部系统调用使用的是dubbo协议;而对于前端提供服务则使用rest协议。前端应用面临许多rest服务，为了解决调用的统一，所以需要一个网关系统。

##为什么自己写



- 对于api网关，现在已有框架可以解决该类问题，但是基于java语言开源的没有(除了zuul,也有可能我没有找到).
- 不会C++,无法扩展nginx
- 不会C,无法扩展nginx
- 不会LUA,无法扩展ngin



##功能设计

最初根据个人理解构建一个版本，但是设计的太复杂，并且关于网关与后端服务的心跳检测的功能没有。本次设计方案参考spring-cloud的zuul的设计思路。

##主要功能



- 前端请求的统一拦截；


- 负载均衡；


- 心跳检测
- 鉴权
- 参数校验
- 路由


##路由规则

很简单：前端请求的url,去掉网关的contextpath后对应的url地址即为相应的后端服务的url；
	对于后端服务的url，分两部：一部分为上下文contextpath;一部分为请求路径；
	通过监听注册中心zk,可以获取所有提供rest服务的服务器列表，根据服务的contextpath进行分组，然后进行路由请求。


##后端服务列表获取

后端服务列表，通过监听注册中心zk，然后解析出相应的服务列表。当注册中心中注入的服务地址发生变化，则重新再次解析；
	zk帮我们完成后端服务列表运态变化以及心跳检测；

##示例




1. 前端请求url:http://localhost:8080/gateway/restapi/test
	


- 则网关的contextpath=gateway
	


- 后端服务的contextpath=restapi


- 根据后端服务的contextpath=restapi得到一组服务器列表，选其一，比如http://localhost:10000/,
- 则后端的服务的完整api服务地址为：
http://localhost:10000/restapi/test



- 然后调用该接口获取相应的响应，返回前端即可



##技术栈



- springmvc
- spring
- servlet3异步
- httpclient 4.5.3
- zookeeper


- apache-chain


##扩展

对于该网关系统，它所使用的web容器仍为tomcat。在实际业务部署的时候，前面仍会采用nginx作为反向代理服务器，主要是由于nginx的单机高并发能力，以及后端服务的高可用检测;

如果采用这种结构部署，则该网关系统只能手动的进行扩缩（这个需要依赖于运维人员）。那么在系统需要紧急扩缩备战中，则会遇到问题。为了解决这类问题，通过扩展一个虚拟服务，将其注册到注册中心即可。具体请参考[https://github.com/zhuzhong/gateway-ngxcfg.git](https://github.com/zhuzhong/gateway-ngxcfg.git) gateway-ngxcfg

