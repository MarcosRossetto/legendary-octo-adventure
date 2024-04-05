package org.acme.services.interfaces;

import io.smallrye.mutiny.Uni;

import org.acme.dtos.output.PostOutputDTO;
import org.acme.entities.Post;
import org.acme.enums.Sorting;
import org.acme.utils.PaginationResultUtil;

public interface PostService {

    Uni<PostOutputDTO> createPost(Post post, Long id);

    Uni<PostOutputDTO> findPostById(Long id);

    Uni<PostOutputDTO> updatePost(Long id, Post post);

    Uni<Boolean> deletePost(Long id);

    Uni<PaginationResultUtil<PostOutputDTO>> listAllPosts(int page, int limit, Sorting direction, String column);

    Uni<PaginationResultUtil<PostOutputDTO>> listAllByUser(Long userId, int page, int limit, Sorting direction, String column);
}
