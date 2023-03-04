package com.example.chatapp

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth


    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val currentuser = auth.currentUser

        if (currentuser!=null){
            val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
            findNavController().navigate(action)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        binding.signupbtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.emailtext.text.toString(),binding.passwordtext.text.toString()).addOnSuccessListener {

                val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                findNavController().navigate(action)



            }.addOnFailureListener {exeption->
                Toast.makeText(requireContext(), exeption.localizedMessage, Toast.LENGTH_LONG).show()

            }

        }
        binding.button.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.emailtext.text.toString(),binding.passwordtext.text.toString()).addOnSuccessListener {

                val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                findNavController().navigate(action)

            }.addOnFailureListener {exeption->
                Toast.makeText(requireContext(), exeption.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}