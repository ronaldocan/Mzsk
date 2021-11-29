# LPWA
LPWA – Low power wide area,简称为低功耗广域技术，是一种功耗低却能实现远距离无线信号传输的一种技术，相对于我们比较熟悉的低功耗蓝牙（BLE）、wifi、zigbee等技术来说，LPWA的传输距离会更远，一般的传输距离都在公里级，其链接预算（link budget）可达160dBm，而BLE和zigbee等一般都在100dbm以下。和传统的蜂窝网络技术（2G、3G）相比来说，LPWA的功耗更低，电池供电设备的使用寿命也能达到数年之久。基于这两个显著的优点，LPWA能真正的助力和引领物联网（LOT）的革命。  

LPWAN – Low power wide area network，低功耗广域网络，也就是使用LPWA技术搭建起来的无线连接网络。LPWAN的网络连接形式可以多种多样，如下图所示，以数据上传（data uplink）为例，无线终端发送数据信号，基站或者是网关接受到数据后，把数据传输到云平台，云平台再根据设备ID把数据分发到对应的客户服务器。对于私有的LPWAN网络，云端和服务器可以是一体的。
 # COAP
COAP协议的设计目标就是在低功耗低速率的设备上实现物联网通信。coap和HTTP协议一样，采用URL标示需要发送的数据，在协议格式的设计上也基本是参考HTTP协议，非常容易理解。同时做了以下几点优化：
1. 采用UDP而不是TCP。这省去了TCP建立连接的成本及协议栈的开销。
2. 将数据包头部都采用二进制压缩，减小数据量以适应低网络速率场景。
3. 发送和接收数据可以异步进行，这样提升了设备响应速度。  

COAP协议就像一个针对物联网场景的http移植品，很多设计保留了HTTP协议的影子，拥有web背景的开发者也能快速上手。但是由于很多物联网设备隐藏在局域网内部，coap设备作为服务器无法被外部设备寻址，在ipv6没有普及之前，coap只能适用于局域网内部（如wifi）通信，这也很大限制了它的发展。

![image](https://user-images.githubusercontent.com/18349925/142965313-108e045b-0b3c-4854-ab7d-9945d20b685b.png)

# GraphQL
GraphQL 是一种可以用任何语言实现的规范。我们的代码页包含许多不同编程语言的一长串库来帮助解决这个问题
GraphQL 可以与IOT进行结合
https://medium.com/thundra/better-iot-with-graphql-and-appsync-c3617d5c02d0
