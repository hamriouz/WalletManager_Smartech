package com.example.WalletManager.repository

import com.example.WalletManager.model.TransactionResult
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionResultRepository: ElasticsearchRepository<TransactionResult, Int> {

}