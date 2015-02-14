package com.anthony.fernandez.nightly.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;


public class RequestSender {

	public void sendRequest(final String url, final String apiPoint, final String[] params, final ArrayList<NameValuePair> nameValuePairs){
		new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject jsonData = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}

			@Override
			protected String doInBackground(String... arg0) {
				String result = "";
				InputStream is = null;
				
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url+ apiPoint);
					httpPost.setHeader("CONTENT_TYPE", "application/x-www-form-urlencoded; charset=utf-8");
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

					HttpResponse response = httpclient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();

					result = sb.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return result;
			}
		}.execute(params);
	}

}
