package hu.rb.cloud.message.message;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import hu.rb.cloud.message.model.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.Assert;

@Configuration
@EnableBinding(TrackMessageSink.class)
public class TrackMessageListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @StreamListener(target = TrackMessageSink.TRACK_SAVE_INPUT)
    public void processTrack(Track track) {
        Assert.notNull(track,"Message cannot be null");
        Assert.notNull(track.getTrackId(),"TrackId field is required");
        Coordinate lastCoordinate = track.getMultiPoint().getCoordinates()[track.getMultiPoint().getCoordinates().length-1];
        Coordinate[] lastCoordinates = {lastCoordinate};
        GeometryFactory gf = new GeometryFactory();
        track.setMultiPoint(gf.createMultiPoint(lastCoordinates));
        simpMessagingTemplate.convertAndSend("/topic/track/" + track.getTrackId(), track);
    }
}
