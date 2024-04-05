package org.acme.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.acme.dtos.output.UserOutputDTO;
import org.acme.entities.User;
import org.acme.enums.Sorting;
import org.acme.exceptions.IncorrectPasswordException;
import org.acme.exceptions.NotFoundException;
import org.acme.repositories.interfaces.UserRepository;
import org.acme.services.interfaces.UserService;
import org.acme.services.mappers.UserMapper;
import org.acme.utils.PaginationResultUtil;
import org.acme.utils.PasswordEncryptUtil;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@WithTransaction
@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    @Named("messages")
    ResourceBundle messages;

    @Override
    public Uni<UserOutputDTO> createUser(User user) {
        user.setPassword(PasswordEncryptUtil.encrypt(user.getPassword()));
        return userRepository.persistAndFlush(user).onItem().transform(UserMapper::toDTO);
    }

    @Override
    public Uni<Boolean> deleteUserById(Long id) {
        return userRepository.findById(id)
            .onItem().ifNotNull().transformToUni(existingUser -> { return userRepository.delete(existingUser).onItem().transform(removed -> true); })
            .onItem().ifNull().failWith(new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "User", id)));
    }

    @Override
    public Uni<UserOutputDTO> updateUser(Long id, User user) {
        return userRepository.findById(id)
            .onItem().ifNull().failWith(() -> new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "User", id)))
            .onItem().transformToUni(existingUser -> {
                UserMapper.updateEntityFromEntity(user, existingUser);
                return userRepository.persistAndFlush(existingUser);
            })
            .onItem().transform(UserMapper::toDTO);
    }

    @Override
    public Uni<Boolean> changePassword(Long id, User user) {
        return userRepository.findById(id)
        .onItem().ifNull().failWith(() -> new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "User", id)))
        .onItem().transformToUni(existingUser -> {

            if (!PasswordEncryptUtil.checkPassword(user.getCurrentPassword(), existingUser.getPassword())) {
                return Uni.createFrom().failure(new IncorrectPasswordException(messages.getString("VALIDATION.PASSWORD.INVALID")));
            }

            existingUser.setPassword(PasswordEncryptUtil.encrypt(user.getNewPassword()));

            UserMapper.updateEntityFromEntity(user, existingUser);
            return userRepository.persistAndFlush(existingUser);
        }).onItem().transform(updated -> true);
    }

    @Override
    public Uni<UserOutputDTO> findUserById(Long id) {
        return userRepository.findById(id)
            .onItem().ifNull().failWith(() -> new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "User", id)))
            .onItem().transform(UserMapper::toDTO);
    }

    @Override
    public Uni<PaginationResultUtil<UserOutputDTO>> listAllUsers(int page, int limit, Sorting direction, String column) {
        Uni<List<UserOutputDTO>> usersDtoListUni = userRepository.listAllPaginated(page, limit, direction, column)
            .onItem().transformToUni(dtoList -> Uni.createFrom().item(dtoList.stream().map(UserMapper::toDTO).collect(Collectors.toList())));
    
        return usersDtoListUni.flatMap(dtoListTransformed -> userRepository.count()
            .onItem().transform(totalElements -> new PaginationResultUtil<>(dtoListTransformed, page, limit, totalElements)));
    }
}
