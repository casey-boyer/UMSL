package edu.umsl.stopwatch

import android.app.Activity
import android.os.Bundle
import android.util.Log
import edu.umsl.stopwatch.database.DBHelper
import edu.umsl.stopwatch.database.LapsPersistence
import kotlinx.android.synthetic.main.fragment_laps_listing.*

class MainActivity : Activity(), MainViewFragment.MainViewFragmentDelegate {

    private var viewFragment: MainViewFragment? = null /*The View fragment for the timer and buttons*/
    private var lapViewFragment: LapsListingViewFragment? = null /*The RecyclerView fragment*/
    private var timerFragment: TimerFragment? = null /*The fragment which performs the handler task*/
    private var model: MainModel? = null /*This model updates time changes and the timestamp*/
    private var lapModel: LapsModel? = null /*This model updates lap insertions, removals, and the lap interval*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Initialize lapModel by getting all rows of the Laps table
        * and passing this (persistence) to the constructor.*/
        val dbHelper = DBHelper(this@MainActivity)
        val persistence = LapsPersistence(dbHelper)

        /*If the savedInstanceState is not null, retrieve the data (lapModel and model)
        * and the saved fragments.*/
        if (savedInstanceState != null) {
            lapModel = ModelHolder.instance.getModel("lapsModel") as? LapsModel
            model = ModelHolder.instance.getModel("mainModel") as? MainModel

            if (lapModel == null) {
                Log.e("MainActivity", "ONCREATE, LAPMODEL NULL")
                lapModel = LapsModel(persistence)
                lapModel?.destroy()
            }

            if (model == null) {
                Log.e("MainActivity", "ONCREATE, MODEL NULL")
                model = MainModel()
            }

            /*Get the viewFragment*/
            viewFragment = fragmentManager.getFragment(savedInstanceState, "viewFragment") as? MainViewFragment

            /*Get the lapViewFragment*/
            if (savedInstanceState.get("lapFragment") != null) {
                lapViewFragment = fragmentManager.getFragment(savedInstanceState, "lapFragment") as? LapsListingViewFragment
            }

            Log.e("MainActivity", "viewFragment null=${viewFragment == null}")
            Log.e("MainActivity", "lapViewFragment null=${lapViewFragment == null}")
            Log.e("MainActivity", "lapView listener null=${lapViewFragment?.listener == null}")

            if (viewFragment != null) {
                /*Display the view fragment*/
                if (viewFragment?.isAdded == true) {
                    Log.e("MAINACTIVITY", "VIEW FRAGMENT ADDED TRUE")
                    fragmentManager.beginTransaction().show(viewFragment).commit()
                }
            }

            /*Display the lapViewFragment*/
            if (lapViewFragment != null) {
                if (lapViewFragment?.isAdded == true)
                    fragmentManager.beginTransaction().show(lapViewFragment).commit()

                lapsListingFragmentView?.adapter?.notifyDataSetChanged()
            }

        }
        else {

            /*Otherwise, initialize the models and fragments.*/
            lapModel = LapsModel(persistence)

            /*This is to remove laps that may have not been removed upon abnormal termination, though
            * that should not be an issue since they are definitely removed in onDestroy().*/
            lapModel?.destroy()
            lapModel = LapsModel(persistence)
            model = MainModel()

            /*Layout fragment that will display the timer, start/stop and lap/reset buttons.*/
            viewFragment = fragmentManager.findFragmentById(R.id.fragment_container) as? MainViewFragment

            if (viewFragment == null) {
                viewFragment = MainViewFragment()

                /*Replace the container (R.id.relativeLayout) with the layout inflated
                * by the viewFragment in its onCreateView method*/
                fragmentManager.beginTransaction()
                        .replace(R.id.relativeLayout, viewFragment).commit()
            }

            /*Layout fragment that will display the laps in a RecyclerView. The layout is added
            * to the view later whenever laps are added.*/
            lapViewFragment = fragmentManager.findFragmentById(R.id.fragment_container) as? LapsListingViewFragment

            if (lapViewFragment == null) {
                lapViewFragment = LapsListingViewFragment()

                /*Replace the container (R.id.recyclerLayout) with the view inflated
                * in lapViewFragment onCreateView*/
                fragmentManager.beginTransaction()
                        .replace(R.id.recyclerLayout, lapViewFragment)
                        .commit()

                /*Initialize lapsList to the amount of laps/rows in the database*/
                lapViewFragment?.listener = object: LapsListingViewFragment.LapsListingViewListener {
                    override var lapsList: List<Laps> = lapModel?.laps ?: listOf()
                        get() = lapModel?.laps ?: listOf()

                    /*This method simply returns the lap at the position clicked in the recycler view,
                    * and then logs the data to Logcat*/
                    override fun selectedItemAtPosition(position: Int) {
                        val lap = lapModel?.laps?.get(position)
                        Log.e("ACTIVITY", "Lap Data: ${lap.toString()}")
                    }

                }
            }

            /*The fragment which will get the elapsed time since the clock was started
            * and when it is paused.*/
            timerFragment = fragmentManager.findFragmentByTag(TimerFragment.TAG) as? TimerFragment

            if (timerFragment == null) {
                timerFragment = TimerFragment()
                fragmentManager.beginTransaction().add(timerFragment, TimerFragment.TAG).commit()
            }

            /*Set the listeners of the fragments*/

            viewFragment?.delegate = this
            timerFragment?.timerFragmentListener = timerFragmentListener
        }
    }

    /*Delete all laps upon destroying the activity*/
    override fun finish() {
        super.finish()

        lapModel?.destroy() /*Will delete all the laps*/
    }

    /*When the activity is destroyed, the app is either closed or a screen orientation
    * change took place*/
    override fun onDestroy() {
        super.onDestroy()

        /*If isFinishing() is true, then the app was closed.*/
        if (isFinishing()) {
            /*Remove references to the model, lapModel, and fragments.*/
            model = null
            lapModel = null
            viewFragment = null
            timerFragment = null
            lapViewFragment = null
        }

    }

    /*When the user presses the back button, they will be brought to their previously opened
    * app or the home screen. This stops the timer and then onDestroy is called no matter what.*/
    override fun onBackPressed() {
        timerFragment?.stopTimer()

        super.onBackPressed()
    }

    /*Save the fragments in the bundle so the may be retrieved during screen orientation changes.*/
    override fun onSaveInstanceState(outState: Bundle?) {
        fragmentManager.putFragment(outState, "viewFragment", viewFragment)
        Log.e("MainActivity", "onSavedInstanceStated")

        if (lapViewFragment != null) {
            fragmentManager.putFragment(outState, "lapFragment", lapViewFragment)
        }
        super.onSaveInstanceState(outState)

    }

    /*Save the model data, model and lapModel, during screen orientation change*/
    override fun onRetainNonConfigurationInstance(): Any {
        Log.e("MainActivity", "onRetainNonConfig")

        var modelHolder = ModelHolder

        /*Save the model data on screen orientation change*/
        modelHolder.instance.saveModel("mainModel", model)
        modelHolder.instance.saveModel("lapsModel", lapModel)
        modelHolder.instance.saveModel("adapter", lapsListingFragmentView.adapter)

        return modelHolder
    }

    /*Listener for the timer fragment so the view may be updated accordingly as time passes.*/
    private val timerFragmentListener = object: TimerFragment.TimerFragmentListener {
        override fun timeElapsed(elapsedTime: Long) {
            /*Update the total time by passing the elapsedTime argument to the updateTime()
            * method of MainModel.*/
            model?.updateTime(elapsedTime)

            /*Invoke the updateTimerText() method of the view fragment with the current
            * timeStamp so the textView reflects the amount of time elapsed.*/
            viewFragment?.updateTimerText(model?.getTimeStamp()!!)
        }

        override fun timeStopped(elapsedTime: Long) {
            /*Save the time the timer stopped at*/
        }
    }

    /*When the start button is clicked, the delegate of mainViewFragment will invoke startTimer().
    * Invoke the startTimer() method of the timerFragment, passing the time to start counting up by.*/
    override fun startTimer() {
        timerFragment?.startTimer(model?.getStartTime()!!)
    }

    /*When the stop button is clicked, the delegate of mainViewFragment will invoke stopTimer().
    * Invoke the stopTimer() method of the timerFragment to remove the Handler's runnable callback.*/
    override fun stopTimer() {
        timerFragment?.stopTimer()
    }

    /*When the reset button is clicked, the delegate of mainViewFragment will invoke resetTimer().
    * Delete the laps recorded/stored in the laps database and reset the timer.*/
    override fun resetTimer() {
        lapModel?.destroy() /*Destroy (remove) all laps and update laps array*/

        /*Update the lapsList variable to reflect no laps*/
        lapViewFragment?.listener?.lapsList = lapModel?.laps ?: listOf()

        /*Notify adapter that all laps were removed*/
        lapsListingFragmentView?.adapter?.notifyItemRangeRemoved(0, model?.getLapNumbers()!!)

        timerFragment?.resetTimer()
        model?.reset()
        viewFragment?.updateTimerText(model?.getTimeStamp()!!)
    }

    /*When the lap button is clicked, the delegate of mainViewFragment will invoke addLap().*/
    override fun addLap() {

        val dbHelper = DBHelper(this@MainActivity)
        val persistence = LapsPersistence(dbHelper)
        if (lapModel == null) {
            lapModel = LapsModel(persistence)
        }

        /*Invoke insertLap() method of lapModel to insert the lap into the database, passing
        * the lap number and time the lap was recorded*/
        lapModel?.insertLap(model!!.getLapNumbers(), model!!.getTimeStamp())
        model?.updateLaps() /*Update total number of laps*/

        /*Update the laps variable of lapModel to reflect the total number of laps/rows
        * in the database*/
        lapModel?.updateLaps()

        /*Update lapsList var of lapViewFragment to reflect the total number of laps*/
        lapViewFragment?.listener?.lapsList = lapModel?.laps ?: listOf()
        //Log.e("MainActivity LAP!!", "lapsList size=${lapViewFragment?.listener?.lapsList?.size}")

        /*Notify the adapter of the recyclerView that a lap/item was inserted, passing the
        * position as the number of the most recently added lap*/

        if (lapsListingFragmentView?.adapter != null) {
            //Log.e("MainActivity", "NOTIFIED ADAPTER!!!")
            lapsListingFragmentView?.adapter?.notifyDataSetChanged()
            lapsListingFragmentView?.adapter?.notifyItemInserted(model!!.getLapNumbers())
        }
    }

    /*When the updateTimer is invoked from the mainViewFragment, get the timeStamp
    * from the MainModel model object, and invoke the fragment's updateTimerText() method
    * with this timestamp*/
    override fun updateTimer() {
        viewFragment?.updateTimerText(model?.getTimeStamp()!!)
    }

}
