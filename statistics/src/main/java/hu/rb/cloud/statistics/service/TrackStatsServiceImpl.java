package hu.rb.cloud.statistics.service;

import com.vividsolutions.jts.geom.Coordinate;
import hu.rb.cloud.statistics.message.TrackStatsMessageSource;
import hu.rb.cloud.statistics.model.TrackStats;
import hu.rb.cloud.statistics.model.dto.Track;
import hu.rb.cloud.statistics.repository.TrackStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TrackStatsServiceImpl implements TrackStatsService {

    @Autowired
    private TrackStatsRepository trackStatsRepository;

    @Autowired
    private TrackStatsMessageSource trackStatsMessageSource;

    @Override
    public void calcStats(Track track) {
        Assert.notNull(track, "Message cannot be null");
        Assert.notNull(track.getTrackId(), "TrackId field is required");
        TrackStats trackStats = trackStatsRepository.findByTrackId(track.getTrackId());
        if(trackStats==null){
            trackStats = new TrackStats();
            trackStats.setCreated(new Date());
            trackStats.setTrackId(track.getTrackId());
        }
        trackStats.setModified(new Date());
        trackStats.setDistance(calcDistance(track));
        trackStats.setDuration(calcDuration(track));
        trackStats.setAverageSpeed(calcSpeed(trackStats));
        trackStats = trackStatsRepository.save(trackStats);
        trackStatsMessageSource.trackStatsChannel().send(MessageBuilder.withPayload(trackStats).build());
    }

    @Override
    public Double calcDistance(Track track) {
        if(track.getMultiPoint()!=null) {
            Double pathDistance = 0d, lat1, lat2, lon1, lon2, el1, el2;
            List<Coordinate> coordinates = Arrays.asList(track.getMultiPoint().getCoordinates());
            for (int i = 0; i + 1 < coordinates.size(); i++) {
                lat1 = coordinates.get(i).y;
                lat2 = coordinates.get(i + 1).y;
                lon1 = coordinates.get(i).x;
                lon2 = coordinates.get(i + 1).x;
                el1 = coordinates.get(i).z;
                el2 = coordinates.get(i + 1).z;
                pathDistance += calcDistance(lat1, lat2, lon1, lon2, el1, el2);
            }
            return pathDistance;
        }
        return null;
    }

    @Override
    public Double calcDistance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        double height = el1 - el2;
        double result = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(result);
    }

    @Override
    public Long calcDuration(Track track) {
        if (track.getCreated() != null && (track.getModified() != null || track.getEndDate() != null)) {
            return  (track.getEndDate() != null ? track.getEndDate().getTime() :
                    track.getModified().getTime()) - track.getCreated().getTime();
        }
        return null;
    }

    @Override
    public Double calcSpeed(TrackStats trackStats) {
        if(trackStats.getDistance()!=null && trackStats.getDuration()!=null) {
            return trackStats.getDistance() / (trackStats.getDuration() / 1000) * 3.6;
        }
        return null;
    }


}
