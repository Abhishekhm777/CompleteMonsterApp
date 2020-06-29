package com.example.androiddata.splash

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.androiddata.R
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 */
const val PERMISSION_REQUEST_CODE=1001
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          if(ContextCompat.checkSelfPermission(
                  requireContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
          == PackageManager.PERMISSION_GRANTED){
              navigateToMainFrag()
          }else {
              requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
              PERMISSION_REQUEST_CODE)
          }




        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun navigateToMainFrag() {
        val navcontroler = Navigation.findNavController(
            requireActivity(), R.id.nav_host
        )
        navcontroler.navigate(
            R.id.action_splash
            , null,
            NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_REQUEST_CODE &&
            grantResults[0]==PackageManager.PERMISSION_GRANTED){
            navigateToMainFrag()
        }else{
            Toast.makeText(requireContext(),"Cant start withou permissin ",Toast.LENGTH_LONG).show()
        }
    }
}
