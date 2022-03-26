package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public DataInteractions dbAccess;
    private ArrayList<Account> accounts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Remove action bar from activity_main.xml template - Found online.
        try
        {
            this.getSupportActionBar().hide();
        }
        catch(NullPointerException e){}


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Initialises the accounts arraylist to populate it with details from the database
        getAccounts();
        //  Displays account data from the database
        displayDetails();
    }

    //  This is called every time we even switch to this app
    // TODO: FIX ME
    @Override
    public void onResume(){
        super.onResume();

        //  Refreshes account data when activity is resumed
        accounts.clear();
        getAccounts();
        displayDetails();
    }

    public void displayDetails(){
        //  Create objects for the container layout, the layout inflater and the one to hold saved data
        LinearLayout container = (LinearLayout) findViewById(R.id.linear_layout_container);
        LayoutInflater inflater = getLayoutInflater();

        View accountView;

        //  Clear container. Repeats displaying already displayed accounts
        // TODO: FIX ME
        container.removeAllViewsInLayout();

        //  Loop through the generated account objects array list and inflate the fields of each
        for (Account account : accounts){
            accountView = inflater.inflate(R.layout.account_details, container, false);

            ImageView editAccount = (ImageView) accountView.findViewById(R.id.edit_account);
            LinearLayout passLine = (LinearLayout) accountView.findViewById(R.id.LinearPasswordLayout); passLine.setVisibility(View.GONE);
            ImageView showPass = (ImageView) accountView.findViewById(R.id.view_pass);
            TextView fragService = (TextView) accountView.findViewById(R.id.service_text);
            TextView fragUser = (TextView) accountView.findViewById(R.id.account_user_text);
            TextView fragPass = (TextView) accountView.findViewById(R.id.account_pass_text);
            TextView fragID = (TextView) accountView.findViewById(R.id.debug_account_id);   fragID.setText(Integer.toString(account.id));
            // DEBUG    ^^  ^^  ^^  ^^  ^^  ^^  ^^

            showPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(passLine.getVisibility()==View.GONE) {
                        passLine.setVisibility(View.VISIBLE);
                    } else {
                        passLine.setVisibility(View.GONE);
                    }
                }
            });

            editAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, AddAccount.class);
                    i.putExtra("account_id",account.id);
                    startActivity(i);
                    accounts.clear();
                }
            });

            fragService.setText(account.service);
            fragUser.setText(account.name);
            fragPass.setText(account.password);

            container.addView(accountView);
        }
    }

    private void showPassword(View view){

    }

    public void getAccounts(){
        dbAccess = new DataInteractions(this);
        long total = getProfilesCount();

        for(int i = 1; i <= total; i++){
            Account acc = dbAccess.readDetails(i);
            accounts.add(acc);
        }

    }

    public long getProfilesCount() {
        SQLiteDatabase db = dbAccess.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DataContract.AccountEntry.TABLE_NAME);
        db.close();

        return count;
    }

    public void addAccount(View view){
        Intent i = new Intent(MainActivity.this, AddAccount.class);
        startActivity(i);
        accounts.clear();
    }

    public void deleteAllDetails(View view){
        SQLiteDatabase db = dbAccess.getWritableDatabase();
        db.execSQL("DELETE FROM "+ DataContract.AccountEntry.TABLE_NAME);

        finish();
        startActivity(getIntent());
    }

}