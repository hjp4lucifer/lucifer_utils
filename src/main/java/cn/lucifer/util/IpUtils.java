package cn.lucifer.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class IpUtils {
	private static final Logger log = Logger.getLogger(IpUtils.class);

	/**
	 * 
	 * @param host
	 * @param port true port exist
	 * @return
	 */
	public static boolean checkExistsPort(String host, int port) {
		Socket socket;
		try {
			socket = new Socket(host, port);
			IOUtils.closeQuietly(socket);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static String getLocalIP() {
		String ip = null;
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!ni.getName().equals("eth0")) {
					continue;
				} else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (SocketException e) {
			log.error("获取eth0 ip 失败", e);
		}
		if (StringUtils.isEmpty(ip)) {
			try {
				String hostName = InetAddress.getLocalHost().getHostName();
				ip = InetAddress.getByName(hostName).getHostAddress();
			} catch (Exception e) {
				log.error("获取eth0 ip 失败", e);
			}
		}
		return ip;
	}
	

}
