package edu.stevens.cs548.clinic.rest.client.stub;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;

import edu.stevens.cs548.clinic.service.dto.util.GsonFactory;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebClient {

	protected final Logger logger = Logger.getLogger(WebClient.class.getCanonicalName());

	protected MediaType jsonType = MediaType.get("application/json");

	/*
	 * The client stub used for Web service calls.
	 */
	private IServerApi client;

	public WebClient(URI baseUri) {
		/*
		 * Create the HTTP client stub.
		 */

//		Interceptor interceptor = new Interceptor() {
//			@Override
//			public okhttp3.Response intercept(Chain chain) throws IOException {
//				Request newRequest = chain.request().newBuilder().addHeader("Connection", "close").build();
//				return chain.proceed(newRequest);
//			}
//		};


		OkHttpClient httpClient = new OkHttpClient.Builder().build();

//		httpClient.interceptors().add(interceptor);

		/*
		 * Gson converter
		 */
		Gson gson = GsonFactory.createGson();

		/*
		 * TODO Wrap the okhttp client with a retrofit stub factory.
		 */
		try {
			System.out.println("Base URI: " + baseUri.toString());
			Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUri.toURL())
					.addConverterFactory(GsonConverterFactory.create(gson)).client(httpClient).build();
			/*
			 * Create the stub that will be used for Web service calls
			 */
			client = retrofit.create(IServerApi.class);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.info("Bad URL");
			e.printStackTrace();
		}

	}

	public void upload(final IStreamingOutput output) throws IOException {
		/*
		 * We will stream the output JSON data in this request body.
		 */

		RequestBody requestBody = new RequestBody() {

			// private long count;

			@Override
			public MediaType contentType() {
				return jsonType;
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				try (OutputStream os = sink.outputStream()) {
					output.write(os);
				}
			}
		};

		/*
		 * Execute the Web service call
		 */
		logger.info("Uploading data to the server....");
		logger.info("Content length: " + requestBody.contentLength() + " Content Type: " + requestBody.contentType());

		Response<Void> response = client.upload(requestBody).execute();
		// logger.info("...done, HTTP status = "+response.code() + " more details: " +
		// response.errorBody().string());

		if (!response.isSuccessful()) {
			throw new IOException("Upload failed with HTTP status " + response.code());
		}
	}

}
