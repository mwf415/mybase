package cn.onlov.evaluate.util;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * author by mowenfeng
 **/

@Component
public class LocalHostUtils {
    private static LocalHostUtils instance;
    private LocalHostUtils() {
    }

    public static LocalHostUtils getInstance() {
        if (instance == null) {
            instance = new LocalHostUtils();
        }
        return instance;
    }

    /**
     * 获取服务器Ip
     * @return
     */

    public  String getLocalHostIp()  {
        InetAddress address = null;//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
        try {
            address = InetAddress.getLocalHost();
            String hostAddress = address.getHostAddress();//192.168.0.121
            return hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
