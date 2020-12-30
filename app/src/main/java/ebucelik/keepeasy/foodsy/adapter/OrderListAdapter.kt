package ebucelik.keepeasy.foodsy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.OrderList

class OrderListAdapter(context: Context, orderList: OrderList) : BaseAdapter(){

    private val homeContext: Context = context
    private val order: OrderList = orderList

    override fun getCount(): Int {
        return order.orderingList.size
    }

    override fun getItem(position: Int): Any {
        return order.orderingList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(homeContext)
        val rowAccOrder = layoutInflater.inflate(R.layout.row_accountorder, viewGroup, false)

        val offeringId = rowAccOrder.findViewById<TextView>(R.id.offeringId)
        offeringId.text = "Ordered Meal Id: ${order.orderingList[position].offeringId}"

        return rowAccOrder
    }
}