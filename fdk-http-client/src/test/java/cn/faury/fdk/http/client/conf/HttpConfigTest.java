package cn.faury.fdk.http.client.conf;

import org.junit.Test;

public class HttpConfigTest {

    @Test
    public void init() throws Exception {
        HttpConfig.Builder builder = new HttpConfig.Builder();
        HttpConfig config = builder.buildFromFile("http.properties");
        System.out.println(config);
    }

}