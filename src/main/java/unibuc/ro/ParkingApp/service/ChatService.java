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
    private final FileService fileService;

    public ChatResponse createChat(String tokenSubClaim, UUID otherUserUUID) {
        log.info("Creating new chat... ");
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        User otherUser = userService.getUserById(otherUserUUID);
        ChatRequest chatRequest = new ChatRequest(currentUser.getUserUUID(),otherUser.getUserUUID());
        Chat chat =  chatRepository.save(chatMapper.chatRequestToChat(chatRequest));
        currentUser.addChat(otherUser.getUserUUID(), chat.getChatUUID());
        otherUser.addChat(currentUser.getUserUUID(), chat.getChatUUID());
        userService.saveUser(otherUser);
        userService.saveUser(currentUser);
        return createChatResponse(chat,otherUser);

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
    public ChatResponse getChatById(UUID chatUUID, String tokenSubClaim) {
        log.info("Getting chat by UUID...");
        UUID currentUserUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        Chat chat =  getChat(chatUUID);
        UUID otherUserUUID;
        if (chat.getUser1UUID().equals(currentUserUUID)) {
            otherUserUUID = chat.getUser2UUID();
        }
        else{
            otherUserUUID = chat.getUser1UUID();
        }
        User otherUser = userService.getUserById(otherUserUUID);
        return createChatResponse(chat,otherUser);
    }
    // todo response exception handler
    private Chat getChat(UUID chatUUID) {
        Optional<Chat> chat = chatRepository.findById(chatUUID);
        if (chat.isEmpty()) {
            throw new RuntimeException();
        }
        return chat.get();
    }
    public ChatResponse getChat(UUID otherUserUUID, String tokenSubClaim) {
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        UUID chatUUID = currentUser.getChats().get(otherUserUUID);
        if (chatUUID == null) {
            throw new ChatNotFound("Chat not found");
        }
        User otherUser = userService.getUserById(otherUserUUID);
        Chat chat =  getChat(chatUUID);
        return createChatResponse(chat,otherUser);


    }
    private ChatResponse createChatResponse (Chat chat, User otherUser){
        log.info("Creating chat response...");
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setChatUUID(chat.getChatUUID());
        chatResponse.setOtherUserUUID(otherUser.getUserUUID());
        if (otherUser.isHasProfilePicture())
            chatResponse.setOtherUserProfileImage(fileService.loadPicture(otherUser.getProfilePicturePath()));
        chatResponse.setOtherUserName(otherUser.getUsername());
        chatResponse.setMessages(chat.getMessages());
        log.info("Chat response created");
        return chatResponse;
    }



}
