package com.amigoscode.awsimageupload.profile;

import com.amigoscode.awsimageupload.filestore.AwsS3File;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private AwsS3File profileImage;//S3 key

    public Optional<AwsS3File> getProfileImage() {
        return Optional.ofNullable(profileImage);
    }

    public String getProfileImageHash(){
        return profileImage == null ? null : (profileImage.getPath().hashCode() + "X" + profileImage.getKey().hashCode());
    }
}
