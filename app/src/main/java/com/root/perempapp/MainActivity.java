package com.root.perempapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.JsonObject;
import com.root.perempapp.servlet.Base64Encoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.root.perempapp.AddProductActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.root.perempapp.R.id.editPassword;
import static com.root.perempapp.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    private EditText User;
    private EditText Password;
    private Button Login;
    private TextView ResetPassword;
    private TextView CreatNewAccount;
    private String baseUrl;
    private String baseUrlProduct;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseUrl = "http://10.0.2.2/perempapp/apil/user/login.php";//(ipemulator !! )
        baseUrlProduct = "http://10.0.2.2/perempapp/apil/product/product.php";//(ipemulator !! )


        User=(EditText)findViewById(R.id.editUser);
        Password=(EditText)findViewById(R.id.editPassword);
        Login=(Button)findViewById(R.id.btnLogin);
        ResetPassword=(TextView)findViewById(R.id.textForgotPassword);
        CreatNewAccount=(TextView)findViewById(R.id.textCreatNewUser);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    username = User.getText().toString();
                    password = Password.getText().toString();

                    ApiAuthenticationClient apiAuthenticationClient =
                            new ApiAuthenticationClient(
                                    baseUrl
                                    , username
                                    , password
                            );
                    apiAuthenticationClient.setParameter("mail",username);
                    apiAuthenticationClient.setParameter("password",password);
                    apiAuthenticationClient.setHttpMethod("GET");
                    AsyncTask<Void, Void, String> execute = new ExecuteNetworkOperation(apiAuthenticationClient);
                    execute.execute();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        CreatNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


    public class ExecuteNetworkOperation extends AsyncTask<Void, Void, String> {

        private ApiAuthenticationClient apiAuthenticationClient;
        private JSONObject isValidCredentials;
        private CredentialsBO credential=new CredentialsBO();
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

            // Login Success
            if (isValidCredentials.getBoolean("status")) {
                credential.setId(isValidCredentials.getString("id"));
                credential.setMail(isValidCredentials.getString("mail"));
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();

                goToSecondActivity();
            }
            // Login Failure
            else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                credential.clearAll();
            }
        }
         catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
    }

  /**
     * Open a new activity window.
     */
    private void goToSecondActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("id", credential.getId());
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    ApiAuthenticationClient apiGetAllProduct = new ApiAuthenticationClient(baseUrlProduct,"","");
                    apiGetAllProduct.setParameter("user_id",credential.getId());
                    apiGetAllProduct.setHttpMethod("GET");
                    JSONArray jsonArrayResponse= apiGetAllProduct.executeArray();
                    for(int n = 0; n < jsonArrayResponse.length(); n++)
                    {
                        try {
                            JSONObject object = jsonArrayResponse.getJSONObject(n);
                            String  name=     object.get("Name").toString();
                            String   category=     object.get("Category").toString();
                            String     date=    object.get("Peremp_Date").toString();
                            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
                            Date todayDate = new Date();

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(todayDate);
                            cal.add(Calendar.DATE, 5);
                            Date dateBeforeDays = cal.getTime();
                            if (todayDate.after(date1)){
                                NotificationHelper notificationHelper=new NotificationHelper(getApplicationContext());
                                notificationHelper.NOTIFICATION_CHANNEL_ID+= notificationHelper.NOTIFICATION_CHANNEL_ID;

                                        notificationHelper.createNotification("Product perempted","Your product :"+name+" with category : "+category+//
                                        " is perempted in:"+date1.toString(),MainActivity.class);
                            }else if (dateBeforeDays.after(date1)){
                                NotificationHelper notificationHelper=new NotificationHelper(getApplicationContext());
                                notificationHelper.NOTIFICATION_CHANNEL_ID+= notificationHelper.NOTIFICATION_CHANNEL_ID;

                                notificationHelper.createNotification("Product will be perempt in 5Days","Your product :"+name+" with category : "+category+//
                                        " is perempted in:"+date1.toString(),MainActivity.class);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
}
