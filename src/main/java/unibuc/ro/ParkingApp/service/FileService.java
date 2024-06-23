package unibuc.ro.ParkingApp.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import unibuc.ro.ParkingApp.exception.FileNotDeleted;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {
    private static final String LISTING_PICTURES_DIR = "listingPictures";
    private static final String PROFILE_PICTURES_DIR = "profilePictures";


    private final Path root = Paths.get(LISTING_PICTURES_DIR);


    // todo handle this with ResponseEntityExceptionHandler
    @SneakyThrows
    public String saveFile(String listingUUID, MultipartFile file) {
        Path directoryPath = Paths.get(LISTING_PICTURES_DIR, listingUUID);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + fileExtension;
        Path filePath = directoryPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }
    @SneakyThrows
    public String saveProfilePicture(UUID userUUID, MultipartFile file) {
        Path directoryPath = Paths.get(PROFILE_PICTURES_DIR);
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = userUUID + fileExtension;
        Path filePath = directoryPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
    // todo same as above

    public byte[] loadPicture(String picturePath) {
        try {
            Path filePath = Paths.get(picturePath);
            return Files.readAllBytes(filePath);
        } catch (Exception e) {
            //todo
            System.out.println("Poza nu a fost gasita");
        }
        return null;
    }
    public void deleteFile(String picturePath)  {
        Path filePath = Paths.get(picturePath);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileNotDeleted("Failed to delete picture");
        }

    }
    public void deleteDirectory(UUID listingUUID) {
        Path directoryPath = Paths.get(LISTING_PICTURES_DIR, listingUUID.toString());
        try {
            FileSystemUtils.deleteRecursively(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
