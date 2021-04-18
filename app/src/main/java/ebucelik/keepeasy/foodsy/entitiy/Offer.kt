package ebucelik.keepeasy.foodsy.entitiy

import java.io.Serializable
import java.util.*

data class Offer(val id:Long = 0,  val mealName:String = "",  val category:String = "",  val area:String = "", val encodedImage:String = "", val ingredients:String = "", val currentTimestamp:Date = Date(), val user:User? = null) : Serializable {
}