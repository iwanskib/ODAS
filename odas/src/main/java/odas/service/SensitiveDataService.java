package odas.service;

import odas.dto.SensitiveDataDTO;
import odas.model.User;
import odas.model.UserSensitiveData;
import odas.repository.UserRepository;
import odas.repository.UserSensitiveDataRepository;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensitiveDataService {

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSensitiveDataRepository userSensitiveDataRepository;


    public SensitiveDataDTO getSensitiveData(String username, String encryptionKey) {
        try {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            UserSensitiveData sensitiveData = userSensitiveDataRepository.findByUser(user);
            if (sensitiveData != null) {
                SensitiveDataDTO sensitiveDataDTO = new SensitiveDataDTO();
                sensitiveDataDTO.setCardNumber(encryptionService.decrypt(sensitiveData.getEncryptedCardNumber(), encryptionKey));
                sensitiveDataDTO.setIdCardNumber(encryptionService.decrypt(sensitiveData.getEncryptedIdCardNumber(), encryptionKey));
                return sensitiveDataDTO;
            }
        }
    } catch (EncryptionOperationNotPossibleException e) {
        return null;
    }
        return null;
    }
}