package com.example.itog

import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.itog.Item
class adapterforotziv(private var dataSetOtziv: MutableList<Otziv>) : RecyclerView.Adapter<adapterforotziv.ViewHolder>() {
    class ViewHolder(otzivView: View) : RecyclerView.ViewHolder(otzivView) {
        // Здесь вы можете инициализировать элементы интерфейса для каждого элемента списка
        val Avatarka: ImageView = otzivView.findViewById(R.id.avatarka)
        val Nickname: TextView = otzivView.findViewById(R.id.nickname)
        val Time: TextView = otzivView.findViewById(R.id.time)
        val Stars: ImageView = otzivView.findViewById(R.id.stars)
        val Textotziv: TextView = otzivView.findViewById(R.id.textotziv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Создаем новый элемент интерфейса из макета и передаем его в ViewHolder
        val otzivView = LayoutInflater.from(parent.context).inflate(R.layout.maketforotziv, parent, false)
        return ViewHolder(otzivView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Получите текущий элемент из вашего набора данных
        val currentItem = dataSetOtziv[position]

        // Здесь вы можете заполнить элементы интерфейса данными из вашего объекта YourItem
        holder.Stars.setImageResource(when (currentItem.rating) {
            1.0f -> R.drawable.star1
            2.0f -> R.drawable.star2
            3.0f -> R.drawable.star3
            4.0f -> R.drawable.star4
            5.0f -> R.drawable.star5
            else -> R.drawable.star1
        })
    }

    override fun getItemCount(): Int {
        return dataSetOtziv.size
    }

    fun addItem(otziv: Otziv) {
        dataSetOtziv.add(otziv)
        notifyItemInserted(dataSetOtziv.size - 1)
    }

    fun removeLastItem() {
        if (dataSetOtziv.isNotEmpty()) {
            val lastPosition = dataSetOtziv.size - 1
            dataSetOtziv.removeAt(lastPosition)
            notifyItemRemoved(lastPosition)
        }
    }
}
