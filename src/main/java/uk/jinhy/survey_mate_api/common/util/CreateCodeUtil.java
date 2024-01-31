package uk.jinhy.survey_mate_api.common.util;


import java.util.Random;
import java.util.stream.Collectors;


public class CreateCodeUtil {

    public static String createCode(int length) {
        Random random = new Random();

        String code = random.ints(length, 0, 10)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining());

        return code;
    }

}
