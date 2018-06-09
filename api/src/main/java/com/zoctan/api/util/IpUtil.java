package com.zoctan.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * IP工具
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public class IpUtil {
    private final static String UNKNOWN = "unknown";
    private final static String LOCALHOST_IPV4 = "127.0.0.1";
    private final static String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取登录用户的IP地址
     *
     * @param request request
     * @return IP地址
     */
    public static String getIpAddress(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 通过IP获取地址(需要联网，调用淘宝的IP库)
     *
     * @param ip ip
     * @return 地址
     */
    public static String getIpInfo(final String ip) {
        String info = "";
        try {
            final URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            final InputStream in = connection.getInputStream();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            final StringBuilder temp = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            final JSONObject obj = (JSONObject) JSON.parse(temp.toString());
            if (obj.getIntValue("code") == 0) {
                final JSONObject data = obj.getJSONObject("data");
                info += data.getString("country") + " ";
                info += data.getString("region") + " ";
                info += data.getString("city") + " ";
                info += data.getString("isp");
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return info;
    }
}
