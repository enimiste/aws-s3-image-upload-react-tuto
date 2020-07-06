package com.amigoscode.awsimageupload.datastore;

import com.amigoscode.awsimageupload.profile.UserProfile;

import java.util.List;

public interface UserProfileDatastore {
    List<UserProfile> getUserProfiles();
}
