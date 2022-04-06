package com.example.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAccount extends AppCompatActivity {
    private ClipboardManager myClipboard;
    private ClipData myClip;

    //  Object create to use my pre-defined DB actions
    DataInteractions db;

    Account acc;
    boolean updating;
    int id;
    String service;
    String user;
    String pass;

    //  Password generator variables
    int length;
    boolean caps;
    boolean nums;
    boolean specials;
    boolean extraSpecials;

    //  DB inputs
    EditText serviceName;
    EditText username;
    EditText password;

    //  Password generator inputs
    Switch capsSelect;
    Switch numsSelect;
    Switch specialsSelect;
    Switch moreSpecialsSelect;
    Spinner passLenSpinner;
    EditText passOutput;

    public AddAccount() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove action bar from xml template
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        //  Get account id, if one was supplied
        Intent i = getIntent();
        id = i.getIntExtra("account_id", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);

        //  If specific user ID was supplied, pre-load the info from that database row.
        if(id != 0){
            populateAccount(id);
            updating = true;    //  Flag this DB entry as an update to an existing entry, not as a new one.
        }
    }

    /**
     * Used to pre-populate account details to edit
     *
     * @param id - unique ID (primary key) of existing database row
     */
    private void populateAccount(int id){
        //  Create db instance and read account details within row at key id, then store these details to a new account object.
        DataInteractions dbAccess = new DataInteractions(this);
        acc = dbAccess.readDetails(id);

        //  Obtain UI elements
        serviceName = findViewById(R.id.service_text);
        username = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);

        //  Assign values from UI elements to relevant variables
        serviceName.setText(acc.getService());
        username.setText(acc.getName());
        password.setText(acc.getPassword());
    }

    /**
     * Generate a password based on the selected user criteria and display it on screen.
     */
    public void generatePassword(View view){
        passOutput = findViewById(R.id.gen_password_text);
        getPassValues();

        Password pass = new Password(length, caps, nums, specials, extraSpecials);

        passOutput.setText(pass.generatePassword());
    }

    /**
     * Assign values regarding password generation from the relevant UI elements.
     */
    public void getPassValues(){
        //  Assign values to declared objects from UI elements
        capsSelect = (Switch) findViewById(R.id.switchCaps);
        numsSelect = (Switch) findViewById(R.id.switchNums);
        specialsSelect = (Switch) findViewById(R.id.switchChars);
        moreSpecialsSelect = (Switch) findViewById(R.id.switchExtraChars);
        passLenSpinner = (Spinner) findViewById(R.id.len_spinner);

        //  Assign values from UI elements to relevant variables
        caps = capsSelect.isChecked();
        nums = numsSelect.isChecked();
        specials = specialsSelect.isChecked();
        extraSpecials = moreSpecialsSelect.isChecked();
        length = Integer.parseInt(passLenSpinner.getSelectedItem().toString());
    }

    /**
     * Attempt to save the user's inputted account details to the database.
     */
    public void saveDetails(View view){
        getUserValues();
        db = new DataInteractions(this);

        //  Check if we are saving a new account or an existing one. If new, create a new row in DB and if not, update the existing row.
        if(!updating) {
            //  Check that all user's inputs are not empty.
            if (validateInput(service) && validateInput(user) && validateInput(pass)) {

                //  Attempts to insert the user's inputted details. Returns false if unable to add the supplied data.
                boolean checkInsert = db.insertData(service, user, pass);

                //  Check if we were successful in saving the user's details and tell them.
                if (checkInsert) {
                    Toast.makeText(getApplicationContext(), "Entry saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Entry unsuccessful", Toast.LENGTH_SHORT).show();
                }
            } else {    //  Tell the user if they left something blank.
                Toast.makeText(getApplicationContext(), "You must enter a service name, username & password", Toast.LENGTH_LONG).show();
            }
        } else {
            //  Attempt to update the record in question and report the result to the user.
            if(db.updateData(id, service, user, pass)){
                Toast.makeText(getApplicationContext(), "Overwritten", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "You must enter a service name, username & password", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Copy the generated password to the user's clipboard.
     */
    public void copyPassword(View view){
        passOutput = findViewById(R.id.gen_password_text);
        String pass = passOutput.getText().toString();

        if (pass != null) {
            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            myClip = ClipData.newPlainText("text", pass);
            myClipboard.setPrimaryClip(myClip);

            Toast.makeText(getApplicationContext(), "Text Copied",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Store the user's inputs from the EditTexts to the fields in this activity.
     */
    public void getUserValues(){
        //  Assign values to declared objects from UI elements
        serviceName = findViewById(R.id.service_text);
        username = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);

        //  Assign values from UI elements to relevant variables
        service = serviceName.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
    }

    /**
     * Check if a string is blank and tell the user if it is.
     *
     * @param input - String to test if blank or not
     * @return - true if input parameter is blank
     */
    public boolean validateInput(String input){
        if(!TextUtils.isEmpty(input)){
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Cannot save empty user data", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
