# Plain WebSocket with Spring Boot / Using WebJars

## Task
Get to know __WebSocket__ in the context of a __Spring Boot__ application.  Features to check in detail

- [x] use __EditorConfig__ to have a common coding-style
- [x] __WebJars__ for __jQuery__ and __bootstrap__ integration w/o separate `npm` structure
- [x] simple `index.html` start page, that can connect to a _WebSocket end-point_ and send plain strings there
- [x]  __WebSocket__ end-point that receives a string sent to it, logs it and sends it back reversed
- [x] check _Spring Boot application_ in __Cloud Foundry__

Not finished yet

- [ ] check out __OAuth2 integration__ at WebSocket level (test against _Cloud Foundry UAA_) 
- [ ] bonus: use project __Lombok__ for creating data classes via annotations


## Building and Deploying
```
$ mvn clean package
...
$ cf push
...
$ # open browser at the route pushed to, e.g. http://websocket.local.pcfdev.io
```


## Environment used 

On my machine I used

- Windows `10.0.17134.137`
- OpenJDK 10 `2018-03-20 (18.3 build 10+46)`
- cf CLI `cf version 6.36.2+18ceab10f.2018-05-16`
- PCF Dev `version 0.30.0 (CLI: 850ae45, OVA: 0.549.0)`
- _IntelliJ IDEA_ as IDE 


## Lessons learned
- __EditorConfig__ works for basic formatting (tabs, spaces, max chars per line) just by placing an `.editorconfig` into root
- IntelliJ needs a plugin to work with it
- to comply with [Google Java Coding Style](https://google.github.io/styleguide/javaguide.html) one must import [intellij-java-google-style.xml](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml) into IntelliJ, though
- using __WebJars__
    - we do not have to acquire _jQuery_ or _bootstrap_ from CDNs, but can directly access them via `/webjars/<webjar>/<version>/<webjar>.min.js`
    - we can fix versions of the libraries as part of the `pom.xml` 
- extending a `org.springframework.web.socket.handler.TextWebSocketHandler` allows us to implement a basic text-based __WebSocket__ protocol
    - `@Override public void handleTextMessage(WebSocketSession session, TextMessage message) {...}` for that
    - `@Override public void afterConnectionEstablished(WebSocketSession session) throws Exception {...}` allows us to manage any incoming session (e.g. to save some state for that session object or maybe broadcasting)
    - `@Override public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {...}` notifies us of a closed session (client closed connection or error, see `CloseStatus` for that)
- register the end-point in a `WebSocketConfig` that implements `org.springframework.web.socket.config.annotation.WebSocketConfigurer`
    - annotate `WebSocketConfig` with `@Configuration` and `@EnableWebSocket`
    - `@Override public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {..}` and publish all handlers and their end-point via `registry.addHandler(...)`  
    - create handlers in the `WebSocketConfig` using the `@Bean` annotation
- Spring recommends to add a __SockJS__ fallback (see [SockJS github org](https://github.com/sockjs)) to backend and client to make the WebSocket communication still work if a proxy on the way does not support native WebSocket
- Spring discourages the use of _plain WebSockets_ with a custom wire-format, but recommends to use __STOMP__ (_Streaming Text Oriented Messaging Protocol_) instead, providing
    - `CONNECT` and `SEND` for connecting to the server and sending messages to it
    - heart-beats
    - `SUBSCRIBE` and `UNSUBSCRIBE` for notification
    - `BEGIN`, `COMMIT` and `ABORT` for transactional messaging
    - see [stomp github page](https://stomp.github.io/)
    - ...and of course there is `stomp-websocket` in `org.webjars` to add support for the client via _WebJars_

