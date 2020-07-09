package com.amigoscode.awsimageupload.profile;

import com.amigoscode.awsimageupload.datastore.UserProfileDatastore;
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

    public void updateProfileImageLink(UUID userProfileId, String path) {
        datastore.findUserProfileById(userProfileId)
                .ifPresent(u -> u.setProfileImageLink(path));
        datastore.flush();
    }
}
