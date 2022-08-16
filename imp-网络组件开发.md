## 网络组件模块说明
1. 类图
![uml](https://user-images.githubusercontent.com/18349925/184789539-022a2c52-97ed-418e-91d3-c8816cb012fe.png)
2. 核心方法说明
![接口说明](https://user-images.githubusercontent.com/18349925/184789553-97d889ac-6e9f-446b-a6f7-c18ade4b4557.png)
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
    - 网关实现类编写 DemoServerGateway
```

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.gateway.DeviceGateway;
import org.jetlinks.community.gateway.monitor.DeviceGatewayMonitor;
import org.jetlinks.community.gateway.monitor.GatewayMonitors;
import org.jetlinks.community.gateway.monitor.MonitorSupportDeviceGateway;
import org.jetlinks.community.network.DefaultNetworkType;
import org.jetlinks.community.network.NetworkType;
import org.jetlinks.community.network.coap.server.DemoServer;
import org.jetlinks.community.network.utils.DeviceGatewayHelper;
import org.jetlinks.core.ProtocolSupport;
import org.jetlinks.core.ProtocolSupports;
import org.jetlinks.core.device.DeviceRegistry;
import org.jetlinks.core.message.DeviceMessage;
import org.jetlinks.core.message.Message;
import org.jetlinks.core.message.codec.DefaultTransport;
import org.jetlinks.core.message.codec.EncodedMessage;
import org.jetlinks.core.message.codec.FromDeviceMessageContext;
import org.jetlinks.core.message.codec.Transport;
import org.jetlinks.core.server.session.DeviceSessionManager;
import org.jetlinks.supports.server.DecodedClientMessageHandler;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description demo网络组件对应的网关
 * @Date 2022/8/16 10:08
 * @Author zhengguican
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
public class DemoServerGateway  implements DeviceGateway, MonitorSupportDeviceGateway {

    private final String id;

    private final DemoServer demoServer;

    private final DeviceGatewayMonitor gatewayMonitor;

    private final EmitterProcessor<Message> messageProcessor = EmitterProcessor.create(false);

    private final FluxSink<Message> sink = messageProcessor.sink(FluxSink.OverflowStrategy.BUFFER);

    private final AtomicReference<Boolean> started = new AtomicReference<>(false);

    private final DeviceGatewayHelper helper;

    private final String protocol;

    private final ProtocolSupports supports;

    public DemoServerGateway(String id,
                                   String protocol,
                                   ProtocolSupports supports,
                                   DemoServer server,
                                   DeviceRegistry registry,
                                   DeviceSessionManager sessionManager,
                                   DecodedClientMessageHandler clientMessageHandler) {
        this.id = id;
        this.protocol = protocol;
        this.supports = supports;
        this.demoServer = server;
        this.gatewayMonitor = GatewayMonitors.getDeviceGatewayMonitor(id);
        this.helper = new DeviceGatewayHelper(registry, sessionManager, clientMessageHandler);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Transport getTransport() {
        return DefaultTransport.Demo;
    }

    @Override
    public NetworkType getNetworkType() {
        return DefaultNetworkType.Demo_Server;
    }

    @Override
    public Flux<Message> onMessage() {
        return messageProcessor;
    }

    @Override
    public Mono<Void> startup() {
        return Mono.fromRunnable(this::doStart);
    }

    @Override
    public Mono<Void> pause() {
        return Mono.fromRunnable(() -> started.set(false));
    }

    @Override
    public Mono<Void> shutdown() {
        return Mono.fromRunnable(() -> {
            started.set(false);
            demoServer.shutdown();
        });
    }

    @Override
    public long totalConnection() {
        return 0;
    }

    /**
     * 处理coap消息
     */
    private void doStart() {
        // 网络组件入站消息处理
        demoServer.handleMessage() // TODO 网络组件入站消息方法
            .flatMap(this::handleMessage)
            .subscribe();
    }

    /**
     * 处理消息 ==>> 设备消息
     *
     * @param demoMessage 消息
     * @return void
     */
    Mono<Void> handleMessage(DemoMessage demoMessage) {
        AtomicReference<Duration> timeoutRef = new AtomicReference<>(Duration.ofSeconds(10));
        return getProtocol()
            .flatMap(protocol -> protocol.getMessageCodec(getTransport()))
            .flatMapMany(codec -> codec.decode(FromDeviceMessageContext.of(new UnknownCoapDeviceSession() {
                @Override
                public Mono<Boolean> send(EncodedMessage encodedMessage) {
                    return super
                        .send(encodedMessage)
                        .doOnSuccess(r -> gatewayMonitor.sentMessage());
                }
                @Override
                public void setKeepAliveTimeout(Duration timeout) {
                    timeoutRef.set(timeout);
                }
                @Override
                public Optional<InetSocketAddress> getClientAddress() {
                    return Optional.of(new InetSocketAddress(0));
                }
            }, demoMessage)))
            .cast(DeviceMessage.class)
            .doOnNext(msg -> gatewayMonitor.receivedMessage())
            .flatMap(demoMessage -> {
                sink.next(demoMessage);
                return helper.handleDeviceMessage(demoMessage, deviceOperator ->
                        new DemoSession(),
                    DeviceGatewayHelper
                        .applySessionKeepaliveTimeout(demoMessage, timeoutRef::get),
                    () -> log.warn("无法从coap[{}]消息中获取设备信息:{}", "host", demoMessage));
            })
            .then();
    }

    public Mono<ProtocolSupport> getProtocol() {
        return supports.getProtocol(protocol);
    }
}
```
## 网络组件配置参数前端页面联调
1. 通常网络组件拥有公共参数(Host、Port等)，但却有协议独有参数如(Tcp:KeepOnlineTime),此时配置新增网络组件页面，需调整前端页面
![页面说明](https://user-images.githubusercontent.com/18349925/184790568-f67ace63-3ce0-4c49-9535-3a64fe622553.png)

