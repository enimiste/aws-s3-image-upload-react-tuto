package com.amigoscode.awsimageupload.datastore;

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

import static java.nio.file.StandardOpenOption.*;
import static java.util.UUID.fromString;

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
                        return new UserProfile(UUID.fromString(parts[0]),
                                parts[1],
                                Optional.ofNullable(parts[2])
                                        .map(String::trim)
                                        .filter(String::isBlank)
                                        .orElse(null));
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
                    sb.append("; ");
                    sb.append(up.getProfileImageLink().orElse(""));
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
