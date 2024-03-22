package org.acme.repositories.interfaces;

import java.util.List;

import org.acme.entities.History;
import org.acme.enums.Sorting;

import io.smallrye.mutiny.Uni;

public interface HistoryRepository {
    
    Uni<List<History>> getWithPaginationAndSort(int page, int size, Sorting sort, String column);

    Uni<Long> countTotal();

    public Uni<History> save(History history);
}
