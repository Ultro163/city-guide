package com.example.service;

import com.example.error.exception.EntityNotFoundException;
import com.example.model.EntityName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Абстрактный сервис для реализации CRUD операций для сущностей.
 * Предоставляет базовые операции создания, получения, обновления и удаления сущностей.
 * Используется для работы с репозиториями, которые расширяют {@link JpaRepository}.
 *
 * @param <T>  Тип сущности, с которой работает сервис
 * @param <ID> Тип идентификатора сущности
 */
@Slf4j
abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {
    protected final JpaRepository<T, ID> repository;
    private final String entityName;

    protected AbstractCrudService(JpaRepository<T, ID> repository, EntityName entityName) {
        this.repository = repository;
        this.entityName = entityName.getDisplayName();
    }

    /**
     * Создает новую сущность и сохраняет её в базе данных.
     *
     * @param entity Сущность, которую необходимо создать
     * @return Сохраненная сущность
     */
    @Override
    public T create(T entity) {
        log.info("Creating {}: {}", entityName, entity);
        T entitySaved = repository.save(entity);
        log.info("Saved {}: {}", entityName, entity);
        return entitySaved;
    }

    /**
     * Получает сущность по идентификатору.
     *
     * @param id Идентификатор сущности
     * @return Найденная сущность
     * @throws EntityNotFoundException Если сущность с данным идентификатором не найдена
     */
    @Override
    public T getById(ID id) {
        log.info("Getting {} with ID: {}", entityName, id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    /**
     * Обновляет сущность с указанным идентификатором.
     *
     * @param id     Идентификатор сущности, которую необходимо обновить
     * @param entity Сущность с обновленными данными
     * @return Обновленная сущность
     */
    @Override
    public T update(ID id, T entity) {
        log.info("Updating {} with ID:{}", entityName, id);
        T result = repository.save(entity);
        log.info("Updated {}: {}", entityName, result);
        return result;
    }

    /**
     * Удаляет сущность с указанным идентификатором.
     *
     * @param id Идентификатор сущности, которую необходимо удалить
     * @throws EntityNotFoundException Если сущность с данным идентификатором не найдена
     */
    @Override
    public void delete(ID id) {
        log.info("Deleting {}: {}", entityName, id);
        validateExistence(id);
        repository.deleteById(id);
        log.info("Deleted {}: {}", entityName, id);
    }

    /**
     * Проверяет существование сущности с указанным идентификатором.
     *
     * @param id Идентификатор сущности
     * @throws EntityNotFoundException Если сущность с данным идентификатором не существует
     */
    protected void validateExistence(ID id) {
        if (!repository.existsById(id)) {
            throw createEntityNotFoundException(id);
        }
    }

    /**
     * Создает исключение {@link EntityNotFoundException} для указанного идентификатора.
     *
     * @param id Идентификатор сущности, для которой не найдена запись
     * @return Исключение {@link EntityNotFoundException}
     */
    private EntityNotFoundException createEntityNotFoundException(ID id) {
        log.warn("{} not found with ID {}", entityName, id);
        return new EntityNotFoundException(entityName + " with ID=" + id + " not found");
    }
}