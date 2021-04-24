package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class User(val userUUID: String = "", val username: String = "", val firstname: String = "", val surname: String = "", val password: String = "", val profileImage: String = "") : Serializable {
}