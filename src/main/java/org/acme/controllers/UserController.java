package org.acme.controllers;

import org.acme.dtos.input.UserChangePasswordInputDTO;
import org.acme.dtos.input.UserInputDTO;
import org.acme.dtos.validations.OnCreate;
import org.acme.dtos.validations.OnUpdate;
import org.acme.enums.Sorting;
import org.acme.services.interfaces.UserService;
import org.acme.services.mappers.UserMapper;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/users")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @POST
    public Uni<Response> createUser(@Valid @ConvertGroup(to = OnCreate.class) UserInputDTO userInputDTO) {
        return userService.createUser(UserMapper.toEntity(userInputDTO))
            .onItem().transform(userOutputDTO -> Response.status(Response.Status.CREATED).entity(userOutputDTO).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteUserById(@PathParam("id") Long id) {
        return userService.deleteUserById(id)
            .onItem().transformToUni(userOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.NO_CONTENT).build()));
    }

    @PATCH
    @Path("/{id}")
    public Uni<Response> updateUser(@PathParam("id") Long id, @Valid @ConvertGroup(to = OnUpdate.class) UserInputDTO userInputDTO) {
        return userService.updateUser(id, UserMapper.toEntity(userInputDTO))
            .onItem().transform(userOutputDTO -> Response.status(Response.Status.OK).entity(userOutputDTO).build());
    }

    @PATCH
    @Path("/{id}/password")
    public Uni<Response> changePassword(@PathParam("id") Long id, @Valid @ConvertGroup(to = OnUpdate.class) UserChangePasswordInputDTO userChangePasswordInputDTO) {
        return userService.changePassword(id, UserMapper.toEntity(userChangePasswordInputDTO))
            .onItem().transformToUni(userChangePasswordOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.NO_CONTENT).build()));
    }

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "get-user-cache")
    public Uni<Response> findUserById(@PathParam("id") Long id) {
        return userService.findUserById(id)
            .onItem().transformToUni(userOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.OK).entity(userOutputDTO).build()));
    }

    @GET
    @Path("/")
    @CacheResult(cacheName = "list-all-users-cache")
    public Uni<Response> listAllUsers(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("limit") @DefaultValue("5") int limit, @QueryParam("direction") @DefaultValue("ASC") Sorting direction, @QueryParam("column") @DefaultValue("id") String column) {
        int numPage = page == 0 ? 0 : page - 1;

        return userService.listAllUsers(numPage, limit, direction, column).onItem().transformToUni(paginationResult -> {
            if (paginationResult.getContent().isEmpty()) {
                return Uni.createFrom().item(Response.noContent().build());
            } else {
                return Uni.createFrom().item(Response.ok(paginationResult).build());
            }
        });
    }
    
}
