package org.acme.repositories.interfaces;

import java.util.List;

import org.acme.entities.Post;
import org.acme.enums.Sorting;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public interface PostRepository extends PanacheRepository<Post> {

    Uni<List<Post>> listAllPaginated(int page, int limit, Sorting direction, String column);

    Uni<List<Post>> listAllByUser(Long userId, int page, int limit, Sorting direction, String column);
}
