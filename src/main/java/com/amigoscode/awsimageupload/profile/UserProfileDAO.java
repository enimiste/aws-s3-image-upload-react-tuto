package com.amigoscode.awsimageupload.profile;

import com.amigoscode.awsimageupload.datastore.UserProfileDatastore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserProfileDAO {
    private final UserProfileDatastore datastore;

    public List<UserProfile> getUserProfiles() {
        return datastore.getUserProfiles();
    }
}
