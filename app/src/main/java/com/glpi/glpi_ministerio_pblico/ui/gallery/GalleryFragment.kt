package com.glpi.glpi_ministerio_pblico.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.glpi.glpi_ministerio_pblico.LoginActivity
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.databinding.FragmentGalleryBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*inicio cerrar sesion
        prefer.delToken()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        // fin cerrar sesion*/

        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)



        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root



        /*val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it


        }*/



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}