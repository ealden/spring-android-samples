package org.springframework.android.showcase;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.os.Bundle;

public class HttpGetJsonActivity extends AbstractAsyncListActivity 
{
	protected String TAG = "HttpGetActivity";
	
	
	//***************************************
    // Activity methods
    //***************************************
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		// when this activity starts, initiate an asynchronous HTTP GET request
		new DownloadStatesTask().execute();
	}
	
	
	//***************************************
    // Private methods
    //*************************************** 
	private void refreshStates(List<State> states) 
	{	
		if (states == null) 
		{
			return;
		}
		
		StatesListAdapter adapter = new StatesListAdapter(this, states);
		setListAdapter(adapter);
	}
	
	
	//***************************************
    // Private classes
    //***************************************
	private class DownloadStatesTask extends AsyncTask<Void, Void, List<State>> 
	{	
		@Override
		protected void onPreExecute() 
		{
			// before the network request begins, show a progress indicator
			showLoadingProgressDialog();
		}
		
		@Override
		protected List<State> doInBackground(Void... params) 
		{
			try 
			{
				// The URL for making the GET request
				final String url = "http://10.0.2.2:8080/spring-android-showcase/states";
				
				// Set the Content-Type header to "application/json"
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setContentType(MediaType.APPLICATION_JSON);
				
				// Populate the headers in an HttpEntity object to use for the request
				HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
				
				// Create a new RestTemplate instance
				RestTemplate restTemplate = new RestTemplate();
				
				// Perform the HTTP GET request
				ResponseEntity<State[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, State[].class);
								
				// convert the array to a list and return it
				return Arrays.asList(responseEntity.getBody());
			} 
			catch(Exception e) 
			{
				logException(e);
			} 
			
			return null;
		}
		
		@Override
		protected void onPostExecute(List<State> result) 
		{
			// hide the progress indicator when the network request is complete
			dismissProgressDialog();
			
			// return the list of states
			refreshStates(result);
		}
	}
}