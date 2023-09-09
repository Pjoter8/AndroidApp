package pl.przyklad.firebase_yt_29_08


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.database.FirebaseDatabase

import kotlinx.coroutines.tasks.await
import pl.przyklad.firebase_yt_29_08.databinding.ActivityTeacherlistclassBinding

import kotlinx.coroutines.*

class Teacherlistclass : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherlistclassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherlistclassBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.IO).launch {
            val username = intent.getStringExtra("key28")

            if (username != null) {
                delay(400)
                val listaStringow = getFirebaseData(username)
                val adapter = MyAdapter(listaStringow)

            withContext(Dispatchers.Main) {

                binding.userList.layoutManager = LinearLayoutManager(applicationContext)
                binding.userList.adapter = adapter

            adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {

                    val classname:String = listaStringow[position]
                    val intent = Intent(this@Teacherlistclass, WordsList::class.java)
                    intent.putExtra("key29", username)
                    intent.putExtra("key30", classname)


                    startActivity(intent)
                }
            })}
        }}
        binding.addClassroundBtn.setOnClickListener {
            val username = intent.getStringExtra("key28")
            val intent = Intent(this, LoggedTeacher::class.java)
            intent.putExtra("key28", username)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        val intent = Intent(this, LoginTeacher::class.java)
        startActivity(intent)
    }

    private suspend fun getFirebaseData(username:String): List<String> {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Users/"+username+"/className")

        val listaStringow = mutableListOf<String>()


        val dataSnapshot = reference.get().await()
        if (dataSnapshot.exists()) {
            for (childSnapshot in dataSnapshot.children) {
                val value = childSnapshot.getValue(String::class.java)
                value?.let {
                    listaStringow.add(it)
                }
            }
        }

        return listaStringow
    }
}