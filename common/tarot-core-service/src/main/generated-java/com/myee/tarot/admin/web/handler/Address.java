package com.myee.tarot.admin.web.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/23
 * Time: 11:18
 * Describe:
 * 根据远程ip，查询对应的物理地址<br/>
 * 只返回所查询到的地址的最后一级，例如address="北京市海淀区西二旗"，则返回"西二旗"<br/>
 * 使用太平洋、淘宝、百度三个ip地址查询接口，多进程异步查询<br/>
 * Version:1.0
 */
public class Address {

	private static final Logger LOGGER = LoggerFactory.getLogger(Address.class);
	/**
	 * 多线程公共变量，key=ip,value=address
	 */
	static final Map<String, String> ipAddressMap = new HashMap<String, String>();
	/**
	 * 线程池线程数
	 */
	static final Integer threadSize = 1;
	/**
	 * 太平洋ip地址查询接口
	 */
	static final String urlPcOnline = "http://whois.pconline.com.cn/ipJson.jsp?ip={ip}&timeStamp={timeStamp}";
	/**
	 * 淘宝ip地址查询接口
	 */
	static final String urlTaobao = "http://ip.taobao.com/service/getIpInfo.php?ip={ip}&timeStamp={timeStamp}";
	/**
	 * 百度ip地址查询接口
	 */
	static final String urlBaidu = "http://opendata.baidu.com/api.php?resource_id=6006&format=json&query={ip}&timeStamp={timeStamp}";

	private Address() {
	}

	private static Address address;

	public static Address getSingleInstance() {
		if (null == address) {
			// 懒加载
			synchronized (Address.class) {
				if (null == address) {
					address = new Address();
				}
			}
		}
		return address;
	}

	/**
	 * 根据远程ip，查询对应的物理地址<br/>
	 * 只返回所查询到的地址的最后一级，例如address="北京市海淀区西二旗"，则返回"西二旗"<br/>
	 * 使用太平洋、淘宝、百度三个ip地址查询接口，多进程异步查询<br/>
	 */
	public String getAddress(String ip) {
		// 清除上次查询结果
		ipAddressMap.remove(ip);
		//创建一个可以执行三个线程的线程池
		ExecutorService pool = Executors.newFixedThreadPool(threadSize);
		// 记录起始查询时间，超时未查到就返回空
		Long begin = System.currentTimeMillis();
		// 设置时间戳，防止ip查询接口缓存数据
		String timeStamp = String.valueOf(begin);
//		// 太平洋ip接口查询
//		pool.execute(new AddressPcOnline(ip, timeStamp));
//		// 淘宝ip接口查询
//		pool.execute(new AddressTaobao(ip, timeStamp));
		// 百度ip接口查询
		pool.execute(new AddressBaidu(ip, timeStamp));
		// 开始判断是否查询到结果
		while (null == ipAddressMap.get(ip) && System.currentTimeMillis() - begin < 3000) {
			// 还未获得地址，且未超时(3秒)，就等待(50毫秒)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// 返回空
				return null;
			}
		}
		// 立即关闭所有进程
		pool.shutdownNow();
		return ipAddressMap.get(ip);
	}

	public class AddressPcOnline implements Runnable {

		private String ip;
		private String timeStamp;

		public AddressPcOnline(String ip, String timeStamp) {
			this.ip = ip;
			this.timeStamp = timeStamp;
		}

		@Override
		public void run() {
			String url = urlPcOnline.replace("{ip}", ip).replace("{timeStamp}", timeStamp);
			try {
				//post请求返回结果
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost method = new HttpPost(url);
				HttpResponse result = httpClient.execute(method);
				url = URLDecoder.decode(url, "UTF-8");
				/**请求发送成功，并得到响应**/
				JSONObject obejct = new JSONObject();
				if (result.getStatusLine().getStatusCode() == 200) {
					String str = EntityUtils.toString(result.getEntity());
					str = StringUtils.trim(str);
					str = str.substring(34, str.length() - 3);
					obejct = JSON.parseObject(str);
				}

				if (StringUtils.isNotBlank(ipAddressMap.get(ip))) {
					return;
				}
				LOGGER.info("urlPcOnline | ip=" + ip + " | result=" + result);
				String region = (String) obejct.get("region");
				String pro = (String) obejct.get("pro");
				String city = (String) obejct.get("city");
				String addr = (String) obejct.get("addr");
				String address = region + "-" + pro + "-" + city + "(" + addr + ")";
				ipAddressMap.put(ip, address);
			} catch (Exception e) {
				LOGGER.error("post请求提交失败:" + url, e);
			}
		}
	}

	public class AddressTaobao implements Runnable {

		private String ip;
		private String timeStamp;

		public AddressTaobao(String ip, String timeStamp) {
			this.ip = ip;
			this.timeStamp = timeStamp;
		}

		@Override
		public void run() {
			String url = urlTaobao.replace("{ip}", ip).replace("{timeStamp}", timeStamp);
			try {
				//post请求返回结果
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost method = new HttpPost(url);
				HttpResponse result = httpClient.execute(method);
				url = URLDecoder.decode(url, "UTF-8");
				/**请求发送成功，并得到响应**/
				JSONObject obejct = new JSONObject();
				if (result.getStatusLine().getStatusCode() == 200) {
					String str = EntityUtils.toString(result.getEntity());
					str = StringUtils.trim(str);
					str = str.substring(17, str.length() - 1);
					obejct = JSON.parseObject(str);
				}

				if (StringUtils.isNotBlank(ipAddressMap.get(ip))) {
					return;
				}
				LOGGER.info("urlPcOnline | ip=" + ip + " | result=" + result);
				String area = (String) obejct.get("area");
				String country = (String) obejct.get("country");
				String region = (String) obejct.get("region");
				String city = (String) obejct.get("city"); //市
				String county = (String) obejct.get("county");  //区
				String address = area + "-" + country + "-" + region + "-" + city + "-" + county;
				ipAddressMap.put(ip, address);
			} catch (Exception e) {
				LOGGER.error("post请求提交失败:" + url, e);
			}
		}
	}

	public static class AddressBaidu implements Runnable {

		private String ip;
		private String timeStamp;

		public AddressBaidu(String ip, String timeStamp) {
			this.ip = ip;
			this.timeStamp = timeStamp;
		}

		@Override
		public void run() {
			String url = urlBaidu.replace("{ip}", ip).replace("{timeStamp}", timeStamp);
			try {
				//post请求返回结果
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpGet method = new HttpGet(url);
				HttpResponse result = httpClient.execute(method);
				url = URLDecoder.decode(url, "UTF-8");
				/**请求发送成功，并得到响应**/
				JSONObject obejct = new JSONObject();
				if (result.getStatusLine().getStatusCode() == 200) {
					String str = EntityUtils.toString(result.getEntity());
					str = StringUtils.trim(str);
					str = str.substring(49, str.length() - 2);
					obejct = JSON.parseObject(str);
				}

				if (StringUtils.isNotBlank(ipAddressMap.get(ip))) {
					return;
				}
				LOGGER.info("urlPcOnline | ip=" + ip + " | result=" + result);
				String address = (String) obejct.get("location");
				ipAddressMap.put(ip, address);
			} catch (Exception e) {
				LOGGER.error("post请求提交失败:" + url, e);
			}
		}
	}
}
