package unibuc.ro.ParkingApp.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {
    private static final String ROOT_DIRECTORY = "listingPicture";

    private final Path root = Paths.get(ROOT_DIRECTORY);

    public byte[] extractFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // todo handle this with ResponseEntityExceptionHandler
    @SneakyThrows
    public String saveFile(UUID listingUUID, MultipartFile file) {
        Path directoryPath = Paths.get(ROOT_DIRECTORY, listingUUID.toString());
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + fileExtension;
        Path filePath = directoryPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
    // todo same as above

    @SneakyThrows
    public byte[] loadPicture(String picturePath)  {
        Path filePath = Paths.get(picturePath);
        return Files.readAllBytes(filePath);
    }
}
