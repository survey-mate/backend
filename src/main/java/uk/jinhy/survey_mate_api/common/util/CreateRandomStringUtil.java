package uk.jinhy.survey_mate_api.common.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CreateRandomStringUtil {

    public static String createRandomStr() {
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(10, useLetters, useNumbers);

        return randomStr;
    }

}
