## 协议开发
1. pom文件配置
    - 添加maven依赖
```
  <dependencies>
    <!-->jetlinks 核心依赖<-->
        <dependency>
            <groupId>org.jetlinks</groupId>
            <artifactId>jetlinks-core</artifactId>
            <version>1.2.0</version>
        </dependency>
        <!-->jetlinks 协议解析接口包<-->
        <dependency>
            <groupId>org.jetlinks</groupId>
            <artifactId>jetlinks-supports</artifactId>
            <version>1.2.0</version>
        </dependency>
        <!-->lombok，需要idea安装lombok插件，否则去掉<-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.10</version>
        </dependency>
        <!-->vertx核心包，可以用来进行网络模拟测试<-->
        <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-core</artifactId>
            <version>3.8.3</version>
            <scope>test</scope>
        </dependency>
        <!-->单元测试包<--> 
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.5.2</version>
            <scope>test</scope>
        </dependency>
		<!-->logback日志<-->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
            <scope>test</scope>
        </dependency>

    </dependencies>
	<!-->netty组件<-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.netty</groupId>
                <artifactId>netty-bom</artifactId>
                <version>${netty.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

    - 添加maven编译规则
    
```
<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${project.build.jdk}</source>
                    <target>${project.build.jdk}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

    - 添加hsweb私服和阿里云仓库
    
```
 <repositories>
        <repository>
            <id>hsweb-nexus</id>
            <name>Nexus Release Repository</name>
            <url>http://nexus.hsweb.me/content/groups/public/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
        <repository>
            <id>aliyun-nexus</id>
            <name>aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        </repository>
    </repositories>
```
2. 协议开发：
    - 新建packag：org.jetlinks.demo.protocol
    - 创建协议编码解码类：DemoDeviceMessageCodec
```
  import io.netty.buffer.ByteBuf;
  import io.netty.buffer.ByteBufUtil;
  import io.netty.buffer.Unpooled;
  import lombok.AllArgsConstructor;
  import lombok.extern.slf4j.Slf4j;
  import org.apache.commons.codec.DecoderException;
  import org.apache.commons.codec.binary.Hex;
  import org.jetlinks.core.message.DeviceMessage;
  import org.jetlinks.core.message.DeviceOnlineMessage;
  import org.jetlinks.core.message.Message;
  import org.jetlinks.core.message.codec.*;
  import org.jetlinks.core.message.function.FunctionInvokeMessage;
  import org.jetlinks.core.message.function.FunctionInvokeMessageReply;
  import org.jetlinks.core.server.session.DeviceSession;
  import org.jetlinks.wt.protocol.message.NbIotMessage;
  import org.jetlinks.wt.protocol.message.data.enums.DataIdEnum;
  import org.jetlinks.wt.protocol.message.enums.ControlEnum;
  import org.reactivestreams.Publisher;
  import reactor.core.publisher.Flux;
  import reactor.core.publisher.Mono;
  
  @AllArgsConstructor
  @Slf4j
  public class DemoDeviceMessageCodec implements DeviceMessageCodec {
      // 传输协议定义
      @Override
      public Transport getSupportTransport() {
          return DefaultTransport.TCP;
      }
      
      // 把tcp消息解码为平台消息，多用于设备上报消息到平台
      @Override
      public Mono<? extends Message> decode(MessageDecodeContext context) {
          return Mono.empty();
      }
      
      // 把平台消息编码为协议传输消息，多用于平台命令下发到设备
      @Override
      public Publisher<? extends EncodedMessage> encode(MessageEncodeContext context) {
          retrun Mono.empty();
      }
  }
```
    - 创建协议入口类： DemoProtocolSupportProvider
