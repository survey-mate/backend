package uk.jinhy.survey_mate_api.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import uk.jinhy.survey_mate_api.common.response.Status;
import uk.jinhy.survey_mate_api.common.response.exception.GeneralException;

public class CreateCodeUtil {

    public static String createCode() {
        int length = 6;
        Random random = new Random();

        String code = random.ints(length, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());

        return code;
    }
}
