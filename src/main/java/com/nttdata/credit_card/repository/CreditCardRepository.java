package com.nttdata.credit_card.repository;

import com.nttdata.credit_card.model.entity.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard,String> {
    Flux<CreditCard> findByClientId(String id);

}
