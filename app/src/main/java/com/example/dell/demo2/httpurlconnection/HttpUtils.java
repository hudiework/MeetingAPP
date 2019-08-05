package com.example.dell.demo2.httpurlconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

	public static int BUFFER_SIZE = 4096;

	public static String httpURLConnectionPostData(String httpPathString,
			String jsonString, String encodeString, String contentType)
			throws Exception {
		String responseString = "";
		URL url = new URL(httpPathString);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setReadTimeout(5 * 1000);
		httpUrlConnection.setRequestProperty("Content-type", contentType + " "
				+ encodeString);
		httpUrlConnection.setRequestMethod("POST");
		OutputStream outStrm = httpUrlConnection.getOutputStream();
		outStrm.write(jsonString.getBytes());
		outStrm.flush();
		outStrm.close();
		if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inStrm = httpUrlConnection.getInputStream();
			responseString = inputStreamTOString(inStrm, encodeString);
			inStrm.close();
		}
		httpUrlConnection.disconnect();
		return responseString;

	}

	public static String httpURLConnectionGetData(String httpPathString,
			String str, String encodeString, String contentType)
			throws Exception {
		String responseString = "";
		URL url = new URL(httpPathString + "?" + str);
		HttpURLConnection httpUrlConnection = (HttpURLConnection) url
				.openConnection();
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setReadTimeout(5 * 1000);
		if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inStrm = httpUrlConnection.getInputStream();
			responseString = inputStreamTOString(inStrm, encodeString);
			inStrm.close();
		}
		httpUrlConnection.disconnect();

		return responseString;

	}

	public static String inputStreamTOString(InputStream in) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		return new String(outStream.toByteArray());
	}

	public static String inputStreamTOString(InputStream in, String encoding)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		return new String(outStream.toByteArray(), encoding);
	}


	public static InputStream stringTOInputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(
				in.getBytes("ISO-8859-1"));
		return is;
	}


	public static byte[] inputStreamTOByte(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return outStream.toByteArray();
	}


	public static InputStream byteTOInputStream(byte[] in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}


	public static String byteTOString(byte[] in) throws Exception {
		InputStream is = byteTOInputStream(in);
		return inputStreamTOString(is);
	}

	public static Bitmap getImage(String path) throws IOException {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(5 * 1000);
		InputStream inputStream = conn.getInputStream();
		byte[] data = readInputStream(inputStream);
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}

	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
}