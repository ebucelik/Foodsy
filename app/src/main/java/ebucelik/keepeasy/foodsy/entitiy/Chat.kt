package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable
import java.util.*

data class Chat(val id: Long, val from_user: String, val chatPool: ChatPool, val message: String, val timestamp: Date) : Serializable
