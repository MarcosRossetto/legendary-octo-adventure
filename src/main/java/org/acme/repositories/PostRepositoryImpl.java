package org.acme.repositories;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acme.entities.Post;
import org.acme.enums.Sorting;
import org.acme.repositories.interfaces.PostRepository;
import org.acme.utils.SortUtil;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class PostRepositoryImpl implements PostRepository {
    
    private static final Set<String> VALID_COLUMNS = new HashSet<>(Arrays.asList("id", "content", "views"));

    @Override
    public Uni<List<Post>> listAllPaginated(int page, int limit, Sorting direction, String column) {
        Sort sort = SortUtil.createSort(column, direction, VALID_COLUMNS);
        return findAll(sort).page(Page.of(page, limit)).list();
    }

    @Override
    public Uni<List<Post>> listAllByUser(Long userId, int page, int limit, Sorting direction, String column) {
        Sort sort = SortUtil.createSort(column, direction, VALID_COLUMNS);
        return find("user.id", sort, userId).page(Page.of(page, limit)).list();
    }

}
