package com.rodrigonovoa.wouldyoudrinkthiscocktail.api

data class ApiResult<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ApiResult<T> = ApiResult(Status.SUCCESS, data, null)

        fun <T> error(message: String, data: T? = null): ApiResult<T> = ApiResult(Status.ERROR, data, message)

        fun <T> loading(data: T? = null): ApiResult<T> = ApiResult(Status.LOADING, data, null)
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}