package com.myee.tarot.core.service;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

public interface GenericEntityService<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> extends TransactionalAspectAware {

    void save(E entity) throws ServiceException;

    void update(E entity) throws ServiceException;

    void create(E entity) throws ServiceException;

    void delete(E entity) throws ServiceException;

    E refresh(E entity);

    E getById(K id);

    List<E> list();

    E getEntity(Class<? extends E> clazz, K id);

    Long count();

    void flush();

    void clear();

}
