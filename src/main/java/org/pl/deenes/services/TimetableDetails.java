package org.pl.deenes.services;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.pl.deenes.services.interfaces.KilometersService;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Data
@Service
@Slf4j
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TimetableDetails implements KilometersService {
    @Builder.Default
    private List<List<String>> allKilometers = new ArrayList<>();
    @Builder.Default
    private Set<Integer> kilometersAfterConvert = new HashSet<>();
    @Builder.Default
    private List<Number> lineNumbers = new LinkedList<>();
    @Builder.Default
    private List<String> stations = new ArrayList<>();

    public static boolean isNumeric(@NonNull String numberToCheck) {
        try {
            Double.parseDouble(numberToCheck);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @Override
    public void giveAllKilometers() {
        for (List<String> kilometer : allKilometers) {
            for (String s : kilometer) {
                if (s == null) {
                    continue;
                }
                String replace = s.replace(",", ".");
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                try {
                    lineNumberOrKilometer(replace, format);
                } catch (ParseException e) {
                    log.error("Error while parsing kilometers: {}", replace, e);
                }
            }
        }
    }
    private void lineNumberOrKilometer(String replace, NumberFormat format) throws ParseException {
        Number number;
        if (isNumeric(replace)) {
            number = format.parse(replace);
            if (replace.contains(".")) {
                double d = number.doubleValue();
                lineNumbers.add(d);
            } else {
                int i = number.intValue();
                lineNumbers.add(i);
            }
        }
    }

    @Override
    public void getAllRailwayLines() {
        for (List<String> kilometer : allKilometers) {
            List<String> collect = kilometer.stream().filter(TimetableDetails::isInteger).toList();
            collect.forEach(a -> {
                try {
                    kilometersAfterConvert.add(Integer.valueOf(a));
                } catch (NumberFormatException e) {
                    log.error("Error while parsing kilometers: {}", a, e);
                }
            });
        }
    }

}
