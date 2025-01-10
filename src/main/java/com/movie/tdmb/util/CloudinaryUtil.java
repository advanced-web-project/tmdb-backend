package com.movie.tdmb.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class CloudinaryUtil {
    private final Cloudinary cloudinary;
    @NonFinal
    @Value("${cloudinary-url}")
    public String CLOUDINARY_URL;
    public CloudinaryUtil() {
        // Automatically configured via CLOUDINARY_URL
        this.cloudinary = new Cloudinary(CLOUDINARY_URL);  ;
    }
    private final Logger logger = LoggerFactory.getLogger(CloudinaryUtil.class);
    public String uploadImageToCloudinary(String imageUrl) {
        try {
            // Upload the image using its URL
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());
            // Return the Cloudinary-hosted URL
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
        }
    }
}
