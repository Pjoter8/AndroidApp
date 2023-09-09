import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.przyklad.firebase_yt_29_08.databinding.WordItemBinding

class MyAdapterWords(private val words: List<Map<String, String>>) : RecyclerView.Adapter<MyAdapterWords.MyViewHolder>() {
    private var onItemLongClickListener: OnItemLongClickListener? = null

    inner class MyViewHolder(binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val wordAngTV = binding.TVwordAng
        val wordPolTV = binding.TVwordPol

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClickListener?.onItemLongClick(position) // Przekazanie numeru pozycji do metody onItemLongClick
                }
                true // Zwróć true, aby oznaczyć, że dłuższe kliknięcie zostało obsłużone
            }
        }
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val wordMap = words[position]
        holder.wordAngTV.text = wordMap.values.firstOrNull() // "ang" to klucz dla angielskiego słowa w mapie
        holder.wordPolTV.text = wordMap.keys.firstOrNull() // "pol" to klucz dla polskiego słowa w mapie
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val wordItemBinding = WordItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(wordItemBinding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }
}
