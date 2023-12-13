package com.example.itog

import Marker
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
import com.example.itog.Item
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.geometry.Point

// Ваш адаптер
class adapterforcont(private var dataSet: MutableList<Item>, private var oldContainer: FrameLayout, private val context: Context, private var locationHelper: LocationHelper) : RecyclerView.Adapter<adapterforcont.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Здесь вы можете инициализировать элементы интерфейса для каждого элемента списка
        val Title: TextView = itemView.findViewById(R.id.Title)
        val Adres: TextView = itemView.findViewById(R.id.Adres)
        val Photo: ImageView = itemView.findViewById(R.id.Photo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Создаем новый элемент интерфейса из макета и передаем его в ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.maketforadd, parent, false)
        return ViewHolder(itemView)
    }
    private val fragmentMap = mutableMapOf<Int, InitialFragment>()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Получите текущий элемент из вашего набора данных
        var currentItem = dataSet[position]

        // Здесь вы можете заполнить элементы интерфейса данными из вашего объекта YourItem
        holder.Title.text = currentItem.title
        holder.Adres.text = currentItem.adres

        val drawable: Drawable = ContextCompat.getDrawable(context,R.drawable.cornercont)!!
        holder.Photo.background = drawable
        holder.Photo.clipToOutline = true
        holder.Photo.setImageBitmap(currentItem.bitmap)

        currentItem.fragm = position
        val fragmentId = currentItem.fragm
        holder.itemView.setOnClickListener {
            val fragmentManager = (holder.itemView.context as FragmentActivity).supportFragmentManager

            val existingFragment = fragmentMap[fragmentId] // Проверяем, есть ли уже сохраненный фрагмент по ID
            if (existingFragment != null) {
                // Если фрагмент найден в коллекции, просто показываем его
                oldContainer.visibility = View.GONE
                fragmentManager.beginTransaction()
                    .replace(R.id.bottom_layout, existingFragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                // Если фрагмент не найден, создаем новый, сохраняем его в коллекцию и показываем
                val newFragment = InitialFragment(currentItem, context, locationHelper)
                oldContainer.visibility = View.GONE
                fragmentManager.beginTransaction()
                    .replace(R.id.bottom_layout, newFragment)
                    .addToBackStack(null)
                    .commit()

                // Сохраняем фрагмент в коллекцию по его ID
                fragmentMap[fragmentId] = newFragment
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addItem(item: Item) {
        dataSet.add(item)
        notifyItemInserted(dataSet.size - 1)
    }

    fun removeItem(id: Int) {
        lateinit var removedItem: Item
        for (el in dataSet){
            if (el.id == id){
               removedItem = el
            }
        }
        val position = dataSet.indexOf(removedItem)
        fragmentMap.remove(removedItem.fragm)
        removedItem.marker.deleteMarker()
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        val db = Firebase.firestore
        db.collection("places")
            .whereEqualTo("id", removedItem.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val docRef = db.collection("places").document(document.id)
                    docRef.delete()
                        .addOnSuccessListener {
                            // Обработка успешного удаления документа
                        }
                        .addOnFailureListener { e ->
                            // Обработка ошибки удаления
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Ошибка при выполнении запроса
            }
        db.collection("photos")
            .whereEqualTo("id", removedItem.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val docRef = db.collection("photos").document(document.id)
                    docRef.delete()
                        .addOnSuccessListener {
                            // Обработка успешного удаления документа
                        }
                        .addOnFailureListener { e ->
                            // Обработка ошибки удаления
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Ошибка при выполнении запроса
            }
    }

    fun newadres(id: Int, adres: String){
        for (el in dataSet){
            if (el.id == id){
                var position = dataSet.indexOf(el)
                dataSet[position].adres = adres
                fragmentMap[dataSet[position].fragm] = InitialFragment(dataSet[position], context, locationHelper)
                notifyItemChanged(position)
            }
        }
    }
}
