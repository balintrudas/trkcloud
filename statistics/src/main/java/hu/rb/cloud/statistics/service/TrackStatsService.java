package hu.rb.cloud.statistics.service;

import hu.rb.cloud.statistics.model.TrackStats;
import hu.rb.cloud.statistics.model.dto.Track;

public interface TrackStatsService {
    void calcStats(Track track);

    Double calcDistance(double lat1, double lat2, double lon1, double lon2, double el1, double el2);

    Double calcDistance(Track track);

    Long calcDuration(Track track);

    Double calcSpeed(TrackStats trackStats);
}
