package pl.przyklad.firebase_yt_29_08

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.przyklad.firebase_yt_29_08.databinding.ClassItemBinding

class MyAdapter(private val contacts: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    inner class MyViewHolder(binding: ClassItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val classTV = binding.TVclassName
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val contactRowBinding = ClassItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(contactRowBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.classTV.text = contacts[position]

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}
