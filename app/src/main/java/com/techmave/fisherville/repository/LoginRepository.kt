package com.techmave.fisherville.repository

class LoginRepository {

    companion object {

        @Volatile
        private var instance: LoginRepository? = null

        @JvmStatic
        fun getInstance(): LoginRepository {

            if (instance == null) {

                instance = LoginRepository()
            }

            return instance!!
        }
    }
}