package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable
import java.util.*

class Offer(val id:Long,  val userUUID:String,  val mealName:String,  val category:String,  val area:String, val encodedImage:String, val ingredients:String, val currentTimestamp:Date, val user:User) : Serializable {
}