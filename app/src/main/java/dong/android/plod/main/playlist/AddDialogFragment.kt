package dong.android.plod.main.playlist

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dong.android.plod.R
import dong.android.plod.databinding.FragmentAddDialogBinding
import dong.android.plod.model.SongData

class AddDialogFragment : DialogFragment() {
    lateinit var binding: FragmentAddDialogBinding

    private val activityViewModel: PlayListViewModel by activityViewModels()
    private val viewModel: AddDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_dialog, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivDone.setOnClickListener {
            activityViewModel.emitSongData(
                SongData(
                    viewModel.song.value!!,
                    viewModel.singer.value!!,
                    viewModel.genre.value!!,
                    ""
                )
            )
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.add_dialog_frame)
        dialog.apply {
            setCanceledOnTouchOutside(true)
        }
        return dialog
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val x = (size.x * 0.75).toInt()

            this.dialog?.window?.setLayout(x, -2)

        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val x = (rect.width() * 0.75).toInt()

            this.dialog?.window?.setLayout(x, -2)
        }
    }
}