package hu.rb.cloud.search.service;
import hu.rb.cloud.search.model.dto.Vehicle;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Account;
import hu.rb.cloud.search.model.dto.Track;
import hu.rb.cloud.search.model.dto.TrackStats;
import hu.rb.cloud.search.repository.TrackSumRepository;
import hu.rb.cloud.search.client.AccountClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.UUID;

import static hu.rb.cloud.search.helper.ModelGenerator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrackSumServiceTest {

    @InjectMocks
    private TrackSumServiceImpl trackSumService;

    @Mock
    private TrackSumRepository trackSumRepository;

    @Mock
    private AccountClient accountClient;

    @Test
    public void shouldReturnTrackSumWhenFinish(){
        Track track = generateTrack(TrackStatus.FINISHED);
        TrackSum trackSum = generateTrackSum(track.getTrackId(),UUID.randomUUID().toString(),TrackStatus.STARTED);
        trackSum.setEndDate(null);
        when(trackSumService.findByTrackId(track.getTrackId())).thenReturn(trackSum);
        when(trackSumService.save(any())).then(returnsFirstArg());
        TrackSum resultTrackSum = trackSumService.map(track);
        assertNotNull(resultTrackSum);
        assertNotNull(resultTrackSum.getEndDate());
    }

    @Test
    public void shouldReturnTrackSumWhenStart(){
        Track track = generateTrack(TrackStatus.STARTED);
        when(trackSumService.save(any())).then(returnsFirstArg());
        when(accountClient.getAccount(null,track.getAccountId())).thenReturn(generateAccount());
        TrackSum resultTrackSum = trackSumService.map(track);
        assertNotNull(resultTrackSum);
        assertEquals(resultTrackSum.getTrackStatus(),TrackStatus.STARTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailTrackSumWhenStartNoAccount(){
        Track track = generateTrack(TrackStatus.STARTED);
        when(trackSumService.save(any())).then(returnsFirstArg());
        when(accountClient.getAccount(null,track.getAccountId())).thenReturn(null);
        TrackSum resultTrackSum = trackSumService.map(track);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailTrackSumWhenNoTrack(){
        Track track = null;
        TrackSum resultTrackSum = trackSumService.map(track);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailTrackSumWhenNoTrackId(){
        Track track = generateTrack(TrackStatus.FINISHED);
        track.setTrackId(null);
        TrackSum resultTrackSum = trackSumService.map(track);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailTrackStatsWhenNoTrackStats(){
        TrackStats stats = null;
        TrackSum resultTrackSum = trackSumService.map(stats);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailTrackStatsWhenNoTrackId(){
        TrackStats stats = generateTrackStats(UUID.randomUUID().toString());
        stats.setTrackId(null);
        TrackSum resultTrackSum = trackSumService.map(stats);
    }



}
