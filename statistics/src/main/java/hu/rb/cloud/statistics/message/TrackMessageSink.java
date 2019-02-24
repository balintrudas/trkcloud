package hu.rb.cloud.statistics.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TrackMessageSink {

    String TRACK_SAVE_INPUT = "trackSaveChannel";

    @Input(TRACK_SAVE_INPUT)
    SubscribableChannel trackSaveChannel();

}
