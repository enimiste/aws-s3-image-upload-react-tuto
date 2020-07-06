package com.amigoscode.awsimageupload.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileDAO dao;

    public List<UserProfile> getUserProfiles() {
        return dao.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile image) {
        /*
        1. check if the file not empty
        2. check if the file is an image
        3. check if the user profile exists
        4. Grab metadata from the file if they exists
        5. store the image on S3 bucket and update the userProfileImageLink with S3 link
         */
    }
}
