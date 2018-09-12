package cn.faury.fdk.pay.common;

import cn.faury.fdk.pay.alipay.AlipayConfig;
import cn.faury.fdk.pay.tenpay.TenpayConfig;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.security.KeyStore;

/**
 * HTTP 客户端工具
 *
 */
public class HttpClient {
	
	private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
	
	private RequestConfig requestConfig; //请求器的配置
    
    private CloseableHttpClient httpClient; //HTTP请求器
	
    private int socketTimeout = 10000; //连接超时时间，默认10秒
    
    private int connectTimeout = 30000; //传输超时时间，默认30秒
    
	private String charset = "utf-8";
	
    /**
     * 简单 HttpURLConnection 请求结果第一行数据, 支持代理
     * @param requestUrl 请求URL
     * @return 获取结果第一行数据
     */
	public static String simpleRequstFirstLine(String requestUrl) {

		String line = "";

		HttpURLConnection con = null;

		BufferedReader in = null;

		try {
			URL url = new URL(requestUrl);

			if (Configure.proxyHost == null) {
				con = (HttpURLConnection) url.openConnection();
			} else {
				SocketAddress sa = new InetSocketAddress(Configure.proxyHost,
						Configure.proxyPort);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
				con = (HttpURLConnection) url.openConnection(proxy);
			}

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			line = in.readLine();
		} catch (MalformedURLException e) {
			log.error("http请求出错", e);
			line = "";
		} catch (IOException e) {
			log.error("http请求出错", e);
			line = "";
		} catch (Exception e) {
			log.error("http请求出错", e);
			line = "";
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (Exception e) {
				log.error("【请求】关闭连接异常");
				e.printStackTrace();
			}
		}
		
		return line;
	}

	
	private HttpClient() {
		
	}
	
	private HttpClient(Aim aim) throws Exception {
		
		SSLConnectionSocketFactory sslsf = null;
		
		if(aim == Aim.Ali){
			charset = AlipayConfig.inputCharset;
    		sslsf = SSLConnectionSocketFactory.getSocketFactory();
    	}

		if (aim == Aim.Ten) {
			File certificate = new File(TenpayConfig.certLocalPath);
	        FileInputStream stream = new FileInputStream(certificate);
	        char[] password = TenpayConfig.certPassword.toCharArray();
	        KeyStore keyStore = KeyStore.getInstance("PKCS12");
	        keyStore.load(stream, password);//设置证书密码
	        
			if (stream != null) {
				try { stream.close();} catch (Exception e) { e.printStackTrace(); }
			}
			
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, password)
	                .build();
	        
	        sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[]{"TLSv1"},
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		}
       
		//代理
        HttpHost proxy = null;
		if (Configure.proxyHost != null) {
        	proxy = new HttpHost(Configure.proxyHost, Configure.proxyPort);
        }
        
        httpClient = HttpClients.custom()
        		.setProxy(proxy)
                .setSSLSocketFactory(sslsf)
                .build();

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom()
        		.setSocketTimeout(socketTimeout)
        		.setConnectTimeout(connectTimeout)
        		.build();
	}
	
	/**
	 * 获取HTTP 客户端实例
	 * @param aim 客户端访问目的站点（Aim.Ali:支付宝，Aim.Ten:财付通）
	 * @return HTTP 客户端实例
	 * @throws Exception
	 */
	public static HttpClient newInstance(Aim aim) throws Exception{
		HttpClient httpClient;
		try {
			httpClient = new HttpClient(aim);
		} catch (Exception e) {
			log.error("实例化http客户端异常，请检查配置是否正确", e);
			throw e;
		}
		return httpClient;
	}
	
	/**
	 * 获取HTTP 客户端实例
	 * @return 客户端实例
	 */
	public static HttpClient newInstance(){
		return new HttpClient();
	}
	
	/**
	 * 发送 Https 请求
	 * @param url 请求路径
	 * @param xmlObj 请求对象,将转换为xml格式
	 * @return 请求结果
	 * @throws Exception
	 */
	public String doPost(String url, Object xmlObj) throws Exception {
		 //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        
        return doPost(url, postDataXML);
	}
	
	/**
	 * 发送 Https 请求
	 * @param url 请求路径
	 * @param queryString 请求字符串，name1=value1&name2=value2&...
	 * @return 请求结果
	 * @throws Exception
	 */
	public String doPost(String url, String queryString) throws Exception {
		
		String result = null;

		HttpPost httpPost = new HttpPost(url);
	     
		StringEntity postEntity = new StringEntity(queryString, charset);
		httpPost.setEntity(postEntity);
		//httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=" + charset);
        httpPost.addHeader("User-Agent", "Mozilla/4.0");
		
		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		try {
			
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, charset);

		} catch (ConnectionPoolTimeoutException e) {
			log.error("http get throw ConnectionPoolTimeoutException(wait time out)", e);
		} catch (ConnectTimeoutException e) {
			log.error("http get throw ConnectTimeoutException", e);
		} catch (SocketTimeoutException e) {
			log.error("http get throw SocketTimeoutException", e);
		} catch (Exception e) {
			log.error("http get throw Exception", e);
		} finally {
			httpPost.abort();
		}

		return result;
		
    }
	
	
	public RequestConfig getRequestConfig() {
		return requestConfig;
	}
	

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}
	

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	

	public int getSocketTimeout() {
		return socketTimeout;
	}
	

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	

	public int getConnectTimeout() {
		return connectTimeout;
	}
	

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 访问目的
	 */
	public enum Aim {
		Ali, //支付宝 
		Ten //财付通
	}
	
	
}
