package hu.rb.cloud.track.repository;

import hu.rb.cloud.track.helper.ModelGenerator;
import hu.rb.cloud.track.model.Track;
import hu.rb.cloud.track.respository.TrackRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest()
public class TrackRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrackRepository trackRepository;

    @Test
    public void shouldFindByTrackId() {
        Track track = ModelGenerator.generateTrack(null);
        entityManager.persist(track);
        Track trackResult = trackRepository.findByTrackId(track.getTrackId());
        assertEquals(trackResult.getId().longValue(), track.getId().longValue());
    }
}
