package top.joyer.Utils;

import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import top.joyer.MidjourneySupport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    private static final CloseableHttpClient httpClient;

    static  {
        httpClient = HttpClients.createDefault();
    }

    /**
     * Get请求
     * @return respond
     * @throws IOException
     */
    public static String sendGetRequest(String url,String key) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization",key);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) { //response为非空且状态为ok才接收
                return EntityUtils.toString(entity);
            }
        }
        return null;
    }
    /**
     * Post请求
     * @return respond
     * @throws IOException
     */
    public static String sendPostRequest(String url, String requestBody,String key) throws IOException {
        HttpPost request = new HttpPost(url);
        StringEntity _entity = new StringEntity(requestBody,StandardCharsets.UTF_8);
        request.setEntity(_entity);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization",key);
        MidjourneySupport.INSTANCE.getLogger().info(requestBody);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //response为非空且状态为ok才接收
                return EntityUtils.toString(entity);
            }
            MidjourneySupport.INSTANCE.getLogger().error("请求发生错误"+response.getStatusLine().getStatusCode()+"\n"+EntityUtils.toString(entity));
        }
        return null;
    }
    public static byte[] getImgToURL(String url) throws IOException {
        URL url1=new URL(url);
        URLConnection connection=url1.openConnection();
        InputStream inputStream=connection.getInputStream();
        return IOUtils.toByteArray(inputStream);
    }

}
