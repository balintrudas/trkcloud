package hu.rb.cloud.track.service.reducer;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.List;

public interface SeriesReducer {

    /**
     * Csökkenti a pontok számát egy sorozatban a Ramer_Douglas_Peucker algoritmussal.
     *
     * @param points
     *          Pontok rendezett listája (objects implementing the {@link Point} interface)
     * @param epsilon
     *          allowed margin of the resulting curve, has to be > 0
     */
    List<Coordinate> reduce(List<Coordinate> points, double epsilon);
}
