/*
package com.example.demo;



import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpsAccessService {
    public void accessURL() throws CertificateException {
//传输协议需要根据自己的判断　
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
   */
/*     CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(new ByteArrayInputStream(baidu
                .getBytes()));
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("baiducom", ca);
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keystore);


        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);


        URL url = new URL("https://www.baidu.com");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url
                .openConnection();
        NullHostNameVerifier nullHostNameVerifier = new NullHostNameVerifier();
        nullHostNameVerifier.verify("baidu",)
        HttpsURLConnection.setDefaultHostnameVerifier();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream input = urlConnection.getInputStream();


        BufferedReader reader = new BufferedReader(new InputStreamReader(input,
                "UTF-8"));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.print(result.toString());*//*


    }

    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            String[] valueNames = arg1.getValueNames();
            for(String name:valueNames){
                if(StringUtils.equals(name,name)){
                    return true;
                }
            }
            return false;
        }
    }

}
*/
