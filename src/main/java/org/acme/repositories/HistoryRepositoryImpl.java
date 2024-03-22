package org.acme.repositories;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acme.entities.History;
import org.acme.enums.Sorting;
import org.acme.repositories.interfaces.HistoryRepository;
import org.acme.utils.SortUtils;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class HistoryRepositoryImpl implements HistoryRepository, PanacheRepositoryBase<History, Long> {

    final Set<String> VALID_COLUMNS = new HashSet<>(Arrays.asList("id", "description"));
    
    @Override
    public Uni<List<History>> getWithPaginationAndSort(int page, int size, Sorting sortDirection, String column) {
        
        Sort sort = SortUtils.createSort(column, sortDirection, VALID_COLUMNS);

        return findAll(sort).page(Page.of(page, size)).list();
    }

    @Override
    public Uni<Long> countTotal() {
        return count();
    }

    @Override
    public Uni<History> save(History history) {
        return persistAndFlush(history);
    }
}
