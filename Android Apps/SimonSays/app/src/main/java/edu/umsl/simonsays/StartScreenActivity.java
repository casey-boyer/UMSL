package edu.umsl.simonsays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartScreenActivity extends Activity {

    /*Declare the Buttons present on the View of the StartScreenActivity, which are the
    * easy, medium, and hard buttons.*/
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Set the View to the activity_start_screen layout xml file.*/
        setContentView(R.layout.activity_start_screen);

        /*Associate each Button with the corresponding button defined in the
        * activity_start_screen layout xml file by calling the findViewById()
        * and passing the corresponding ID of the easyButton, mediumButton, and hardButton.*/
        easyButton = findViewById(R.id.easyButton);
        mediumButton = findViewById(R.id.mediumButton);
        hardButton = findViewById(R.id.hardButton);

        /*Invoke the bindButtons method, which will declare an onClickListener for the
        * easyButton, mediumButton, and hardButton.*/
        bindButtons();
    }


    /*Override the onDestroy() method of StartScreenActivity, invoke super.onDestroy(), and remove
    * the reference to each button object to clean up resources.*/
    @Override
    protected void onDestroy() {
        /*Remove the reference from the easyButton, mediumButton, and hardButton objects.*/
        easyButton = null;
        mediumButton = null;
        hardButton = null;

        Log.e("onDestroy", "Cleaned up resources in StartScreenActivity.");
        super.onDestroy();
    }

    /*Prevent the user from going back to either the ResultsScreenActivity or to the previous
    location before the app.*/
    @Override
    public void onBackPressed() {
    }

    void bindButtons() {
        /*Bind the buttons to a listener View.onClickListener and override the onClick method
        so that the MainActivity is started, passing the difficulty level associated with the buttons.
        If the any button is clicked, then the an Intent is created using the current packageContext
        and the component class (MainActivity) that will be used for this component.
        Additionally, depending on the button that was clicked, an extra String called 'level'
        is passed; if the easy button was clicked, level has the value "1",
                   if the medium button was clicked, level has the value "2",
                   if the hard button was clicked, level has the value "3".*/

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Tag", "Pressed easy button");
                /*Define and initialize an Intent object, passing the current context (StartScreenActivity)
                * and the MainActivity class that will be executed with the startActivity()
                * method; this way, the screen will transition to the MainActivity. Additionally,
                * invoke the putExtra() method to pass the String "level" with value "1" to indicate
                * that the difficulty level chosen was easy.*/
                Intent intent = new Intent(StartScreenActivity.this, MainActivity.class)
                        .putExtra("level", "1");

                /*If the "score" String retrieved using the getStringExtra() method is not null,
                * then the high score was passed from the ResultsScreenActivity. So, invoke the
                * putExtra method to pass the String "score" with the value of the String retrieved
                * from the ResultsScreenActivity.*/
                if((getIntent().getStringExtra("score")) != null)
                    intent.putExtra("score", (getIntent().getStringExtra("score")));

                /*Start the MainActivity.*/
                startActivity(intent);

                /*Invoke the onDestroy() method of the StartScreenActivity to clean up resources.*/
                finish();
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Tag", "Pressed medium button");
                /*Define and initialize an Intent object, passing the current context (StartScreenActivity)
                * and the MainActivity class that will be executed with the startActivity()
                * method; this way, the screen will transition to the MainActivity. Additionally,
                * invoke the putExtra() method to pass the String "level" with value "2" to indicate
                * that the difficulty level chosen was medium.*/
                Intent intent = new Intent(StartScreenActivity.this, MainActivity.class)
                        .putExtra("level", "2");

                /*If the "score" String retrieved using the getStringExtra() method is not null,
                * then the high score was passed from the ResultsScreenActivity. So, invoke the
                * putExtra method to pass the String "score" with the value of the String retrieved
                * from the ResultsScreenActivity.*/
                if((getIntent().getStringExtra("score")) != null)
                    intent.putExtra("score", (getIntent().getStringExtra("score")));

                /*Start the MainActivity.*/
                startActivity(intent);

                /*Invoke the onDestroy() method of the StartScreenActivity to clean up resources.*/
                finish();
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Tag", "Pressed hard button");
                /*Define and initialize an Intent object, passing the current context (StartScreenActivity)
                * and the MainActivity class that will be executed with the startActivity()
                * method; this way, the screen will transition to the MainActivity. Additionally,
                * invoke the putExtra() method to pass the String "level" with value "3" to indicate
                * that the difficulty level chosen was hard.*/
                Intent intent = new Intent(StartScreenActivity.this, MainActivity.class)
                        .putExtra("level", "3");

                /*If the "score" String retrieved using the getStringExtra() method is not null,
                * then the high score was passed from the ResultsScreenActivity. So, invoke the
                * putExtra method to pass the String "score" with the value of the String retrieved
                * from the ResultsScreenActivity.*/
                if((getIntent().getStringExtra("score")) != null)
                    intent.putExtra("score", (getIntent().getStringExtra("score")));

                /*Start the MainActivity.*/
                startActivity(intent);

                /*Invoke the onDestroy() method of the StartScreenActivity to clean up resources.*/
                finish();
            }
        });
    }
}
