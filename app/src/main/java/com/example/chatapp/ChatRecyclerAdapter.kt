package com.example.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerAdapter():RecyclerView.Adapter<ChatRecyclerAdapter.ChatHolder>() {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECIEVED = 2

    class ChatHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<ChatModule>(){
        override fun areItemsTheSame(oldItem: ChatModule, newItem: ChatModule): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ChatModule, newItem: ChatModule): Boolean {
            return oldItem.equals(newItem)
        }

    }

    private val recyclerListDiffer =AsyncListDiffer(this,diffUtil)

    var chats:List<ChatModule>
    get() =recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {

        val chat = chats.get(position)

        if (chat.user==FirebaseAuth.getInstance().currentUser?.email.toString())
        {
            return VIEW_TYPE_MESSAGE_SENT

        }else{
            return VIEW_TYPE_MESSAGE_RECIEVED

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {

        if (viewType==VIEW_TYPE_MESSAGE_RECIEVED){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
            return ChatHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_right,parent,false)
            return ChatHolder(view)
        }


    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val textView  = holder.itemView.findViewById<TextView>(R.id.recyclertext)
        textView.setText("${chats.get(position).user} : ${chats.get(position).text}")
    }
}