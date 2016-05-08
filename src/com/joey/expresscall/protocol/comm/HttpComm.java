package com.joey.expresscall.protocol.comm;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpComm implements IHttpComm {
	private final String TAG = "HttpCommImp";

	private final String CHARSET = "UTF-8";
	private final String APPLICATION_JSON = "application/json";

	private final int CONNECTION_TIMEOUT = 20;

	private final int SO_TIMEOUT = 20;
	private final int cache = 10240;
	private HttpClient mHttpClient;

	public HttpComm() {
		this.mHttpClient = createHttpClient();
	}

	public String httpRequestPost(String url, HashMap<String, Object> param) {
		String jsonString = null;
		Set keys = param.keySet();
		JSONObject root = new JSONObject();
		JSONObject request = new JSONObject();
		Log.v(TAG, "Url  " + url);

		try {
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = (String) param.get(key);
				if ((key == null) || (value == null)) {
					if (key != null)
						Log.v("HttpCommImp", "key=" + key + "value=null");
					else if (value != null)
						Log.v("HttpCommImp", "value=" + value + "key=null");
					else {
						Log.v("HttpCommImp", "key=null,value=null");
					}
				}

				if (value == null) {
					return "-6";
				}
				value = URLEncoder.encode(value, "UTF-8");
				request.put(key, value);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			return "-5:[" + sw.toString() + "][" + e.getMessage() + "]";
		}
		try {
			root.put("Request", request);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.v(TAG, "Post string = " + root.toString());

		// HttpResponse response = null;
		// HttpPost post = new HttpPost(url);
		// post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		// try {
		// StringEntity s = new StringEntity(root.toString());
		// s.setContentEncoding("UTF-8");
		// s.setContentType("application/json");
		// post.setEntity(s);
		// response = executeHttpRequest(post);
		// Log.v(TAG, "Post string = "+root.toString());
		//
		// int statusCode = response.getStatusLine().getStatusCode();
		// if (statusCode == 200) {
		// InputStream is = response.getEntity().getContent();
		// try {
		// jsonString = parseResponse(is);
		// } finally {
		// is.close();
		// }
		// } else if (response != null) {
		// response.getEntity().consumeContent();
		// }
		// } catch (ConnectionPoolTimeoutException e) {
		// return "-3";
		// } catch (ConnectTimeoutException e) {
		// return "-1";
		// } catch (SocketTimeoutException e) {
		// return "-2";
		// } catch (Exception e) {
		// StringWriter sw = new StringWriter();
		// PrintWriter pw = new PrintWriter(sw);
		// e.printStackTrace(pw);
		//
		// return "-4:[" + sw.toString() + "][" + e.getMessage() + "]";
		// }
		try {
			jsonString = doJsonPost(url, root.toString());
		} catch (Exception e) {

		}
		Log.v(TAG, "Post request string = " + jsonString);
		return jsonString;
	}

	public String httpRequestGet(String url, HashMap<String, Object> param) {
		String jsonString = null;
		String paramStr = "";
		Set keys = param.keySet();
		try {
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = (String) param.get(key);

				if ((key == null) || (value == null)) {
					if (key != null)
						Log.v("HttpCommImp", "key=" + key + "value=null");
					else if (value != null)
						Log.v("HttpCommImp", "value=" + value + "key=null");
					else {
						Log.v("HttpCommImp", "key=null,value=null");
					}
				}

				if (value == null) {
					return "-6";
				}
				value = URLEncoder.encode(value, "UTF-8");

				paramStr = paramStr + key + "=" + value + "&";
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			return "-5:[" + sw.toString() + "][" + e.getMessage() + "]";
		}

		if (!paramStr.equals("")) {
			if (url.endsWith("?"))
				url = url + paramStr;
			else {
				url = url + "?" + paramStr;
			}

		}

		HttpResponse response = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			response = executeHttpRequest(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				InputStream is = response.getEntity().getContent();
				try {
					jsonString = parseResponse(is);
				} finally {
					is.close();
				}
			} else if (response != null) {
				response.getEntity().consumeContent();
			}
		} catch (ConnectionPoolTimeoutException e) {
			return "-3";
		} catch (ConnectTimeoutException e) {
			return "-1";
		} catch (SocketTimeoutException e) {
			return "-2";
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			return "-4:[" + sw.toString() + "][" + e.getMessage() + "]";
		}

		return jsonString;
	}

	public boolean httpRequestGetFile(String url,
			HashMap<String, Object> param, String filepath) {
		String paramStr = "";
		Set keys = param.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) param.get(key);
			paramStr = paramStr + key + "=" + value + "&";
		}

		if (!paramStr.equals("")) {
			if (url.endsWith("?"))
				url = url + paramStr;
			else {
				url = url + "?" + paramStr;
			}

		}

		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = executeHttpRequest(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();

				InputStream in = entity.getContent();

				File file = new File(filepath);
				file.getParentFile().mkdirs();
				FileOutputStream fileout = new FileOutputStream(file);

				byte[] buffer = new byte[10240];
				int ch = 0;
				while ((ch = in.read(buffer)) != -1) {
					fileout.write(buffer, 0, ch);
				}
				in.close();
				fileout.flush();
				fileout.close();

				return true;
			}
			if (response != null) {
				response.getEntity().consumeContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private String parseResponse(InputStream in) {
		String result = null;
		try {
			byte[] data = new byte[1024];
			int length = 0;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while ((length = in.read(data)) != -1) {
				bout.write(data, 0, length);
			}
			result = new String(bout.toByteArray(), "UTF-8");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "-7:[" + sw.toString() + "][" + e.getMessage() + "]";
		}

		if ((result != null) && (!result.equals(""))) {
			try {
				JSONObject root = new JSONObject(result).getJSONObject("root");
				result = root.toString();
			} catch (JSONException e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				return "-8:[" + sw.toString() + "][" + e.getMessage() + "]";
			}
		}

		return result;
	}

	private final DefaultHttpClient createHttpClient() {
		SchemeRegistry supportedSchemes = new SchemeRegistry();

		SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));

		HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, true);

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				httpParams, supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}

	private final HttpParams createHttpParams() {
		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 20000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		return params;
	}

	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws Exception {
		try {
			this.mHttpClient.getConnectionManager().closeExpiredConnections();
			return this.mHttpClient.execute(httpRequest);
		} catch (Exception e) {
			httpRequest.abort();
			throw e;
		}
	}

	public String doJsonPost(String url, String postStr) throws Exception {

		System.out.println("发送doPost请求 : " + url);
		StringBuffer parameterBuffer = new StringBuffer();

		if (postStr != null) {
			System.out.println("POST parameter : " + postStr);
			parameterBuffer.append(postStr);
		}

		URL localURL = new URL(url);

		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection
				.setRequestProperty("Content-Type", "application/json");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(parameterBuffer.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			if (postStr != null) {
				outputStream = httpURLConnection.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(outputStream);

				outputStreamWriter.write(parameterBuffer.toString());
				outputStreamWriter.flush();
			}
			System.out.println("httpURLConnection.getResponseCode()"
					+ httpURLConnection.getResponseCode());
			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception(
						"HTTP Request is not success, Response code is "
								+ httpURLConnection.getResponseCode());
			}

			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}

			if (outputStream != null) {
				outputStream.close();
			}

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}
		System.out.println("接收到响应字符串：" + resultBuffer.toString());
		return resultBuffer.toString();
	}
}
