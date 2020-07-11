package com.amigoscode.awsimageupload.filestore;

import lombok.Data;
import lombok.Value;

@Value
public class AwsS3File {
    String path;
    String key;
}
