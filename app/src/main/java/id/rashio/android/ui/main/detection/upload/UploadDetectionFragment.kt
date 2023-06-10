package id.rashio.android.ui.main.detection.upload

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.rashio.android.R
import id.rashio.android.databinding.FragmentUploadDetectionBinding
import id.rashio.android.utils.getFileFromUri
import id.rashio.android.utils.reduceFileImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class UploadDetectionFragment : Fragment() {

    private var _binding: FragmentUploadDetectionBinding? = null
    private val binding get() = _binding!!

    private var imageFile: File? = null
    private val viewModel: UploadDetectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUploadDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navView.setupWithNavController(findNavController())

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener {
            findNavController().navigate(UploadDetectionFragmentDirections.actionDetectionFragmentToCameraFragment())
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.uploadButton.setOnClickListener {
            uploadImage()
            binding.progressBar.visibility = View.VISIBLE
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when (it) {
                    is UploadDetectionUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UploadDetectionUiState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Snackbar.make(
                            requireView(),
                            it.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is UploadDetectionUiState.Success -> {
                        if (it.navigateToResult == true && it.diseaseName != null && it.percentage != null) {
                            findNavController().navigate(
                                UploadDetectionFragmentDirections.actionDetectionFragmentToResultDetectionFragment(
                                    diseaseName = it.diseaseName,
                                    percentage = it.percentage
                                )
                            )
                            viewModel.navigatedToResult()
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                }

            }
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

    private fun uploadImage() {

        if (imageFile != null) {
            val file = reduceFileImage(imageFile as File)
            viewModel.uploadFile(file)

        } else {
            Toast.makeText(
                requireContext(),
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()

        }

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

    override fun onStart() {
        super.onStart()
        binding.navView.selectedItemId = R.id.detectionFragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}