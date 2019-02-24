package hu.rb.cloud.search.service;

import hu.rb.cloud.search.model.dto.Track;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.TrackStats;
import org.springframework.data.domain.Page;

public interface TrackSumService {
    TrackSum save(TrackSum trackSum);
    TrackSum map(Track track);
    TrackSum map(TrackStats trackStats);
    TrackSum findByTrackId(String trackId);
    void deleteAll();
    Long countAll();
    Page search(hu.rb.cloud.search.model.dto.Page page);
}
