package co.ghostnotes.iccardreader.ui.nfc

import android.app.Dialog
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.ghostnotes.iccardreader.MainActivity
import co.ghostnotes.iccardreader.R
import timber.log.Timber
import java.lang.RuntimeException

class NfcFragment : Fragment(), NfcContract.View {

    companion object {
        const val FRAGMENT_TAG = "co.ghostnotes.iccardreader.ui.main.NfcFragment"

        fun newInstance() = NfcFragment()
    }

    private lateinit var viewModel: NfcViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Timber.d("### on create view.")
        lifecycle.addObserver(NfcLifecycleObserver(this))
        return inflater.inflate(R.layout.fragment_nfc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Timber.d("### on activity created.")
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(NfcViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun showNfcEnableDialog() {
        val dialog = NfcNeedsToBeEnabledDialogFragment.newInstance()
        dialog.show(activity!!.supportFragmentManager, NfcNeedsToBeEnabledDialogFragment::class.java.simpleName)
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    override fun showMessageAndFinish(@StringRes resId: Int) {
        showToast(resId)
        activity!!.finish()
    }

    override fun startWirelessSettings() {
        startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
    }

    internal class NfcNeedsToBeEnabledDialogFragment: DialogFragment() {
        companion object {
            fun newInstance() = NfcNeedsToBeEnabledDialogFragment()
        }

        private lateinit var view: NfcContract.View

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity!!)
                .setMessage(R.string.message_please_enable_nfc)
                .setPositiveButton(android.R.string.yes, OnPositiveButtonClickListener(view))
                .setNegativeButton(android.R.string.no, OnNegativeButtonClickListener(view))
                .create()
        }

        override fun onAttach(context: Context?) {
            super.onAttach(context)

            view = activity!!.supportFragmentManager.findFragmentByTag(NfcFragment.FRAGMENT_TAG) as NfcContract.View
        }

        internal class OnPositiveButtonClickListener(private val view: NfcContract.View): DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                view.startWirelessSettings()
            }
        }

        internal class OnNegativeButtonClickListener(private val view: NfcContract.View): DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                view.showMessageAndFinish(R.string.error_message_nfc_is_not_enabled)
            }
        }
    }

}
