package cn.faury.fdk.http.client.conf;

import org.junit.Test;

import java.io.File;

public class HttpConfigTest {

    @Test
    public void init() throws Exception {
        HttpConfig.Builder builder = new HttpConfig.Builder();
        File file = new File("D:\\faury\\github\\fdk\\fdk-http-client\\src\\test\\resources\\http.properties");
        HttpConfig config = builder.buildFromFile(file);
        System.out.println(config);
    }

}