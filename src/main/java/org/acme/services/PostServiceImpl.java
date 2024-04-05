package org.acme.services;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.acme.dtos.output.PostOutputDTO;
import org.acme.entities.Post;
import org.acme.enums.Sorting;
import org.acme.exceptions.NotFoundException;
import org.acme.repositories.interfaces.PostRepository;
import org.acme.repositories.interfaces.UserRepository;
import org.acme.services.interfaces.PostService;
import org.acme.services.mappers.PostMapper;
import org.acme.utils.PaginationResultUtil;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@WithTransaction
@ApplicationScoped
public class PostServiceImpl implements PostService {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    @Named("messages")
    ResourceBundle messages;
    
    @Override
    public Uni<PostOutputDTO> createPost(Post post, Long id) {
        return userRepository.findById(id)
            .onItem().ifNotNull().transformToUni(user -> {
                post.setUser(user);
                return postRepository.persistAndFlush(post).onItem().transform(PostMapper::toDTO);
            })
            .onItem().ifNull().failWith(() -> new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "Post", id)));
    }

    @Override
    public Uni<Boolean> deletePost(Long id) {
        return postRepository.findById(id)
            .onItem().ifNotNull().transformToUni(existingPost -> { return postRepository.delete(existingPost).onItem().transform(removed -> true); })
            .onItem().ifNull().failWith(new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "Post", id)));
    }

    @Override
    public Uni<PostOutputDTO> updatePost(Long id, Post post) {
        return postRepository.findById(id)
            .onItem().ifNull().failWith(() -> new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "Post", id)))
            .onItem().transformToUni(existingPost -> {
                PostMapper.updateEntityFromEntity(post, existingPost);
                return postRepository.persistAndFlush(existingPost);
            })
            .onItem().transform(PostMapper::toDTO);
    }

    @Override
    public Uni<PostOutputDTO> findPostById(Long id) {
        return postRepository.findById(id)
            .onItem().ifNotNull().transform(PostMapper::toDTO)
            .onItem().ifNull().failWith(new NotFoundException(MessageFormat.format(messages.getString("ERROR.NOT_FOUND"), "Post", id)));
    }

    @Override
    public Uni<PaginationResultUtil<PostOutputDTO>> listAllPosts(int page, int limit, Sorting direction, String column) {
        Uni<List<PostOutputDTO>> postsDtoListUni = postRepository.listAllPaginated(page, limit, direction, column)
            .onItem().transformToUni(dtoList -> Uni.createFrom().item(dtoList.stream().map(PostMapper::toDTO).collect(Collectors.toList())));
    
        return postsDtoListUni.flatMap(dtoListTransformed -> postRepository.count()
            .onItem().transform(totalElements -> new PaginationResultUtil<>(dtoListTransformed, page, limit, totalElements)));
    }

    @Override
    public Uni<PaginationResultUtil<PostOutputDTO>> listAllByUser(Long userId, int page, int limit, Sorting direction, String column) {
        Uni<List<PostOutputDTO>> postsDtoListUni = postRepository.listAllByUser(userId, page, limit, direction, column)
            .onItem().transformToUni(dtoList -> Uni.createFrom().item(dtoList.stream().map(PostMapper::toDTO).collect(Collectors.toList())));

        return postsDtoListUni.flatMap(dtoListTransformed -> postRepository.count()
            .onItem().transform(totalElements -> new PaginationResultUtil<>(dtoListTransformed, page, limit, totalElements)));
    }
}
