package org.acme.services.interfaces;

import io.smallrye.mutiny.Uni;

import org.acme.dtos.output.UserOutputDTO;
import org.acme.entities.User;
import org.acme.enums.Sorting;
import org.acme.utils.PaginationResultUtil;

public interface UserService {

    Uni<UserOutputDTO> createUser(User user);

    Uni<UserOutputDTO> findUserById(Long id);

    Uni<UserOutputDTO> updateUser(Long id, User user);

    Uni<Boolean> deleteUserById(Long id);

    Uni<PaginationResultUtil<UserOutputDTO>> listAllUsers(int page, int limit, Sorting direction, String column);

    Uni<Boolean> changePassword(Long id, User user);

}