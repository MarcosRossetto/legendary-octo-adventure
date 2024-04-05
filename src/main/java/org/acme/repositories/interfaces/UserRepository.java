package org.acme.repositories.interfaces;

import java.util.List;

import org.acme.entities.User;
import org.acme.enums.Sorting;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

public interface UserRepository extends PanacheRepository<User> {
    Uni<List<User>> listAllPaginated(int page, int limit, Sorting direction, String column);
}