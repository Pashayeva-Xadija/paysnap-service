package az.devlab.paysnapservice.util;

import java.time.LocalDateTime;

public class TimeUtils {

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static boolean isExpired(LocalDateTime time) {
        return time != null && time.isBefore(LocalDateTime.now());
    }

    public static LocalDateTime minutesFromNow(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }
}
