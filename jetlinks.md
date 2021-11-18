## jetlink协议接入方式
目前提供的协议有：
* mqtt
* coap
* http
* tcp
### mqtt接入
1. 选择 设备接入-->协议管理--> 点击新建按钮
![alt 属性文本](http://doc.jetlinks.cn/assets/img/new-protocol.878fbbf2.png)
2. - 输入协议ID
   -  输入型号名称
   - 选择型号类型为 jar
   - 输入类名org.jetlinks.protocol.official.JetLinksProtocolSupportProvider
   - 上传jar包jetlinks-official-protocol-2.0-SNAPSHOT.jar
   - 点击确认，完成协议新增。
![alt 属性文本](http://doc.jetlinks.cn/assets/img/product-protocol.c07eaa2e.png)
3. 发布刚新建的协议
![image](https://user-images.githubusercontent.com/18349925/142365934-cb7a6114-9b41-4c18-b497-b1c4677dbcb4.png)

4.
   -  新建设备型号
   -  选择消息协议为刚新建的协议
![image](https://user-images.githubusercontent.com/18349925/142365521-114d1060-5c78-4f4a-9065-eaeb1befba71.png)
5. 
   -  添加设备
