package com.kyleduo.androidapknaming.plugin

import java.text.SimpleDateFormat

class DateProcessor {

    String process(String template) {
        if (template == null) {
            return null
        }

        def pattern = ~'\\$date\\([^\\(\\)]+\\)'
        def matcher = pattern.matcher(template)
        def rangeItems = new ArrayList<DateRange>()
        while (matcher.find()) {
            def match = matcher.group()
            def format = match.substring(6, match.length() - 1)
            rangeItems.add(new DateRange(matcher.start(), matcher.end(), format))
        }

        if (rangeItems.isEmpty()) {
            return template
        }

        Date now = new Date()
        SimpleDateFormat formatter = new SimpleDateFormat()

        def builder = new StringBuilder()
        int lastIndex = 0
        int rangeItemIndex = 0
        while (lastIndex < template.length()) {
            if (rangeItemIndex == rangeItems.size()) {
                builder.append(template.substring(lastIndex, template.length()))
                break
            }
            DateRange dateRange = rangeItems[rangeItemIndex]
            if (dateRange.start > lastIndex) {
                builder.append(template.substring(lastIndex, dateRange.start))
            }
            formatter.applyPattern(dateRange.pattern)
            String replace = formatter.format(now)
            builder.append(replace)
            lastIndex = dateRange.end
            rangeItemIndex += 1
        }

        return builder.toString()
    }

    class DateRange {
        int start
        int end
        String pattern

        DateRange(int start, int end, String pattern) {
            this.start = start
            this.end = end
            this.pattern = pattern
        }
    }
}
