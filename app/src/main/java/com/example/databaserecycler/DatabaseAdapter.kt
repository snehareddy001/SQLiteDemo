package com.example.databaserecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.databaserecycler.databinding.ActivityMainBinding
import com.example.databaserecycler.databinding.DatabaseListBinding

class DatabaseAdapter(private val user: List<MyUser>):RecyclerView.Adapter<DatabaseAdapter.DatabaseViewHolder>() {
    private lateinit var binding:DatabaseListBinding

    inner class DatabaseViewHolder(private val binding: DatabaseListBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(dataItem:MyUser)
        {
            with(binding)
            {
                txtName.text=dataItem.name
                txtAge.text=dataItem.age.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder {
        binding= DatabaseListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DatabaseViewHolder(binding)
    }

    override fun getItemCount()=user.size


    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) {
        holder.bind(user[position])
    }
}