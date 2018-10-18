package co.ghostnotes.iccardreader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.ghostnotes.iccardreader.ui.nfc.NfcFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NfcFragment.newInstance(), NfcFragment.FRAGMENT_TAG)
                .commitNow()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Timber.d("### on new Intent.")
        super.onNewIntent(intent)
    }

}
