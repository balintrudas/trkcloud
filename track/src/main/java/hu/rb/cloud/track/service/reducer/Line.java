package hu.rb.cloud.track.service.reducer;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.Arrays;
import java.util.List;

public class Line {

    private Coordinate start;
    private Coordinate end;

    private double dx;
    private double dy;
    private double sxey;
    private double exsy;
    private  double length;

    public Line(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        dx = start.x - end.x;
        dy = start.y - end.y;
        sxey = start.x * end.y;
        exsy = end.x * start.y;
        length = Math.sqrt(dx*dx + dy*dy);
    }

    @SuppressWarnings("unchecked")
    public List<Coordinate> asList() {
        return Arrays.asList(start, end);
    }

    double distance(Coordinate p) {
        return Math.abs(dy * p.x - dx * p.y + sxey - exsy) / length;
    }
}

