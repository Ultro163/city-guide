package com.example.service;

public interface CrudService<T, ID> {
    T create(T entity);

    T getById(ID id);

    T update(ID id, T entity);

    void delete(ID id);
}
