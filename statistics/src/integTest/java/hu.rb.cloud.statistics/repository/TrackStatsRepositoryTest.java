package hu.rb.cloud.statistics.repository;

import hu.rb.cloud.statistics.helper.ModelGenerator;
import hu.rb.cloud.statistics.model.TrackStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest()
public class TrackStatsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrackStatsRepository trackStatsRespository;

    @Test
    public void shouldFindByTrackId() {
        TrackStats trackStats = ModelGenerator.generateTrackStats(null);
        entityManager.persist(trackStats);
        TrackStats resultTrackStats = trackStatsRespository.findByTrackId(trackStats.getTrackId());
        assertNotNull(resultTrackStats);
        assertNotNull(resultTrackStats.getId());
        assertEquals(resultTrackStats.getTrackId(), trackStats.getTrackId());
    }

    @Test
    public void shouldNotFindByTrackId() {
        TrackStats trackStats = ModelGenerator.generateTrackStats(null);
        entityManager.persist(trackStats);
        TrackStats resultTrackStats = trackStatsRespository.findByTrackId(UUID.randomUUID().toString());
        assertNull(resultTrackStats);
    }
}
