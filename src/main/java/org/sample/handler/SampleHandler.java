package org.sample.handler;

import java.io.IOException;
import java.lang.StringBuilder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SampleHandler extends TextWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(SampleHandler.class);


  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    String payload = message.getPayload();
    logger.info("Received text: {}", payload);

    String reversed = (new StringBuilder(payload)).reverse().toString();

    try {
      session.sendMessage(new TextMessage(reversed));
    } catch (IOException e) {
      logger.error("Error sending reversed string: {}", e);
    }
  }


  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    super.afterConnectionEstablished(session);
    logger.info("New session: {}", session.toString());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
    logger.info("Session closed: {}  Status: {}", session.toString(), status);
  }
}
