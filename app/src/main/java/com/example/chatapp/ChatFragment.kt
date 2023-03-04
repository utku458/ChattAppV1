package com.example.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var  auth:FirebaseAuth
    private lateinit var firestore:FirebaseFirestore
    private lateinit var adapter: ChatRecyclerAdapter
    private var  chats = arrayListOf<ChatModule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore=Firebase.firestore
        auth=Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatRecyclerAdapter()
        binding.chatrecycler.adapter=adapter
        binding.chatrecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.sendbutton.setOnClickListener {


            auth.currentUser?.let {
                val chatText = binding.chattext.text.toString()
                val user=it.email.toString()
                val date = FieldValue.serverTimestamp()


                val dataMap = HashMap<String,Any>()
                dataMap.put("text",chatText)
                dataMap.put("useremail",user)
                dataMap.put("date",date)


                firestore.collection("Chats").add(dataMap).addOnSuccessListener {


                    binding.chattext.setText("")

                }.addOnFailureListener {

                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    binding.chattext.setText("")

                }


            }

        }

        firestore.collection("Chats").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->


            if (error!=null){
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }
            else{
                if(value!=null){
                    if (value.isEmpty){
                        Toast.makeText(requireContext(), "Mesaj Yok", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val documents = value.documents
                        chats.clear()
                        for(document in documents){
                            val text = document.get("text") as String
                            val user = document.get("useremail") as String
                            val chat=ChatModule(user,text)
                            chats.add(chat)
                            adapter.chats=chats

                        }

                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}