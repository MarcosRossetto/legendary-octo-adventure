package org.acme.controllers;

import org.acme.dtos.HistoryDTO;
import org.acme.dtos.validations.OnCreate;
import org.acme.enums.Sorting;
import org.acme.services.interfaces.HistoryService;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Path("/histories")
@RequiredArgsConstructor
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoryController {

    @Inject
    HistoryService historyService;

    @GET
    @CacheResult(cacheName = "history-cache")
    public Uni<Response> findAll(
        @QueryParam("page") @DefaultValue("1") int page, 
        @QueryParam("size") @DefaultValue("5") int size, 
        @QueryParam("sort") @DefaultValue("ASC") Sorting sort, 
        @QueryParam("column") @DefaultValue("id") String column) {
        
        int numPage = page == 0 ? 0 : page - 1;
        return historyService.findAll(numPage, size, sort, column).onItem().transformToUni(paginationResult -> {
            if (paginationResult.getContent().isEmpty()) {
                log.info("Não foram encontrados itens");
                return Uni.createFrom().item(Response.noContent().build());
            } else {
                return Uni.createFrom().item(Response.ok(paginationResult).build());
            }
        });
    }

    @POST
    public Uni<Response> save(@Valid @ConvertGroup(to = OnCreate.class) HistoryDTO historyDTO) {
        return historyService.save(historyDTO).onItem().transform(savedHistory -> 
            Response.status(Response.Status.CREATED).entity(savedHistory).build());
    }
}
