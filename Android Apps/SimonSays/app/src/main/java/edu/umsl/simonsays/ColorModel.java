package edu.umsl.simonsays;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class ColorModel {

    private ColorOptions[] mColors; //The array that will contain all 4 colors, red, yellow, blue and green.

    //The array that generates a random sequence, using random numbers to access a
    //create a specific index value for an element of the mColors array.
    private int[] mColorSequence;

    private ArrayList<Integer> storedSequence = new ArrayList<Integer>();

    /*The difficulty level chosen by the user, where '1' is easy, '2' is medium, and '3' is hard.*/
    private int difficultyLevel;

    /*The sequenceIncrease, animationDuration, startDelay and timer are all based on the difficulty level.*/
    private int sequenceIncrease = 0;
    private int animationDuration = 0;
    private int startDelay = 0;
    private long timer;

    /*The user's score for the current round.*/
    private int score = 0;

    /*The user's high score.*/
    private int highScore = 0;

    /*The user's position in the current sequence.*/
    private int mPosition;

    /*boolean variables used to indicate if the user selected a wrong color, or if the
    * end of the current color sequence has been reached.*/
    private boolean gameOver = false;
    private boolean isEndSequence = false;

    /*The constructor for a ColorModel object; it generates the array which holds all four
    * possible colors, and sets the user's current position to 0.*/
    ColorModel() {
        generateArray();
        mPosition = 0;
    }

    /*A method that generates an array which contains the 4 possible color values. This array
    * will be used to generate a random sequence of colors.*/
    private void generateArray() {
        ColorOptions[] mColors = new ColorOptions[] {
                new ColorOptions(R.string.red_text),
                new ColorOptions(R.string.yellow_text),
                new ColorOptions(R.string.blue_text),
                new ColorOptions(R.string.green_text)
        };

        /*Set the member variable, mColors[], equal to the array generated.*/
        this.mColors = mColors;

    }

    /*Set the difficulty level, and initial sequence increase value.
    *   If the difficulty level is easy, the sequence begins with one color, and
    *   each animation is delayed 2000 milliseconds and runs for a total of 2000 milliseconds; the
    *   timer begins at 30 seconds.
    *   If the difficulty level is medium, the sequence begins with three colors, and
    *   each animation is delayed by 1000 milliseconds and runs for a total of 1000 milliseconds;
    *   the timer begins at 15 seconds.
    *   If the difficulty level is hard, the sequence begins with five colors, and
    *   each animation duration is delayed by 500 milliseconds and runs for a total of 500 milliseconds;
    *   the timer begins at 10 seconds.*/
    public void setLevel(int difficulty) {

        this.difficultyLevel = difficulty;

        if(difficultyLevel == 1) {
            setSequenceIncrease(1); //Initial sequence increase is one.
            setAnimationDuration(2000); //Initial animation duration is 2000 milliseconds.
            setStartDelay(2000); //Initial animation start delay is 2000 milliseconds.
            setTimer(30000); //Initial timer is 30 seconds.
            Log.e("setLevel", "sequenceIncrease="+this.sequenceIncrease);
        }
        else if(difficultyLevel == 2) {
            setSequenceIncrease(3); //Initial sequence increase is three.
            setAnimationDuration(1000); //Initial animation duration is 1000 milliseconds.
            setStartDelay(1000); //Initial animation start delay is 1000 milliseconds.
            setTimer(15000); //Initial timer is 15 seconds.
        }
        else if(difficultyLevel == 3) {
            setSequenceIncrease(5); //Initial sequence increase is five.
            setAnimationDuration(500); //Initial animation duration is 500 milliseconds.
            setStartDelay(500); //Initial animation start delay is 500 milliseconds.
            setTimer(10000); //Initial timer is 10 seconds.
        }

        Log.e("setLevel:", ""+this.difficultyLevel);
        Log.e("setLevel", ""+this.sequenceIncrease);
        Log.e("setLevel", "timer="+timer);
    }

    /*After the user successfully completes a sequence, the difficulty of the next sequence is updated.
    * This is done by decrementing the animationDuration, startDelay, and timer for the next sequence,
    * and the decrement is more dramatic depending on the difficulty level.
    * Since the medium and hard difficulty levels begin with a shorter timer, animationDuration, and
    * animationDelay, the decrement after each sequence is less than that of the easy difficulty
    * level; however, it is more palpable as the sequences will be larger and already have less time
    * to respond.*/
    private void updateDifficulty() {
        if(difficultyLevel == 1) { /*Easy difficulty level*/
            setAnimationDuration((this.animationDuration - 100)); //Shorten animation duration by 100 milliseconds
            setStartDelay((this.startDelay - 100)); //Shorten animation delay by 100 milliseconds
            setTimer((this.timer - 1000)); //Shorten the timer by one second
        }
        else if(difficultyLevel == 2) { /*Medium difficulty level*/
            setAnimationDuration((this.animationDuration - 50)); //Shorten animation duration by 50 milliseconds
            setStartDelay((this.startDelay - 50)); //Shorten animation delay by 50 milliseconds
            setTimer((this.timer - 1500)); //Shorten the timer by 1.5 seconds
        }
        else if(difficultyLevel == 3) { /*Hard difficulty level*/
            setAnimationDuration((this.animationDuration - 25)); //Shorten the animation duration by 25 milliseconds
            setStartDelay((this.startDelay - 25)); //Shorten the animation delay by 25 milliseconds
            setTimer((this.timer - 2000)); //Shorten the timer by 2 seconds
        }
    }

    /*A setter method which sets the sequence increase based on the value passed
    * as an argument.*/
    private void setSequenceIncrease(int sequenceIncrease) {
        this.sequenceIncrease = sequenceIncrease;
    }

    /*A setter method which sets the animation duration based on the value passed
    * as an argument.*/
    private void setAnimationDuration(int animationDuration) {
        /*If the animationDuration is less than 500 milliseconds, then keep the animationDuration
        * as 500 milliseconds to avoid decrementing the duration to zero.*/
        if(animationDuration < 500) {
            this.animationDuration = 500;
        }
        else { /*Otherwise, set the animationDuration equal to the parameter value.*/
            this.animationDuration = animationDuration;
        }
    }

    /*A setter method which sets the animation start delay based on the value passed
    * as an argument.*/
    private void setStartDelay(int startDelay) {
        /*If the startDelay is less than 500 milliseconds, then keep the startDelay as
        * 500 milliseconds to avoid decrementing the duration to zero.*/
        if(startDelay < 500) {
            this.startDelay = 500;
        }
        else { /*Otherwise, set the startDelay equal to the parameter value.*/
            this.startDelay = startDelay;
        }
    }

    /*A setter method which sets the timer for the user to respond based on the value
    * passed as an argument.*/
    private void setTimer(long timer) {
        /*If the timer is less than 2000 milliseconds/2 seconds, then keep the timer as
        * 2000 milliseconds to avoid decrementing the duration to less than 2 seconds.*/
        if(timer < 2000) {
            this.timer = 2000;
        }
        else { /*Otherwise, set the timer equal to the parameter value.*/
            this.timer = timer;
        }
    }

    /*A getter method which returns the sequence increase.*/
    public int getSequenceIncrease() {
        return this.sequenceIncrease;
    }

    /*A getter method which returns the animation duration.*/
    public int getAnimationDuration() {
        return this.animationDuration;
    }

    /*A getter method which returns the animation start delay.*/
    public int getStartDelay() {
        return this.startDelay;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    /*A getter method which returns the timer (time in seconds the user has to respond).*/
    public long getTimer() { return this.timer; }

    /*Generate the color sequence. This is done by creating an int array with size equal
    * to sequenceIncrease (the number of colors in a sequence), and generating random
    * numbers (using generateIndex()) from 0-3, where each random number is an index that corresponds
    * to an element in mColor[]. Add the randomly generated numbers to the
    * storedSequence arrayList, so the previous sequence set is retained.
    * Set mColorSequence equal to this generated array, and update the sequenceIncrease by
    * one (so the next sequence is longer).*/
    private void generateSequence() {

        /*Create an integer variable, which will hold the index of an mColor[] element (which
        corresponds to the resource ID's of the to-be generated colors) and initialize
        it to the size of sequence increase.*/
        int mColorSequence[] = new int[this.sequenceIncrease];

        /*A for loop with a counter that begins at the size of the storedSequence ArrayList, which is
        * the end of the storedSequence ArrayList; this way, newly generated colors will be added
        * at the end of the retained sequence. The terminating condition is when i is equal to
        * sequenceIncrease.*/
        for(int i = (storedSequence.size()); i < sequenceIncrease; i++) {
            /*Add the newly generated random color to the end of the current sequence.*/
            storedSequence.add(i, generateIndex());
            Log.e("storedSequence", "storedSequence[" + i + "]" + (storedSequence.get(i)));
        }

        /*In a for loop, initialize each value to the return value of generateIndex()
        * until the size of the array is reached. Then, mColorSequence will contain
        * an array of indices that correspond to mColor[] elements.*/
        for(int i = 0; i < mColorSequence.length; i++) {
            mColorSequence[i] = storedSequence.get(i);
            Log.e("generateSeq loop", "index["+i+"]="+mColorSequence[i]);
            Log.e("generateSeq loop", "storedSequence["+i+"]="+storedSequence.get(i));
        }

        /*Set the mColorSequence[] array to the generated array.*/
        this.mColorSequence = mColorSequence;

        /*Increase the sequenceIncrease by one, so when this function is called again (assuming
        * the user answers the correct sequence), the next array will be one element longer.*/
        this.sequenceIncrease++;

        /*Update the difficulty for the sequence.*/
        updateDifficulty();
    }

    /*Generates and returns a random integer from 0-3. Will be used
     * as an index for the mColor[] array, where the element at the
     * randomly generated index corresponds to the randomly generated color.*/
    private int generateIndex() {
        /*Create an instance of the random() object.*/
        Random randomNumber = new Random();

        /*Get a random number between 0-3. The nextInt() method of Random returns
        * a uniformly distributed int value between 0 and the argument specified bound.*/
        int randomIndex = randomNumber.nextInt(4);

        /*Return the randomly generated index value.*/
        return randomIndex;
    }

    /*A getter method that updates the position after the user has given a response.
    * This is done by incrementing mPosition by one, then dividing this result by
    * the length of the generated sequence and retrieving the remainder value.*/
    private int getPosition() {

        return (mPosition + 1) % mColorSequence.length;
    }

    /*A method which determines if the end of the sequence has been reached. If mPosition equals
    * 0, then either the sequence array consists of one element and mPosition has been updated
    * to 0, or the user has successfully clicked through all buttons corresponding to the sequence
    * then mPosition must equal 0, and thus the end of the sequence has been reached. Otherwise,
    * if mPosition equals any other value, then the end of the sequence has not yet been reached.*/
    private void setIsEndSequence() {
        if( mPosition == 0 ) {
            isEndSequence = true;
        }
        else
            isEndSequence = false;
    }

    /*The method which tests if the user's input is correct, accepting the user's input
    * as an argument; since the resource ID of the color is an int value, the user's input
    * is passed as an int value.
    * The method then compares the user's input to the value of the element at the current position
    * in the mColorSequence array. If the user's input matches this value, then they correctly
    * selected the color and the boolean gameOver value is false.
    * If the user's input does not match this value, then gameOver is true, since the game
    * must end when a user selects an incorrect value.*/
    private void testUser(int color) {
        /*If the color the user selected corresponds to the color at the current index
        * (the current user selection and current color in the sequence), then the
        * user selected the correct color and gameOver is false.*/
        //if (color == mColors[mColorSequence[mPosition]].getTextResId()) {
        if (color == mColors[mColorSequence[mPosition]].getTextResId()) {
            Log.e("Comparison:", "true");
            gameOver = false;
        }
        else {
            /*Otherwise, if the color the user selected does not match the color at the
            * current position in mColorSequence, then gameOver is true.*/
            gameOver = true;
            Log.e("Comparison:", "false");
        }
    }

    /*A method which retrieves the user's input as an argument, and returns a boolean value
    * as to whether the user selected the correct color or not. Since the color is a Resource ID,
    * it is passed as an int value to the method argument.
    * The method calls the testUser() method, passing the user's selection, which returns a true
    * or false value if the user was correct or not.
    * Additionally, getPosition() is called to retrieve the next position in the color sequence, and
    * setIsEndSequence is called to update the boolean isEndSequence variable if the end of the
    * current color sequence has been reached.*/
    public boolean getUserSelection(int color) {
        Log.e("getUserColor():", "" + color);

        //Test the user input; if correct, gameOver will be false, if incorrect, gameOver will be true.
        testUser(color);

        //If gameOver is false, update the position in the sequence.
        if(gameOver == false)
            mPosition = getPosition();
        Log.e("userSelection", "mPosition = "+mPosition);

        //Update the boolean value to indicate if the end of the current sequence has been
        //reached or not.
        setIsEndSequence();

        //Return the value of gameOver to the method caller.
        return gameOver;
    }

    /*A getter method which returns the value of isEndSequence, which is true
    * if the end of the current color sequence has been reached, or false otherwise.*/
    public boolean getIsEndSequence() {
        Log.e("getIsEndSequence", ""+this.isEndSequence);
        return this.isEndSequence;
    }

    /*A method which increments the user's score by one. This method is called when the
    * user succesfully completes a sequence.*/
    public void updateScore() {
        this.score++;
    }

    /*A method which returns the user's score for the current game.*/
    public int getScore() {
        return this.score;
    }

    /*A method which returns the user's high score for the current app session.*/
    public int getHighScore() { return this.highScore; }

    /*A getter method which returns the current color sequence to the caller. The method
    * calls generateSequence() to generate the color sequence, and returns an array
     * of Resource IDs corresponding to the randomly generated colors.*/
    public int[] getSequence() {
        //Generate the color sequence.
        generateSequence();

        /*Create an int array equal to the size of the length of the generated sequence. This
        * array will hold the Resource ids of the colors in the generated sequence.*/
        int[] resIds = new int[mColorSequence.length];

        /*Get the size of the array which contains the generated sequence.*/
        int size = mColorSequence.length;

        /*Populate the resIds array with the Resource IDs of the generated color sequence,
        * using mColors[] to get the element corresponding to the numeric value of mColorSequence,
        * and then calling the getmTextResId() method in ColorOptions to get the Resource ID
        * for this color.*/
        for(int i = 0; i < size; i++) {
            resIds[i] = mColors[mColorSequence[i]].getTextResId();
        }

        /*Reset the user's position to zero.*/
        mPosition = 0;

        /*Return the array to the calling method.*/
        return resIds;
    }

    /*A getter method which returns the Resource String ID of the numerical element at
    the index of mPosition in the mColorSequence array, which corresponds to a specific element
    in the mColors array; the value is then resolved using the getTextResId() method.*/
    public int getColor() {
        Log.e("getColor:", ""+mColors[mColorSequence[mPosition]].getTextResId());
        return mColors[mColorSequence[mPosition]].getTextResId();
    }
}
