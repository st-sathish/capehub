package com.capestart.security.util;

import java.net.URL;

import org.apache.commons.lang.StringUtils;

import static com.capestart.util.data.Tuple.tuple;

import com.capestart.util.data.Tuple;

/** Capehub security helpers. */
public final class SecurityUtil {

	private SecurityUtil(){}
	
	/** Extract hostname and port number from a URL. */
	public static Tuple<String, Integer> hostAndPort(URL url) {
		return tuple(StringUtils.strip(url.getHost(), "/"), url.getPort());
	}
}
