package com.amigoscode.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileStore {
    private final AmazonS3 s3;

    /**
     * @param path         based on the bucket name
     * @param fileName
     * @param userMetadata optional
     * @param content
     */
    public void save(String path,
                     String fileName,
                     FileMetadata userMetadata,
                     InputStream content) {
        ObjectMetadata metadata = Optional.ofNullable(userMetadata).orElse(FileMetadata.empty()).asObjectMetadata();
        try {
            s3.putObject(path, fileName, content, metadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload file \"" + fileName + "\" to S3", e);
        }
    }

    public interface FileMetadata extends Map<String, String> {
        /**
         * @return
         */
        static FileMetadata empty() {
            return new FileMetadataImpl();
        }

        /**
         * @return
         */
        default ObjectMetadata asObjectMetadata() {
            ObjectMetadata metadata = new ObjectMetadata();
            forEach(metadata::addUserMetadata);
            return metadata;
        }
    }

    protected static class FileMetadataImpl extends HashMap<String, String> implements FileMetadata {

    }
}
