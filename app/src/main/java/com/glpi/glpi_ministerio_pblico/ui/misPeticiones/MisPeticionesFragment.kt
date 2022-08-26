package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.glpi.glpi_ministerio_pblico.ui.tickets.TicketsActivity
import com.glpi.glpi_ministerio_pblico.databinding.FragmentHomeBinding
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisPeticionesBinding
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity

class MisPeticionesFragment : Fragment() {
    private var _binding: FragmentMisPeticionesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMisPeticionesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val Item = requireActivity().intent.extras!!.getString("session_token")

        binding.tickets.setOnClickListener{
            //Toast.makeText(activity, "abriendo tickets", Toast.LENGTH_LONG ).show()
            val intent_tickets = Intent(activity, NavFooterTicketsActivity::class.java)
            startActivity(intent_tickets)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}