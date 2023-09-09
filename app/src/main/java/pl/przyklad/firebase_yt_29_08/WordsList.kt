package pl.przyklad.firebase_yt_29_08


import MyAdapterWords
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            val username = intent.getStringExtra("key29")

            if (classname != null&&username!=null) {
                val listDicts = getFirebaseData(classname)
                runOnUiThread {
                }
                val adapter = MyAdapterWords(listDicts)


                adapter.setOnItemLongClickListener(object : MyAdapterWords.OnItemLongClickListener {
                    override fun onItemLongClick(position: Int) {

                        val clickedItem = listDicts[position]
                        val ListLength :Int = listDicts.size
                        val MoveWord: Map<String,String> = listDicts[ListLength-1]


                        deleteItemFirebase(classname, position, ListLength, MoveWord,username)

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
            val username = intent.getStringExtra("key29")
            val intent = Intent(this, AddWords::class.java)
            intent.putExtra("key32", classname)
            intent.putExtra("key29", username)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val username = intent.getStringExtra("key29")
        val intent = Intent(this, Teacherlistclass::class.java)
        intent.putExtra("key28", username)
        startActivity(intent)
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

    private suspend fun getFirebaseClass(username: String): List<String> {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Users/$username/className")

        val listClass = mutableListOf<String>()

        val dataSnapshot = reference.get().await()
        if (dataSnapshot.exists()) {
            for (childSnapshot in dataSnapshot.children) {
                val value = childSnapshot.value as? String
                value?.let {
                    listClass.add(it)
                }
            }
        }

        return listClass
    }


    private fun deleteItemFirebase(classname: String, position:Int, ListLength:Int, MoveWord: Map<String, String>,username:String){
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Classes/"+classname+"/words")
        val indexToRemove = ListLength -1

        if (indexToRemove != 0) {
            val elementRefToWrite = reference.child(position.toString())
            elementRefToWrite.setValue(MoveWord)

            val elementRefToRemove = reference.child((ListLength-1).toString())
            elementRefToRemove.removeValue()

            Toast.makeText(this@WordsList, "Word Removed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, WordsList::class.java)
            intent.putExtra("key30", classname)
            intent.putExtra("key29", username)
            startActivity(intent)
        }
        else {
            val elementRefToRemove = reference.child((ListLength - 1).toString())
            elementRefToRemove.removeValue()


            val databaseUser = FirebaseDatabase.getInstance()
            val referenceUser = databaseUser.getReference("Users/$username/className")

            referenceUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val listClass = mutableListOf<String>()
                        for (childSnapshot in dataSnapshot.children) {
                            val value = childSnapshot.value as? String
                            value?.let {
                                listClass.add(it)
                            }

                        }


                        val positionOfDeletedClass = listClass.indexOf(classname)
                        val lengthlist = listClass.size

                        val elementRefToWrite2 = referenceUser.child(positionOfDeletedClass.toString())
                        elementRefToWrite2.setValue(listClass[lengthlist-1])


                        val elementRefToRemove2 = referenceUser.child((lengthlist-1).toString())
                        elementRefToRemove2.removeValue()

                        Toast.makeText(this@WordsList, "Deleted word and class", Toast.LENGTH_SHORT).show()

                    }

                }


                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@WordsList, "Error while fetching user classes", Toast.LENGTH_SHORT).show()
                }
            })


            val intent = Intent(this@WordsList, Teacherlistclass::class.java)
            intent.putExtra("key28", username)
            startActivity(intent)
        }

    }
}
