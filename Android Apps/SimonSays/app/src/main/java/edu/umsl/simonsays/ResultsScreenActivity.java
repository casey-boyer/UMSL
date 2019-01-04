package edu.umsl.simonsays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsScreenActivity extends Activity {

    /*Declare the startOver Button, gameOver TextView, and score TextView
    * that are present in the View. The int highScore variable will be used to
    * retrieve the high score passed from MainActivity, and to send the high score to
    * StartScreenActivity. The int score variable will be used to retrieve the score
    * passed from MainActivity, which is the score of the most recent round.*/
    private Button startOverButton;
    private TextView gameOverTextView;
    private TextView scoreTextView;
    private int score;
    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Set the View to the activity_results_screen xml layout file.*/
        setContentView(R.layout.activity_results_screen);

        /*Associate the startOver button, gameOver and score TextViews
        * with the corresponding Button and TextViews defined in
        * activity_results_screen layout xml file, using the findViewById() method
        * and passing the id present in the layout file.*/
        startOverButton = findViewById(R.id.startOverButton);
        gameOverTextView = findViewById(R.id.gameOverTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        /*If the String "score" passed from the MainActivity is not null, then
        * set the highScore variable equal to the String "score" parsed as an int.*/
        if( (getIntent().getStringExtra("score")) != null)
            highScore = Integer.parseInt(getIntent().getStringExtra("score"));

        /*If the String "currentScore" passed from the MainActivity is not null,
        * then set the score variable equal to the String "currentScore" parsed
        * as an int.*/
        if( (getIntent().getStringExtra("currentScore")) != null)
            score = Integer.parseInt(getIntent().getStringExtra("currentScore"));

        /*Update the scoreTextView to display the high score retrieved from the MainActivity,
        and the score retrieved from the most recent round.*/
        scoreTextView.setText("High score: "+highScore + "\nScore for last round: " + score);

        /*Update the gameOverTextView to notify the user that they lost the game.*/
        gameOverTextView.setText("Game over!");

        /*Define the onClickListener for the startOverButton so that when the button is clicked,
        * the user is redirected to the StartScreenActivity. Additionally, pass the high score
        * (retrieved from MainActivity) to the StartScreenActivity.*/
        startOverButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Define and initialize an Intent object, passing the current context (ResultsScreenActivity)
                * and the StartScreenActivity class that will be executed with the startActivity()
                * method; this way, the screen will transition to the StartScreenActivity.*/
                Intent intent = new Intent(ResultsScreenActivity.this, StartScreenActivity.class);

                /*Invoke the putExtra() method of the intent object, to pass the String "score" with
                * the value of the score variable parsed to a String.*/
                intent.putExtra("score", String.valueOf(highScore));

                /*Start the StartScreenActivity.*/
                startActivity(intent);
                /*Call the finish() method, so the onDestroy() method of ResultsScreenActivity will
                * be invoked to clean up all resources used.*/
                finish();
            }
        });

    }

    /*Prevent the user from going back to the lost round in MainActivity.*/
    @Override
    public void onBackPressed() {
    }

    /*Override the onDestroy() method of the ResultsScreenActivity, invoke super.onDestroy(), and
    * remove the reference to the startOverButton, scoreTextView, and gameOverTextView objects.*/
    @Override
    protected void onDestroy() {
        /*Remove the reference from the startOverButton.*/
        startOverButton = null;

        /*Remove the references from the scoreTextView and the gameOverTextView.*/
        scoreTextView = null;
        gameOverTextView = null;

        Log.e("onDestroy", "Cleaned up resources in ResultsScreenActivity.");

        super.onDestroy();
    }
}
