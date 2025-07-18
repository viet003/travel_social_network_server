package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.entities.PostMedia;
import api.v1.travel_social_network_server.utilities.MediaTypeEnum;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(byte[] fileData, String folderName, String resourceType) {
        try {
            Map uploadOptions = ObjectUtils.asMap(
                    "folder", folderName,
                    "resource_type", resourceType  // "image", "video", "auto"
            );

            Map uploadResult = cloudinary.uploader().upload(fileData, uploadOptions);

            return uploadResult.get("url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload lên Cloudinary: " + e.getMessage());
        }
    }


    public List<PostMedia> uploadMultipleFiles(List<MultipartFile> files, String folderName) {
        List<PostMedia> postMedias = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                byte[] fileData = file.getBytes();
                MediaTypeEnum type = detectMediaType(file);

                // switch expression
                String resourceType = switch (type) {
                    case IMAGE -> "image";
                    case VIDEO -> "video";
                    default -> "auto";
                };

                String url = uploadFile(fileData, folderName, resourceType);

                PostMedia postMedia = PostMedia.builder()
                        .url(url)
                        .type(type)
                        .build();

                postMedias.add(postMedia);

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload one of the files: " + e.getMessage());
            }
        }

        return postMedias;
    }


    private MediaTypeEnum detectMediaType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return MediaTypeEnum.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return MediaTypeEnum.VIDEO;
            }
        }

        return MediaTypeEnum.IMAGE;
    }

}
