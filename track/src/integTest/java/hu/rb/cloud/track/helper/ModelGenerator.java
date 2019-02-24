package hu.rb.cloud.track.helper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.model.dto.Account;
import hu.rb.cloud.track.model.dto.Vehicle;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class ModelGenerator {

    public static Track generateTrack(Long id) {
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> lineCoordinates = new ArrayList<>();
        for(int i = 0 ; i< 100 ; i++){
            lineCoordinates.add(new Coordinate(i,i,0));
        }
        MultiPoint multiPoint = gf.createMultiPoint(
                lineCoordinates.toArray(new Coordinate[lineCoordinates.size()]));

        Track track = new Track();
        track.setId(id);
        track.setModified(new Date());
        track.setCreated(Date.from(LocalDateTime.now()
                .minus(5,ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        track.setAccountId(1l);
        track.setVehicleId(1l);
        track.setTrackId(UUID.randomUUID().toString());
        track.setTrackStatus(TrackStatus.FINISHED);
        track.setMultiPoint(multiPoint);
        track.setEndDate(new Date());
        return track;
    }

    public static Account generateAccount(Long id){
        Account account = new Account();
        account.setId(id);
        account.setUsername("username");
        account.setFirstName("lastname");
        account.setLastName("lastname");
        account.setAge(10);
        account.setCurrentVehicle(new Vehicle());
        return account;
    }
}
