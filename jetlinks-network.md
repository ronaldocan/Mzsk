# UDP、HTTP设备模拟接入
![企业微信截图_16448334605430](https://user-images.githubusercontent.com/18349925/154002341-8817bd2c-bc55-4b40-b54c-14bb6e6f435d.png)

## HTTP设备
1. [参考官方文档创建产品](http://doc.jetlinks.cn/best-practices/tcp-connection.html#%E5%88%9B%E5%BB%BA%E8%AE%BE%E5%A4%87)，ID为http_demo_v1
 ![37f87ae0b72e644461eafb142a67aec](https://user-images.githubusercontent.com/18349925/154005180-2d33f66c-3492-4064-8e08-691077992e17.png)  
2. 创建设备网关：
    - 选择 设备接入-->设备网关--> 点击新建按钮。
    ![image](https://user-images.githubusercontent.com/18349925/154004768-fa577b25-e96f-45eb-9af3-79b6fa9b83ff.png)
    - 在操作列点击启动按钮启动网关。
    ![image](https://user-images.githubusercontent.com/18349925/154005348-85c460c9-e66d-473b-9021-c7a6d2f639b5.png)
3. 推送消息
此处使用postman模拟设备请求。
![image](https://user-images.githubusercontent.com/18349925/154008725-2757156e-0a46-4cea-972d-fa04c4d7fd97.png)
模拟报文
```
{
    "deviceId":"http_demo_v2_test",
    "properties":{
        "param_1":"21212331",
        "p2":"22222333"
    }
}
```
## UDP设备
 1. [参考官方文档创建产品](http://doc.jetlinks.cn/best-practices/tcp-connection.html#%E5%88%9B%E5%BB%BA%E8%AE%BE%E5%A4%87)，ID为udp_test_product_v1
![a4444e47dc9239fd5ed413c0f6ddd7e](https://user-images.githubusercontent.com/18349925/154013346-bc79ce11-a026-4ad4-9a69-031a6470f185.png)  
2. 创建设备网关：
    - 选择 设备接入-->设备网关--> 点击新建按钮。
    ![image](https://user-images.githubusercontent.com/18349925/154013425-fe8fb9ea-d96e-479f-a9f3-b16224f65091.png)
    - 在操作列点击启动按钮启动网关。
   ![image](https://user-images.githubusercontent.com/18349925/154013441-14938228-4bd0-4afc-b321-876a8e77d791.png)
3. 推送消息
    - 运行sscom5工具，连接到server端
![fc33dad10fa8e82693d70181b5b7f38](https://user-images.githubusercontent.com/18349925/154014071-9d564e83-d0c8-4f8a-8a97-a83479a6870c.png)
    - 发送模拟报文到server
 ```
 {
    "deviceId":"http_demo_v2_test",
    "param_1":"21212331"
}
```
    - 收到上报的消息后平台中设备状态将变为上线。
![image](https://user-images.githubusercontent.com/18349925/154015143-5efeab19-b4bb-4321-bb32-0050446d3652.png)
    - 在设备运行状态中可以看到属性已发生变化。
![bd61b6aa13530bc09ba97f4f7f4fb89](https://user-images.githubusercontent.com/18349925/154015196-63b51a77-3a5c-4e49-b0ff-1f30af5b1070.png)

* [Ngrok](https://ngrok.com/)
* [Ngrok安装教程](https://zhuanlan.zhihu.com/p/43628167)
* [Coap介绍](https://iot.mushuwei.cn/#/internal-work/coap-part1)
