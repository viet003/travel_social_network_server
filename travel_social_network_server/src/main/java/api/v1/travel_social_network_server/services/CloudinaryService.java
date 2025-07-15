package api.v1.travel_social_network_server.services;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String upload(byte[] fileData, String folderName) {
        try {
            System.out.println("Uploading file...");
            Map<String, Object> data = cloudinary.uploader().upload(fileData, Map.of("folder", folderName));
            String url = (String) data.get("secure_url");
            System.out.println("Upload successful. URL: " + url);
            return url;
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException("Image upload failed: " + io.getMessage());
        }
    }
}
