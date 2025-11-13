package com.example.myfinalproject.Model.Repo

interface UserRepo {
    suspend fun getUserId(): String
}