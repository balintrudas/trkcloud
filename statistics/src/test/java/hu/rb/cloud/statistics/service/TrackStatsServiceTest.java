package hu.rb.cloud.statistics.service;

import hu.rb.cloud.statistics.message.TrackStatsMessageSource;
import hu.rb.cloud.statistics.model.TrackStats;
import hu.rb.cloud.statistics.model.dto.Track;
import hu.rb.cloud.statistics.repository.TrackStatsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.text.DecimalFormat;
import java.text.ParseException;

import static hu.rb.cloud.statistics.helper.ModelGenerator.generateTrack;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrackStatsServiceTest {

    @InjectMocks
    @Spy
    private TrackStatsServiceImpl trackStatsService;

    @Mock
    private TrackStatsRepository trackStatsRepository;

    @Mock
    private TrackStatsMessageSource trackStatsMessageSource;

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCalcStatsWithNoTrack(){
        trackStatsService.calcStats(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailCalcStatsWithNoTrackId(){
        Track track = generateTrack();
        track.setTrackId(null);
        trackStatsService.calcStats(track);
    }

    @Test
    public void shouldCreateNewTrackStats(){
        ArgumentCaptor<TrackStats> captor = ArgumentCaptor.forClass(TrackStats.class);
        when(trackStatsRepository.findByTrackId(any())).thenReturn(null);
        when(trackStatsRepository.save(any())).then(returnsFirstArg());
        when(trackStatsMessageSource.trackStatsChannel()).thenReturn(mock(MessageChannel.class));
        doReturn(1D).when(trackStatsService).calcDistance(any());
        doReturn(1D).when(trackStatsService).calcSpeed(any());
        doReturn(1L).when(trackStatsService).calcDuration(any());
        when(trackStatsRepository.save(any())).then(returnsFirstArg());
        Track track = generateTrack();
        trackStatsService.calcStats(track);
        verify(trackStatsRepository).save(captor.capture());
        assertEquals(captor.getValue().getTrackId(), track.getTrackId());
        assertEquals(1d, captor.getValue().getDistance(), 0.0);
    }

    @Test
    public void shouldNotCreateNewTrackStats(){
        TrackStats refTrackStats = new TrackStats();
        refTrackStats.setTrackId("trackId");
        when(trackStatsRepository.findByTrackId(any())).thenReturn(refTrackStats);
        when(trackStatsRepository.save(any())).then(returnsFirstArg());
        when(trackStatsMessageSource.trackStatsChannel()).thenReturn(mock(MessageChannel.class));
        doReturn(1D).when(trackStatsService).calcDistance(any());
        doReturn(1D).when(trackStatsService).calcSpeed(any());
        doReturn(1L).when(trackStatsService).calcDuration(any());
        Track track = generateTrack();
        trackStatsService.calcStats(track);
        assertEquals(refTrackStats.getTrackId(), "trackId");
        assertEquals(1d, refTrackStats.getDistance(), 0.0);
    }

    @Test
    public void shouldCalcDistance() throws ParseException {
        Track track = generateTrack();
        Double distance = trackStatsService.calcDistance(track);
        assertNotNull(distance);
        assertTrue(distance>0);
    }

}
