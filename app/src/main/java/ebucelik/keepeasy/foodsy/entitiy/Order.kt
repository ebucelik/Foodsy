package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class Order(val id:Long, val offer:Offer, val user:User) : Serializable {
}