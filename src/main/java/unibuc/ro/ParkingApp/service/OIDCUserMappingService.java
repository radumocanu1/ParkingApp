package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.exception.OIDCUserNotFound;
import unibuc.ro.ParkingApp.model.user.OIDCUserMapping;
import unibuc.ro.ParkingApp.model.user.User;
import unibuc.ro.ParkingApp.repository.OIDCUserMappingRepository;

import java.util.Optional;

@Log
@Service
@AllArgsConstructor
public class OIDCUserMappingService {
    OIDCUserMappingRepository oidcUserMappingRepository;

    public void delete(OIDCUserMapping oidcUserMapping) {
        oidcUserMappingRepository.delete(oidcUserMapping);
    }
    public OIDCUserMapping create(String subClaim, User user) {
        return oidcUserMappingRepository.save(new OIDCUserMapping(subClaim, user));
    }
    public OIDCUserMapping findBySubClaim(String subClaim) {
        Optional<OIDCUserMapping> oidcUserMapping = oidcUserMappingRepository.findById(subClaim);
        if (oidcUserMapping.isEmpty())
        {
            log.warning("Cannot map OIDC user with sub claim " + subClaim + " against any existing user. Consider creating the mapping and try again.");
            throw new OIDCUserNotFound(subClaim);
        }
        return oidcUserMapping.get();
    }




}
