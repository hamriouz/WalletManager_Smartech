package com.example.WalletManager.model.response

import com.example.WalletManager.model.ResponseResult

class FullUserResponse(
    val userId: Int,
    val userName: String?,
    val balance: Int?,
    val status: ResponseResult
) {}

