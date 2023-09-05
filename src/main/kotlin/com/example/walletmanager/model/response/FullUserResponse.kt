package com.example.walletmanager.model.response

import com.example.walletmanager.model.ResponseResult

class FullUserResponse(
    val userId: Int,
    val userName: String?,
    val balance: Int?,
    val status: ResponseResult
) {}

