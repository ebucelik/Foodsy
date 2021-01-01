package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class User(private val userUUID: String, private val username: String, private val firstname: String, private val surname: String, private val password: String, private val profileImage: String) : Serializable {

    fun getUUID(): String{
        return userUUID
    }

    fun getUsername(): String{
        return username
    }

    fun getFirstname(): String{
        return firstname
    }

    fun getSurname(): String{
        return surname
    }

    fun getPassword(): String{
        return password
    }

    fun getProfileImage(): String{
        return profileImage
    }
}