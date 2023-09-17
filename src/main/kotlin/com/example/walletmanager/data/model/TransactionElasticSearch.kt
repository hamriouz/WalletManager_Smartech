package com.example.walletmanager.data.model

import com.example.walletmanager.enums.TransactionType
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import javax.persistence.Id


@Document(indexName = "transaction_result", shards = 8)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class TransactionElasticSearch(userId: Int, amount: Int, type: TransactionType) {

    @Id
    var id: String? = null

    @Field(name = "user_id", type = FieldType.Integer, storeNullValue = false)
    var userId: Int? = userId

    @Field
    var amount: Int? = amount

    @Field
    var type: TransactionType? = type

}