package unibuc.ro.ParkingApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class ChatService {
    @Value("${adminUUID}")
    String adminUUID;
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
    public void createGenericAdminChat(String tokeSubClaim) {
        User user = oidcUserMappingService.findBySubClaim(tokeSubClaim).getUser();
        ChatRequest chatRequest = new ChatRequest(UUID.fromString(adminUUID),user.getUserUUID());
        Chat chat =  chatRepository.save(chatMapper.chatRequestToChat(chatRequest));
        user.addChat(UUID.fromString(adminUUID), chat.getChatUUID());
        userService.saveUser(user);
        sendMessageToGenericAdminChat(user,"Bine ai venit la Parco, " + user.getUsername()+  "! Poti face X,Y,Z!");

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
    private void sendMessageToGenericAdminChat(User user, String message){
        log.info("Sending message to generic admin chat... ");
        UUID chatUUID = user.getChats().get(UUID.fromString(adminUUID));
        user.addUnreadMessage(chatUUID);
        userService.saveUser(user);
        Message messageObject = new Message();
        messageObject.setSenderUUID(UUID.fromString(adminUUID));
        messageObject.setChat(getChat(chatUUID));
        messageObject.setMessageContent(message);
        messageRepository.save(messageObject);
        log.info("Message sent to generic admin chat... ");

    }
    public List<MinimalChat> getAllUserChats(String tokenSubClaim){
        log.info("Getting user chats...");
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        UUID currentUserUUID = user.getUserUUID();
        List<Chat> userChats =  chatRepository.findAllById(user.getChats().values());
        log.info("Converting to minimal chats...");
        List<MinimalChat> minimalChats = new ArrayList<>();
        for(Chat chat : userChats){
            // make sure we don't try to get the generic admin user
            if (!chat.getUser1UUID().equals(UUID.fromString(adminUUID)))
                minimalChats.add(chatToMinimalChat(chat,userService.getUserById(getOtherUserUUID(chat,currentUserUUID)), user));
            else
                minimalChats.add(createGenericAdminMinimalChat(chat, user));
        }

        log.info("Minimal chats created!");
        return minimalChats;


    }
    private MinimalChat createGenericAdminMinimalChat(Chat chat, User user) {
        MinimalChat minimalChat = new MinimalChat();
        minimalChat.setOtherUsername("Echipa Parco");
        minimalChat.setChatUUID(chat.getChatUUID());
        String lastMessage = chat.getMessages().get(chat.getMessages().size() - 1).getMessageContent();
        if (lastMessage.length() > 15)
            minimalChat.setLastMessage(lastMessage.substring(0, 15) + " ...");
        else
            minimalChat.setLastMessage(lastMessage);
        minimalChat.setHasUnreadMessages(user.getUnreadChats().contains(chat.getChatUUID()));
        return minimalChat;

    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    public ChatResponse getChatById(UUID chatUUID, String tokenSubClaim) {
        log.info("Getting chat by UUID...");
        User currentUser = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        Chat chat =  getChat(chatUUID);
        // check if this should be the generic admin chat
        if (!chat.getUser1UUID().equals(UUID.fromString(adminUUID))){
            User otherUser = userService.getUserById(getOtherUserUUID(chat,currentUser.getUserUUID()));
            currentUser.removeUnreadMessage(chatUUID);
            userService.saveUser(currentUser);
            return createChatResponse(chat,otherUser);
        }
        // create generic admin chat response
        currentUser.removeUnreadMessage(chatUUID);
        userService.saveUser(currentUser);
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setMessages(chat.getMessages());
        return chatResponse;


    }
    // todo response exception handler
    private Chat getChat(UUID chatUUID) {
        Optional<Chat> chat = chatRepository.findById(chatUUID);
        if (chat.isEmpty()) {
            throw new RuntimeException();
        }
        return chat.get();
    }
    public void sendAdminMessage( UUID userUUID,String message){
        sendMessageToGenericAdminChat(userService.getUserById(userUUID),message);
    }
    public AdminChat getAdminChatUUID(String tokenSubClaim) {
        User user = oidcUserMappingService.findBySubClaim(tokenSubClaim).getUser();
        return new AdminChat( user.getChats().get(UUID.fromString(adminUUID)));
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
        if (!chat.getMessages().isEmpty()){
            String lastMessage = chat.getMessages().get(chat.getMessages().size() - 1).getMessageContent();
            if (lastMessage.length() > 15)
                minimalChat.setLastMessage(lastMessage.substring(0, 15) + " ...");
            else
                minimalChat.setLastMessage(lastMessage);        }
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
