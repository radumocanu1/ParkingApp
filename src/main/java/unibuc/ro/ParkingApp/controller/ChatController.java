package unibuc.ro.ParkingApp.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.configuration.ApplicationConstants;
import unibuc.ro.ParkingApp.model.chat.Chat;
import unibuc.ro.ParkingApp.model.chat.MessageRequest;
import unibuc.ro.ParkingApp.service.ChatService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@Log
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @PostMapping("/{otherUserUUID}")
    public ResponseEntity<Chat> createChat(@PathVariable UUID otherUserUUID, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.createChat((String) token.getTokenAttributes().get("sub"), otherUserUUID), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Chat>> getChats() {
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }
    @PostMapping("/send-message/{chatUUID}")
    public ResponseEntity<String> sendMessage(@PathVariable UUID chatUUID, @RequestBody MessageRequest messageRequest,Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        chatService.sendMessage(chatUUID,(String) token.getTokenAttributes().get("sub"),messageRequest);
        return new ResponseEntity<>(ApplicationConstants.MESSAGE_SENT, HttpStatus.OK);

    }
    @GetMapping("/{chatUUID}")
    public ResponseEntity<Chat> getChatById(@PathVariable UUID chatUUID, Principal principal) {
        return new ResponseEntity<>(chatService.getChatById(chatUUID), HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Chat>> getChats(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.getAllUserChats((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
}
