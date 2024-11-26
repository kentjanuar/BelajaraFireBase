package com.implisit.belajarafirebase

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    var data: MutableList<Map<String,String>> = ArrayList()
    val db = Firebase.firestore
    var DataProvinsi = ArrayList<daftarProfinsi>()
    lateinit var lvAdapter: SimpleAdapter
    lateinit var _etProvinsi: EditText
    lateinit var _etIbukota: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        _etProvinsi = findViewById(R.id.etProvinsi)
        _etIbukota = findViewById(R.id.etIbukota)
        val _btSimpan = findViewById<Button>(R.id.btSimpan)
        val _lvData = findViewById<ListView>(R.id.lvData)

        

        lvAdapter = SimpleAdapter(
            this,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("Pro", "Ibu"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        _lvData.adapter = lvAdapter

        _btSimpan.setOnClickListener {
            tambahData(db, _etProvinsi.text.toString(), _etIbukota.text.toString())
        }

        readData(db)
    }

    fun tambahData(db: FirebaseFirestore, Provinsi:String, Ibukota:String){
        val dataBaru = daftarProfinsi(Provinsi, Ibukota)
        db.collection("tbProvinsi")
            .add(dataBaru)
            .addOnSuccessListener {
                _etProvinsi.setText("")
                _etIbukota.setText("")
                Log.d("Firebase", "Data berhasil ditambahkan")
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

fun readData(db: FirebaseFirestore) {
    db.collection("tbProvinsi")
        .get()
        .addOnSuccessListener { result ->
            DataProvinsi.clear()

            for (document in result) {
                val readData = daftarProfinsi(
                    document.getString("profinsi").orEmpty(),
                    document.getString("ibukota").orEmpty()
                )
                DataProvinsi.add(readData)

                data.clear()
                DataProvinsi.forEach{
                    val dt: MutableMap<String, String> = HashMap(2)
                    dt["Pro"] = it.profinsi
                    dt["Ibu"] = it.ibukota
                    data.add(dt)
                }
            }

            lvAdapter.notifyDataSetChanged()
        }
        .addOnFailureListener {
            Log.d("Firebase", it.message.toString())
        }
}



}