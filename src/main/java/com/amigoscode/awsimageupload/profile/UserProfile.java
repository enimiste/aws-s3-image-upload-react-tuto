package com.amigoscode.awsimageupload.profile;

import lombok.*;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfile {
    private UUID profileId;
    private String username;
    private String profileImageLink;//S3 key

    public Optional<String> getProfileImageLink() {
        return Optional.ofNullable(profileImageLink);
    }
}
