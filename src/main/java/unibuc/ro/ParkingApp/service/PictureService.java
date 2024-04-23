package unibuc.ro.ParkingApp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unibuc.ro.ParkingApp.model.listing.Listing;
import unibuc.ro.ParkingApp.model.picture.Picture;
import unibuc.ro.ParkingApp.repository.PictureRepository;


@Service
@AllArgsConstructor
public class PictureService {
    private PictureRepository repository;
    public Picture addPicture(Picture picture, Listing listing){
        picture.setListing(listing);
        repository.save(picture);
        return picture;
    }
}
