package uk.jinhy.survey_mate_api.common.util;

import java.util.UUID;

public class Util {
    public static String generateRandomString() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
