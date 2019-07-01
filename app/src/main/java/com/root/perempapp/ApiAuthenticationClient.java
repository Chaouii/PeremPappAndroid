package com.root.perempapp;


import android.util.Log;
import com.root.perempapp.servlet.Base64Encoder;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 12/8/17.
 */

public class ApiAuthenticationClient {

    private String baseUrl;
    private String username;
    private String password;
    private String urlResource;
    private String httpMethod; // GET, POST, PUT, DELETE
    private String urlPath;
    private String lastResponse;
    private String payload;
    private HashMap<String, String> parameters;
    private Map<String, List<String>> headerFields;

    /**
     *
     * @param baseUrl String
     * @param username String
     * @param password String
     */
    public ApiAuthenticationClient(String  baseUrl, String username, String password) {
if(username.equals("1")){
    this.baseUrl = baseUrl;
}else{
    setBaseUrl(baseUrl);
}
        this.username = username;
        this.password = password;
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "";
        parameters = new HashMap<>();
        lastResponse = "";
        payload = "";
        headerFields = new HashMap<>();
        // This is important. The application may break without this line.
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    /**
     * --&gt;http://BASE_URL.COM&lt;--/resource/path
     * @param baseUrl the root part of the URL
     * @return this
     */
    public ApiAuthenticationClient setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        if (!baseUrl.substring(baseUrl.length() - 1).equals("/")) {
            this.baseUrl += "/";
        }
        return this;
    }

    /**
     * Set the name of the resource that is used for calling the Rest API.
     * @param urlResource http://base_url.com/--&gt;URL_RESOURCE&lt;--/url_path
     * @return this
     */
    public ApiAuthenticationClient setUrlResource(String urlResource) {
        this.urlResource = urlResource;
        return this;
    }

    /**
     * Set the path  that is used for calling the Rest API.
     * This is usually an ID number for Get single record, PUT, and DELETE functions.
     * @param urlPath http://base_url.com/resource/--&gt;URL_PATH&lt;--
     * @return this
     */
    public final ApiAuthenticationClient setUrlPath(String urlPath) {
        this.urlPath = urlPath;
        return this;
    }

    /**
     * Sets the HTTP method used for the Rest API.
     * GET, PUT, POST, or DELETE
     * @return this
     */
    public ApiAuthenticationClient setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    /**
     * Get the output from the last call made to the Rest API.
     * @return String
     */
    public String getLastResponse() {
        return lastResponse;
    }

    /**
     * Get a list of the headers returned by the last call to the Rest API.
     * @return Map&lt;String, List&lt;String&gt;&gt;
     */
    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    /**
     * Replace all of the existing parameters with new parameters.
     * @param parameters
     * @return this
     */
    public ApiAuthenticationClient setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Set a parameter to be used in the call to the Rest API.
     * @param key the name of the parameter
     * @param value the value of the parameter
     * @return this
     */
    public ApiAuthenticationClient setParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    /**
     * Delete all parameters that are set for the Rest API call.
     * @return this
     */
    public ApiAuthenticationClient clearParameters() {
        this.parameters.clear();
        return this;
    }

    /**
     * Remove a specified parameter
     * @param key the name of the parameter to remove
     */
    public ApiAuthenticationClient removeParameter(String key) {
        this.parameters.remove(key);
        return this;
    }

    /**
     * Deletes all values used to make Rest API calls.
     * @return this
     */
    public ApiAuthenticationClient clearAll() {
        parameters.clear();
        baseUrl = "";
        this.username = "";
        this.password = "";
        this.urlResource = "";
        this.urlPath = "";
        this.httpMethod = "";
        lastResponse = "";
        payload = "";
        headerFields.clear();
        return this;
    }

