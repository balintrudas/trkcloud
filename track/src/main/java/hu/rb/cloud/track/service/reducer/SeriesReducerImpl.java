package hu.rb.cloud.track.service.reducer;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

@Service
public class SeriesReducerImpl implements SeriesReducer {

    @Override
    public List<Coordinate> reduce(List<Coordinate> points, double epsilon) {
        if (epsilon < 0) {
            throw new IllegalArgumentException("Epsilon cannot be less then 0.");
        }
        double furthestPointDistance = 0.0;
        int furthestPointIndex = 0;
        Line line = new Line(points.get(0), points.get(points.size() - 1));
        for (int i = 1; i < points.size() - 1; i++) {
            double distance = line.distance(points.get(i));
            if (distance > furthestPointDistance ) {
                furthestPointDistance = distance;
                furthestPointIndex = i;
            }
        }
        if (furthestPointDistance > epsilon) {
            List<Coordinate> reduced1 = reduce(points.subList(0, furthestPointIndex+1), epsilon);
            List<Coordinate> reduced2 = reduce(points.subList(furthestPointIndex, points.size()), epsilon);
            List<Coordinate> result = new ArrayList<>(reduced1);
            result.addAll(reduced2.subList(1, reduced2.size()));
            return result;
        } else {
            return line.asList();
        }
    }

}
