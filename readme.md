#网关系统

##原因
我们的框架使用的dubbox(这个不用再多描述了），前端h5调用后端服务，是通过dubbox的rest服务提供。为了解决调用的统一，所以需要一个网关系统。



##功能设计

最初根据个人理解构建一个版本，但是设计的太复杂，并且关于网关与后端服务的心跳检测的功能没有。本次重构设计方案参考spring-cloud的zuul的设计思路。

本次设计的思路，采用前正后反式的思路。

###路由规则

	很简单：前端请求的url,去掉网关的contextpath后对应的url地址即为相应的后端服务的url；
	对于后端服务的url，分两部：一部分为上下文contextpath;一部分为请求路径；
	通过监听注册中心zk,可以获取所有提供rest服务的所有服务列表，根据服务的contextpath进行分组，然后进行路由请求。


###后端服务列表获取

	后端服务列表，通过监听注册中心zk，然后解析出相应的服务列表。当注册中心中注入的服务地址发生变化，则重新再次解析；
	zk帮我们完成后端服务列表运态变化以及心跳检测；

###示例

	前端请求url:http://localhost:8080/gateway/restapi/test
	则网关的contextpath=gateway
	后端服务的contextpath=restapi
	根据后端服务的contextpath得到一组服务器列表，选其一，比如http://localhost:10000/,则后端的服务的完整api服务地址为：
	http://localhost:10000/restapi/test
	然后调用该接口获取相应的响应，返回前端即可




