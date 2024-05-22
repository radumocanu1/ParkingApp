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

import java.util.ArrayList;
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
        log.info("New chat created");
        return createChatResponse(chat,otherUser);

    }
    public void sendMessage(UUID chatUUID, String tokenSubClaim, MessageRequest messageRequest) {
        log.info("Sending a message to chat... ");
        UUID currentUserUUID = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser().getUserUUID();
        Chat chat = getChat(chatUUID);
        User otherUser = userService.getUserById(getOtherUserUUID(chat, currentUserUUID));
        otherUser.addUnreadMessage(chatUUID);
        userService.saveUser(otherUser);
        Message message = chatMapper.messageRequestToMessage(messageRequest);
        message.setChat(chat);
        message.setSenderUUID(currentUserUUID);
        messageRepository.save(message);
        log.info("Message sent to chat... ");

    }
    public List<MinimalChat> getAllUserChats(String tokenSubClaim){
        log.info("Getting user chats...");
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        UUID currentUserUUID = user.getUserUUID();
        List<Chat> userChats =  chatRepository.findAllById(user.getChats().values());
        log.info("Converting to minimal chats...");
        List<MinimalChat> minimalChats = new ArrayList<>();
        for(Chat chat : userChats){
            minimalChats.add(chatToMinimalChat(chat,userService.getUserById(getOtherUserUUID(chat,currentUserUUID)), user));
        }
        log.info("Minimal chats created!");
        return minimalChats;


    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }
    public ChatResponse getChatById(UUID chatUUID, String tokenSubClaim) {
        log.info("Getting chat by UUID...");
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        Chat chat =  getChat(chatUUID);
        User otherUser = userService.getUserById(getOtherUserUUID(chat,currentUser.getUserUUID()));
        currentUser.removeUnreadMessage(chatUUID);
        userService.saveUser(otherUser);
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
    public void deleteChat(UUID chatUUID) {
        log.info("Deleting chat... ");
        chatRepository.deleteById(chatUUID);
    }
    public UnreadChatResponse checkForUnreadMessages(String tokenSubClaim) {
//        log.info("Checking for unread messages...");
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        return new UnreadChatResponse(user.getUnreadChats().size());
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
    private MinimalChat chatToMinimalChat(Chat chat, User otherUser, User currentUser){
        MinimalChat minimalChat = new MinimalChat();
        minimalChat.setChatUUID(chat.getChatUUID());
        if (!chat.getMessages().isEmpty())
            minimalChat.setLastMessage(chat.getMessages().get(chat.getMessages().size() - 1).getMessageContent());
        minimalChat.setHasUnreadMessages(currentUser.getUnreadChats().contains(chat.getChatUUID()));
        if (otherUser.isHasProfilePicture())
            minimalChat.setOtherUserProfilePicture(fileService.loadPicture(otherUser.getProfilePicturePath()));
        minimalChat.setOtherUsername(otherUser.getUsername());
        return minimalChat;
    }
    private UUID getOtherUserUUID(Chat chat, UUID currentUserUUID){
        if (chat.getUser1UUID().equals(currentUserUUID))
            return chat.getUser2UUID();
        return chat.getUser1UUID();
    }



}
