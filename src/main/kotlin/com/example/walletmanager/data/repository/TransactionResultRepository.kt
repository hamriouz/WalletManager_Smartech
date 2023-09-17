package com.example.walletmanager.data.repository

import com.example.walletmanager.data.model.TransactionElasticSearch
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionResultRepository : ElasticsearchRepository<TransactionElasticSearch, Int> {

}