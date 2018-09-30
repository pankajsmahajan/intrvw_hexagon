package com.intvw.hexagon.repository;

import org.springframework.data.repository.CrudRepository;

import com.intvw.hexagon.entity.CreditCardEntity;
/**
 * Repository class to perform CRUD operation for card entity.
 * @author pankaj.mahajan
 *
 */
public interface CreditCardRepository extends CrudRepository<CreditCardEntity, Long> {

}