```
  import org.jetlinks.core.ProtocolSupport;
  import org.jetlinks.core.Value;
  import org.jetlinks.core.defaults.CompositeProtocolSupport;
  import org.jetlinks.core.device.AuthenticationResponse;
  import org.jetlinks.core.device.DeviceRegistry;
  import org.jetlinks.core.device.MqttAuthenticationRequest;
  import org.jetlinks.core.message.codec.DefaultTransport;
  import org.jetlinks.core.metadata.DefaultConfigMetadata;
  import org.jetlinks.core.metadata.types.PasswordType;
  import org.jetlinks.core.metadata.types.StringType;
  import org.jetlinks.core.spi.ProtocolSupportProvider;
  import org.jetlinks.core.spi.ServiceContext;
  import org.jetlinks.demo.protocol.tcp.DemoTcpMessageCodec;
  import org.jetlinks.supports.official.JetLinksDeviceMetadataCodec;
  import reactor.core.publisher.Mono;
  
  public class DemoProtocolSupportProvider implements ProtocolSupportProvider {
      
      @Override
      public Mono<? extends ProtocolSupport> create(ServiceContext context) {
          CompositeProtocolSupport support = new CompositeProtocolSupport();
          // 协议ID
          support.setId("demo-v1");
          // 协议名称
          support.setName("演示协议v1");
          // 协议说明
          support.setDescription("演示协议");
          // 物模型编解码，固定为JetLinksDeviceMetadataCodec
          support.setMetadataCodec(new JetLinksDeviceMetadataCodec());
          //TCP消息编解码器
          DemoDeviceMessageCodec codec = new DemoDeviceMessageCodec();
          // 两个参数，协议支持和编解码类DemoDeviceMessageCodec中保持一致，第二个参数定义使用的编码解码类
          support.addMessageCodecSupport(DefaultTransport.TCP, () -> Mono.just(codec));
          return Mono.just(support);
      }
  }
```
3. 设备上报消息解码
```
@AllArgsConstructor
@Slf4j
public class DemoTcpMessageCodec implements DeviceMessageCodec {
        
       ....
        
        // 把tcp消息解码为平台消息，多用于设备上报消息到平台
        @Override
        public Publisher<? extends Message> decode(MessageDecodeContext context) {
    		return Flux.defer(() -> {
                // 消息上下文
                FromDeviceMessageContext ctx = ((FromDeviceMessageContext) context);
                // 从上下文中获取消息字节数组
                ByteBuf byteBuf = context.getMessage().getPayload();
                byte[] payload = ByteBufUtil.getBytes(byteBuf, 0, byteBuf.readableBytes(), false);
                // 把字节流转换为字符串，根据不同设备不同协议进行解析，
                String text=new String(payload);
                ReportPropertyMessage message = new ReportPropertyMessage();
                // 设置消息ID为我们获得的消息内容
                message.setDeviceId(text);
                // 以当前时间戳为消息时间
                long time= LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                message.setTimestamp(time);
                // 构造上报属性
                Map<String, Object> properties = new HashMap<>();
                properties.put("text",text);
                // 设置上报属性
                message.setProperties(properties);
    
                // 获取设备会话信息
                DeviceSession session = ctx.getSession();
                // 如果session中没有设备信息，则为设备首次上线
                if (session.getOperator() == null) {
                    DeviceOnlineMessage onlineMessage = new DeviceOnlineMessage();
                    onlineMessage.setDeviceId(text);
                    onlineMessage.setTimestamp(System.currentTimeMillis());
                    // 返回到平台上线消息
                    return Flux.just(message,onlineMessage);
                }
                // 返回到平台属性上报消息
                return Mono.just(message);
            });
        }
.....
  }
```
4.平台发送消息到设备（消息编码）
```
@AllArgsConstructor
@Slf4j
public class DemoTcpMessageCodec implements DeviceMessageCodec {
    ..........
    // 把平台消息编码为协议传输消息，多用于平台命令下发到设备
    @Override
    public Publisher<? extends EncodedMessage> encode(MessageEncodeContext context) {
         // 从平台消息上下文中获取消息内容
        CommonDeviceMessage message = (CommonDeviceMessage) context.getMessage();
        EncodedMessage encodedMessage = EncodedMessage.simple(Unpooled.wrappedBuffer(message.toString().getBytes()));
        // 根据消息类型的不同，构造不同的消息
        if (message instanceof ReadPropertyMessage) {
            ReadPropertyMessage readPropertyMessage = (ReadPropertyMessage) message;
            // 获取需要传输的字节
            byte[] bytes = readPropertyMessage.toString().getBytes();
            // 构造为平台传输到设备的消息体
            encodedMessage = EncodedMessage.simple(Unpooled.wrappedBuffer(bytes));
        }
        return Mono.just(encodedMessage);
    }
}
```
5. 调试(debug)协议
将协议包工程放到和jetlinks相同到工程目录里,即可使用IDE进行debug.

参照目录:
```
--jetlinks
----|--dev
----|---|-- demo-protocl    # 开发中的协议
----|--jetlinks-components 
----|--jetlinks-standalone
----|-- ....
```
注意

在调试过程中修改代码可以进行热加载,但是重启服务后会失效,需要重新发布协议.
