package cn.faury.fdk.http.client;

import cn.faury.fdk.http.client.core.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;

public class HttpUtilTest {
    @Test
    public void get1() throws Exception {
        String uri = "http://test.wassx.cn/ssk-platform-mobile/mobile/exec";
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("m","getAppVersionUp"));
        params.add(new BasicNameValuePair("appCode","sskAPP"));
        System.out.println(HttpUtil.get(uri,params));
    }

    @Test
    public void get2() throws Exception {
        String uri = "http://test.wassx.cn/ssk-platform-mobile/mobile/exec?m=getAppVersionUp";
        System.out.println(HttpUtil.get(uri));
    }

    @Test
    public void get3() throws Exception {
        String uri = "http://test.wassx.cn/ssk-platform-mobile/mobile/exec?m=getAppVersionUp";
        System.out.println(HttpUtil.get(uri));
    }

    @Test
    public void get() throws Exception {
        String uri = "http://test.wassx.cn/ssk-platform-mobile/mobile/exec?m=getAppVersionUp";
        System.out.println(HttpUtil.get(uri));
        uri = "https://www.wassk.cn/ssk-platform-mobile/mobile/exec?m=getAppVersionUp";
        System.out.println(HttpUtil.get(uri));
    }

    @Test
    public void getByProxy(){
        String uri = "http://test.wassx.cn/ssk-platform-mobile/mobile/exec";
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("m","getAppVersionUp"));
        params.add(new BasicNameValuePair("appCode","sskAPP"));
        HttpResponse response = HttpUtil.get(uri,params);
        System.out.println(response);
    }
}