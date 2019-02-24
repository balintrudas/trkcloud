package hu.rb.cloud.track.service;

import hu.rb.cloud.track.model.Track;
import com.vividsolutions.jts.geom.MultiPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface TrackService {

    Track save(Track track);

    Track tracking(Track track, Principal principal);

    Track endTracking(Track track, Principal principal);

    MultiPoint getPathByTrackId(String trackId);

    Optional<Track> find(Long id);

    List<Track> findAll();

    List<Track> findAll(Sort sort);

    Page<Track> findAll(Pageable pageable);

    void delete(Long id);

    void delete(Track track);

    void deleteAll();

    long count();

}