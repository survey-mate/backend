package uk.jinhy.survey_mate_api.common.util;

import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;

public class Util {

    public static String generateRandomString(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    public static String generateRandomNumberString(int length) {
        Random random = new Random();

        return random.ints(length, 0, 10)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining());
    }
}
