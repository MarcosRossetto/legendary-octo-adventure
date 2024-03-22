package org.acme.utils;

import io.quarkus.panache.common.Sort;
import java.util.Set;

import org.acme.enums.Sorting;

public class SortUtils {

    /**
     * Cria um objeto Sort para uso com PanacheQuery baseado em uma lista de colunas e uma direção de ordenação.
     * 
     * @param column A coluna pela qual ordenar.
     * @param direction A direção da ordenação (ASC ou DESC).
     * @param validColumns Um conjunto de colunas válidas para ordenação, usado para validação.
     * @return Um objeto Sort configurado.
     */
    public static Sort createSort(String column, Sorting direction, Set<String> validColumns) {
        Sort sort = null;

        // Verifica se a coluna é válida antes de adicionar à ordenação
        if (validColumns != null && !validColumns.contains(column)) {
            throw new IllegalArgumentException("Coluna inválida: " + column);
        }

        sort = (direction == Sorting.DESC) ? Sort.by(column).descending() : Sort.by(column).ascending();

        return sort;
    }
}
