package unibuc.ro.ParkingApp.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileService {
    private final Path root = Paths.get("profilePictures");

    public void save(MultipartFile file, String tokenSubClaim) {
        try {
            String originalExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            try (Stream<Path> files = Files.walk(root)) {
                files.forEach(filePath -> {
                    String currentFileName = filePath.getFileName().toString();
                    String currentExtension = StringUtils.getFilenameExtension(currentFileName);
                    String currentFileToken = StringUtils.stripFilenameExtension(currentFileName);
                    if (currentFileToken.equals(tokenSubClaim)) {
                        assert originalExtension != null;
                        if (!originalExtension.equals(currentExtension)) {
                            try {
                                Files.delete(filePath);
                            } catch (IOException e) {
                                throw new RuntimeException("Error deleting existing file: " + e.getMessage(), e);
                            }
                        }
                    }
                });
            }
            String fileNameWithExtension = tokenSubClaim + "." + originalExtension;
            Files.copy(file.getInputStream(), this.root.resolve(fileNameWithExtension), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }
    public Resource load(String tokenSubClaim) {
        try {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, tokenSubClaim + "*")) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file) && Files.isReadable(file)) {
                        return new UrlResource(file.toUri());
                    }
                }
            }
            throw new FileNotFoundException("Could not find or read the file with tokenSubClaim: " + tokenSubClaim);
        } catch (IOException e) {
            throw new RuntimeException("Error loading file: " + e.getMessage(), e);
        }
    }
}
