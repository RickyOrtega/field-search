package com.github.rickyortega.fieldsearch.repository;

import com.github.rickyortega.fieldsearch.dto.FieldSearchRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class FieldSearch<T> {
    private final FieldSearchRepository<T> repository;

    public FieldSearch(EntityManager entityManager, Class<T> entityClass) {
        this.repository = new FieldSearchRepository<>(entityManager, entityClass);
    }

    public List<Map<String, Object>> search(FieldSearchRequest request) {
        return repository.search(request.getFields(), request.getFilters());
    }

    public Map<String, Object> searchOne(FieldSearchRequest request) {
        return repository.searchOne(request.getFields(), request.getFilters());
    }
}
