package hu.rb.cloud.track.respository;

import hu.rb.cloud.track.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Track findByTrackId(String trackId);
}