package edu.umsl.stopwatch

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_laps_listing.*

internal class LapsListingViewFragment: Fragment() {

    /*Instance of the adapter so it can be saved during screen orientation changes.*/
    private var recyclerAdapter: LapsAdapter? = null

    /*The listener to communicate with the activity*/
    var listener: LapsListingViewListener? = null

    /*The listener defines a variable to represent the number of laps
    * (items that will be displayed in the RecyclerView) and to
    * get an item at a specific position*/
    internal interface LapsListingViewListener {
        var lapsList: List<Laps>

        fun selectedItemAtPosition(position: Int)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        /*Inflate the RecyclerView defined in fragment_laps_listing*/
        val rootView: View? = inflater?.inflate(R.layout.fragment_laps_listing, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*If there was a screen rotation, this will retrieve the previous adapter. Otherwise,
        * this will be null.*/
        recyclerAdapter = ModelHolder.instance.getModel("adapter") as? LapsAdapter

        /*If savedInstanceState is null, this is the first invocation of the fragment;
        * initialize the layoutManager and adapter of the RecyclerView*/
        if (savedInstanceState == null) {
            lapsListingFragmentView?.apply {
                layoutManager = LinearLayoutManager(activity)
                recyclerAdapter = LapsAdapter()
                adapter = recyclerAdapter
                setHasFixedSize(true)
            }
        }
        else {
            /*Otherwise, get the previously saved adapter and set the adapter to it.*/
            lapsListingFragmentView?.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = recyclerAdapter
                setHasFixedSize(true)
            }
        }
    }

    /*When the activity and view is destroyed during orientation changes, save the adapter
    * so the recycler view uses the SAME adapter (this way, items can still be added and reflected
    * on the view after orientation change)*/
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        ModelHolder.instance.saveModel("adapter", recyclerAdapter)

        Log.e("onSaveInstance", "saved recycleradapter")
    }

    /*When the app is closed, remove the reference to the adapter.*/
    override fun onDestroy() {
        recyclerAdapter = null
        Log.e("LapsListingView", "onDestroy")

        super.onDestroy()
    }


    /*Inner class which defines the adapter to be used by the RecyclerView.*/
    inner class LapsAdapter: RecyclerView.Adapter<LapHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LapHolder {
            /*Inflate the view for each item within the RecyclerView*/
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.lap_item_layout, parent, false)

            /*Return an instance of LapHolder() with the view so the item view will be inflated
            * for each lap*/
            return LapHolder(view)
        }

        /*Method to get the items currently in the data set held by the adapter*/
        override fun getItemCount(): Int {
            return listener?.lapsList?.size ?: 0
        }

        /*Update the RecyclerView view contents with the specific view (LapHolder)
        * at the specified position*/
        override fun onBindViewHolder(holder: LapHolder?, position: Int) {
            /*To set the TextViews in each item view*/
            holder?.bindLap(listener?.lapsList?.get(position))
        }
    }

    /*The view for each item (lap) in the RecyclerView*/
    inner class LapHolder(view: View?): RecyclerView.ViewHolder(view), View.OnClickListener {
        /*TextViews for each item (lap)*/
        private val lapTitleView: TextView? = view?.findViewById(R.id.lapNumberTitleView)
        private val lapTimestampView: TextView? = view?.findViewById(R.id.lapTimestampView)

        /*The onClickListener method for each item view*/
        init {
            view?.setOnClickListener(this)
        }

        /*Set the TextViews in each item view to the lap number and lap interval*/
        fun bindLap(lap: Laps?) {
            lapTitleView?.text = resources.getString(R.string.lap_title_view_text, lap?.lapNumber)
            lapTimestampView?.text = resources.getString(R.string.lap_details_view_text, lap?.lapInterval)
        }

        /*When an item is clicked, this invokes the listener's selectedItemAtPosition method
        * which simply displays data about the Lap in the Logcat*/
        override fun onClick(v: View?) {
            listener?.selectedItemAtPosition(adapterPosition)
        }


    }
}