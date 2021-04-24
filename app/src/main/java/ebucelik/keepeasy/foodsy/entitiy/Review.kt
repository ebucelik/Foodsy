package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable

class Review(val id:Long, val reviewPoints:Int, val reviewText:String, val ordering:Order):Serializable {
}