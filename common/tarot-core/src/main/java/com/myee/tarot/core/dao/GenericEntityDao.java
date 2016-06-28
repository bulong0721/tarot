package com.myee.tarot.core.dao;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.List;


/**
 * @param <E> type entity
 */
public interface GenericEntityDao<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> {

	/**
	 * @param clazz class
	 * @param id id
	 * @return entity
	 */
	E getEntity(Class<? extends E> clazz, K id);
	
	/**
	 * @param id id
	 * @return entity
	 */
	E getById(K id);
	
	/**
	 * @param entity entity
	 */
	void save(E entity);
	
	/**
	 * @param entity entity
	 */
	E update(E entity);
	
	/** 
	 * @param entity entity
	 */
	void delete(E entity);
	
	/**
	 * @param entity entity
	 */
	E refresh(E entity);
	
	/**
	 * @return liste d'entitys
	 */
	List<E> list();
	
	/**
	 * @return nombre d'entitys
	 */
	Long count();
	
	void flush();
	
	void clear();

	/**
	 * @param <V> type value
	 * @param attribute
	 * @param fieldValue
	 * @return numbers of entities
	 */
	<V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	/**
	 * @param <V> type value
	 * @param attribute
	 * @param fieldValue
	 * @return entities
	 */
	<V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	/**
	 * @param <V>
	 * @param attribute
	 * @param fieldValue
	 * @return
	 * @throws NoResultException
	 * @throws {@link NonUniqueResultException}
	 */
	<V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue);

	<T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders);

	Long count(Expression<Boolean> filter);
	
	EntityManager getEntityManager();

	

}
