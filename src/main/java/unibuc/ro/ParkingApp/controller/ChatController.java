package unibuc.ro.ParkingApp.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import unibuc.ro.ParkingApp.model.chat.*;
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
    public ResponseEntity<ChatResponse> createChat(@PathVariable String otherUserUUID, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.createChat((String) token.getTokenAttributes().get("sub"), UUID.fromString(otherUserUUID)), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Chat>> getChats() {
        return new ResponseEntity<>(chatService.getAllChats(), HttpStatus.OK);
    }
    @PostMapping("/send-message/{chatUUID}")
    public ResponseEntity<Void> sendMessage(@PathVariable String chatUUID, @RequestBody MessageRequest messageRequest,Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        chatService.sendMessage(UUID.fromString(chatUUID),(String) token.getTokenAttributes().get("sub"),messageRequest);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/try/{otherUserUUID}")
    public ResponseEntity<ChatResponse> tryToGetChat(@PathVariable String otherUserUUID, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.getChat(UUID.fromString(otherUserUUID),(String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @GetMapping("/admin")
    public ResponseEntity<AdminChat> adminChat(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.getAdminChatUUID((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @GetMapping("/{chatUUID}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable String chatUUID, Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.getChatById(UUID.fromString(chatUUID),(String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<MinimalChat>> getChats(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.getAllUserChats((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @GetMapping("/unread")
    public ResponseEntity<UnreadChatResponse> getUnreadChats(Principal principal){
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        return new ResponseEntity<>(chatService.checkForUnreadMessages((String) token.getTokenAttributes().get("sub")), HttpStatus.OK);
    }
    @PostMapping("/generic/welcome")
    public ResponseEntity<Void> sendWelcomeMessage(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        chatService.createGenericAdminChat((String)token.getTokenAttributes().get("sub"));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllUserChats(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        chatService.deleteAllUserChats((String) token.getTokenAttributes().get("sub"));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
