# WebSocket

---

##

---

### 特点

双向请求

长连接

基于 HTTP

现代浏览器都已经支持 WebSocket 协议

### Java

```
@Bean
WebSocketConfigurer createWebSocketConfigurer(
        @Autowired ChatHandler chatHandler,
        @Autowired ChatHandshakeInterceptor chatInterceptor) {
    return new WebSocketConfigurer() {
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            // 把URL与指定的WebSocketHandler关联，可关联多个:
            registry.addHandler(chatHandler, "/chat").addInterceptors(chatInterceptor);
        }
    };
}
```

处理 WebSocket 连接

和处理普通 HTTP 请求不同，没法用一个方法处理一个 URL。

Spring 提供了 TextWebSocketHandler 和 BinaryWebSocketHandler 分别处理文本消息和二进制消息，这里我们选择文本消息作为聊天室的协议，

因此，ChatHandler 需要继承自 TextWebSocketHandler：

```
@Component
public class ChatHandler extends TextWebSocketHandler {
...
}
```

```
@Component
public class ChatHandler extends TextWebSocketHandler {
    // 保存所有Client的WebSocket会话实例:
    private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 新会话根据ID放入Map:
        clients.put(session.getId(), session);
        session.getAttributes().put("name", "Guest1");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
    }
}
```

### 客户端开发

在完成了服务器端的开发后，我们还需要在页面编写一点 JavaScript 逻辑：

```
// 创建WebSocket连接:
var ws = new WebSocket('ws://' + location.host + '/chat');
// 连接成功时:
ws.addEventListener('open', function (event) {
console.log('websocket connected.');
});
// 收到消息时:
ws.addEventListener('message', function (event) {
console.log('message: ' + event.data);
var msgs = JSON.parse(event.data);
// TODO:
});
// 连接关闭时:
ws.addEventListener('close', function () {
console.log('websocket closed.');
});
// 绑定到全局变量:
window.chatWs = ws;
```

用户可以在连接成功后任何时候给服务器发送消息：

```
var inputText = 'Hello, WebSocket.';
window.chatWs.send(JSON.stringify({text: inputText}));
```

---

##

---

### 关闭连接

WebSocket 的关闭，在实际操作中经常遇到的有三种情况，一种是浏览器的关闭，一种是我们 js 代码主动关闭，还有一种是浏览器刷新（没错，刷新，我一开始没注意这个问题）。

而无论哪种方式，对于 WebSocket 来说，它必须发一个关闭的控制帧数据到对端。也就是上面提到的 Opcode 必须为 0x8。

### Ping/Pong

WebSocket 基于 Tcp，同时它也改进了 Tcp 的一些实现特性。

比如 WebSocket 自带 Ping/Pong，以此来实现其保持长连接的特性。

使用 Tcp 时，我们往往要自己实现心跳，但 WebSocket 的 Ping/Pong 则完全替我们实现了心跳。

不过很讽刺的是，虽然其 WebSocket 标准明确的实现了 Ping/Pong 但是现在各浏览器，或是 WebSocket 库，并没有提供发送 Ping/Pong 的 API，也就是你如果不是自己实现 WebSocket 的协议的话，这 Ping/Pong 根本是没法发的。

但目前的浏览器或者 JS 库，虽然不同供发 Ping 的 API，但它们可以接收 Ping 处理，并回发 Pong 数据。

另外，当一端收到多次 ping 时，并不需要返回每一个响应，只要返回最近一次 Ping 的 Pong 响应即可。

### 分包

WebSocket 会将大的数据，自动分片发送。

所以 WebSocket 会自动分包发送，因为这种分包发送，WebSocket 的数据不会溢出接收缓冲区，所以也不会有半包的情况发送。

###

###

---

参考链接：

- [使用WebSocket](https://www.liaoxuefeng.com/wiki/1252599548343744/1282384966189089)
- [游戏网络编程（三）——WebSocket 入门及实现自己的 WebSocket 协议](https://www.cnblogs.com/indie-developer/p/7097707.html)
- []()
- []()
- []()

---










