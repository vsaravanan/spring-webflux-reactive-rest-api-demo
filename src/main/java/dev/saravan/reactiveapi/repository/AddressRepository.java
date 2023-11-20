package dev.saravan.reactiveapi.repository;

import dev.saravan.reactiveapi.model.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AddressRepository extends ReactiveCrudRepository<Address, Long> {

}