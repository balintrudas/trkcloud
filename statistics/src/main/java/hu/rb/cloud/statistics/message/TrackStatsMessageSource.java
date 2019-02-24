package hu.rb.cloud.statistics.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TrackStatsMessageSource {

    @Output("trackStatsChannel")
    MessageChannel trackStatsChannel();

}
