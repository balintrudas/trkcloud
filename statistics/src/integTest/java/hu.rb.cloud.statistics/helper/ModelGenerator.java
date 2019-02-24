package hu.rb.cloud.statistics.helper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.statistics.model.TrackStats;
import hu.rb.cloud.statistics.model.dto.Track;
import hu.rb.cloud.statistics.model.dto.TrackStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class ModelGenerator {

    public static TrackStats generateTrackStats(Long id) {
        TrackStats trackStats = new TrackStats();
        trackStats.setId(id);
        trackStats.setTrackId(UUID.randomUUID().toString());
        trackStats.setCreated(new Date());
        trackStats.setModified(new Date());
        trackStats.setDistance(100D);
        trackStats.setAverageSpeed(1.0D);
        trackStats.setDuration(600000L);
        return trackStats;
    }

    public static Track generateTrack(Long id) {
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> lineCoordinates = new ArrayList<>();
        for(int i = 0 ; i< 100 ; i++){
            lineCoordinates.add(new Coordinate(i,i,0));
        }
        MultiPoint multiPoint = gf.createMultiPoint(
                lineCoordinates.toArray(new Coordinate[lineCoordinates.size()]));

        Track track = new Track();
        track.setId(id);
        track.setModified(new Date());
        track.setCreated(Date.from(LocalDateTime.now()
                .minus(3,ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        track.setAccountId(1l);
        track.setVehicleId(1l);
        track.setTrackId(UUID.randomUUID().toString());
        track.setTrackStatus(TrackStatus.FINISHED);
        track.setMultiPoint(multiPoint);
        track.setEndDate(new Date());
        return track;
    }
}
