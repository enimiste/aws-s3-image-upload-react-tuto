package com.amigoscode.awsimageupload.datastore;

import com.amigoscode.awsimageupload.filestore.AwsS3File;
import com.amigoscode.awsimageupload.profile.UserProfile;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;
import static java.util.function.Predicate.not;

@Repository
public class FakeUserProfileDatastore implements UserProfileDatastore {
    private final List<UserProfile> userProfiles = new ArrayList<>();
    private static Path path;

    static {
        path = Path.of(URI.create("file:///db.csv"));
    }

    @PostConstruct
    @SneakyThrows
    protected void loadData() {
        if (Files.exists(path)) {
            List<String> lines = Files.readAllLines(path);
            userProfiles.addAll(lines.stream()
                    .map(line -> {
                        String[] parts = line.split(";");
                        if (parts.length < 2)
                            throw new IllegalStateException("Invalid datastore in : " + path);
                        AwsS3File profileImage = null;
                        if (parts.length >= 4) {
                            Optional<String> pathOpt = ofNullable(parts[2])
                                    .map(String::trim)
                                    .filter(not(String::isBlank));
                            profileImage = pathOpt.flatMap(pth -> {
                                Optional<String> keyOpt = ofNullable(parts[3])
                                        .map(String::trim)
                                        .filter(not(String::isBlank));
                                return keyOpt.map(key -> Map.entry(pth, key));
                            })
                                    .map(e -> new AwsS3File(e.getKey(), e.getValue()))
                                    .orElse(null);
                        }
                        return new UserProfile(fromString(parts[0]),
                                parts[1].trim(),
                                profileImage);
                    }).collect(Collectors.toList()));
        } else {
            userProfiles.add(new UserProfile(fromString("25e13d3c-c746-4de3-b9f7-ce27d7778daa"), "e.nouni", null));
            userProfiles.add(new UserProfile(fromString("9897800e-5ec4-48e2-9a62-c324a3e55829"), "t.saissihassani", null));
            userProfiles.add(new UserProfile(fromString("908261c3-b9d0-4c6f-a841-f509c8ec8b28"), "y.nouni", null));
        }
    }

    @PreDestroy
    @SneakyThrows
    public void flush() {
        List<String> lines = userProfiles
                .stream()
                .map(up -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(up.getProfileId());
                    sb.append("; ");
                    sb.append(up.getUsername());
                    up.getProfileImage()
                            .ifPresent(pi -> {
                                sb.append("; ");
                                sb.append(pi.getPath());
                                sb.append("; ");
                                sb.append(pi.getKey());
                            });

                    return sb.toString();
                }).collect(Collectors.toList());
        Files.write(path, lines, TRUNCATE_EXISTING, CREATE);
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        return Collections.unmodifiableList(userProfiles);
    }

    @Override
    public Optional<UserProfile> findUserProfileById(UUID id) {
        return userProfiles.stream()
                .filter(u -> u.getProfileId().equals(id))
                .findFirst();
    }
}
