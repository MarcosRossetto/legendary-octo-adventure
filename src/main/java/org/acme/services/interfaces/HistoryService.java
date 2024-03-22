package org.acme.services.interfaces;

import org.acme.dtos.HistoryDTO;
import org.acme.enums.Sorting;
import org.acme.utils.PaginationResult;

import io.smallrye.mutiny.Uni;

public interface HistoryService {
    Uni<PaginationResult<HistoryDTO>> findAll(int page, int size, Sorting sort, String column);
    Uni<HistoryDTO> save(HistoryDTO historyDTO);
}

