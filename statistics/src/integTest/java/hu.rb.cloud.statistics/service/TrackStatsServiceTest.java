package hu.rb.cloud.statistics.service;

import hu.rb.cloud.statistics.helper.ModelGenerator;
import hu.rb.cloud.statistics.model.TrackStats;
import hu.rb.cloud.statistics.model.dto.Track;
import hu.rb.cloud.statistics.repository.TrackStatsRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
public class TrackStatsServiceTest {

    @Autowired
    private TrackStatsService trackStatsService;

    @Autowired
    private TrackStatsRepository trackStatsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @ClassRule
    public static GenericContainer redis = new FixedHostPortGenericContainer("rabbitmq:management")
            .withFixedExposedPort(5673,5672)
            .waitingFor(Wait.forLogMessage(".*Server startup complete.*\\s",1));

    @Test
    public void shouldCalcDistance() {
        Track track = ModelGenerator.generateTrack(null);
        Double distance = trackStatsService.calcDistance(track);
        assertNotNull(distance);
        assertTrue(distance>0);
    }

    @Test
    public void shouldCalcSpeed() {
        TrackStats trackStats = ModelGenerator.generateTrackStats(null);
        Double speed = trackStatsService.calcSpeed(trackStats);
        assertNotNull(speed);
        assertTrue(speed>0);
    }

    @Test
    public void shouldCalcDuration() {
        Track track = ModelGenerator.generateTrack(null);
        Long duration = trackStatsService.calcDuration(track);
        assertNotNull(duration);
        assertTrue(duration>0);
    }

    @Test
    public void shouldCalcStats() {
        TrackStats trackStats = ModelGenerator.generateTrackStats(null);
        trackStats.setAverageSpeed(null);
        trackStats.setDuration(null);
        trackStats.setDistance(null);
        Track track = ModelGenerator.generateTrack(null);
        track.setTrackId(trackStats.getTrackId());
        entityManager.persist(trackStats);
        trackStatsService.calcStats(track);
        TrackStats savedTrackStats = trackStatsRepository.findByTrackId(trackStats.getTrackId());
        assertNotNull(savedTrackStats);
        assertNotNull(savedTrackStats.getDuration());
        assertNotNull(savedTrackStats.getAverageSpeed());
        assertNotNull(savedTrackStats.getDistance());
    }

    @Test
    public void shouldCalcStatsWithNewTrackStats() {
        Track track = ModelGenerator.generateTrack(null);
        trackStatsService.calcStats(track);
        TrackStats savedTrackStats = trackStatsRepository.findByTrackId(track.getTrackId());
        assertNotNull(savedTrackStats);
        assertNotNull(savedTrackStats.getDuration());
        assertNotNull(savedTrackStats.getAverageSpeed());
        assertNotNull(savedTrackStats.getDistance());
    }

}
