package id.rashio.android.ui.main.detection

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.databinding.FragmentDetectionBinding
import id.rashio.android.utils.getFileFromUri
import java.io.File

@AndroidEntryPoint
class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener{
            findNavController().navigate(DetectionFragmentDirections.actionDetectionFragmentToCameraFragment())
        }

        binding.galleryButton.setOnClickListener{
            startGallery()
        }

        binding.uploadButton.setOnClickListener {

        }


        setFragmentResultListener("requestImage") { requestKey, bundle ->
            bundle.getString("responseImage")?.let { imageFile = File(it) }
            if (imageFile != null) {
                Glide.with(requireContext())
                    .load(imageFile)
                    .into(binding.previewImageView)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    activity,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = getFileFromUri(uri, requireContext())
                imageFile = myFile
                if (imageFile != null) {
                    Glide.with(requireContext())
                        .load(imageFile)
                        .into(binding.previewImageView)
                }
            }
        }
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}