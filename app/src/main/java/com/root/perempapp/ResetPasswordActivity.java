package com.root.perempapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText Email;
    private Button Reset;
    private Button Cancel;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        Email=(EditText)findViewById(R.id.editMailAddress);
        Reset=(Button)findViewById(R.id.btnReset);
        Cancel=(Button)findViewById(R.id.btnCancel);
        baseUrl = "http://10.0.2.2/perempapp/apil/user/sendMail.php";//(ipemulator !! )

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ApiAuthenticationClient apiAuthenticationClient =
                            new ApiAuthenticationClient(
                                    baseUrl
                                    ,""
                                    , ""
                            );
                    apiAuthenticationClient.setHttpMethod("GET");
                    apiAuthenticationClient.setParameter("mail",Email.getText().toString());

                    AsyncTask<Void, Void, String> execute = new ResetPasswordActivity.ExecuteNetworkOperation(apiAuthenticationClient);
                    execute.execute();
                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ResetPasswordActivity.this,MainActivity.class);
                startActivity(intent);            }
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
            try {
                isValidCredentials = apiAuthenticationClient.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (isValidCredentials.getBoolean("status")) {

                    Toast.makeText(getApplicationContext(), "Email of reset password resent", Toast.LENGTH_LONG).show();

            }else {
                    Toast.makeText(getApplicationContext(), "Email of reset password not resent please contact us", Toast.LENGTH_LONG).show();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
}
}
