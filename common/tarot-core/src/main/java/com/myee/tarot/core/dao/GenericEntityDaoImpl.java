package com.myee.tarot.core.dao;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.util.GenericEntityUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;

/**
 * @param <K> entity type
 */
public abstract class GenericEntityDaoImpl<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
        extends GenericJpaDaoSupport
        implements GenericEntityDao<K, E> {

    private Class<E> objectClass;

    @SuppressWarnings("unchecked")
    public GenericEntityDaoImpl() {
        this.objectClass = (Class<E>) GenericEntityUtil.getGenericEntityClassFromComponentDefinition(getClass());
    }

    protected final Class<E> getObjectClass() {
        return objectClass;
    }

    public E getEntity(Class<? extends E> clazz, K id) {
        return super.getEntity(getObjectClass(), id);
    }

    public E getById(K id) {
        return super.getEntity(getObjectClass(), id);
    }

    public <V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
        return super.getByField(getObjectClass(), attribute, fieldValue);
    }

    public E update(E entity) {
        return super.update(entity);
    }

    public void save(E entity) {
        super.save(entity);
    }

    public void delete(E entity) {
        super.delete(entity);
    }

    public E refresh(E entity) {
        return super.refresh(entity);
    }

    public List<E> list() {
        return super.listEntity(getObjectClass());
    }

    public <V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
        return super.listEntityByField(getObjectClass(), attribute, fieldValue);
    }

    public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
        return super.listEntity(objectClass, filter, limit, offset, orders);
    }

    public Long count() {
        return super.countEntity(getObjectClass());
    }

    public <V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
        return super.countEntityByField(getObjectClass(), attribute, fieldValue);
    }

    public Long count(Expression<Boolean> filter) {
        return super.countEntity(getObjectClass(), filter);
    }

    @Override
    public EntityManager getEntityManager() {
        return super.getEntityManager();
    }
}
