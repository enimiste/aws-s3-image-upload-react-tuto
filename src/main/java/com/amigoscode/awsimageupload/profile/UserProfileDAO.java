package com.amigoscode.awsimageupload.profile;

import com.amigoscode.awsimageupload.datastore.UserProfileDatastore;
import com.amigoscode.awsimageupload.filestore.AwsS3File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserProfileDAO {
    private final UserProfileDatastore datastore;

    public List<UserProfile> getUserProfiles() {
        return datastore.getUserProfiles();
    }

    public Optional<UserProfile> getUserProfileById(UUID userProfileId) {
        return datastore.findUserProfileById(userProfileId);
    }

    public void updateProfileImageLink(UUID userProfileId, String path, String key) {
        datastore.findUserProfileById(userProfileId)
                .ifPresent(u -> u.setProfileImage(new AwsS3File(path, key)));
        datastore.flush();
    }
}
