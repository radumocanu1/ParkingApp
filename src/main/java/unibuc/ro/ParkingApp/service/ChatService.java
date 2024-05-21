package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.ChatNotFound;
import unibuc.ro.ParkingApp.model.chat.*;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.ChatRepository;
import unibuc.ro.ParkingApp.repository.MessageRepository;
import unibuc.ro.ParkingApp.service.mapper.ChatMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final OIDCUserMappingService oidcUserMappingService;
    private final ChatMapper chatMapper;
    private final MessageRepository messageRepository;
    public Chat createChat(String tokenSubClaim, UUID otherUserUUID) {
        log.info("Creating new chat... ");
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        User otherUser = userService.getUserById(otherUserUUID);
        ChatRequest chatRequest = new ChatRequest(currentUser.getUserUUID(),otherUser.getUserUUID());
        Chat chat =  chatRepository.save(chatMapper.chatRequestToChat(chatRequest));
        currentUser.addChat(otherUser.getUserUUID(), chat.getChatUUID());
        otherUser.addChat(currentUser.getUserUUID(), chat.getChatUUID());
        userService.saveUser(otherUser);
        userService.saveUser(currentUser);
        return chat;

    }
    public void sendMessage(UUID chatUUID, String tokenSubClaim, MessageRequest messageRequest) {
        log.info("Sending a message to chat... ");
        UUID currentUserUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        Chat chat = getChat(chatUUID);
        Message message = chatMapper.messageRequestToMessage(messageRequest);
        message.setChat(chat);
        message.setSenderUUID(currentUserUUID);
        messageRepository.save(message);
        log.info("Message sent to chat... ");

    }
    public List<Chat> getAllUserChats(String tokenSubClaim){
        log.info("Getting user chats...");
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        return chatRepository.findAllById(user.getChats().values());
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }
    // todo response exception handler
    public Chat getChatById(UUID chatUUID) {
        return getChat(chatUUID);
    }
    private Chat getChat(UUID chatUUID) {
        Optional<Chat> chat = chatRepository.findById(chatUUID);
        if (chat.isEmpty()) {
            throw new RuntimeException();
        }
        return chat.get();
    }
    public Chat getChat(UUID otherUserUUID, String tokenSubClaim) {
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        UUID chatUUID = currentUser.getChats().get(otherUserUUID);
        if (chatUUID == null) {
            throw new ChatNotFound("Chat not found");
        }
        return getChat(chatUUID);

    }



}
