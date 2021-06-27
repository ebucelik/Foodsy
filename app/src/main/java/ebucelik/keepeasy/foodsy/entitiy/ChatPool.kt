package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

data class ChatPool(val id: Long, val user: User, val withUser: User) : Serializable
