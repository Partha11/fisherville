package com.techmave.fisherville.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*

class FirebaseRepository: LiveData<DataSnapshot?> {

    private val query: Query
    private val listener: FirebaseEventListener = FirebaseEventListener()

    constructor(query: Query) {

        this.query = query
    }

    constructor(ref: DatabaseReference) {

        query = ref
    }

    override fun onActive() {

        query.addValueEventListener(listener)
    }

    override fun onInactive() {

        query.removeEventListener(listener)
    }

    private inner class FirebaseEventListener : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {

            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {

            Log.e("FirebaseRepository", "Can't listen to query $query", databaseError.toException())
        }
    }
}