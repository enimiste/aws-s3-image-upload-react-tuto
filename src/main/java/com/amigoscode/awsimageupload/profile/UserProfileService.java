package com.amigoscode.awsimageupload.profile;

import com.amigoscode.awsimageupload.bucket.BucketName;
import com.amigoscode.awsimageupload.filestore.FileStore;
import com.amigoscode.awsimageupload.filestore.FileStore.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileDAO dao;
    private final FileStore fileStore;

    public List<UserProfile> getUserProfiles() {
        return dao.getUserProfiles();
    }

    @SneakyThrows(IOException.class)
    public void uploadUserProfileImage(UUID userProfileId, MultipartFile image) {
        Objects.requireNonNull(userProfileId, "userProfileId should'nt be null");
        Objects.requireNonNull(userProfileId, "Profile image should'nt be null");

        //1. check if the file not empty
        if (image.isEmpty())
            throw new IllegalArgumentException("Profile Image is empty [" + image.getSize() + "]");
        //2. check if the file is an image
        String contentType = image.getContentType();
        if (contentType == null
                || !Arrays.asList(MediaType.IMAGE_JPEG_VALUE,
                MediaType.IMAGE_GIF_VALUE,
                MediaType.IMAGE_PNG_VALUE).contains(contentType))
            throw new IllegalArgumentException("Profile Image should be a JPEG, PNG or a GIF file [" + contentType + "]");
        //3. check if the user profile exists
        UserProfile userProfile = dao.getUserProfileById(userProfileId).orElseThrow(() -> new IllegalArgumentException("User profile not found"));

        /*
        4. Grab metadata from the file if they exists
         */
        FileMetadata meta = FileMetadata.empty()
                .meta("Content-Type", image.getContentType())
                .meta("Content-Length", image.getSize());

        //5. store the image on S3 bucket and update the userProfileImageLink with S3 link
        String path = fileStore.save(BucketName.PROFILE_IMAGE,
                userProfileId.toString(),
                String.format("%s-%s", image.getOriginalFilename(), UUID.randomUUID()),
                meta, image.getInputStream());

        dao.updateProfileImageLink(userProfileId, path);
    }
}
