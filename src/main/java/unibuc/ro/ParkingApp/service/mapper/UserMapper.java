package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import unibuc.ro.ParkingApp.model.user.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToUser(CreateUserRequest createUserRequest);
    MinimalUser userToMinimalUser(User user);
    UserResponse userToUserResponse(User user);
    void fill(UpdateUserRequest updateUserRequest, @MappingTarget User target);
}
