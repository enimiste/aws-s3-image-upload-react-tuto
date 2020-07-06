package com.amigoscode.awsimageupload.datastore;

import com.amigoscode.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDatastore implements UserProfileDatastore {
    private final static List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "e.nouni", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "t.saissihassani", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "y.nouni", null));
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return Collections.unmodifiableList(USER_PROFILES);
    }
}
