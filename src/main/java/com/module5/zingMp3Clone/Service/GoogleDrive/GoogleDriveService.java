package com.module5.zingMp3Clone.Service.GoogleDrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleDriveService {
    @Value("${DRIVE_FOLDER}")
    private String FOLDER_ID;

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
        // Load service account key
        ServiceAccountCredentials credentials = (ServiceAccountCredentials) ServiceAccountCredentials
                .fromStream(new ClassPathResource("GoogleDrive/auth.json").getInputStream())
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Build the Drive service
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("My application").build();
    }

    private java.io.File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        java.io.File tempFile = java.io.File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    public void streamFileToResponse(String fileId, HttpServletResponse response, HttpServletRequest request)
            throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        // Tạo URL để tải file
        String fileDownloadUrl = String.format("https://www.googleapis.com/drive/v3/files/%s?alt=media", fileId);
        // Gửi yêu cầu tải file
        HttpResponse googleResponse = driveService.getRequestFactory()
                .buildGetRequest(new GenericUrl(fileDownloadUrl))
                .execute();

        long fileLength = googleResponse.getHeaders().getContentLength();

        // Xử lý header Range từ client
        String range = request.getHeader("Range");
        long start = 0;
        long end = fileLength - 1;

        if (range != null) {
            // Ví dụ: "bytes=500-1000"
            String[] ranges = range.replace("bytes=", "").split("-");
            start = Long.parseLong(ranges[0]);
            if (ranges.length > 1) {
                end = Long.parseLong(ranges[1]);
            }
        }

        // Tính toán độ dài phần dữ liệu sẽ gửi
        long contentLength = end - start + 1;

        // Thiết lập header cho response
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // HTTP 206
        response.setContentType("audio/mpeg"); // Định dạng file, ví dụ MP3
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, fileLength));
        response.setHeader("Content-Length", String.valueOf(contentLength));

        // Gửi dữ liệu từ Google Drive đến client
        try (InputStream in = googleResponse.getContent();
             OutputStream out = response.getOutputStream()) {

            // Bỏ qua phần đầu file nếu cần
            in.skip(start);

            byte[] buffer = new byte[8192];
            long bytesRead = 0;

            while (bytesRead < contentLength) {
                int bytesToRead = (int) Math.min(buffer.length, contentLength - bytesRead);
                int len = in.read(buffer, 0, bytesToRead);
                if (len == -1) break;

                out.write(buffer, 0, len);
                bytesRead += len;
            }
            //incrementNumOfListen(fileId);
            out.flush();
        }
    }

//    private void incrementNumOfListen(String fileId) {
//        SongEntity song = songRepository.findByUrl(fileId);
//        song.setNumsOfListen(song.getNumsOfListen() + 1);
//        songRepository.save(song);
//    }
}
