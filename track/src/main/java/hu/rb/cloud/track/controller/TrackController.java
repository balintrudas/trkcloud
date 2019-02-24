package hu.rb.cloud.track.controller;

import com.sun.org.apache.xpath.internal.operations.Mult;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TrackController {

    @Autowired
    private TrackService trackService;

    @PostMapping("/")
    public Track startOrContinueTracking(@RequestBody Track track, Principal principal) {
        return trackService.tracking(track, principal);
    }

    @PostMapping("/end")
    public Track endTracking(@RequestBody Track track, Principal principal) {
        return trackService.endTracking(track, principal);
    }

    @GetMapping("/{id}")
    public Track getById(@PathVariable(value = "id") Long id) {
        return trackService.find(id).orElse(null);
    }

    @GetMapping("/path/{trackid}")
    public MultiPoint getPathByTrackId(@PathVariable(value = "trackid") String trackid) {
        return trackService.getPathByTrackId(trackid);
    }

    @GetMapping("/")
    @PreAuthorize("#oauth2.hasScope('server')")
    public List<Track> getAll() {
        return trackService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        trackService.delete(id);
    }

}