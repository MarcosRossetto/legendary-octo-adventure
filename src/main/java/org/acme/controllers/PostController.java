package org.acme.controllers;

import org.acme.dtos.input.PostInputDTO;
import org.acme.dtos.validations.OnCreate;
import org.acme.dtos.validations.OnUpdate;
import org.acme.enums.Sorting;
import org.acme.services.interfaces.PostService;
import org.acme.services.interfaces.UserService;
import org.acme.services.mappers.PostMapper;

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

@Path("/posts")
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostController {

    @Inject
    PostService postService;

    @Inject
    UserService userService;
    
    @POST
    public Uni<Response> createPost(@Valid @ConvertGroup(to = OnCreate.class) PostInputDTO postInputDTO) {
        return postService.createPost(PostMapper.toEntity(postInputDTO), postInputDTO.getUser())
            .onItem().transform(postOutputDTO -> Response.status(Response.Status.CREATED).entity(postOutputDTO).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deletePostById(@PathParam("id") Long id) {
        return postService.deletePost(id)
            .onItem().transformToUni(postOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.NO_CONTENT).build()));
    }

    @PATCH
    @Path("/{id}")
    public Uni<Response> updatePost(@PathParam("id") Long id, @Valid @ConvertGroup(to = OnUpdate.class) PostInputDTO postInputDTO) {
        return postService.updatePost(id, PostMapper.toEntity(postInputDTO))
            .onItem().transformToUni(postOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.OK).entity(postOutputDTO).build()));
    }

    @GET
    @Path("/{id}")
    public Uni<Response> findPostById(@PathParam("id") Long id) {
        return postService.findPostById(id)
            .onItem().transformToUni(postOutputDTO -> Uni.createFrom().item(Response.status(Response.Status.OK).entity(postOutputDTO).build()));
    }

    @GET
    @Path("/")
    public Uni<Response> listAllPosts(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("limit") @DefaultValue("5") int limit, @QueryParam("direction") @DefaultValue("ASC") Sorting direction, @QueryParam("column") @DefaultValue("id") String column) {
        int numPage = page == 0 ? 0 : page - 1;

        return postService.listAllPosts(numPage, limit, direction, column).onItem().transformToUni(paginationResult -> {
            if (paginationResult.getContent().isEmpty()) {
                return Uni.createFrom().item(Response.noContent().build());
            } else {
                return Uni.createFrom().item(Response.ok(paginationResult).build());
            }
        });
    }

    @GET
    @Path("/listAllByUser/{id}")
    public Uni<Response> listAllByUser(@PathParam("id") Long id, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("limit") @DefaultValue("5") int limit, @QueryParam("direction") @DefaultValue("ASC") Sorting direction, @QueryParam("column") @DefaultValue("id") String column) {
        int numPage = page == 0 ? 0 : page - 1;

        return postService.listAllByUser(id, numPage, limit, direction, column).onItem().transformToUni(paginationResult -> {
            if (paginationResult.getContent().isEmpty()) {
                return Uni.createFrom().item(Response.noContent().build());
            } else {
                return Uni.createFrom().item(Response.ok(paginationResult).build());
            }
        });
    }

}
