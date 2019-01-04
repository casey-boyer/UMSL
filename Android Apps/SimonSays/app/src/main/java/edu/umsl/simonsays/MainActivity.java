package edu.umsl.simonsays;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends Activity implements MainActivityViewFragment.MainActivityViewFragmentListener {

    /*Declare variables for the view fragment, MainActivityViewFragment object,
    * the ColorModel object which will store data for the game, and the Handler
    * and Runnable objects which will delay the Result Screen activity to show
    * the user the correct color when it is game over.*/
    private MainActivityViewFragment mainActivityFragment = null;
    private ColorModel mColorModel;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Set the content view to the blank layout, fragment_container_layout.xml*/
        setContentView(R.layout.fragment_container_layout);

        /*Initialize the ColorModel object.*/
        mColorModel = new ColorModel();

        /*Get the difficulty level that the user selected on the Start screen,
        * by calling the getStringExtra from the intent with the String argument "level";
        * parse this value to an int so it may be stored in the ColorModel object.*/
        int level = Integer.parseInt(getIntent().getStringExtra("level"));

        /*Store the difficulty level in the ColorModel object by calling its method
        * setLevel, passing the level retrieved from the StartScreenActivity.*/
        mColorModel.setLevel(level);

        /*Attempt to retrieve the high score from the StartScreenActivity. If this is the
        * first game since the app has been launched, then the user does not yet have
        * a high score, so call the ColorModel's method setHighScore  with an argument of 0.
         * Otherwise, if the value of the String "score" from the StartScreenActivity is not
         * null, then parse the value to an int, and call the ColorModel's method setHighScore,
         * passing the string as an int to store the current high score.*/
        if(((getIntent().getStringExtra("score")) != null)) {
            mColorModel.setHighScore(Integer.parseInt(getIntent().getStringExtra("score")));
        }
        else {
            mColorModel.setHighScore(0);
        }

        /*Declare and initialize a FragmentManager object to attempt to add the MainActivityViewFragment.*/
        FragmentManager fragmentManager = getFragmentManager();

        /*Use the FragmentManager object to find the fragment ID declared in the fragment_container_layout
        * xml file, and cast the result to MainActivityViewFragment.*/
        mainActivityFragment = (MainActivityViewFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        /*If mainActivityFragment is null, initialize the fragment, then use the fragmentManager
        * to begin the FragmentTransaction, add the fragment to the activity, and commit
        * the transaction..*/
        if(mainActivityFragment == null) {
            mainActivityFragment = new MainActivityViewFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, mainActivityFragment).commit();
        }

        /*Initialize the listener of the MainActivityViewFragment to ensure that
        * MainActivity implements the mainActivityListener interface.*/
        mainActivityFragment.mainActivityListener = this;
    }

    /*Override the onResume method and call the superclass onResume method,
    * then begin the first sequence.*/
    @Override
    protected void onResume() {
        super.onResume();

        /*Start the first animation sequence by triggering the mainActivityFragment's
        * animateSequence method, and passing the sequence, animation start delay and duration,
        * and timer from the ColorModel object.*/
        mainActivityFragment.animateSequence(mColorModel.getSequence(),
                mColorModel.getStartDelay(), mColorModel.getAnimationDuration(), mColorModel.getTimer());
    }

    /*Prevent the user from going back to the StartScreenActivity.*/
    @Override
    public void onBackPressed() {
    }

    /*Override the onDestroy method and call the superclass onDestroy method,
        * and clean up all resources used by MainActivity.*/
    @Override
    protected void onDestroy() {
        /*Remove all callbacks to the Handler object.*/
        handler.removeCallbacks(runnable);

        /*Remove the reference to the handler, runnable, mColorModel,
        * and mainActivityFragment objects so they can be collected
        * by the garbage collector. Additionally, this will trigger
        * the onDestroy method of the mainActivityFragment, ensuring that the
        * resources used in the fragment are also collected by the garbage
        * collector.*/
        handler = null;
        runnable = null;
        mColorModel = null;
        mainActivityFragment = null;

        Log.e("onDestroy", "Cleaned up resources in MainActivity.");

        super.onDestroy();
    }

    /*Override the userValue(int) method defined in the mainActivityListener interface.
    * This method tests the button selected by the user to determine if the
    * selection was true, if the selection was false, and if the end of the current
    * color sequence has been reached. It accepts the String Resource ID of the button's
    * color as an argument.*/
    @Override
    public void userValue(int color) {
        /*Log message to document the color selected by the user.*/
        Log.e("userValue", "color="+color);

        /*Pass the user's selection to the ColorModel object.*/

        /*If the button selected by the user is incorrect, the gameOver variable
        * in mColorModel will be true, and so gameOver is true.*/
        if( mColorModel.getUserSelection(color) == true ) {

            /*Disable the clickable property of the buttons so the user
            * cannot click a button while the correct button is being flashed.*/
            mainActivityFragment.enableButtons(false);

            /*Call the flashCorrectColor() method defined in the mainActivityFragment
            * object, and call the mColorModel getColor() method to pass the correct color
             * (the color which was supposed to be clicked).*/
            mainActivityFragment.flashCorrectColor(mColorModel.getColor());

            /*Initialize the handler and implement the run() method of the
            * Runnable object; the run() method will be triggered after 3 seconds,
            * because a runnable object and 3000 milliseconds are passed as
            * arguments to handler's postDelayed() method.*/
            handler = new Handler();
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    /*Define and initialize an Intent object, passing the current context (MainActivity)
                    * and the ResultsScreenActivity class that will be executed with the startActivity()
                    * method; this way, the screen will transition to the ResultsScreenActivity.*/
                    Intent intent = new Intent(MainActivity.this, ResultsScreenActivity.class)
                            .putExtra("currentScore", String.valueOf(mColorModel.getScore()));

                    /*If the mColorModel's current highScore variable is greater than the current
                    * game's score variable, then use the putExtra() method of the intent to
                    * pass the string "score" and the value of the game's score to the results
                    * screen.*/
                    if(mColorModel.getHighScore() > mColorModel.getScore()) {
                        /*Pass the String "score" to the intent with the value of the
                        parsed mColorModel's highScore variable to a String.*/
                        intent.putExtra("score", String.valueOf(mColorModel.getHighScore()));
                        Log.e("gameOver", "high score= "+mColorModel.getHighScore());
                    }
                    else {
                        /*Otherwise, if mColorModel's highScore variable is not greater than
                        * the current game's score variable, pass the mColorModel's score variable
                        * as a string to the intent, using the putExtra() method.*/
                        intent.putExtra("score", String.valueOf(mColorModel.getScore()));
                        Log.e("gameOver", "score= "+mColorModel.getScore());
                    }
                    /*Start the ResultsScreenActivity.*/
                    startActivity(intent);
                    /*Call the finish() method, so the onDestroy() method of MainActivity is
                    * triggered, which will clean up all the resources used.*/
                    finish();
                }
            }, (3000)); /*Call the run() method after 3 milliseconds/3 seconds have passed.*/

            Log.e("mainActivity", "gameOver is true");
        }
        else if(mColorModel.getIsEndSequence()) {
            /*Otherwise, the user selected the correct color and it is the end of the
            * color sequence. Update the score stored in the mColorModel object.*/
            mColorModel.updateScore();
            /*Animate the next sequence by triggering the animateSequence() method of the mainActivityFragment,
            * passing the sequence generated by mColorModel, and the mColorModel variables
            * animation start delay and animation duration, and the timer.*/
            mainActivityFragment.animateSequence(mColorModel.getSequence(),
                    mColorModel.getStartDelay(), mColorModel.getAnimationDuration(), mColorModel.getTimer());
        }
    }

    /*Override the timerExpired() method defined in the mainActivityListener interface.
    * The purpose of this method is to retrieve the color from mColorModel that the user failed to
    * click before the timer ended, and notify the user of the necessary color before
    * transitioning to the ResultsScreenActivity.*/
    @Override
    public void timerExpired() {
        /*Disable the clickable property of the buttons so the user cannot click any
        * buttons while the missed button is flashed and before the transition to the
        * ResultsScreenActivity.*/
        mainActivityFragment.enableButtons(false);

        /*Trigger the flashCorrectColor() method of the mainActivityFragment, and
        * pass the color that was missed/not selected in time by calling the
        * getColor() method of the mColorModel object.*/
        mainActivityFragment.flashCorrectColor(mColorModel.getColor());

        /*Initialize the handler and implement the run() method of the
        * Runnable object; the run() method will be triggered after 3 seconds,
        * because a runnable object and 3000 milliseconds are passed as
        * arguments to handler's postDelayed() method.*/
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                /*Define and initialize an Intent object, passing the current context (MainActivity)
                 * and the ResultsScreenActivity class that will be executed with the startActivity()
                 * method; this way, the screen will transition to the ResultsScreenActivity.
                 * Additionally, use the putExtra method and pass a String "currentScore" with the
                 * value of the score (parsed as a String) so that ResultsScreenActivity may display
                 * the score for the most recent round.*/
                Intent intent = new Intent(MainActivity.this, ResultsScreenActivity.class)
                        .putExtra("currentScore", String.valueOf(mColorModel.getScore()));;

                /*If the mColorModel's current highScore variable is greater than the current
                 * game's score variable, then use the putExtra() method of the intent to
                 * pass the string "score" and the value of the game's score to the results
                 * screen.*/
                if(mColorModel.getHighScore() > mColorModel.getScore()) {
                    /*Pass the String "score" to the intent with the value of the
                    * parsed mColorModel's highScore variable to a String.*/
                    intent.putExtra("score", String.valueOf(mColorModel.getHighScore()));
                    Log.e("gameOver", "high score= "+mColorModel.getHighScore());
                }
                else {
                    /*Otherwise, if mColorModel's highScore variable is not greater than
                    * the current game's score variable, pass the mColorModel's score variable
                    * as a string to the intent, using the putExtra() method.*/
                    intent.putExtra("score", String.valueOf(mColorModel.getScore()));
                    Log.e("gameOver", "score= "+mColorModel.getScore());
                }

                /*Start the ResultsScreenActivity.*/
                startActivity(intent);

                /*Call the finish() method, so the onDestroy() method of MainActivity is
                * triggered, which will clean up all the resources used.*/
                finish();
            }
        }, (3000)); /*Call the run() method after 3 milliseconds/3 seconds have passed.*/

    }

    /*Override the getScore() method defined in the mainActivityListener interface.
    * The method returns an int value, specifically the score variable stored in
    * the mColorModel object.*/
    @Override
    public int getScore() {
        Log.e("mainActivity", "score="+mColorModel.getScore());

        /*Call the getScore() method of the mColorModel object to retrieve the score
        * for the current game, and return this int value.*/
        return mColorModel.getScore();
    }

    /*Override the getHighScore() method defined in the mainActivityListener interface.
    * The method returns an int value, specifically the highScore variable stored in the
    * mColorModel object.*/
    @Override
    public int getHighScore() {
        /*Call the getHighScore() method of the mColorModel object to retrieve the
        * high score the persists throughout the session of the game, and return this value.*/
        return mColorModel.getHighScore();
    }
}
