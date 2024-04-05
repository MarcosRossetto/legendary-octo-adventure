package org.acme.services.mappers;

import org.acme.dtos.input.UserChangePasswordInputDTO;
import org.acme.dtos.input.UserInputDTO;
import org.acme.dtos.output.UserOutputDTO;
import org.acme.entities.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addMappings(new PropertyMap<User, User>() {
            @Override
            protected void configure() {
                skip().setPosts(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<UserChangePasswordInputDTO, User>() {
            @Override
            protected void configure() {
                map().setCurrentPassword(source.getCurrentPassword());
                map().setNewPassword(source.getNewPassword());
                skip().setPassword(null);
            }
        });
    }

    public static UserOutputDTO toDTO(User user) {
        return modelMapper.map(user, UserOutputDTO.class);
    }

    public static User toEntity(UserInputDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public static User toEntity(UserChangePasswordInputDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public static void updateEntityFromEntity(User source, User target) {
        modelMapper.map(source, target);
    }
}
