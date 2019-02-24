package hu.rb.cloud.search.configuration;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.service.TrackSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Profile("!test")
public class DataInitialization {

    @Autowired
    private TrackSumService trackSumService;

    @PostConstruct
    public void initData() {
        trackSumService.deleteAll();
        TrackSum trackSum = new TrackSum();
        trackSum.setVehicleName("Mini Cooper");
        trackSum.setFirstName("Eszter");
        trackSum.setLastName("Nagy");
        trackSum.setUsername("second_user");
        trackSum.setStartDate(Date.from(LocalDateTime.now()
                .minus(13,ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        trackSum.setEndDate(new Date());
        trackSum.setDuration(46800000L);
        trackSum.setDistance(1484000d);
        trackSum.setAverageSpeed(76d);
        trackSum.setTrackId("11c10f28-c6fd-4543-a2f9-5c3215b72cab");
        trackSum.setTrackStatus(TrackStatus.FINISHED);
        trackSumService.save(trackSum);
    }

}
