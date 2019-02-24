package hu.rb.cloud.track.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TrackMessageSource {

    @Output("trackSaveChannel")
    MessageChannel trackSaveChannel();

}
