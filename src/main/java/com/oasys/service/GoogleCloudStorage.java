package com.oasys.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class GoogleCloudStorage {

    private Storage storage;
    private final String CREDENTIALS_FILE = "C:\\Users\\bensc\\cred\\CompSci316-1cc4a519054f.json";
    private final String BUCKET_NAME = "oasys-images";

    public GoogleCloudStorage() {
        try {
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE));
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            storage = StorageOptions.getDefaultInstance().getService();
        }
    }
    /**
     * Uploads a file to Google Cloud Storage to the bucket specified in the BUCKET_NAME
     * environment variable, appending a timestamp to end of the uploaded filename.
     */
    @SuppressWarnings("deprecation")
    public String uploadFile(MultipartFile filePart) throws IOException {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        String dtString = dt.toString(dtf);
        String originalName = filePart.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        final String fileName = filePart.getName() + dtString + "." + extension;

        // the inputstream is closed by default, so we don't need to close it here
        BlobInfo blobInfo =
                storage.create(
                        BlobInfo
                                .newBuilder(BUCKET_NAME, fileName)
                                // Modify access list to allow all users with link to read file
                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
                                .build(),
                        filePart.getInputStream());
        // return the public download link
        return blobInfo.getMediaLink();
    }
}
