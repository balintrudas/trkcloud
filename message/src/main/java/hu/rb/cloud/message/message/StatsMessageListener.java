package hu.rb.cloud.message.message;

import hu.rb.cloud.message.model.dto.TrackStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.Assert;

@Configuration
@EnableBinding(StatsMessageSink.class)
public class StatsMessageListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @StreamListener(target = StatsMessageSink.STATS_TRACK_INPUT)
    public void processTrack(TrackStats trackStats) {
        Assert.notNull(trackStats,"Message cannot be null");
        Assert.notNull(trackStats.getTrackId(),"TrackId field is required");
        simpMessagingTemplate.convertAndSend("/topic/track/stats/" + trackStats.getTrackId(), trackStats);
    }
}
