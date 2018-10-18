package co.ghostnotes.iccardreader

import android.app.Application
import timber.log.Timber

class ICCardReaderApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    internal class CrashReportingTree: Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        }
    }

}