package com.root.perempapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {
    private EditText FirstName;
    private EditText LastName;
    private EditText BDate;
    private EditText Mail;
    private EditText Password;
    private Button Save;
    private Button Cancel;
    private String baseUrl;
    private CredentialsBO credential= new CredentialsBO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirstName=(EditText)findViewById(R.id.editFName);
        LastName=(EditText)findViewById(R.id.editLName);
        BDate=(EditText)findViewById(R.id.editBDate);
        Mail=(EditText)findViewById(R.id.editMail);
        Password=(EditText)findViewById(R.id.editPassword);
        Save=(Button)findViewById(R.id.btnSave);
        Cancel=(Button)findViewById(R.id.btnCancel);
        baseUrl = "http://10.0.2.2:3000/api/credentials";//(ipemulator !! )
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    credential.setfName(FirstName.getText().toString());
                    credential.setlName(LastName.getText().toString());
                    credential.setbDate(BDate.getText().toString());
                    credential.setMail(Mail.getText().toString());
                    credential.setPassword(Password.getText().toString());

                    ApiAuthenticationClient apiAuthenticationClient =
                            new ApiAuthenticationClient(
                                    baseUrl
                                    ,"1",""
                            );
                   apiAuthenticationClient.setParameter("fname",FirstName.getText().toString());
                    apiAuthenticationClient.setParameter("lname",LastName.getText().toString());
                    apiAuthenticationClient.setParameter("mail",Mail.getText().toString());
                    apiAuthenticationClient.setParameter("password",Password.getText().toString());
                    apiAuthenticationClient.setParameter("bdate",BDate.getText().toString());
                    AsyncTask<Void, Void, String> execute = new RegistrationActivity.ExecuteNetworkOperation(apiAuthenticationClient);
                    execute.execute();
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        });

}
    public class ExecuteNetworkOperation extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClient apiAuthenticationClient;
        private JSONObject isValidCredentials;
        private CredentialsBO credential = new CredentialsBO();

        /**
         * Overload the constructor to pass objects to this class.
         */
        public ExecuteNetworkOperation(ApiAuthenticationClient apiAuthenticationClient) {
            this.apiAuthenticationClient = apiAuthenticationClient;
        }


        @Override
        protected String doInBackground(Void... params) {
                isValidCredentials = apiAuthenticationClient.executeHttpClient();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                goToSecondActivity(String.valueOf(isValidCredentials.getInt("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void goToSecondActivity(String id) {
        Bundle bundle = new Bundle();

        bundle.putString("id", id);
        Intent intent = new Intent(RegistrationActivity.this, AddProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
