package com.fingerprint.service;

import com.fingerprint.entity.IPAddress;
import com.fingerprint.entity.User;
import com.fingerprint.repository.IPAddressRepository;
import com.fingerprint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IPAddressService {
    private final IPAddressRepository ipAddressRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;

    public IPAddress saveOrUpdateIP(User user, String ip) {
        List<IPAddress> ipAddresses = user.getIpAddresses();
        Optional<IPAddress> optionalIP = Optional.empty();

        if (ipAddresses != null) {
            optionalIP = ipAddresses.stream()
                    .filter(i -> i.getIp().equals(ip))
                    .findFirst();
        }

        if (optionalIP.isPresent()) {
            IPAddress ipAddress = optionalIP.get();
            ipAddress.setLastSeen(LocalDateTime.now());
            ipAddressRepository.save(ipAddress);
            return ipAddress;
        } else {
            String apiUrl = "http://ip-api.com/json/" + "ip" + "?fields=status,country,city";
            IPApiResponse response = restTemplate.getForObject(apiUrl, IPApiResponse.class);

            IPAddress ipAddress = new IPAddress();
            ipAddress.setIp(ip);
            ipAddress.setFirstSeen(LocalDateTime.now());
            ipAddress.setLastSeen(LocalDateTime.now());
            ipAddress.setUser(user);

            if (response != null && "success".equals(response.getStatus())) {
                ipAddress.setCountry(response.getCountry());
                ipAddress.setCity(response.getCity());
            }

            ipAddressRepository.save(ipAddress);
            return ipAddress;
        }
    }


    // Вспомогательный класс для ответа от IP-API
    private static class IPApiResponse {
        private String status;
        private String country;
        private String city;

        public String getStatus() { return status; }
        public String getCountry() { return country; }
        public String getCity() { return city; }
    }
}
