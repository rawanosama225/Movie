package com.example.myfinalproject.Model.Repo

import com.google.firebase.auth.FirebaseAuth

class UserRepoImpl(
    val firebaseAuth: FirebaseAuth
): UserRepo {

    //TODO: use coroutine
    override suspend fun getUserId(): String {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: ""
            userId

        }
        catch (e: Exception){
            ""
        }
    }

}