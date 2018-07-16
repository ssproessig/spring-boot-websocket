package org.sample.handler;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SampleHandler extends TextWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(SampleHandler.class);


  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    logger.debug(message.toString());
  }

}
