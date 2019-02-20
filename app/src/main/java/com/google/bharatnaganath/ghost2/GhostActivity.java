

package com.google.bharatnaganath.ghost2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String fragment = "";
    private TextView gamestatus;
    TextView wordlabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        /**
         **  YOUR CODE GOES HERE
         **/
        wordlabel= (TextView) findViewById(R.id.ghostText);
        gamestatus=(TextView)findViewById(R.id.gameStatus);
        try {
            InputStream in = getAssets().open("words.txt");
            dictionary = new FastDictionary(in);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load words", Toast.LENGTH_LONG);
            toast.show();
        }


        onStart(null);
    }
    public void onChallenge(View view)
    {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String gameText=(String)label.getText();
        Log.i("TAG","Game text: "+gameText+" gameText.Contains('wins'): "+gameText.contains("Wins"));
        if(gameText.contains("Wins")||gameText.contains("wins"))
            Toast.makeText(getApplicationContext(),"Hit 'Reset' to start new game! ",Toast.LENGTH_LONG).show();
        else if(fragment.length()<4)
            Toast.makeText(getApplicationContext(),"Can't challenge since fragment size is less than 4!",Toast.LENGTH_LONG).show();
        else if(fragment.length()>=4&&dictionary.isWord(fragment))
            label.setText("User Wins!");

        else if(dictionary.getAnyWordStartingWith(fragment)!="XYZ")
        {
            String newFragment=dictionary.getAnyWordStartingWith(fragment);

            label.setText("Computer Wins! (A valid word can be formed)");
            text.setText(newFragment);
        }



    }

    public void onReset(View view)
    {
        fragment="";
        onStart(null);

    }
    public void onKeyboard(View view)
    {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstancesState)
    {
        savedInstancesState.putString("fragment",fragment);
        savedInstancesState.putBoolean("userTurn",userTurn);
        wordlabel.setText(fragment);
        Log.i("TAG","SENT-> fragment: "+fragment+" | UserTurn: "+userTurn);
        super.onSaveInstanceState(savedInstancesState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstancesState)
    {
        super.onRestoreInstanceState(savedInstancesState);
        fragment=savedInstancesState.getString("fragment");
        userTurn=savedInstancesState.getBoolean("userTurn");
        Log.i("TAG","RECIEVED-> fragment: "+fragment+" | UserTurn: "+userTurn);
        if (userTurn) {
            gamestatus.setText(USER_TURN);
        } else {
            gamestatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        wordlabel.setText(fragment);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again

        if(fragment.length()>=4&&dictionary.isWord(fragment)){
            gamestatus.setText("Computer Wins!");
            return;
        }


        if(!fragment.equals(""));
        String newfragment=dictionary.getAnyWordStartingWith(fragment);
        Log.i("TAG",newfragment);

        if(newfragment==null)
        {
            userTurn = true;
            label.setText("Computer Challenges and Wins! (No word can be formed by the fragment)");

            Log.i("TAG","NULL returned");
        }

        else
        {
            String s=newfragment.substring(fragment.length(),fragment.length()+1);
            fragment=fragment+s;
            Log.i("TAG","fragment: "+fragment);
            wordlabel.setText(fragment);
            userTurn=true;
            label.setText(USER_TURN);
        }


    }


    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **  YOUR CODE GOES HERE
         **/

        char userInput = (char) event.getUnicodeChar();
        userInput = Character.toLowerCase(userInput);
        if (userInput < 'a' || userInput > 'z') {
            return super.onKeyUp(keyCode, event);
        } else {

            fragment = (fragment + event.getDisplayLabel()).toLowerCase();
            wordlabel.setText(fragment);
            //Log.i("TAG","FLength-> "+fragment.length()+"Bool value-> "+dictionary.isWord(fragment));

            computerTurn();
            return true;
        }
    }


}
