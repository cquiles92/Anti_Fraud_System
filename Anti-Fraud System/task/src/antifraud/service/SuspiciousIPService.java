package antifraud.service;

import antifraud.dto.DeletedIPAddressDto;
import antifraud.entity.IPAddress;
import antifraud.exception.conflict.ExistingIPAddressException;
import antifraud.exception.notfound.IPAddressNotFoundException;
import antifraud.exception.badrequest.InvalidIPAddressException;
import antifraud.repository.IPAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousIPService {
    @Autowired
    IPAddressRepository ipAddressRepository;

    public void saveIPAddress(IPAddress address) {
        Optional<IPAddress> savedAddress = ipAddressRepository.findByIp(address.getIp());
        if (savedAddress.isPresent()) {
            throw new ExistingIPAddressException(address.getIp());
        } else {
            ipAddressRepository.save(address);
        }
    }

    public DeletedIPAddressDto deleteIPAddress(String address) {
        if (!isValidIPAddress(address)) {
            throw new InvalidIPAddressException(address);
        }

        Optional<IPAddress> savedAddress = ipAddressRepository.findByIp(address);
        if (savedAddress.isEmpty()) {
            throw new IPAddressNotFoundException(address);
        }
        DeletedIPAddressDto dto = new DeletedIPAddressDto(savedAddress.get());
        ipAddressRepository.delete(savedAddress.get());
        return dto;
    }

    public List<IPAddress> getIPAddressList() {
        return ipAddressRepository.findAll(Sort.by("id"));
    }

    protected boolean isValidIPAddress(String ipAddress) {
        return ipAddress.matches("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    }

    protected Optional<IPAddress> getSuspiciousIP(String ip) {
        if (!isValidIPAddress(ip)) {
            throw new InvalidIPAddressException(ip);
        }

        return ipAddressRepository.findByIp(ip);
    }
}