    /**
     * Get the last response from the Rest API as a JSON Object.
     * @return JSONObject
     */
    public JSONObject getLastResponseAsJsonObject() {
        try {
            return new JSONObject(String.valueOf(lastResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the last response from the Rest API as a JSON Array.
     * @return JSONArray
     */
    public JSONArray getLastResponseAsJsonArray() {
        try {
            return new JSONArray(String.valueOf(lastResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the payload as a string from the existing parameters.
     * @return String
     */
    private String getPayloadAsString() {
        // Cycle through the parameters.
        StringBuilder stringBuffer = new StringBuilder();
        Iterator it = parameters.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (count > 0) {
                stringBuffer.append("&");
            }
            stringBuffer.append(pair.getKey()).append("=").append(pair.getValue());

            it.remove(); // avoids a ConcurrentModificationException
            count++;
        }
        return stringBuffer.toString();
    }
    private String getJSONPayloadAsString() {
        // Cycle through the parameters.
        JSONObject jsonObject   = new JSONObject();

        StringBuilder stringBuffer = new StringBuilder("{");
        Iterator it = parameters.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (count > 0) {
                stringBuffer.append("&");
            }
            stringBuffer.append("/\"").append(pair.getKey()).append("/\":/\"").append(pair.getValue()).append("/\"");
if(it.hasNext()){
    stringBuffer.append(",");
}else {
    stringBuffer.append("}");
}
            it.remove(); // avoids a ConcurrentModificationException
            count++;
        }
        return stringBuffer.toString();
    }
    private JSONObject getJSONPayloadAsJSONOBJ() throws JSONException {
        // Cycle through the parameters.
        JSONObject jsonObject   = new JSONObject();

        StringBuilder stringBuffer = new StringBuilder("{");
        Iterator it = parameters.entrySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (count > 0) {
                stringBuffer.append("&");
            }
            jsonObject.put(pair.getKey().toString(),pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
            count++;
        }
        return jsonObject;
    }
    private JSONObject getJSONPayloadAsJSONOBJAc() throws JSONException {
        // Cycle through the parameters.
        JSONObject jsonObject   = new JSONObject();

        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if(pair.getKey().toString().equals("userId")){
                jsonObject.accumulate(pair.getKey().toString(),Integer.valueOf(pair.getValue().toString()));
            }else{
                jsonObject.accumulate(pair.getKey().toString(),pair.getValue().toString());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return jsonObject;
    }
    private List<NameValuePair> getArray()  {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();


            nameValuePairs.add(new BasicNameValuePair(pair.getKey().toString(), pair.getValue().toString()));

            it.remove(); // avoids a ConcurrentModificationException
        }

        return nameValuePairs;
    }
      /**
     * Make the call to the Rest API and return its response as a string.
     * @return String
     */
    public JSONObject execute() {
        String line;
        JSONObject jsonReturns;
        StringBuilder outputStringBuilder = new StringBuilder();

        try {
            StringBuilder urlString = new StringBuilder(baseUrl + urlResource);
if(!username.equals("1")){


    if (!urlPath.equals("")) {
        urlString.append("/" + urlPath);
    }

    if (parameters.size() > 0 && httpMethod.equals("GET")) {
        payload = getPayloadAsString();
        urlString.append("?" + payload);
    }
}
            URL url = new URL(urlString.toString());

            String encoding = Base64Encoder.encode(username + ":" + password);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
          //  connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cache-Control", "no-cache");


            // Make the network connection and retrieve the output from the server.
            if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {


              //  payload = getPayloadAsString();
                JSONObject cred   = new JSONObject();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                try {
                    JSONObject request = getJSONPayloadAsJSONOBJ();
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                    writer.write(request.toString());
                    writer.flush();

                 //   InputStream input = new URL(urlString.toString()).openStream();

                    // headerFields = connection.getHeaderFields();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        outputStringBuilder.append(line);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                connection.disconnect();
            }
            else {
                InputStream content = (InputStream) connection.getInputStream();


                //connection.
                BufferedReader in = new BufferedReader(new InputStreamReader(content));

                while ((line = in.readLine()) != null) {
                    outputStringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the outputStringBuilder is blank, the call failed.
        if (!outputStringBuilder.toString().equals("")) {
            lastResponse = outputStringBuilder.toString();
        }
        jsonReturns=getLastResponseAsJsonObject();
        return jsonReturns;
    }
    /**
     * Make the call to the Rest API and return its response as a string.
     * @return String
     */
    public JSONArray executeArray() {
        String line;
        JSONArray jsonReturns;
        StringBuilder outputStringBuilder = new StringBuilder();

        try {
            StringBuilder urlString = new StringBuilder(baseUrl + urlResource);
            if(!username.equals("1")){


                if (!urlPath.equals("")) {
                    urlString.append("/" + urlPath);
                }

                if (parameters.size() > 0 && httpMethod.equals("GET")) {
                    payload = getPayloadAsString();
                    urlString.append("?" + payload);
                }
            }
            URL url = new URL(urlString.toString());

            String encoding = Base64Encoder.encode(username + ":" + password);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            //  connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cache-Control", "no-cache");


            // Make the network connection and retrieve the output from the server.
            if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {


                //  payload = getPayloadAsString();
                JSONObject cred   = new JSONObject();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                try {
                    JSONObject request = getJSONPayloadAsJSONOBJ();
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                    writer.write(request.toString());
                    writer.flush();

                    //   InputStream input = new URL(urlString.toString()).openStream();

                    // headerFields = connection.getHeaderFields();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        outputStringBuilder.append(line);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                connection.disconnect();
            }
            else {
                InputStream content = (InputStream) connection.getInputStream();


                //connection.
                BufferedReader in = new BufferedReader(new InputStreamReader(content));

                while ((line = in.readLine()) != null) {
                    outputStringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the outputStringBuilder is blank, the call failed.
        if (!outputStringBuilder.toString().equals("")) {
            lastResponse = outputStringBuilder.toString();
        }
        jsonReturns=getLastResponseAsJsonArray();
        return jsonReturns;
    }



    /**
     * Make the call to the Rest API and return its response as a string.
     * @return String
     */
   public JSONObject executeHttpClient() {
        String json="";
       String line="";

       JSONObject jsonReturns;
        StringBuilder outputStringBuilder = new StringBuilder();
       InputStream inputStream = null;
       HttpResponse httpResponse;
       HttpClient httpClient; //
        try {

            httpClient = HttpClientBuilder.create().build();

            HttpPost httpPost = new HttpPost(baseUrl);

            JSONObject request = getJSONPayloadAsJSONOBJAc();
            json=request.toString();
            StringEntity se = new StringEntity(json);
            se.setContentType("application/json");
            se.setContentEncoding("UTF-8");
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            httpPost.setHeader("Cache-Control", "no-cache");
            httpResponse = httpClient.execute(httpPost);


            try {
                    inputStream = httpResponse.getEntity().getContent();

                    int status_code=httpResponse.getStatusLine().getStatusCode();


                     if(status_code==404){
                        Log.d("MYLOG","Sorry! Page not found! Check the URL ");

                    }else if(status_code==500){
                        Log.d("MYLOG","Server is not responding! Sorry try again later..");
                    }
                    else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream ));
                        while ((line = br.readLine()) != null) {
                            outputStringBuilder.append(line);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the outputStringBuilder is blank, the call failed.
        if (!outputStringBuilder.toString().equals("")) {
            lastResponse = outputStringBuilder.toString();
        }
        jsonReturns=getLastResponseAsJsonObject();
        return jsonReturns;
    }
}
