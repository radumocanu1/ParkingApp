package unibuc.ro.ParkingApp.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import unibuc.ro.ParkingApp.model.user.CreateUserRequest;
import unibuc.ro.ParkingApp.model.user.UpdateUserRequest;
import unibuc.ro.ParkingApp.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userRequestToUser(CreateUserRequest createUserRequest);
    void fill(UpdateUserRequest updateUserRequest, @MappingTarget User target);
}
