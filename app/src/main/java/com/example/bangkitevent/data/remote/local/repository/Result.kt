package com.example.bangkitevent.data.remote.local.repository

// untuk menyimpan status pengambilan data
// gak kepake akhirnya hehe
sealed class Result<out R> private constructor() {
    @Suppress("unused")
    data class Success<out T>(val data: T) : Result<T>()

    @Suppress("unused")
    data class Error(val error: String) : Result<Nothing>()

    @Suppress("unused")
    object Loading : Result<Nothing>()
}