package arnold.agura.com.stopalarmwatch.AlarmClock


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import arnold.agura.com.stopalarmwatch.MainActivity
import android.app.AlarmManager.OnAlarmListener

import arnold.agura.com.stopalarmwatch.R
import java.util.*
import kotlin.coroutines.experimental.suspendCoroutine
import android.media.RingtoneManager
import android.media.Ringtone
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.TextView
import android.widget.TimePicker
import kotlinx.android.synthetic.main.fragment_alarm_clockfragment.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AlarmClockFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AlarmClockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmClockFragment : Fragment() {

    internal lateinit var context : Context
    lateinit var alarmManager : AlarmManager
    lateinit var mTp: TimePicker
    lateinit var mText: TextView
    lateinit var mStart: Button
    lateinit var mStop: Button
    lateinit var pendingIntent: PendingIntent
    var hour: Int = 0
    var minute: Int = 0
    private var mButton : Button? = null
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }
    class Receiver : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("AlarmClockFragment", "Receiver : " + Date().toString() )
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_alarm_clockfragment, container, false)
        context = this.getContext()
        var intent: Intent = Intent(context,AlarmReceiver::class.java)

        alarmManager =  activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mTp = rootView!!.findViewById(R.id.timepicker)
        mText = rootView!!.findViewById(R.id.text)
        mStart = rootView!!.findViewById(R.id.btnStart)
        mStop = rootView!!.findViewById(R.id.btnStop)
        var calendar : Calendar = Calendar.getInstance()

        mStart.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(p0: View?) {

                mStop.visibility = View.VISIBLE
                mStart.visibility = View.INVISIBLE
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    calendar.set(Calendar.HOUR_OF_DAY,mTp.hour)
                    calendar.set(Calendar.MINUTE, mTp.minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = mTp.hour
                    minute = mTp.minute
                }
                else{
                    calendar.set(Calendar.HOUR_OF_DAY,mTp.currentHour)
                    calendar.set(Calendar.MINUTE, mTp.currentMinute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = mTp.currentHour
                    minute = mTp.currentMinute
                }
                var hr_str: String = hour.toString()
                var min_str: String = minute.toString()

                if(hour>12){
                    hr_str = (hour - 12).toString()
                }
                if(minute < 10){
                    min_str = "0$minute"
                }
                set_alarm_text("Alarm has set to : $hr_str : $min_str")
                intent.putExtra("extra", "on")
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        })
        mStop.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    Log.d("AlarmClockFragment", "Stop  " )
                mStop.visibility = View.INVISIBLE
                mStart.visibility = View.VISIBLE
                set_alarm_text("Alarm Off")
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)
                intent.putExtra("extra", "off")
                context.sendBroadcast(intent)
            }

        })


        return rootView
    }
    private fun set_alarm_text(s:String){
        mText.setText(s)

    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            Toast.makeText(context, "Alarm Attached", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmClockFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): AlarmClockFragment {
            val fragment = AlarmClockFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
