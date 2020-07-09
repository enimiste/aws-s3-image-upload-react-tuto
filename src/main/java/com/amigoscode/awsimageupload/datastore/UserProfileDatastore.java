package com.amigoscode.awsimageupload.datastore;

import com.amigoscode.awsimageupload.profile.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileDatastore {
    List<UserProfile> getUserProfiles();

    Optional<UserProfile> findUserProfileById(UUID id);

    void flush();
}
