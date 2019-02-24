package hu.rb.cloud.search.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface StatsMessageSink {

    String STATS_TRACK_INPUT = "statsTrackChannel";

    @Input(STATS_TRACK_INPUT)
    SubscribableChannel statsTrackChannel();

}
