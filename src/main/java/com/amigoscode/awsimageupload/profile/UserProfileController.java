package com.amigoscode.awsimageupload.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService service;

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return service.getUserProfiles();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadUserProfileImage(
            @PathVariable("userProfileId") UUID userProfileId,
            @RequestParam("userProfileImage") MultipartFile image
    ) {
        String hash = service.uploadUserProfileImage(userProfileId, image);
        return ResponseEntity.ok(hash);
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(
            @PathVariable("userProfileId") UUID userProfileId
    ){
        return service.downloadProfileImage(userProfileId);
    }
}
