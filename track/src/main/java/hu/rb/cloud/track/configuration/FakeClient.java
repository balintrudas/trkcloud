package hu.rb.cloud.track.configuration;

import com.netflix.discovery.converters.Auto;
import com.sun.security.auth.UserPrincipal;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.respository.TrackRepository;
import hu.rb.cloud.track.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
public class FakeClient {

    @Autowired
    private TrackService trackService;
    @Value(value = "classpath:coordinates.txt")
    private Resource coordinates;
    private List<Coordinate> lineCoordinates;
    private int coordinateIndex = 0;
    private Track track = new Track();

    @PostConstruct
    public void postConstruct() throws IOException {
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> lineCoordinates = new ArrayList<>();

        List<String> list = new ArrayList<String>();
        if (coordinates.getFile().exists()) {
            try {
                list = Files.readAllLines(coordinates.getFile().toPath(), Charset.defaultCharset());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (list.isEmpty()) {
                return;
            }
        }
        for (String line : list) {
            String[] res = line.split(",");
            lineCoordinates.add(new Coordinate(Double.valueOf(res[0]), Double.valueOf(res[1]), Double.valueOf(res[2])));
        }
        this.lineCoordinates = lineCoordinates;
    }

    @Scheduled(fixedRate = 1000)
    public void fakeClientCall() {
        if (coordinateIndex < lineCoordinates.size()) {
            GeometryFactory gf = new GeometryFactory();
            Coordinate targetCoordinate = lineCoordinates.get(coordinateIndex);
            Coordinate[] coordinates = {targetCoordinate};
            MultiPoint multiPoint = gf.createMultiPoint(coordinates);
            Track fakeTrackEntry = new Track();
            fakeTrackEntry.setMultiPoint(multiPoint);
            fakeTrackEntry.setId(track.getId());
            fakeTrackEntry = trackService.tracking(fakeTrackEntry, new UserPrincipal("user"));
            track.setId(fakeTrackEntry.getId());
            coordinateIndex++;
        } else if(!track.getTrackStatus().equals(TrackStatus.FINISHED)) {
            Track fakeTrackEntry = new Track();
            track.setId(fakeTrackEntry.getId());
            trackService.endTracking(fakeTrackEntry, new UserPrincipal("user"));
            track.setTrackStatus(TrackStatus.FINISHED);
        }
    }
}
