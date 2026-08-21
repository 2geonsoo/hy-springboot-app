package com.example.hyspringbootapp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    private final String appVersion;
    private final String privateAddress;

    public WebController(
            @Value("${app.version:${APP_VERSION:dev}}") String appVersion,
            @Value("${app.private-address:}") String configuredPrivateAddress) {
        this.appVersion = appVersion;
        this.privateAddress = configuredPrivateAddress.isBlank()
                ? resolvePrivateAddress()
                : configuredPrivateAddress;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Hello, Spring Boot!",
                "version", appVersion,
                "privateAddress", privateAddress);
    }

    private static String resolvePrivateAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address && address.isSiteLocalAddress()) {
                        return address.getHostAddress();
                    }
                }
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException exception) {
            return "unknown";
        }
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/timecheck")
    public Map<String, String> timecheck() {
        return Map.of("time", OffsetDateTime.now().toString());
    }
}
