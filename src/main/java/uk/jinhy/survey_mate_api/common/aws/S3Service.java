package uk.jinhy.survey_mate_api.common.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.survey_mate_api.common.config.S3Config;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

import java.io.IOException;

@Service
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private S3Config config;

    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(config.getBucket(), keyName, file.getInputStream(), metadata));
        } catch (IOException e){
            throw new GeneralException(Status.INTERNAL_SERVER_ERROR);
        }

        return amazonS3.getUrl(config.getBucket(), keyName).toString();
    }

    public String generateDataFileKeyName(String uuid) {
        return config.getDataFilePath() + '/' + uuid;
    }
}

