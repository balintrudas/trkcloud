package hu.rb.cloud.track.configuration;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.message.TrackMessageSource;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.service.TrackService;
import hu.rb.cloud.track.service.reducer.SeriesReducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@Profile("!noDataInit")
public class DataInitialization implements SmartLifecycle {

    @Value(value = "classpath:coordinates.txt")
    private Resource coordinates;

    @Autowired
    private TrackService trackService;

    @Autowired
    private SeriesReducer seriesReducer;

    @Autowired
    private TrackMessageSource trackSumSource;

    private boolean running;

    public void initData() throws IOException {
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> lineCoordinates = new ArrayList<>();

        List<String> list = new ArrayList<String>();
        if(coordinates.getFile().exists()){
            try {
                list = Files.readAllLines(coordinates.getFile().toPath(), Charset.defaultCharset());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(list.isEmpty())
                return;
        }
        for(String line : list){
            String [] res = line.split(",");
            lineCoordinates.add(new Coordinate(Double.valueOf(res[0]),Double.valueOf(res[1]),Double.valueOf(res[2])));
        }

        lineCoordinates = seriesReducer.reduce(lineCoordinates,0.0001);

        MultiPoint multiPoint = gf.createMultiPoint(
                lineCoordinates.toArray(new Coordinate[lineCoordinates.size()]));

        Track track = new Track();
        track.setModified(new Date());
        track.setCreated(Date.from(LocalDateTime.now()
                .minus(3,ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        track.setAccountId(2l);
        track.setVehicleId(2l);
        track.setTrackId("11c10f28-c6fd-4543-a2f9-5c3215b72cab");
        track.setTrackStatus(TrackStatus.FINISHED);
        track.setMultiPoint(multiPoint);
        track.setEndDate(new Date());
        trackService.save(track);
    }

    @Override
    public void start() {
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 2147482648;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
}
