package pl.przyklad.firebase_yt_29_08


import MyAdapterWords
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import pl.przyklad.firebase_yt_29_08.databinding.ActivityWordsListBinding
import kotlinx.coroutines.*

class WordsList : AppCompatActivity() {
    private lateinit var binding: ActivityWordsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            val classname = intent.getStringExtra("key30")

            if (classname != null) {
                val listDicts = getFirebaseData(classname)
                runOnUiThread {
                }
                val adapter = MyAdapterWords(listDicts)

                // Dodaj obsługę kliknięcia na elemencie listy
                adapter.setOnItemLongClickListener(object : MyAdapterWords.OnItemLongClickListener {
                    override fun onItemLongClick(position: Int) {
                        // Tutaj możesz obsłużyć kliknięcie na elemencie na liście
                        val clickedItem = listDicts[position] // Pobierz kliknięty element z listy
                        val ListLength :Int = listDicts.size
                        val MoveWord: Map<String,String> = listDicts[ListLength-1]


                        deleteItemFirebase(classname, position, ListLength, MoveWord)
                        // Możesz teraz wykorzystać te dane lub wykonać jakąś akcję w zależności od klikniętego elementu
                        Toast.makeText(this@WordsList, "Word Removed", Toast.LENGTH_SHORT).show()
                    }
                })

                withContext(Dispatchers.Main) {
                    binding.userList.layoutManager = LinearLayoutManager(applicationContext)
                    binding.userList.adapter = adapter
                }
            }
        }

        binding.addwordroundBtn.setOnClickListener {
            val classname = intent.getStringExtra("key30")
            val intent = Intent(this, AddWords::class.java)
            intent.putExtra("key32", classname)
            startActivity(intent)
        }
    }
    private suspend fun getFirebaseData(classname:String): List<Map<String, String>> {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Classes/"+classname+"/words")

        val listDicts = mutableListOf<Map<String, String>>()

        val dataSnapshot = reference.get().await()
        if (dataSnapshot.exists()) {
            for (childSnapshot in dataSnapshot.children) {
                val value = childSnapshot.value as? Map<String, String>
                value?.let {
                    listDicts.add(it)
                }
            }
        }

        return listDicts
    }
    private fun deleteItemFirebase(classname: String, position:Int, ListLength:Int, MoveWord: Map<String, String>){
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Classes/"+classname+"/words")
        val indexToRemove = ListLength -1

// Użyj metody removeValue() na odpowiedniej referencji
        if (indexToRemove != 0) {
            reference.child(indexToRemove.toString()).setValue(MoveWord)
            val elementRefToRemove = reference.child((ListLength-1).toString())
            elementRefToRemove.removeValue()
            val intent = Intent(this, WordsList::class.java)
            intent.putExtra("key30", classname)
            startActivity(intent)

        }
    }
}
