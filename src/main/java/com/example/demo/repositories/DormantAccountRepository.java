package com.example.demo.repositories;

import com.example.demo.domains.es.DormantAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DormantAccountRepository extends ElasticsearchRepository<DormantAccount, String> {
}
