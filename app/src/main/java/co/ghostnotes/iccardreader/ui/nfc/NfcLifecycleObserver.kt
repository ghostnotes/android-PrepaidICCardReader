package co.ghostnotes.iccardreader.ui.nfc

import android.app.PendingIntent
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import co.ghostnotes.iccardreader.MainActivity
import co.ghostnotes.iccardreader.R
import timber.log.Timber

internal class NfcLifecycleObserver(private val view: NfcContract.View): LifecycleObserver {

    companion object {
        private const val DATA_TYPE_TEXT_PLAIN = "text/plain"
        private const val DATA_TYPE_APPLICATION = "application/co.ghostnotes.iccardreader"

        private const val PENDING_INTENT_REQUEST_CODE = 1
    }

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var nfcPendingIntent: PendingIntent
    private lateinit var readTagFilters: Array<IntentFilter>
    private val techLists: Array<Array<String>> = arrayOf(arrayOf(NfcF::class.java.name))


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun initializeNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(view.getActivity()!!.applicationContext)
        if (nfcAdapter == null) {
            view.showMessageAndFinish(R.string.error_message_nfc_is_not_available)
            return
        }

        if (!isNfcEnabled()) {
            view.showNfcEnableDialog()
            return
        }


        val intent = Intent(view.getActivity(), MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        nfcPendingIntent = PendingIntent.getActivity(view.getActivity()!!.applicationContext, PENDING_INTENT_REQUEST_CODE, intent, 0)

        val ndefDiscovered = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)

        try {
            ndefDiscovered.addDataType(DATA_TYPE_APPLICATION)
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            Timber.e(e)
            // TODO
            throw RuntimeException(e)
        }

        readTagFilters = arrayOf(ndefDiscovered)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun enableForegroundDispatch() {
        Timber.d("### on resume.")

        if (this::nfcPendingIntent.isInitialized) {
            nfcAdapter!!.enableForegroundDispatch(view.getActivity()!!, nfcPendingIntent, readTagFilters, techLists)
            Timber.d("### NFCAdapter enables the foreground dispatch.")
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disableForegroundDispatch() {
        Timber.d("### on pause.")

        nfcAdapter?.disableForegroundDispatch(view.getActivity()!!)
    }

    private fun isNfcEnabled(): Boolean = nfcAdapter!!.isEnabled

}