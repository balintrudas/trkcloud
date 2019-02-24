package hu.rb.cloud.search.helper;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Account;
import hu.rb.cloud.search.model.dto.Track;
import hu.rb.cloud.search.model.dto.TrackStats;
import hu.rb.cloud.search.model.dto.Vehicle;

import java.util.Date;
import java.util.UUID;

public final class ModelGenerator {

    public static Track generateTrack(TrackStatus trackStats){
        Track track = new Track();
        track.setId(1L);
        track.setAccountId(1L);
        track.setVehicleId(1L);
        track.setCreated(new Date());
        track.setEndDate(new Date());
        track.setModified(new Date());
        track.setTrackId(UUID.randomUUID().toString());
        track.setTrackStatus(trackStats);
        return track;
    }

    public static TrackSum generateTrackSum(String trackId, String id, TrackStatus trackStatus){
        TrackSum trackSum = new TrackSum();
        trackSum.setId(id);
        trackSum.setTrackId(trackId);
        trackSum.setVehicleName("vehiclename");
        trackSum.setStartDate(new Date());
        trackSum.setEndDate(new Date());
        trackSum.setDistance(0.0D);
        trackSum.setTrackStatus(trackStatus);
        trackSum.setUsername("aaa");
        trackSum.setFirstName("aaa");
        trackSum.setLastName("aaa");
        trackSum.setAverageSpeed(0.0D);
        trackSum.setDuration(0L);
        return trackSum;
    }

    public static Account generateAccount(){
        Account account = new Account();
        account.setId(1L);
        account.setUsername("account");
        account.setFirstName("account");
        account.setLastName("account");
        account.setAge(0);
        account.setCurrentVehicle(new Vehicle());
        return account;
    }

    public static TrackStats generateTrackStats(String trackId){
        TrackStats stats = new TrackStats();
        stats.setTrackId(trackId);
        stats.setCreated(new Date());
        stats.setModified(new Date());
        stats.setDistance(0.0D);
        stats.setAverageSpeed(0.0D);
        stats.setDuration(0L);
        return stats;
    }

}
