package com.mycompany.myproject.verticles.reverseproxy.configuration;

/**
 * Used by Configuration
 *
 * @author robertjchristian
 * @see com.mycompany.myproject.verticles.reverseproxy.configuration.ReverseProxyConfiguration
 */
public class RewriteRule {

	public RewriteRule(String protocol, String host, Integer port) {
		this(protocol, host, null, port);
	}

	public RewriteRule(String protocol, String host, String name, Integer port) {
		this(protocol, host, name, port, null, null);
	}

	public RewriteRule(String protocol, String host, String name, Integer port, String trustStorePath, String trustStorePassword) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.name = name;
		this.trustStorePath = trustStorePath;
		this.trustStorePassword = trustStorePassword;
	}

	private String name;
	private String protocol;
	private String host;
	private Integer port;
	private String trustStorePath;
	private String trustStorePassword;

	public String getProtocol() {
		return null == protocol ? "http" : protocol;
	}

	public String getHost() {
		return host;
	}

	public String getName() {
		return name;
	}

	public Integer getPort() {
		return null == port ? ("http".equals(protocol) ? 80 : 443) : port;
	}

	public String getTrustStorePath() {
		return trustStorePath;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}
}
