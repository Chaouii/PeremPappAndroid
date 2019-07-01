package com.root.perempapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AddProductActivity extends AppCompatActivity {
    private EditText ProductName;
    private EditText ProductCat;
    private EditText PeremptionDate;

    private Button Logout;
    private Button Add;
    private String baseUrl;
    private ProductBO product= new ProductBO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent lastData= getIntent();
        product.setUserId(lastData.getStringExtra("id"));
        setContentView(R.layout.activity_add_product);
        ProductName = (EditText) findViewById(R.id.editProductName);
        ProductCat = (EditText) findViewById(R.id.editCat);
        PeremptionDate = (EditText) findViewById(R.id.editPerempDate);
        Logout = (Button) findViewById(R.id.btnLogOut);
        Add = (Button) findViewById(R.id.btnAdd);
        baseUrl = "http://10.0.2.2:3000/api/products";//(ipemulator !! )

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    product.setName(ProductName.getText().toString());
                    product.setCategory(ProductCat.getText().toString());
                    product.setPerempDate(PeremptionDate.getText().toString());

                    ApiAuthenticationClient apiAuthenticationClient =
                            new ApiAuthenticationClient(
                                    baseUrl
                                    ,"1"
                                    , ""
                            );
                    apiAuthenticationClient.setParameter("userId",product.getUserId());
                    apiAuthenticationClient.setParameter("name",product.getName());
                    apiAuthenticationClient.setParameter("category",product.getCategory());
                    apiAuthenticationClient.setParameter("perempDate",product.getPerempDate());

                    AsyncTask<Void, Void, String> execute = new AddProductActivity.ExecuteNetworkOperation(apiAuthenticationClient);
                    execute.execute();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
        startActivity(intent);
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

                isValidCredentials = apiAuthenticationClient.executeHttpClient();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*try {

                //credential.setId(isValidCredentials.getString("id"));
              //  credential.setMail(isValidCredentials.getString("mail"));
               // credential.setPassword(isValidCredentials.getString("password"));
                //credential.setMessage(isValidCredentials.getString("password"));

                // Login Success
               /* if (isValidCredentials.getBoolean("status")) {

                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();

                    // goToSecondActivity();
                }
                // Login Failure
                else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                    credential.clearAll();
                }
            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
                // Do something to recover ... or kill the app.
            }*/
        }
    }
}