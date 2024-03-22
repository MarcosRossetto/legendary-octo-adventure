package org.acme.utils;

import org.modelmapper.ModelMapper;

public class EntityDtoMapper {
    private static final ModelMapper modelmapper = new ModelMapper();

    private EntityDtoMapper() {}

    public static<D, E> D convertToDTO(E entity, Class<D> dtoClass) {
        return modelmapper.map(entity, dtoClass);
    }

    public static <E, D> E convertToEntity(D dto, Class<E> entitClass) {
        return modelmapper.map(dto, entitClass);
    }
}

