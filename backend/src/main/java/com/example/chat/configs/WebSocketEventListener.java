package com.example.chat.configs;

import com.example.chat.models.ChatMessage;
import com.example.chat.models.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    //private static final Set<String> userCount = ConcurrentHashMap.newKeySet();
    private static int userCount = 0;

    public static void addUser(String user) {
//        userCount.add(user);
        userCount++;
    }

    public static void removeUser(String user) {
//        userCount.remove(user);
        userCount--;
        userCount = Math.max(0, userCount);
    }

    public static int getUserCount() {
//        return userCount.size();
        return userCount;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username  != null) {
            removeUser(username);
            ChatMessage chatMessage = ChatMessage.buildChatmessage(username + "has left the chat." , username , MessageType.LEAVE);
            messagingTemplate.convertAndSend("/topic/messages" , chatMessage);
            messagingTemplate.convertAndSend("/topic/userCount", userCount);
        }
    }

}
