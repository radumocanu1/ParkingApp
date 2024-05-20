package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import unibuc.ro.ParkingApp.model.user.CreateUserRequest;
import unibuc.ro.ParkingApp.model.user.UpdateUserRequest;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.model.user.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToUser(CreateUserRequest createUserRequest);
    UserResponse userToUserResponse(User user);
    void fill(UpdateUserRequest updateUserRequest, @MappingTarget User target);
}
