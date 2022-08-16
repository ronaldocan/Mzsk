## 网络组件开发
1. pom文件配置
    - 添加maven-网络组件核心依赖
```
  <dependencies>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>network-core</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-core</artifactId>
        </dependency>

        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>gateway-component</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```
2. 组件开发：
    - 创建网络组件Server类：DemoServer
```
 import org.jetlinks.community.network.Network;
 import org.jetlinks.community.network.NetworkType;

/**
 * @Description demo 网络组件server
 * @Date 2022/8/16 10:04
 * @Author zhengguican
 */
public class DemoServer implements Network {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public NetworkType getType() {
        // 组件类型，新增类型需在枚举类添加对应的枚举 DefaultNetworkType.class
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isAutoReload() {
        return false;
    }
}

```
    - 创建网络组件会话类： DemoSession
```
import org.jetlinks.core.device.DeviceOperator;
import org.jetlinks.core.message.codec.EncodedMessage;
import org.jetlinks.core.message.codec.Transport;
import org.jetlinks.core.server.session.DeviceSession;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * @Description 网络组件会话
 * @Date 2022/8/16 10:07
 * @Author zhengguican
 */
public class DemoSession implements DeviceSession {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDeviceId() {
        return null;
    }

    @Nullable
    @Override
    public DeviceOperator getOperator() {
        return null;
    }

    @Override
    public long lastPingTime() {
        return 0;
    }

    @Override
    public long connectTime() {
        return 0;
    }

    @Override
    public Mono<Boolean> send(EncodedMessage encodedMessage) {
        return null;
    }

    @Override
    public Transport getTransport() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void ping() {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void onClose(Runnable call) {

    }
}

```
3. 网关实现类编写
```
import org.jetlinks.community.gateway.DeviceGateway;
import org.jetlinks.community.gateway.monitor.MonitorSupportDeviceGateway;
import org.jetlinks.community.network.NetworkType;
import org.jetlinks.core.message.Message;
import org.jetlinks.core.message.codec.Transport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Description demo网络组件对应的网关
 * @Date 2022/8/16 10:08
 * @Author zhengguican
 */
public class DemoServerGateway  implements DeviceGateway, MonitorSupportDeviceGateway {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Transport getTransport() {
        return null;
    }

    @Override
    public NetworkType getNetworkType() {
        return null;
    }

    @Override
    public Flux<Message> onMessage() {
        return null;
    }

    @Override
    public Mono<Void> startup() {
        return null;
    }

    @Override
    public Mono<Void> pause() {
        return null;
    }

    @Override
    public Mono<Void> shutdown() {
        return null;
    }

    @Override
    public long totalConnection() {
        return 0;
    }
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
