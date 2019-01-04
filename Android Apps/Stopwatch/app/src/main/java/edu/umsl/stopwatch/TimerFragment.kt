package edu.umsl.stopwatch

import android.app.Fragment
import android.os.*
import android.util.Log

class TimerFragment : Fragment() {

    /*The handler which will use runnable to get elapsed time.*/
    private var handler: Handler = Handler()

    private var startTime: Long = 0 /*The time the "timer" started*/
    private var elapsedTime: Long = 0 /*the total elapsedTime*/
    private var elapsedTimeMS: Long = 0 /*the recent elapsedTime*/
    private var offsetTime: Long = 0 /*The offset time from which the timer was paused.*/

    /*The listener so the TimerFragment may communicate with the activity.*/
    var timerFragmentListener: TimerFragmentListener ? = null

    interface TimerFragmentListener {
        fun timeElapsed(elapsedTime: Long) /*The time that has elapsed*/
        fun timeStopped(elapsedTime: Long) /*Dont need this*/
    }

    /*The tag used to locate the fragment*/
    companion object {
        const val TAG = "TimerFragmentTag"
    }

    /*Retain the instance during configuration changes*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    /*When the app is closed, remove all callbacks from the handler
    * and remove the reference from the listener.*/
    override fun onDestroy() {
        handler.removeCallbacks(runnable)

        timerFragmentListener = null

        Log.e("timerFrag", "onDestroy")
        super.onDestroy()
    }

    /*When the timer is started; the start time is passed as the argument,
    * which may be 0 or the time it was paused.*/
    fun startTimer(startTime: Long) {
        this.startTime = startTime

        /*Begin collecting the elapsed time*/
        handler.post(runnable)
    }

    /*When the timer is stopped*/
    fun stopTimer() {
        /*Remove callbacks from the handler and stop collecting elapsed time.*/
        handler.removeCallbacks(runnable)

        /*Get the offset time; this is always the offset time, plus the difference from when
        * the system was booted and the start time (elapsedTimeMS)*/
        offsetTime += elapsedTimeMS
    }

    /*Reset the timer. Reassign all values 0.*/
    fun resetTimer() {
        startTime = 0
        elapsedTime = 0
        elapsedTimeMS = 0
        offsetTime = 0
    }

    /*The run() method which will collect elapsed time.*/
    private var runnable = object: Runnable {
        override fun run() {
            /*The time difference from when the system was booted and the time the timer began.*/
            elapsedTimeMS = SystemClock.elapsedRealtime() - startTime

            /*elapsedTime is the offsetTime (if the timer was paused) plus the elapsedTimeMS.*/
            elapsedTime = offsetTime + elapsedTimeMS

            /*Notify the activity that time was collected (so the view and data must be updated).*/
            timerFragmentListener?.timeElapsed(elapsedTime)

            /*Continue invoking run()*/
            handler.post(this)
        }
    }


}
