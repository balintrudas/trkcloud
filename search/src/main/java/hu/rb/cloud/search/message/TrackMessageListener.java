package hu.rb.cloud.search.message;

import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Track;
import hu.rb.cloud.search.service.TrackSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@EnableBinding(TrackMessageSink.class)
public class TrackMessageListener {

    @Autowired
    private TrackSumService trackSumService;

    @StreamListener(target = TrackMessageSink.TRACK_SAVE_INPUT)
    public void processTrack(Track track) {
        trackSumService.map(track);
    }
}
