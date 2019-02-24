package hu.rb.cloud.message.message;

import hu.rb.cloud.message.message.TrackMessageListener;
import hu.rb.cloud.message.model.dto.Track;
import hu.rb.cloud.message.model.dto.TrackStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;

import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;

public class TrackMessageListenerTest {

    @InjectMocks
    private TrackMessageListener trackMessageListener;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldFailWhenTrackNull(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Message cannot be null");
        trackMessageListener.processTrack(null);
    }

    @Test
    public void shouldFailWhenTrackIdNull(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("TrackId field is required");
        Track track = new Track();
        track.setId(1l);
        track.setTrackStatus(TrackStatus.FINISHED);
        track.setEndDate(new Date());
        trackMessageListener.processTrack(track);
    }
}
