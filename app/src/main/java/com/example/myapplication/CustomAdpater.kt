package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dto.Massage

class CustomAdapter(private val data: List<Massage>) :

    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val rowItem = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return ViewHolder(rowItem, data)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        var text = data[position].login + ":\n" + data[position].content;

        if(text.length > 50)
        {
            text = text.substring(0, 45);
            text = text + "(. . .)"
            holder.textView.text = text;
        }
        else
        {
            holder.textView.text = text;
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun remove(position: Int):Massage {
        //Toast.makeText(view.context,"Go to edit massage", Toast.LENGTH_SHORT).show()
        //data[position].id;
        notifyItemRemoved(position);
        return data[position]
    }

    class ViewHolder(view: View, data: List<Massage>) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        val textView: TextView
        val hold = data;

        override fun onClick(view: View)
        {
            var shar = view.context.getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
            var nameGet = shar.getString("NAME", "")

            if(hold.size > layoutPosition)
            {
                if(hold[layoutPosition].login == nameGet) {

                    Toast.makeText(view.context,"Edit massage", Toast.LENGTH_SHORT).show()

                    val editor = shar.edit();
                    editor.putString("TEXT", hold[layoutPosition].content)
                    editor.putString("ID", hold[layoutPosition].id)
                    editor.putString("LOGIN", hold[layoutPosition].login)
                    editor.putBoolean("STATUS", true);
                    editor.apply()

                    val intent = Intent(view.context, edit::class.java)
                    view.context.startActivity(intent)
                }
                else
                {
                    Toast.makeText(view.context, "View massage", Toast.LENGTH_SHORT).show()
                    val editor = shar.edit();
                    editor.putString("TEXT", hold[layoutPosition].content)
                    editor.putString("ID", hold[layoutPosition].id)
                    editor.putString("LOGIN", hold[layoutPosition].login)
                    editor.putBoolean("STATUS", false);
                    editor.apply()

                    val intent = Intent(view.context, edit::class.java)
                    view.context.startActivity(intent)
                }
            }

        }

        init
        {
            view.setOnClickListener(this)
            textView = view.findViewById(R.id.textview)
        }
    }
}

