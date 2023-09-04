package com.example.WalletManager.model

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.apache.kafka.common.utils.Time
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.Date
import javax.persistence.Id


@Document(indexName = "transaction_result", shards = 8)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class TransactionResult() {

    @Id
    var id: String? = null

    @Field
    var isSuccessful: Boolean? = null

    @Field(name = "user_id", type = FieldType.Integer, storeNullValue = false)
    var userId: Int? = null

    @Field
    var failureReason: String? = null

}