package org.example.utils.formatter;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoubleFormatterFactory {
    public static DefaultFormatterFactory createDoubleFormatterFactory() {
        NumberFormat format = NumberFormat.getNumberInstance();
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text == null || text.trim().isEmpty()) {
                    return null; // Tratar campos vacíos como valores nulos
                }
                // Intentar convertir el texto en un valor Double
                try {
                    return super.stringToValue(text);
                } catch (ParseException e) {
                    throw new ParseException("Formato inválido", e.getErrorOffset());
                }
            }
        };

        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        return new DefaultFormatterFactory(formatter);
    }
}

