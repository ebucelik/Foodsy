package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class Order(val id:Long, val orderingUuid:String, val offeringUuid:String, val offeringId:Long, val offer:Offer) : Serializable {
}