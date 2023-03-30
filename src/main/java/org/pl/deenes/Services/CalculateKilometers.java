package org.pl.deenes.Services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pl.deenes.Data.Line;
import org.pl.deenes.Data.RoadStats;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@Data
@Service
public class CalculateKilometers {

    private Kilometers kilometers;

    public RoadStats calculateKilometers() {
        LinkedList<Line> lineList = createLinesAndAddToLineList();

        calculateKilometersForEachLine(lineList);

        lineList.removeAll(Collections.singleton(null));
        RoadStats roadStats = new RoadStats(lineList);
        roadStats.setHowManyKilometers(lineList.stream().map(Line::getSize).filter(Objects::nonNull).reduce(Double::sum).orElse(0.0));
        return roadStats;
    }

    private LinkedList<Line> createLinesAndAddToLineList() {
        LinkedList<Line> lineList = new LinkedList<>();

        LinkedList<Double> doubleList = new LinkedList<>();

        for (Number number : kilometers.getResult()) {
            if (number instanceof Integer integer) {
                if (!doubleList.isEmpty()) {
                    lineList.add(new Line(integer, doubleList));
                    doubleList = new LinkedList<>();
                }
            } else {
                doubleList.add((Double) number);
            }
        }
        return lineList;
    }

    private static void calculateKilometersForEachLine(LinkedList<Line> lineList) {
        for (Line line : lineList) {
            List<Double> kilometers1 = line.getKilometers();
            if (kilometers1.size() < 2) {
                continue;
            }
            List<Double> doubles = Stream.concat(
                            Stream.of(kilometers1.get(0)),
                            Stream.of(kilometers1.get(kilometers1.size() - 1)))
                    .toList();
            if (doubles.get(0) > doubles.get(1)) {
                line.setSize(doubles.get(0) - doubles.get(1));
            } else {
                line.setSize(doubles.get(1) - doubles.get(0));
            }
        }
    }
}