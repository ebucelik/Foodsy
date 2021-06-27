package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

data class User(val id: Long = 0, val userUUID: String = "", val username: String = "", val firstname: String = "", val surname: String = "", val password: String = "", val profileImage: String = "") : Serializable {
}