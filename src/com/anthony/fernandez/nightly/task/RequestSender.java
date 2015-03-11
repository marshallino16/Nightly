package com.anthony.fernandez.nightly.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import com.anthony.fernandez.nightly.api.ParametersApi;
import com.anthony.fernandez.nightly.model.RequestReturn;


public class RequestSender {

	public synchronized RequestReturn sendRequestPost(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs, final String accessToken){
		String result = "";
		int code = 0;
		InputStream is = null;
		
		try {
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			HttpClient client = new DefaultHttpClient();
			
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			DefaultHttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());

			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			HttpPost httpPost;
			if(null == accessToken){
				httpPost = new HttpPost(url+ apiPoint);
			} else {
				httpPost = new HttpPost(url+ apiPoint + "?"+ParametersApi.ACCESS_TOKEN +"="+ accessToken);
			}
			httpPost.setHeader("CONTENT_TYPE", "application/x-www-form-urlencoded; charset=utf-8");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			code = response.getStatusLine().getStatusCode();
			is = entity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return new RequestReturn(result, code);
	}

	
	public synchronized RequestReturn sendRequestGet(final String url, final String apiPoint, final ArrayList<NameValuePair> nameValuePairs){
		String result = "";
		int code = 0;
		InputStream is = null;
		
		try {
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			HttpClient client = new DefaultHttpClient();
			
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			DefaultHttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());

			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			
			String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
			HttpGet httpGet = new HttpGet(url+ apiPoint+ "?" + paramsString);
			httpGet.setHeader("CONTENT_TYPE", "application/x-www-form-urlencoded; charset=utf-8");

			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			code = response.getStatusLine().getStatusCode();
			is = entity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return new RequestReturn(result, code);
	}
}
