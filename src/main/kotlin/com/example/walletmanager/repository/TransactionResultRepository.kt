package com.example.walletmanager.repository

import com.example.walletmanager.model.TransactionResult
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionResultRepository : ElasticsearchRepository<TransactionResult, Int> {

}