package by.godevelopment.rsshool2021_android_task_storage_advanced.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.ItemCatBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat

class CatAdapter(
    private val onItemClick: ((position: Int) -> Unit)? = null
) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    private val dataList = mutableListOf<Cat>()

    fun setCatList(borrowedItemList: List<Cat>) {
        dataList.clear()
        dataList.addAll(borrowedItemList)
        notifyDataSetChanged()
    }

    class CatViewHolder(
        @NonNull private val binding: ItemCatBinding,
        private val onItemClick: ((position: Int) -> Unit)?
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

        fun bindTo(cat: Cat) {
            with (binding) {
                Name.text = ("Name cat: ${cat.name}").toString()
                Age.text = ("Age cat: ${cat.age}").toString()
                Breed.text = ("Breed cat: ${cat.breed}").toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder(ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = dataList[position]
        holder.bindTo(cat)
    }

    override fun getItemCount(): Int = dataList.size
}
