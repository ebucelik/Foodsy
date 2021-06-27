package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class Order(val id:Long = 0, val offer:Offer = Offer(), val user:User? = null) : Serializable {
}