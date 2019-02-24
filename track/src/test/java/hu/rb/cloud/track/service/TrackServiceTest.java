package hu.rb.cloud.track.service;

import com.sun.security.auth.UserPrincipal;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.client.AccountClient;
import hu.rb.cloud.track.helper.ModelGenerator;
import hu.rb.cloud.track.message.TrackMessageSource;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.model.dto.Account;
import hu.rb.cloud.track.respository.TrackRepository;
import hu.rb.cloud.track.service.reducer.SeriesReducer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.MessageChannel;

import javax.jws.WebParam;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrackServiceTest {

    @InjectMocks
    private TrackServiceImpl trackService;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private SeriesReducer seriesReducer;

    @Mock
    private TrackMessageSource trackMessageSource;

    @Mock
    private AccountClient accountClient;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldGetPathForTrack() {
        Track track = ModelGenerator.generateTrack(1L);
        when(trackRepository.findByTrackId(any())).thenReturn(track);
        MultiPoint multiPoint = trackService.getPathByTrackId(track.getTrackId());
        assertNotNull(multiPoint);
        assertTrue(multiPoint.getCoordinates().length > 0);
    }

    @Test
    public void shouldFailGetPathForTrack() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find track");
        when(trackRepository.findByTrackId(any())).thenReturn(null);
        trackService.getPathByTrackId(UUID.randomUUID().toString());
    }

    @Test
    public void shouldFailTrackingWithNullTrack() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Track cannot be null");
        Principal principal = new UserPrincipal("user");
        trackService.tracking(null, principal);
    }

    @Test
    public void shouldFailTrackingWithNullPrincipal() {
        Track track = ModelGenerator.generateTrack(1L);
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Principal cannot be null");
        trackService.tracking(track, null);
    }

    @Test
    public void shouldTrackingReturnNewTrack() {
        Track track = ModelGenerator.generateTrack(null);
        track.setTrackId(null);
        Account account = ModelGenerator.generateAccount(1L);
        Principal principal = new UserPrincipal("user");
        track.setTrackStatus(TrackStatus.STARTED);
        when(accountClient.getAccount("user", null)).thenReturn(account);
        when(trackRepository.save(any())).then(returnsFirstArg());
        when(trackMessageSource.trackSaveChannel()).thenReturn(mock(MessageChannel.class));
        Track resultTrack = trackService.tracking(track, principal);
        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
    }

    @Test
    public void shouldTrackingReturnUpdatedTrack() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        Track savedTrack = ModelGenerator.generateTrack(1L);
        savedTrack.setTrackId(inputTrack.getTrackId());
        savedTrack.setTrackStatus(TrackStatus.STARTED);

        Account account = ModelGenerator.generateAccount(1L);
        Principal principal = new UserPrincipal("user");

        when(accountClient.getAccount("user", null)).thenReturn(account);
        when(trackRepository.findById(1L)).thenReturn(Optional.of(savedTrack));
        when(trackRepository.save(any())).then(returnsFirstArg());
        when(trackMessageSource.trackSaveChannel()).thenReturn(mock(MessageChannel.class));

        Track resultTrack = trackService.tracking(inputTrack, principal);

        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
    }

    @Test
    public void shouldFailTrackingWithFinishedButNotFound() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        inputTrack.setTrackStatus(TrackStatus.FINISHED);
        Principal principal = new UserPrincipal("user");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find track to finish");
        when(trackRepository.findById(1L)).thenReturn(Optional.empty());
        trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldFailTrackingWithNoPermissionToSave() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        Track savedTrack = ModelGenerator.generateTrack(1L);
        savedTrack.setTrackId(inputTrack.getTrackId());
        savedTrack.setTrackStatus(TrackStatus.STARTED);
        Account account = ModelGenerator.generateAccount(3L);
        Principal principal = new UserPrincipal("user");
        when(accountClient.getAccount("user", null)).thenReturn(account);
        when(trackRepository.findById(1L)).thenReturn(Optional.of(savedTrack));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Permission denied to save the track");
        Track resultTrack = trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldFailTrackingWithNoAccount() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        Track savedTrack = ModelGenerator.generateTrack(1L);
        savedTrack.setTrackId(inputTrack.getTrackId());
        savedTrack.setTrackStatus(TrackStatus.STARTED);
        Principal principal = new UserPrincipal("user");
        when(accountClient.getAccount("user", null)).thenReturn(null);
        when(trackRepository.findById(1L)).thenReturn(Optional.of(savedTrack));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find account");
        Track resultTrack = trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldEndTrackingReturnFinishedTrack() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        Track savedTrack = ModelGenerator.generateTrack(1L);
        savedTrack.setTrackId(inputTrack.getTrackId());
        savedTrack.setTrackStatus(TrackStatus.STARTED);

        Account account = ModelGenerator.generateAccount(1L);
        Principal principal = new UserPrincipal("user");

        when(accountClient.getAccount("user", null)).thenReturn(account);
        when(trackRepository.findById(1L)).thenReturn(Optional.of(savedTrack));
        when(trackRepository.save(any())).then(returnsFirstArg());
        when(trackMessageSource.trackSaveChannel()).thenReturn(mock(MessageChannel.class));

        Track resultTrack = trackService.endTracking(inputTrack, principal);

        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
        assertEquals(resultTrack.getTrackStatus(),TrackStatus.FINISHED);
    }
}
