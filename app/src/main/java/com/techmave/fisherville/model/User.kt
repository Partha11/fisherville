package com.techmave.fisherville.model

import androidx.room.Ignore

class User {

    var name: String = ""
    var type: Long = 0

    @Ignore
    var number: String = ""
}