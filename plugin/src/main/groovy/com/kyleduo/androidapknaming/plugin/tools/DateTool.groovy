package com.kyleduo.androidapknaming.plugin.tools

import java.text.SimpleDateFormat

@SuppressWarnings('unused')
class DateTool {

    private Date time = new Date()
    String timestamp = timestamp()
    String timestampInSeconds = timestampInSeconds()

    String format(String template) {
        return new SimpleDateFormat(template).format(time)
    }

    String timestamp() {
        return String.valueOf(time.time)
    }

    String timestampInSeconds() {
        return String.valueOf((int) (time.time / 1000L))
    }

    @Override
    String toString() {
        return format("yyyy-MM-dd")
    }
}
