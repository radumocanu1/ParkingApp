package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToUser(UserRequest userRequest);
    void fill(UserRequest userRequest, @MappingTarget User target);
}
