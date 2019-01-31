package org.schabi.newpipe;

import android.util.Log;

import org.schabi.newpipe.extractor.exceptions.ReCapthaException;
import org.schabi.newpipe.util.ExtractorHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Downloader implements org.schabi.newpipe.extractor.Downloader{
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0 Gecko/20100101 Firefox/43.0");
    private static String mCookies = "";
    private static Downloader instance = null;

    private Downloader(){}

    public static Downloader getInstance(){
        if(instance==null){
            synchronized (Downloader.class){
                if(instance==null){
                    instance=new Downloader();
                }
            }
        }
        return instance;
    }

    public static synchronized void setCookies(String cookies){
        Downloader.mCookies=cookies;
    }

    public static synchronized String getCookies(){
        return Downloader.mCookies;
    }

    @Override
    public String download(String siteUrl, String language) throws IOException, ReCapthaException{
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept-Language", language);
        return download(siteUrl, requestProperties);
    }

    @Override
    public String download(String siteUrl, Map<String, String> customProperties) throws IOException, ReCapthaException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        Iterator it = customProperties.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            con.setRequestProperty((String) pair.getKey(), (String) pair.getValue());
        }
        return dl(con);
    }

    @Override
    public String download(String siteUrl) throws IOException, ReCapthaException{
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        return dl(con);
    }

    private static String dl(HttpsURLConnection con) throws IOException, ReCapthaException{
        StringBuilder response = new StringBuilder();
        BufferedReader in= null;

        try{
            con.setReadTimeout(30*1000);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            if (getCookies().length() > 0){
                con.setRequestProperty("Cookie", getCookies());
            }

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
        } catch (Exception e)
    }
}