package edu.umsl.simonsays;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivityViewFragment extends Fragment {

    /*The red, yellow, blue, and green buttons needed for the
    * flash sequence and user feedback, as well as a prompt TextView, timer TextView,
    * current game score TextView, and current app session high score TextView.
    * Additionally, a CountDownTimer object is used to display a count down
    * on the screen, which indicates how long the user has to respond and
    * answer the current sequence. The long time variable will create a new
    * timer in a separate thread (after the current sequence is animated)
    * using the time provided by the ColorModel object.
    * The Handler object will use the postDelayed() method to prevent the timer from
    * beginning before the sequence is done animating, and to prevent the user from
    * being able to click buttons while the sequence is animating. The Runnable object
    * is required in this postDelayed() method, and will implement the run() method.*/
    private Button mRedButton;
    private Button mYellowButton;
    private Button mBlueButton;
    private Button mGreenButton;
    private TextView mPromptTextView;
    private TextView mTimerTextView;
    private TextView mHighScoreTextView;
    private TextView mScoreTextView;
    private CountDownTimer countTimer = null;
    private Handler handler;
    private Runnable runnable;
    private long time = 0;

    /*Declare the mainActivityListener to null.*/
    MainActivityViewFragmentListener mainActivityListener = null;

    /*Methods which MainActivity must implement to communicate data from the model
    * to the view.*/
    interface MainActivityViewFragmentListener {
        /*This method, userValue, will pass the ID of the button
        * clicked (the method argument), and the MainActivity will call the
        * corresponding method in the ColorModel object to test if the
        * user selected the correct color.*/
        public void userValue(int color);

        /*This method, timerExpired(), will be called in the onFinish() method
        * of the CountDownTimer object; MainActivity will retrieve the correct color
        * the user needed to click. This method is only called when the timer expires
        * and the user does not select all colors in the sequence in time.*/
        public void timerExpired();

        /*This method, getHighScore, will be called to update the mHighScore TextView;
        * MainActivity will retrieve the high score from the ColorModel object and
        * return this to the view, so the TextView may be set.*/
        public int getHighScore();

        /*This method, getScore, will be called to update the mScore TextView;
        * MainActivity will retrieve the score from the ColorModel object and
        * return this to the view, so the TextView may be updated.*/
        public int getScore();
    }

    /*Required empty public constructor for the MainActivityViewFragment.*/
    public MainActivityViewFragment() {
    }

    /*Override the onAttach method, calling the superclass constructor, to attach the fragment
    * to the activity.*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /*Override the onCreateView method, because MainActivityViewFragment is a view fragment.
    * Return the inflated view to the MainActivity.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, which will serve as the View for the MainActivity.
        View fragView = inflater.inflate(R.layout.fragment_main_activity_view, container, false);

        /*Associate each defined button with the corresponding button
        * on the fragment_main_activity_view layout by using the findViewById()
        * method and passing the id of the button defined in the layout xml.
        * Additionally, associate each defined TextView with the corresponding
        * TextView by using the findViewById() method and passing the id of the
        * TextView defined in the layout xml. Call the createTimer function, with an
        * argument of 0, to initialize the CountDownTimer (rather than leaving it NULL).*/
        mRedButton = fragView.findViewById(R.id.redButton);
        mYellowButton = fragView.findViewById(R.id.yellowButton);
        mBlueButton = fragView.findViewById(R.id.blueButton);
        mGreenButton = fragView.findViewById(R.id.greenButton);
        mPromptTextView = fragView.findViewById(R.id.promptTextView);
        mTimerTextView = fragView.findViewById(R.id.timerTextView);
        mHighScoreTextView = fragView.findViewById(R.id.highScoreTextView);
        mScoreTextView = fragView.findViewById(R.id.scoreTextView);
        countTimer = createTimer(0);

        /*Call the bindButtons() function to bind the red, yellow, blue, and green
        * buttons to their corresponding onClickListeners.*/
        bindButtons();

        /*Return the fragment_main_activity_view.*/
        return fragView;
    }

    /*Override the fragment's onActivityCreated method, and call the superclass
    * constructor, passing the Bundle savedInstanceState as an argument.
    * Then, retrieve the app's high score for the current session, and the
    * score for the current game, and set the score and high score TextViews
    * accordingly.*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        /*Required to call the superclass constructor.*/
        super.onActivityCreated(savedInstanceState);

        /*Retrieve the score for the current game and the high score
        * for the current app session.*/
        int score = mainActivityListener.getScore();
        int highScore = mainActivityListener.getHighScore();

        /*Update the score and high score TextViews accordingly.*/
        mScoreTextView.setText("Score:   " + score);
        mHighScoreTextView.setText("High Score:   " + highScore);
        mTimerTextView.setText("");

    }

    /*When the MainActivity's onDestroy() method is called, the MainActivityViewFragment's
    * onDestroy method will also be triggered. This method invokes super.onDestroy(), and
    * removes the reference to all Object variables (Buttons, TextViews, the CountDownTimer,
    * the listener, the Handler and Runnable objects), and also removes the callbacks from
    * the Handler object.*/
    @Override
    public void onDestroy() {
        /*Remove the reference from the red, yellow, blue and green Buttons, the TextViews,
        * and the CountDownTimer object.*/
        mRedButton = null;
        mYellowButton = null;
        mBlueButton = null;
        mGreenButton = null;
        mScoreTextView = null;
        mHighScoreTextView = null;
        mPromptTextView = null;
        mTimerTextView = null;
        countTimer = null;

        /*Remove the reference from the mainActivityListener.*/
        mainActivityListener = null;

        /*Remove all callbacks from the Handler object.*/
        handler.removeCallbacks(runnable);

        /*Remove the reference from the handler and runnable objects.*/
        handler = null;
        runnable = null;

        Log.e("onDestroy", "Cleaned up resources in fragment.");
        super.onDestroy();
    }



    /*This function binds the red, yellow, blue, and green buttons to
        * an onClickListener to perform the necessary actions when they
        * are clicked, which include:
        *      Highlight the button to notify the user that their button click
        *      was recognized and processed;
        *      Call the function userValue, implemented in MainActivity,
        *      and pass the String Resource ID that corresponds to the color
        *      of the button that was pressed (using the mainActivityListener).*/
    void bindButtons() {
        mRedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Log message notifying the red button was clicked.*/
                Log.e("bindButtons", "Pressed red button");

                /*Pass the red button to the buttonHighlight method
                * to perform an animation indicating it was clicked.*/
                buttonHighlight(mRedButton);

                /*Using the mainActivityListener, call the userValue()
                * method that was implemented in MainActivity and pass
                * the String Resource ID that corresponds to the
                * red button.*/
                mainActivityListener.userValue(R.string.red_text);
            }
        });

        mYellowButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Log message notifying that the yellow button was clicked.*/
                Log.e("bindButtons", "Pressed yellow button");

                /*Pass the yellow button to the buttonHighlight method
                * to perform an animation indicating it was clicked.*/
                buttonHighlight(mYellowButton);

                /*Using the mainActivityListener, call the userValue()
                * method that was implemented in MainActivity and pass
                * the String Resource ID that corresponds to the
                * yellow button.*/
                mainActivityListener.userValue(R.string.yellow_text);
            }
        });

        mBlueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Log message notifying that the blue button was clicked.*/
                Log.e("bindButtons", "Pressed blue button");

                /*Pass the blue button to the buttonHighlight method
                * to perform an animation indicating it was clicked.*/
                buttonHighlight(mBlueButton);

                /*Using the mainActivityListener, call the userValue()
                * method that was implemented in MainActivity and pass
                * the String Resource ID that corresponds to the
                * blue button.*/
                mainActivityListener.userValue(R.string.blue_text);
            }
        });

        mGreenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Log message notifying that the green button was clicked.*/
                Log.e("bindButtons", "Pressed green button");

                /*Pass the green button to the buttonHighlight method
                * to perform an animation indicating it was clicked.*/
                buttonHighlight(mGreenButton);

                /*Using the mainActivityListener, call the userValue()
                * method that was implemented in MainActivity and pass
                * the String Resource ID that corresponds to the
                * green button.*/
                mainActivityListener.userValue(R.string.green_text);
            }
        });
    }

    /*The method that highlights a button if it was clicked, called by the listener
    * associated with the clicked button. It accepts the clicked button as the argument,
    * and animates a flash on this button for 500 milliseconds using an AlphaAnimation.*/
    void buttonHighlight(Button button) {
        /*Create the 'flash' animation using an AlphaAnimation. The arguments
        * 0.5f and 1.0f specify two floats, where 0.5 is half the opacity of the button,
        * and 1.0 is full opacity of the button. So, the animation changes the button's
        * opacity from half opacity to full opacity.*/
        Animation flashAnim = new AlphaAnimation(0.5f, 1.0f);

        /*Set the animation duration for only 500 milliseconds.*/
        flashAnim.setDuration(500);

        /*Set the repeat count to 0, so the animation does not unintentionally repeat.*/
        flashAnim.setRepeatCount(0);

        /*Start the animation on the clicked button, which was passed as an argument.*/
        button.startAnimation(flashAnim);
    }

    /*A method used to flash a button that corresponds to the button the
    * user was supposed to click, when the user selects an incorrect button.
    * The method accepts the String resource ID that corresponds to the
    * correct button (the button that was supposed to be clicked).*/
    void flashCorrectColor(int color) {
        /*Cancel the current CountDownTimer object.*/
        countTimer.cancel();

        /*Update the prompt text view to notify the user that they
        * selected the wrong color, and they are about to see the
        * correct color.*/
        mPromptTextView.setText("Correct color was: ");

        /*Define an ObjectAnimator object to animate the button that
        * was supposed to be pressed.*/
        ObjectAnimator objAnim;

        /*A switch statement that highlights the red, yellow, blue, or green button
        * using an alpha animation, sets the animation start delay to 500 milliseconds, sets the
        * animation duration to 200 milliseconds, and repeats this animation 3 times. The
        * alpha animation ranges from no opacity (0.0f) to full opacity. The repeat
        * and shorter animation duration distinguishes this alpha animation from the buttonHighlight
        * animation, and also draws the user's attention to the correct color with quick repeating.*/
        switch(color) {
            case R.string.red_text: /*If color corresponds to the red button*/
                objAnim = ObjectAnimator.ofFloat(mRedButton, "alpha", 0.0f, 1.0f);
                objAnim.setStartDelay(500);
                objAnim.setDuration(200);
                objAnim.setRepeatCount(3);
                objAnim.start(); /*Start the animation.*/
                break;
            case R.string.yellow_text: /*If color corresponds to the yellow button.*/
                objAnim = ObjectAnimator.ofFloat(mYellowButton, "alpha", 0.0f, 1.0f);
                objAnim.setStartDelay(500);
                objAnim.setDuration(200);
                objAnim.setRepeatCount(3);
                objAnim.start(); /*Start the animation.*/
                break;
            case R.string.blue_text: /*If color corresponds to the blue button.*/
                objAnim = ObjectAnimator.ofFloat(mBlueButton, "alpha", 0.0f, 1.0f);
                objAnim.setStartDelay(500);
                objAnim.setDuration(200);
                objAnim.setRepeatCount(3);
                objAnim.start(); /*Start the animation.*/
                break;
            case R.string.green_text: /*If color corresponds to the green button.*/
                objAnim = ObjectAnimator.ofFloat(mGreenButton, "alpha", 0.0f, 1.0f);
                objAnim.setStartDelay(500);
                objAnim.setDuration(200);
                objAnim.setRepeatCount(3);
                objAnim.start(); /*Start the animation.*/
                break;
        }
    }

    /*This method creates and returns a CountDownTimer, accepting the time to begin
    * counting down from as a parameter (long time). To create the CountDownTimer object,
    * the onTick() and onFinish() methods must be overridden. The CountDownTimer
    * is initialized with the time to begin counting down from as milliseconds.
    * The onTick() method accepts the value of the remaining milliseconds at the tick,
    * and within this method, the timer TextView is updated to display this remaining time as
    * seconds.
    * The onFinish() method clears the prompt TextView.
    * At the end, the CountDownTimer object is returned.*/
    CountDownTimer createTimer(long time) {
        /*Initialize the CountDownTimer with the total time to begin counting down from,
        * and using 1000 milliseconds as the count down interval.*/
        CountDownTimer timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                /*Set the timer TextView to the remaining time
                in seconds on a newline during each tick.*/
                mTimerTextView.setText("Time remaining: " + (millisUntilFinished/ 1000) );
            }

            @Override
            public void onFinish() {
                /*Clear the timer text. Set the timer TextView to "TIMES UP", notifying
                * the user that they have ran out of time to complete the sequence,
                * and call the timerExpired() function (defined in MainActivity)
                * to get the button that needed to be clicked.*/
                mTimerTextView.setText("");
                mTimerTextView.append("TIMES UP!\n");
                mainActivityListener.timerExpired();
            }
        };

        /*Return the CountDownTimer object.*/
        return timer;
    }

    /*The method which animates the sequence; it accepts the color sequence (i.e., which
    * buttons to highlight), the start delay for each animation, the animation duration
    * for each animation, and the time required for the user to respond and answer the current
    * sequence.*/
    void animateSequence(int[] sequence, int startDelay, int animDuration, long timer) {
        /*Set the time variable equal to the time for this given animation; the timer
        * variable will vary based on the difficulty level, and how many sequences the user
        * has answered.*/
        time = timer;

        /*Update the user's score using the mainActivityListener and calling the getScore()
        * method, which is implemented in the MainActivity.*/
        int score = mainActivityListener.getScore();

        /*Log message that notifies the user of the current score.*/
        Log.e("animationSequence", "score="+score);

        /*Set the score TextView to update the user's current score.*/
        mScoreTextView.setText("Score:   " + score);

        /*Create a List object of Animator objects, and initialize this an ArrayList of
        * Animator objects with a capacity equal to the sequence array length. This is done
        * because AnimatorSet accepts a List of Animator objects, and will not accept a List of
        * ObjectAnimator objects, an ArrayList, and so forth.*/
        List<Animator> animationSequence = new ArrayList<Animator>(sequence.length);

        /*Update the prompt TextView to prepare the user for a new sequence.*/
        mPromptTextView.setText("New sequence: ");

        /*Cancel the current CountDownTimer object.*/
        countTimer.cancel();
        mTimerTextView.setText("");

        /*While the sequence is being animated, disable the clickable feature of each
        * button so the user cannot click a button while they are being animated.*/
        enableButtons(false);

        /*A for loop which loops through the sequence array, and animates the button
        * which corresponds to the current entry of the sequence array at index i.*/
        for(int i = 0; i < sequence.length; i++) {
            /*Define a new ObjectAnimator object and a new Animator object. The
            * ObjectAnimator object will be used to create the alpha animation, set the target
            * to the corresponding button, set the start delay and animation duration. Then,
            * the Animator object will be initialized to this ObjectAnimator object (cast as an Animator),
            * so that it can be sucessfully added to the animationSequence List.*/
            ObjectAnimator objAnim = new ObjectAnimator();
            Animator anim;

            /*A switch statement which attempts to match the current element at index i of the
            * sequence array. For the case of the red, yellow, blue, or green button, the ObjectAnimator
            * object creates an alpha animation (ranging from none to full opacity), sets the target
            * to the corresponding button, and sets the start delay and animation duration according to the
            * difficulty level set by the ColorModel object. Then, ObjectAnimator is cast to an Animator object
            * and anim is initialized to this value. This way, the anim Animator can be added to the
            * animationSequence list. Since the switch statement will go through each element of
            * the for loop in increasing order, this ensures that the element in position 0 will
            * be animated first, and so on.*/
            switch(sequence[i]) {
                case R.string.red_text: /*If the red button must be highlighted*/
                    objAnim = ObjectAnimator.ofFloat(mRedButton, "alpha", 0.0f, 1.0f);
                    objAnim.setTarget(mRedButton); /*Set the target to the red button.*/
                    objAnim.setStartDelay(startDelay); /*Set the startDelay based on the method parameter*/
                    objAnim.setDuration(animDuration); /*Set animationDuration based on the method parameter*/
                    Log.e("animationSequence", "redButton"); /*Simple log message*/

                    /*Initialize the Animator object to the ObjectAnimator, casting ObjectAnimator
                    * to an Animator object, so this animation can be added to the animationSequence List.*/
                    anim = (Animator) objAnim;
                    animationSequence.add(anim);
                    break;
                case R.string.yellow_text: /*If the yellow button must be highlighted*/
                    objAnim = ObjectAnimator.ofFloat(mYellowButton, "alpha", 0.0f, 1.0f);
                    objAnim.setTarget(mYellowButton); /*Set the target to the yellow button*/
                    objAnim.setStartDelay(startDelay); /*Set the startDelay based on the method parameter*/
                    objAnim.setDuration(animDuration); /*Set animationDuration based on the method parameter*/

                    /*Initialize the Animator object to the ObjectAnimator, casting ObjectAnimator
                    * to an Animator object, so this animation can be added to the animationSequence List.*/
                    anim = (Animator) objAnim;
                    animationSequence.add(anim);

                    Log.e("animationSequence", "yellowbutton"); /*Simple log message*/
                    break;
                case R.string.blue_text: /*If the blue button was highlighted*/
                    objAnim = ObjectAnimator.ofFloat(mBlueButton, "alpha", 0.0f, 1.0f);
                    objAnim.setTarget(mBlueButton); /*Set the target to the blue button*/
                    objAnim.setStartDelay(startDelay); /*Set the startDelay based on the method parameter*/
                    objAnim.setDuration(animDuration); /*Set animationDuration based on the method parameter*/

                    /*Initialize the Animator object to the ObjectAnimator, casting ObjectAnimator
                    * to an Animator object, so this animation can be added to the animationSequence List.*/
                    anim = (Animator) objAnim;
                    animationSequence.add(anim);

                    Log.e("animationSequence", "blueButton"); /*Simple log message*/
                    break;
                case R.string.green_text: /*If the green button was highlighted*/
                    objAnim = ObjectAnimator.ofFloat(mGreenButton, "alpha", 0.0f, 1.0f);
                    objAnim.setTarget(mGreenButton); /*Set the target to the green button*/
                    objAnim.setStartDelay(startDelay); /*Set the startDelay based on the method parameter*/
                    objAnim.setDuration(animDuration); /*Set animationDuration based on the method parameter*/

                    /*Initialize the Animator object to the ObjectAnimator, casting ObjectAnimator
                    * to an Animator object, so this animation can be added to the animationSequence List.*/
                    anim = (Animator) objAnim;
                    animationSequence.add(anim);

                    Log.e("animationSequence", "greenButton"); /*Simple log message*/
                    break;
            }
        }

        /*Define and initialize the AnimatorSet object. Then, use the playSequentially()
        * method of the AnimatorSet object and pass the animationSequence List as an argument;
        * this allows all animations defined in the above switch statement to play in the
        * intended order as dictated by the sequence array. The duration of each animation
        * in the list is set by invoking the setDuration() method of the AnimatorSet object,
        * which uses the mColorModel animationDuration variable as the argument. Finally,
        * invoke the start() method of the AnimatorSet object to play each animation in sequential
        * order.*/
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animationSequence);
        animSet.setDuration(animDuration);
        animSet.start(); /*Start the AnimatorSet.*/

        /*Initialize the handler and implement the run() method of the
        * Runnable object; the run() method will be triggered  based on the duration
        * of each animation in the sequence, and the startDelay of each animation in the
        * sequence. For example:
        *               If the sequence has 2 colors, and the duration is 100 ms and the start delay
        *               is 100 ms, then the run method() within the postDelayed() method will not
        *               be triggered until (2 * 100) + (2 * 100) = 400 milliseconds have passed.
        * This ensures that the animation sequence will finish before the CountDownTimer triggers
        * its onTick() method, and that the user will not be able to click any buttons until the
        * animation sequence is finished.*/
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                /*Call the createTimer method, passing the time variable (retrieved from the
                * mColorModel object) as the total amount of time the user has to answer
                * the sequence; set the CountDownTimer equal to the return value of the method.*/
                countTimer = createTimer(time);
                /*Start the CountDownTimer object, so the onTick() method will be invoked.*/
                countTimer.start();
                /*Enable the buttons clickable property so the user may answer the sequence.*/
                enableButtons(true);
                /*Update the prompt TextView, indicating to the user to begin answering.*/
                mPromptTextView.setText("Go!");
            }
        }, ( (sequence.length * animSet.getDuration() )+ (sequence.length * startDelay)) );
    }

    /*A simple method which enables or disables the clickable property of the red, yellow,
    * blue, and green buttons. If enabled is false, then none of the buttons are clickable.
    * If enabled is true, then all of the buttons are clickable.*/
    void enableButtons(boolean enabled) {
        mRedButton.setClickable(enabled);
        mYellowButton.setClickable(enabled);
        mBlueButton.setClickable(enabled);
        mGreenButton.setClickable(enabled);
    }
}
