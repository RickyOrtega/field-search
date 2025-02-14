package com.github.rickyortega.fieldsearch.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.*;

public class FieldSearchRepository<T> {
    private final EntityManager entityManager;
    private final Class<T> entityClass;

    public FieldSearchRepository(
            EntityManager entityManager,
            Class<T> entityClass
    ) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public List<Map<String, Object>> search(
            List<String> fields,
            Map<String, Object> filters
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<T> root = query.from(entityClass);

        List<Selection<?>> selections = new ArrayList<>();
        for (String field : fields) {
            selections.add(root.get(field));
        }
        query.multiselect(selections);

        applyFilters(cb, root, query, filters);

        List<Object[]> results = entityManager.createQuery(query).getResultList();
        return mapResults(fields, results);
    }

    public Map<String, Object> searchOne(
            List<String> fields,
            Map<String, Object> filters
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<T> root = query.from(entityClass);

        List<Selection<?>> selections = new ArrayList<>();
        for (String field : fields) {
            selections.add(root.get(field));
        }
        query.multiselect(selections);

        applyFilters(cb, root, query, filters);

        List<Object[]> results = entityManager.createQuery(query).setMaxResults(1).getResultList();
        return results.isEmpty() ? null : mapSingleResult(fields, results.get(0));
    }

    private void applyFilters(
            CriteriaBuilder cb,
            Root<T> root,
            CriteriaQuery<?> query,
            Map<String, Object> filters
    ) {
        if (filters != null && !filters.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
            }
            query.where(predicates.toArray(new Predicate[0]));
        }
    }

    private List<Map<String, Object>> mapResults(
            List<String> fields,
            List<Object[]> results
    ) {
        List<Map<String, Object>> finalResults = new ArrayList<>();
        for (Object[] row : results) {
            finalResults.add(mapSingleResult(fields, row));
        }
        return finalResults;
    }

    private Map<String, Object> mapSingleResult(
            List<String> fields,
            Object[] row
    ) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            map.put(fields.get(i), row[i]);
        }
        return map;
    }
}
