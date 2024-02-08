package uk.jinhy.survey_mate_api.common.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.survey_mate_api.common.config.S3Config;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

@Service
@AllArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final S3Config config;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(
                new PutObjectRequest(config.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new GeneralException(Status.INTERNAL_SERVER_ERROR);
        }

        return amazonS3.getUrl(config.getBucket(), keyName).toString();
    }

    public String generateDataFileKeyName(String uuid, String extension) {
        return config.getDataFilePath() + '/' + uuid + '.' + extension;
    }
}

