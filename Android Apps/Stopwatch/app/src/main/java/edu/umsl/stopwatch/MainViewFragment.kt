package edu.umsl.stopwatch

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_view.*

class MainViewFragment : Fragment() {

    /*Delegate for the fragment to communicate with the activity*/
    var delegate: MainViewFragmentDelegate? = null

    interface MainViewFragmentDelegate {
        fun startTimer() /*When the start button is clicked*/
        fun stopTimer() /*When the stop button is clicked*/
        fun resetTimer() /*When the reset button is clicked*/
        fun addLap() /*When the lap button is clicked*/

        fun updateTimer() /*To update the timer TextView during orientation changes*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        retainInstance = true
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        /*Inflate the view defined in R.layout.fragment_main_view*/
        return inflater!!.inflate(R.layout.fragment_main_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Update the timer TextView. This is especially important after orientation changes so
        * the correct time is displayed*/
        delegate?.updateTimer()
        bindButtons() /*Set the onClickListeners for each button*/

        /*If the savedInstanceState is not null, retrieve the "state" the buttons were
        * in during the previous orientation (if the start button was visible and the lap button
        * was disabled, etc)*/
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("startButton") == "true") {
                /*This means the start button was clickable and visible*/
                startButton.setEnabled(true)
                startButton.setVisibility(View.VISIBLE)
            }
            else {
                /*This means the start button was not clickable and invisible*/
                startButton.setEnabled(false)
                startButton.setVisibility(View.INVISIBLE)
            }

            if (savedInstanceState.getString("stopButton") == "true") {
                /*This means the stop button was clickable and visible*/
                stopButton.setEnabled(true)
                stopButton.setVisibility(View.VISIBLE)
            }
            else {
                /*This means the stop button was not clickable and invisible*/
                stopButton.setEnabled(false)
                stopButton.setVisibility(View.INVISIBLE)
            }

            if (savedInstanceState.getString("resetButton") == "true") {
                /*This means the reset button was clickable and visible*/
                resetButton.setEnabled(true)
                resetButton.setVisibility(View.VISIBLE)
            }
            else {
                /*This means the reset button was not clickable and invisible*/
                resetButton.setEnabled(false)
                resetButton.setVisibility(View.INVISIBLE)
            }

            if (savedInstanceState.getString("lapButton") == "true") {
                /*This means the lap button was clickable and visible*/
                lapButton.setEnabled(true)
                lapButton.setVisibility(View.VISIBLE)
            }
            else {
                /*This means the lap button was not clickable and invisible*/
                lapButton.setEnabled(false)
                lapButton.setVisibility(View.INVISIBLE)
            }
        }
        else {
            /*The default state when the app is started, in which only the start
            * button is clickable.*/
            lapButton.setEnabled(false)
            stopButton.setEnabled(false)
            resetButton.setEnabled(false)
        }
    }

    fun bindButtons() {
        /*Start incrementing the timer*/
        startButton.setOnClickListener {
            /*Disable and hide the start and reset buttons.*/
            startButton.setEnabled(false)
            startButton.setVisibility(View.INVISIBLE)

            resetButton.setEnabled(false)
            resetButton.setVisibility(View.INVISIBLE)

            /*Enable and show the stop and lap buttons.*/
            stopButton.setVisibility(View.VISIBLE)
            stopButton.setEnabled(true)

            lapButton.setEnabled(true)
            lapButton.setVisibility(View.VISIBLE)

            delegate?.startTimer()
        }

        /*Stop the timer*/
        stopButton.setOnClickListener {
            /*Disable and hide the stop and lap buttons.
            Enable and show the start and reset buttons.*/
            stopButton.setEnabled(false)
            stopButton.setVisibility(View.INVISIBLE)

            startButton.setVisibility(View.VISIBLE)
            startButton.setEnabled(true)

            lapButton.setEnabled(false)
            lapButton.setVisibility(View.INVISIBLE)

            resetButton.setEnabled(true)
            resetButton.setVisibility(View.VISIBLE)
            delegate?.stopTimer()
        }

        /*This will invoke the addLap() method so the activity can properly
        * pass data and update the view(s).*/
        lapButton.setOnClickListener {
            delegate?.addLap()
        }

        /*Reset the timer*/
        resetButton.setOnClickListener {
            /*Disable and hide the reset button.
            Disable and show the lap button.
            Enable and show the start button.*/
            resetButton.setEnabled(false)
            resetButton.setVisibility(View.INVISIBLE)

            lapButton.setEnabled(false)
            lapButton.setVisibility(View.VISIBLE)

            startButton.setEnabled(true)
            startButton.setVisibility(View.VISIBLE)

            delegate?.resetTimer()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    /*Save the "state" of the buttons when the view is destroyed.*/
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (startButton.visibility == View.VISIBLE) {
            outState!!.putString("startButton", "true")
        }
        else if(startButton.visibility == View.INVISIBLE) {
            outState!!.putString("startButton", "false")
        }

        if (stopButton.visibility == View.VISIBLE) {
            outState!!.putString("stopButton", "true")
        }
        else if(stopButton.visibility == View.INVISIBLE) {
            outState!!.putString("stopButton", "false")
        }

        if (resetButton.visibility == View.VISIBLE) {
            outState!!.putString("resetButton", "true")
        }
        else if(resetButton.visibility == View.INVISIBLE) {
            outState!!.putString("resetButton", "false")
        }

        if (lapButton.visibility == View.VISIBLE) {
            outState!!.putString("lapButton", "true")
        }
        else if(lapButton.visibility == View.INVISIBLE) {
            outState!!.putString("lapButton", "false")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /*When the app is closed, remove the reference to the delegate.*/
    override fun onDestroy() {
        delegate = null
        Log.e("MainViewFrag", "onDestroy")

        super.onDestroy()
    }

    /*Update the TextView to reflect the elapsed time.*/
    fun updateTimerText(elapsedTime: String) {
        //stopWatchClock.setText(elapsedTime)
        stopWatchClock.text = elapsedTime
    }


}
