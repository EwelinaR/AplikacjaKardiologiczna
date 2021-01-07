package com.github.aplikacjakardiologiczna.model.database.entity

import java.io.Serializable

class NextId(
        val id: Long,
        val nextId: Long
) : Serializable {
    override fun toString(): String {
        return "NextId [id: ${this.id}, nextId: ${this.nextId}]"
    }
}
