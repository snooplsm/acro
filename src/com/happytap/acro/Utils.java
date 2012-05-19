package com.happytap.acro;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utils {

	public static String getLocalIpAddress() {
	    try {
		for (Enumeration<NetworkInterface> en = NetworkInterface
			 .getNetworkInterfaces(); en.hasMoreElements();) {
		    NetworkInterface intf = en.nextElement();
		    for (Enumeration<InetAddress> enumIpAddr = intf
			     .getInetAddresses(); enumIpAddr.hasMoreElements();) {
			InetAddress inetAddress = enumIpAddr.nextElement();
			if (!inetAddress.isLoopbackAddress()) {
			    return inetAddress.getHostAddress().toString();
			}
		    }
		}
	    } catch (SocketException ex) {
	    	
	    }
	    return null;
	}

}
