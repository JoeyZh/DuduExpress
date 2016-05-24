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
import java.io.UnsupportedEncodingException;
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
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.general.utils.MyLog;

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
		String requestJson = createRequestJsonString(param);
		MyLog.i(TAG,"url = "+url);
		HttpResponse response = null;
		HttpPost post = new HttpPost(url);
		
		try {
			this.setPostEntity(post, requestJson);
			response = executeHttpRequest(post);
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
		String paramStr = createRequestJsonString(param);
		HttpResponse response = null;
		HttpPost post = new HttpPost(url);
		try {
			setPostEntity(post, paramStr);
			response = executeHttpRequest(post);
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
	@Override
	public boolean httpUpLoadFile(String path, String url,
			HashMap<String, Object> parasMap) {
		String json = createRequestJsonString(parasMap);
		 FileEntity fileBody = new FileEntity(new File(path),"multipart/form-data");  
	      
		 HttpPost post = new HttpPost(url);
		 try {
//			post.addHeader(HTTP.CONTENT_TYPE, "multipart/form-data");
			post.addHeader(HTTP.CONTENT_TYPE, "application/octet-stream");
			post.setEntity(fileBody);
			setPostEntity(post, json);
			HttpResponse response = executeHttpRequest(post);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private String createRequestJsonString(HashMap<String, Object> param) {
		Set keys = param.keySet();
		JSONObject request = new JSONObject();
		JSONObject root = new JSONObject();
		try {
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Object value = (Object) param.get(key);
				if (key == null) {
					continue;
				}
				if (value instanceof String)
					value = URLEncoder.encode((String) value, "UTF-8");
				request.put(key, value);
			}
			root.put("Request", request);
		} catch (Exception e) {

		}
		Log.v(TAG, "Post string = " + root.toJSONString());

		return root.toJSONString();
	}

	private void setPostEntity(HttpPost post, String str) throws Exception {
		post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		StringEntity s = new StringEntity(str);
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");
		post.setEntity(s);
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
				JSONObject root = JSON.parseObject(result).getJSONObject(
						"Response");
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


}
