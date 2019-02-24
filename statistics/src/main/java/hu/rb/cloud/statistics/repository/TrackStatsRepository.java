package hu.rb.cloud.statistics.repository;

import hu.rb.cloud.statistics.model.TrackStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackStatsRepository extends JpaRepository<TrackStats, Long> {
    TrackStats findByTrackId(String trackId);
}
