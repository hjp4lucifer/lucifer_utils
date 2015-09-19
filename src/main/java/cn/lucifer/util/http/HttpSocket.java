package cn.lucifer.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.conn.util.InetAddressUtils;

/**
 * http请求, 使用socket的方式访问, 默认使用keep-alive
 * 
 * @author Lucifer
 *
 */
public class HttpSocket {

	private static final Log log = LogFactory.getLog(HttpSocket.class);

	/**
	 * 域名, 不含http://, 注意, 传入是ip时, 勿使用ip V6
	 */
	protected String host;

	/**
	 * 使用默认端口
	 */
	protected int port = 80;

	/**
	 * {@link #host}对应的ip地址
	 */
	protected InetAddress inetAddress;

	protected Socket socket;

	/**
	 * @see Socket#setSoTimeout
	 */
	protected int timeout;

	protected boolean keepAlive = true;

	/**
	 * 
	 * @param host
	 * @throws UnknownHostException
	 */
	public HttpSocket(String host) throws UnknownHostException {
		this.host = host;
		inetAddress = getInetAddress(host);
	}

	public HttpSocket(String host, int timeout) throws UnknownHostException {
		this(host);
		this.timeout = timeout;
	}

	public HttpSocket(String host, int port, int timeout)
			throws UnknownHostException {
		this(host, timeout);
		this.port = port;
	}

	/**
	 * 
	 * @param method
	 * @param uri
	 *            不含host, 可拼GET参数
	 * @return
	 * @throws IOException
	 * @throws HttpClientException
	 */
	public byte[] request(HttpMethod method, String uri,
			Map<String, String> httpHeads, InputStream httpBody)
			throws IOException, HttpException {
		connect();
		StrBuilder builder = new StrBuilder(method.toString());
		builder.append(' ').append(uri).append(" HTTP/1.1\r\n");
		if (null == httpHeads) {
			httpHeads = new HashMap<>();
		} else {
			httpHeads.remove("Host");
		}

		if (keepAlive) {
			httpHeads.put("Connection", "keep-alive");
		} else {
			httpHeads.put("Connection", "close");
		}

		builder.append("Host: ").append(host).append("\r\n");
		for (Entry<String, String> head : httpHeads.entrySet()) {
			builder.append(head.getKey()).append(": ").append(head.getValue())
					.append("\r\n");
		}
		builder.append("\r\n");
		String headerStr = builder.toString();
		log.debug(headerStr);

		OutputStream output = socket.getOutputStream();
		IOUtils.write(headerStr.getBytes(), output);

		if (null != httpBody) {
			IOUtils.copy(httpBody, output);
		}
		// output.write(0);
		output.flush();
		if (method == HttpMethod.DELETE) {
			return new byte[0];
		}

		InputStream input = socket.getInputStream();
		HttpResponse respone = new HttpResponse();
		respone.readResponse(input);

		if (respone.getRspCode() != HttpStatus.SC_OK) {
			throw new HttpException(respone.getRspCode(), new String(
					respone.getRspData()));
		}

		return respone.getRspData();
	}

	public InetAddress getInetAddress(String host) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(host);
		if (log.isDebugEnabled()) {
			log.debug("The hostname was: " + inetAddress.getHostName());
			log.debug("The hosts IP address is: "
					+ inetAddress.getHostAddress());
		}
		return inetAddress;
	}

	public boolean isIPAddress(final String hostname) {
		return hostname != null
				&& (InetAddressUtils.isIPv4Address(hostname) || InetAddressUtils
						.isIPv6Address(hostname));
	}

	/**
	 * 无论连接是否已经建立，都重新建立一次连接
	 * 
	 * @throws IOException
	 */
	public void reConnect() throws IOException {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		for (int i = 0;; i++) {
			try {
				socket = new Socket(inetAddress, port);
				if (keepAlive) {
					socket.setKeepAlive(keepAlive);
				}
				if (timeout > 0) {
					socket.setSoTimeout(timeout);
				}
				break;
			} catch (IOException e) {
				log.debug("", e);
				if (i == 2) {
					throw e;
				}
			}
		}
	}

	/**
	 * 如果socket已经连接，则不再重连，否则重新连接
	 * 
	 * @throws IOException
	 */
	public void connect() throws IOException {
		if (socket == null || !socket.isConnected()) {
			reConnect();
		}
	}

	public void close() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			socket = null;
		}
	}
}
