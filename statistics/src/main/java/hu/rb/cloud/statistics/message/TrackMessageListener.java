package hu.rb.cloud.statistics.message;

import hu.rb.cloud.statistics.model.dto.Track;
import hu.rb.cloud.statistics.service.TrackStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(TrackMessageSink.class)
public class TrackMessageListener {

    @Autowired
    private TrackStatsService trackStatsService;

    @StreamListener(target = TrackMessageSink.TRACK_SAVE_INPUT)
    public void processTrack(Track track) {
        trackStatsService.calcStats(track);
    }
}
