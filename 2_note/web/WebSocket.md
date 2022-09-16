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

处理WebSocket连接

和处理普通HTTP请求不同，没法用一个方法处理一个URL。

Spring提供了TextWebSocketHandler和BinaryWebSocketHandler分别处理文本消息和二进制消息，这里我们选择文本消息作为聊天室的协议，

因此，ChatHandler需要继承自TextWebSocketHandler：

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

在完成了服务器端的开发后，我们还需要在页面编写一点JavaScript逻辑：

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

###

###

###

---

参考链接：

- [使用WebSocket](https://www.liaoxuefeng.com/wiki/1252599548343744/1282384966189089)
- []()
- []()
- []()

---










