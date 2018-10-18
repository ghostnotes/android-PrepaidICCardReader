package co.ghostnotes.iccardreader.ui.nfc

import android.app.Activity
import android.support.annotation.StringRes


internal interface NfcContract {

    interface View {

        fun getActivity(): Activity?

        fun startWirelessSettings()

        fun showNfcEnableDialog()

        fun showMessageAndFinish(@StringRes resId: Int)

    }

}