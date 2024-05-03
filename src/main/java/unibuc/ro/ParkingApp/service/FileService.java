package unibuc.ro.ParkingApp.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {
    private final Path root = Paths.get("profilePictures");

    public byte[] extractFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public byte[] loadProfilePicture(String tokenSubClaim) {
        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, tokenSubClaim + "*")) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file) && Files.isReadable(file)) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        Files.copy(file, outputStream);
                        return outputStream.toByteArray();
                    }
                }
            }
            throw new FileNotFoundException("Could not find or read the file with tokenSubClaim: " + tokenSubClaim);
        } catch (IOException e) {
            throw new RuntimeException("Error loading file: " + e.getMessage(), e);
        }
    }
}
