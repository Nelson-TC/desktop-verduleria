package org.example.utils.formatter;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoubleFormatterFactory {
    public static DefaultFormatterFactory createDoubleFormatterFactory() {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setMinimumIntegerDigits(0);

        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text == null || text.isEmpty()) {
                    return 0.0;
                }
                try {
                    double value = Double.parseDouble(text);
                    if (value < 0.0) {
                        return 0.0;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        };

        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false);

        return new DefaultFormatterFactory(formatter);
    }
}
