package com.example.databaserecycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databaserecycler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseadapter: DatabaseAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var users = mutableListOf<MyUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDatabase()
        fetchData()
        initViews()
    }

    private fun initViews() {
        with(binding)
        {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            databaseadapter = DatabaseAdapter(users)
            recyclerView.adapter = databaseadapter

            btnInsert.setOnClickListener {
                val txtName = titleTextView.text.toString()
                val txtAge = ageTextView.text.toString()
                val record = MyUser(id = null, txtName, txtAge)
                databaseHelper.insertData(record)
                users.add(record)
                databaseadapter.notifyItemInserted(users.size - 1)
                titleTextView.text?.clear()
                ageTextView.text?.clear()
            }

            btnUpdate.setOnClickListener {
                val txtName = titleTextView.text.toString()
                val txtAge = ageTextView.text.toString()
                val txtId = edtId.text.toString()
                val record = MyUser(id = txtId.toLong(), txtName, txtAge)

                databaseHelper.updateUser(record)
                val uresult=databaseHelper.updateUser(record)
                if(uresult.toInt()==-1){
                    Toast.makeText(this@MainActivity,"the user id doesnot exist",Toast.LENGTH_SHORT).show()
                }
                val users = databaseHelper.readData()

                databaseadapter = DatabaseAdapter(users)
                recyclerView.adapter = databaseadapter

                titleTextView.text?.clear()
                ageTextView.text?.clear()
                edtId.text?.clear()
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val user = users[position]
                    user.id?.let {
                        val deleted = databaseHelper.deleteUser(it)
                        if (deleted > 0) {
                            users.removeAt(position)
                            databaseadapter.notifyItemRemoved(position)
                            Toast.makeText(this@MainActivity, "this record is removed from database", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
            }).attachToRecyclerView(recyclerView)
        }
    }

    private fun fetchData() = users.addAll(databaseHelper.readData())
    private fun initDatabase() {
        databaseHelper = DatabaseHelper(this)
    }
}

