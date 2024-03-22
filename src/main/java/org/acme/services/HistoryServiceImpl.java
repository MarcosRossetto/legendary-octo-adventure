package org.acme.services;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.dtos.HistoryDTO;
import org.acme.entities.History;
import org.acme.enums.Sorting;
import org.acme.repositories.interfaces.HistoryRepository;
import org.acme.services.interfaces.HistoryService;
import org.acme.utils.EntityDtoMapper;
import org.acme.utils.PaginationResult;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@WithTransaction
@ApplicationScoped
public class HistoryServiceImpl implements HistoryService {

    @Inject
    HistoryRepository historyRepository;
    
    @Override
    public Uni<PaginationResult<HistoryDTO>> findAll(int page, int size, Sorting sort, String column) {
        return historyRepository.getWithPaginationAndSort(page, size, sort, column)
            .onItem().transformToUni(dtoList -> {
                List<HistoryDTO> dtoListTransformed = dtoList.stream()
                    .map(entity -> EntityDtoMapper.convertToDTO(entity, HistoryDTO.class))
                    .collect(Collectors.toList());
                
                return historyRepository.countTotal()
                    .onItem().transform(totalElements -> {
                        int totalPages = (int) Math.ceil((double) totalElements / size);
                        boolean hasNext = page < totalPages - 1;
                        boolean hasPrevious = page > 0;
                        int currentPage = page + 1;

                        return new PaginationResult<HistoryDTO>(dtoListTransformed, currentPage, totalElements, totalPages, hasNext, hasPrevious);

                    });
            });
    }

    @Override
    public Uni<HistoryDTO> save(HistoryDTO historyDTO) {
        History history = EntityDtoMapper.convertToEntity(historyDTO, History.class);

        return historyRepository.save(history).onItem().transform(savedHistory -> EntityDtoMapper.convertToDTO(savedHistory, HistoryDTO.class));
    }
}
