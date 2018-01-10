package com.crimps.ui;

import java.net.InetAddress;

/**
 * @author crimps
 * @create 2018-01-10 9:32
 **/
public class AuthUtils {
    private static String[] hostNames = {"DESKTOP-K3IUK62", "xmcares-PC", "wzt", "QYC-PC"};

    public static boolean authComputer() {
        boolean flag = false;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String localHostName = inetAddress.getHostName().toLowerCase();
            for (String str : hostNames) {
                if (str.equalsIgnoreCase(localHostName)) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
