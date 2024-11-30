package com.module5.zingMp3Clone.Service.GoogleDrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveService {
    private final String FOLDER_ID = "13e7iNQVQZC7WPfQNlDPieuiHpsH0hsvA";

    public String uploadFile(MultipartFile multipartFile) throws GeneralSecurityException, IOException {
        String mimeType = multipartFile.getContentType();
        if (mimeType == null || !mimeType.equals("audio/mpeg")) {
            throw new RuntimeException("File must be an MP3 audio file.");
        }
        // Get Drive service
        Drive driveService = getDriveService();

        // File metadata
        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());
        fileMetadata.setMimeType(multipartFile.getContentType());
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        // File temp
        java.io.File fileTemp = convertMultiPartToFile(multipartFile);

        FileContent fileContent = new FileContent(mimeType, fileTemp);

        // Upload the file
        File uploadedFile = driveService.files().create(fileMetadata, fileContent)
                .setFields("id, name")
                .execute();

        fileTemp.delete();
        return uploadedFile.getId();
    }

    public void deleteFile(String fileId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        driveService.files().delete(fileId).execute();
    }

    private Drive getDriveService() throws IOException, GeneralSecurityException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/googleAuth.json");
        // Load service account key
        assert inputStream != null;
        ServiceAccountCredentials credentials = (ServiceAccountCredentials) ServiceAccountCredentials
                .fromStream(inputStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Build the Drive service
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("My Application").build();
    }

    private java.io.File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        java.io.File tempFile = java.io.File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        return tempFile;
    }
}
