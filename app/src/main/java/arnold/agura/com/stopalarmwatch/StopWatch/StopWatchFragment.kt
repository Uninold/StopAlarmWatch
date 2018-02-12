package arnold.agura.com.stopalarmwatch.StopWatch

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import arnold.agura.com.stopalarmwatch.R
import kotlinx.android.synthetic.main.fragment_stop_watch.*
import android.widget.Chronometer.OnChronometerTickListener


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StopWatchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StopWatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StopWatchFragment : Fragment() {
    var handler: Handler? = null
    var hour: TextView? = null
    var minute: TextView? = null
    var seconds: TextView? = null
    var milli_seconds: TextView? = null

    internal var MillisecondTime: Long = 0
    internal var StartTime: Long = 0
    internal var TimeBuff: Long = 0
    internal var UpdateTime = 0L

    internal var Time = 0L
    internal var Seconds: Int = 0
    internal var Minutes: Int = 0
    internal var MilliSeconds: Int = 0
    internal var flag:Boolean=false

    var mStartButton: ImageButton? = null
    var mReset: Button? = null
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater?.inflate(R.layout.fragment_stop_watch, container, false)
        hour = rootView!!.findViewById(R.id.hour)
        minute = rootView!!.findViewById(R.id.minute)
        seconds = rootView!!.findViewById(R.id.seconds)
        mStartButton = rootView!!.findViewById(R.id.startButton)
        milli_seconds = rootView!!.findViewById(R.id.milli_second)
        mReset = rootView!!.findViewById(R.id.reset)
        mStartButton?.setOnClickListener {
            if (flag){
                handler?.removeCallbacks(runnable)
                Time = Time + (SystemClock.uptimeMillis() - StartTime)
                startButton?.setImageResource(R.drawable.ic_play)
                flag=false
            }else{
                startButton?.setImageResource(R.drawable.pause)
                StartTime = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                flag=true
            }

        }
        mReset?.setOnClickListener{
            handler?.removeCallbacks(runnable)
            startButton?.setImageResource(R.drawable.ic_play)
            flag=false
            this.Time = 0L
            Seconds = 0
            Minutes = Seconds / 60

            Seconds = Seconds % 60

            MilliSeconds = 0


            if (Minutes.toString().length < 2) {
                minute?.text = "0" + Minutes.toString()
            } else {
                minute?.text = Minutes.toString()
            }
            if (Seconds.toString().length < 2) {
                seconds?.text = "0" + Seconds.toString()
            } else {
                seconds?.text = Seconds.toString()
            }

            milli_seconds?.text = MilliSeconds.toString()

        }


        handler = Handler()
        return rootView
    }

    var runnable: Runnable = object : Runnable {

        override fun run() {

            MillisecondTime = Time + (SystemClock.uptimeMillis() - StartTime)
            UpdateTime = TimeBuff + MillisecondTime

            Seconds = (UpdateTime / 1000).toInt()

            Minutes = Seconds / 60

            Seconds = Seconds % 60

            MilliSeconds = (UpdateTime % 1000).toInt()


            if (Minutes.toString().length < 2) {
                minute?.text = "0" + Minutes.toString()
            } else {
                minute?.text = Minutes.toString()
            }
            if (Seconds.toString().length < 2) {
                seconds?.text = "0" + Seconds.toString()
            } else {
                seconds?.text = Seconds.toString()
            }

            milli_seconds?.text = MilliSeconds.toString()

            handler?.postDelayed(this, 0)
        }

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
         * @return A new instance of fragment StopWatchFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): StopWatchFragment {
            val fragment = StopWatchFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
