package arnold.agura.com.stopalarmwatch.AlarmClock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Arnold on 11 Feb 2018.
 */
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var getResult : String = p1!!.getStringExtra("extra")

        var service: Intent = Intent(p0, RingtoneService::class.java)
        service.putExtra("extra", getResult)
        p0!!.startService(service)
    }
}