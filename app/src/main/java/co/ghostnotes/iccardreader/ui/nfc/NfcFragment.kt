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
        //private const val DATA_TYPE_TEXT_PLAIN = "text/plain"
        //private const val DATA_TYPE_APPLICATION = "application/co.ghostnotes.iccardreader"

        //private const val PENDING_INTENT_REQUEST_CODE = 1

        const val FRAGMENT_TAG = "co.ghostnotes.iccardreader.ui.main.NfcFragment"

        fun newInstance() = NfcFragment()
    }

    //private var nfcAdapter: NfcAdapter? = null
    //private lateinit var nfcPendingIntent: PendingIntent
    //private lateinit var readTagFilters: Array<IntentFilter>
    //private val techLists: Array<Array<String>> = arrayOf(arrayOf(NfcF::class.java.name))

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

        /*
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity!!.applicationContext)
        if (nfcAdapter == null) {
            showMessageAndFinish(R.string.error_message_nfc_is_not_available)
            return
        }

        if (!isNfcEnabled()) {
            showNfcEnableDialog()
            return
        }


        val intent = Intent(activity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        nfcPendingIntent = PendingIntent.getActivity(context, PENDING_INTENT_REQUEST_CODE, intent, 0)

        val ndefDiscovered = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)

        try {
            ndefDiscovered.addDataType(DATA_TYPE_APPLICATION)
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            Timber.e(e)
            // TODO
            throw RuntimeException(e)
        }

        readTagFilters = arrayOf(ndefDiscovered)
        */
    }

    /*
    override fun onResume() {
        super.onResume()
        Timber.d("### on resume.")

        if (this::nfcPendingIntent.isInitialized) {
            nfcAdapter!!.enableForegroundDispatch(activity, nfcPendingIntent, readTagFilters, techLists)
            Timber.d("### NFCAdapter enables the foreground dispatch.")
        }
    }
    */

    /*
    override fun onPause() {
        Timber.d("### on pause.")
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(activity)
    }
    */



    override fun showNfcEnableDialog() {
        val dialog = NfcNeedsToBeEnabledDialogFragment.newInstance()
        dialog.show(activity!!.supportFragmentManager, "NfcNeedsToBeEnabledDialogFragment")
    }

    //private fun isNfcEnabled(): Boolean = nfcAdapter!!.isEnabled

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
