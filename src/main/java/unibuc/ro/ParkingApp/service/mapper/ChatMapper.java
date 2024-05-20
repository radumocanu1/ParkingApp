package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import unibuc.ro.ParkingApp.model.chat.Chat;
import unibuc.ro.ParkingApp.model.chat.ChatRequest;
import unibuc.ro.ParkingApp.model.chat.Message;
import unibuc.ro.ParkingApp.model.chat.MessageRequest;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    Chat chatRequestToChat(ChatRequest chatRequest);
    Message messageRequestToMessage(MessageRequest messageRequest);
}
