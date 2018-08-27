package cn.faury.fdk.http.client.conf;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class HttpConfigTest {

    @Test
    public void init() throws Exception {
        HttpConfig.Builder builder = new HttpConfig.Builder();
        File file = new File("D:\\faury\\github\\fdk\\fdk-http-client\\src\\test\\resources\\http.properties");
        HttpConfig config = builder.buildFromInputStream(new FileInputStream(file));
        System.out.println(config);
    }

}