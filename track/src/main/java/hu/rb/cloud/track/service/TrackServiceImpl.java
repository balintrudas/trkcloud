package hu.rb.cloud.track.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.client.AccountClient;
import hu.rb.cloud.track.message.TrackMessageSource;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.model.dto.Account;
import hu.rb.cloud.track.respository.TrackRepository;
import hu.rb.cloud.track.service.reducer.SeriesReducer;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.*;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private SeriesReducer seriesReducer;

    @Autowired
    private TrackMessageSource trackMessageSource;

    @Autowired
    private AccountClient accountClient;

    @Override
    public Track save(Track track) {
        return trackRepository.save(track);
    }

    @Override
    public Track tracking(Track track, Principal principal) {
        Assert.notNull(track, "Track cannot be null");
        Assert.notNull(principal, "Principal cannot be null");
        Track savedTrack = null;
        if (track.getId() != null) {
            savedTrack = trackRepository.findById(track.getId()).orElse(null);
        }
        if(track.getTrackStatus()==null){
            track.setTrackStatus(TrackStatus.STARTED);
        }
        if(track.getTrackStatus().equals(TrackStatus.FINISHED)){
            Assert.notNull(savedTrack, "Can't find track to finish");
        }

        Account account = accountClient.getAccount(principal.getName(),null);
        Assert.notNull(account, "Can't find account");

        if (savedTrack != null) {
            Assert.isTrue(savedTrack.getAccountId().equals(account.getId()), "Permission denied to save the track");
            Assert.isTrue(!savedTrack.getTrackStatus().equals(TrackStatus.FINISHED), "Track is already finished");
            GeometryFactory gf = new GeometryFactory();
            if(track.getMultiPoint()!=null) {
                savedTrack.setMultiPoint(gf.createMultiPoint((Coordinate[]) ArrayUtils.addAll(
                        savedTrack.getMultiPoint().getCoordinates(), track.getMultiPoint().getCoordinates())));
            }
            //Reduce geo point session
            List<Coordinate> reduced = seriesReducer.reduce(Arrays.asList(savedTrack.getMultiPoint().getCoordinates()),0.0001);
            savedTrack.setMultiPoint(gf.createMultiPoint(reduced.toArray(new Coordinate[reduced.size()])));
            if(track.getTrackStatus().equals(TrackStatus.FINISHED)){
                savedTrack.setEndDate(new Date());
                savedTrack.setTrackStatus(TrackStatus.FINISHED);
            }
            savedTrack.setModified(new Date());
            track = savedTrack;
        }else{
            track.setAccountId(account.getId());
            track.setVehicleId(account.getCurrentVehicle().getId());
            track.setCreated(new Date());
            track.setModified(new Date());
            track.setTrackStatus(TrackStatus.STARTED);
            track.setTrackId(UUID.randomUUID().toString());
        }
        track = trackRepository.save(track);
        trackMessageSource.trackSaveChannel().send(MessageBuilder.withPayload(track).build());
        return track;
    }

    @Override
    public Track endTracking(Track track, Principal principal) {
        track.setTrackStatus(TrackStatus.FINISHED);
        return this.tracking(track,principal);
    }

    @Override
    public MultiPoint getPathByTrackId(String trackId) {
        Track track = trackRepository.findByTrackId(trackId);
        Assert.notNull(track, "Can't find track");
        return track.getMultiPoint();
    }

    @Override
    public Optional<Track> find(Long id) {
        return trackRepository.findById(id);
    }

    @Override
    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    @Override
    public List<Track> findAll(Sort sort) {
        return trackRepository.findAll(sort);
    }

    @Override
    public Page<Track> findAll(Pageable pageable) {
        return trackRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        trackRepository.deleteById(id);
    }

    @Override
    public void delete(Track track) {
        trackRepository.delete(track);
    }

    @Override
    public void deleteAll() {
        trackRepository.deleteAll();
    }

    @Override
    public long count() {
        return trackRepository.count();
    }

}