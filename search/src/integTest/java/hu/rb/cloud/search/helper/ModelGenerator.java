package hu.rb.cloud.search.helper;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;

import java.util.Date;

public final class ModelGenerator {
    public static TrackSum generateTrackSum(String trackId, TrackStatus trackStatus) {
        TrackSum trackSum = new TrackSum();
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
}
