package ebucelik.keepeasy.foodsy.entitiy

import org.jetbrains.annotations.NotNull
import java.io.Serializable
import java.util.*

data class Offer(val id:Long = 0, val mealName:String = "", val category:String = "", val area:String = "", val encodedImage:String = "", val ingredients:String = "", val currentTimestamp:Date = Date(), @NotNull val price:Int = 0, val user:User? = null) : Serializable {
}