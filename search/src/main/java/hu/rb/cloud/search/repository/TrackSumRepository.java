package hu.rb.cloud.search.repository;

import hu.rb.cloud.search.model.TrackSum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrackSumRepository extends ElasticsearchRepository<TrackSum, String> {
    TrackSum findByTrackId(String trackId);
}
