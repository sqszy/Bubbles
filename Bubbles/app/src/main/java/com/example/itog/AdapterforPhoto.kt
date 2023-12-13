import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.itog.R
import com.example.itog.myPhoto

class AdapterforPhoto(private var yourDataSet: MutableList<myPhoto>, private val context: Context) : RecyclerView.Adapter<AdapterforPhoto.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Добавьте элементы интерфейса, которые вы хотите отображать в элементе списка
        // Например: val textView: TextView = itemView.findViewById(R.id.textView)
        val Photo: ImageView = itemView.findViewById(R.id.photo1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // Создание нового элемента списка (ViewHolder)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Наполнение элемента списка данными из yourDataSet[position]
        val currentItem = yourDataSet[position]
        val drawable: Drawable = ContextCompat.getDrawable(context,R.drawable.cornerforimage)!!
        holder.Photo.background = drawable
        holder.Photo.clipToOutline = true
        holder.Photo.setImageBitmap(currentItem.bitmap)
        val layoutParams = holder.Photo.layoutParams // Получаем текущие параметры LayoutParams

        if (position == itemCount - 1) {
            // Устанавливаем ширину для последнего элемента, чтобы он занимал всю доступную ширину
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        else {
            layoutParams.width = (240 * context.resources.displayMetrics.density).toInt()
        }
        // Применяем новые параметры LayoutParams
        holder.Photo.layoutParams = layoutParams
    }

    override fun getItemCount() = yourDataSet.size

    fun addPhoto(bitmap: Bitmap) {
        val newPhoto = myPhoto(bitmap)
        yourDataSet.add(newPhoto)
        notifyDataSetChanged()
    }

    fun updateDataSet(newDataSet: MutableList<myPhoto>) {
        yourDataSet = newDataSet
        notifyDataSetChanged()
    }

    fun removePhoto() {
        if (yourDataSet.size > 1) {
            val firstPhoto = yourDataSet[0]
            yourDataSet.clear()
            yourDataSet.add(firstPhoto) // Добавляем обратно первую фотографию
            notifyDataSetChanged()
        }
    }
}
