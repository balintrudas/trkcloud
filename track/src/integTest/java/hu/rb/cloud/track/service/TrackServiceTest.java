package hu.rb.cloud.track.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.sun.security.auth.UserPrincipal;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.client.AccountClient;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.model.TrackStatus;
import hu.rb.cloud.track.model.dto.Account;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import hu.rb.cloud.track.helper.ModelGenerator;
import org.springframework.ui.Model;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.transaction.Transactional;

import java.io.File;
import java.security.Principal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@ActiveProfiles(value = {"test","noDataInit"})
public class TrackServiceTest {

    @Autowired
    private TrackService trackService;

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(
            wireMockConfig().dynamicPort()
    );

    @ClassRule
    public static GenericContainer redis = new FixedHostPortGenericContainer("rabbitmq:management")
            .withFixedExposedPort(5673,5672)
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*\\s",1));

    @TestConfiguration
    public static class LocalRibbonClientConfiguration {
        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(new Server("localhost", wireMockRule.port()));
        }
    }

    @Test
    public void shouldGetPathForTrack() {
        Track track = ModelGenerator.generateTrack(null);
        entityManager.persist(track);
        MultiPoint multiPoint = trackService.getPathByTrackId(track.getTrackId());
        assertNotNull(multiPoint);
        assertTrue(multiPoint.getCoordinates().length > 0);
    }

    @Test
    public void shouldFailGetPathForTrack() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find track");
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
    public void shouldTrackingReturnNewTrack() throws JsonProcessingException {
        Track track = ModelGenerator.generateTrack(null);
        track.setTrackStatus(TrackStatus.STARTED);
        track.setTrackId(null);
        Account account = ModelGenerator.generateAccount(1L);
        stubFor(get(urlEqualTo("/?username=user"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(account))));
        Principal principal = new UserPrincipal("user");
        Track resultTrack = trackService.tracking(track, principal);
        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
    }

    @Test
    public void shouldTrackingReturnUpdatedTrack() throws JsonProcessingException {
        Track track = ModelGenerator.generateTrack(null);
        track.setTrackStatus(TrackStatus.STARTED);
        track.setTrackId(UUID.randomUUID().toString());
        entityManager.persist(track);
        Account account = ModelGenerator.generateAccount(1L);
        stubFor(get(urlEqualTo("/?username=user"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(account))));
        Principal principal = new UserPrincipal("user");
        Track resultTrack = trackService.tracking(track, principal);
        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
        assertEquals(resultTrack.getTrackId(), track.getTrackId());
    }

    @Test
    public void shouldFailTrackingWithFinishedButNotFound() {
        Track inputTrack = ModelGenerator.generateTrack(1L);
        inputTrack.setTrackStatus(TrackStatus.FINISHED);
        Principal principal = new UserPrincipal("user");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find track to finish");
        trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldFailTrackingWithNoPermissionToSave() throws JsonProcessingException {
        Track inputTrack = ModelGenerator.generateTrack(null);
        inputTrack.setAccountId(1L);
        entityManager.persist(inputTrack);

        Account account = ModelGenerator.generateAccount(3L);
        stubFor(get(urlEqualTo("/?username=user"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(account))));

        Principal principal = new UserPrincipal("user");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Permission denied to save the track");
        trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldFailTrackingWithNoAccount() throws JsonProcessingException {
        Track inputTrack = ModelGenerator.generateTrack(null);
        inputTrack.setAccountId(1L);
        entityManager.persist(inputTrack);
        entityManager.flush();
        entityManager.clear();

        Account account = null;
        stubFor(get(urlEqualTo("/?username=user"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(account))));

        Principal principal = new UserPrincipal("user");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't find account");
        trackService.tracking(inputTrack, principal);
    }

    @Test
    public void shouldEndTrackingReturnFinishedTrack() throws JsonProcessingException {
        Track track = ModelGenerator.generateTrack(null);
        track.setTrackStatus(TrackStatus.STARTED);
        track.setTrackId(UUID.randomUUID().toString());
        entityManager.persist(track);
        entityManager.flush();
        entityManager.clear();

        Account account = ModelGenerator.generateAccount(1L);
        stubFor(get(urlEqualTo("/?username=user"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(account))));
        Principal principal = new UserPrincipal("user");
        Track resultTrack = trackService.endTracking(track, principal);

        assertNotNull(resultTrack);
        assertNotNull(resultTrack.getTrackId());
        assertEquals(resultTrack.getTrackStatus(),TrackStatus.FINISHED);
    }
}
