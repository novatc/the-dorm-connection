package com.novatc.ap_app.model

import androidx.annotation.Nullable


class Request<T> private constructor(
    val status: Status, @field:Nullable @param:Nullable val data: T,
    @field:Nullable @param:Nullable val message: Int?
) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): Request<T> {
            return Request(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: Int?, @Nullable data: T): Request<T> {
            return Request(Status.ERROR, data, msg)
        }

        fun <T> loading(@Nullable data: T): Request<T> {
            return Request(Status.LOADING, data, null)
        }
    }
}