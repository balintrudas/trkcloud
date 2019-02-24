package hu.rb.cloud.search.repository;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.IndexNotFoundException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static hu.rb.cloud.search.helper.ModelGenerator.generateTrackSum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TrackSumRepositoryTest {

    @Autowired
    private TrackSumRepository trackSumRepository;

    private static final Log logger = LogFactory
            .getLog(TrackSumRepositoryTest.class);

    static Map<String, String> env = new HashMap<String, String>()
    {{
        put("discovery.type", "single-node");
        put("cluster.name", "search-node-cluster");
        put("bootstrap.memory_lock", "true");
        put("xpack.security.enabled", "false");
    }};

    @ClassRule
    public static GenericContainer redis = new FixedHostPortGenericContainer("docker.elastic.co/elasticsearch/elasticsearch:6.2.1")
            .withFixedExposedPort(9302,9300).withEnv(env)
            .waitingFor(Wait.forLogMessage(".*started.*\\s",2));

    @Before
    public void before() {
        try{
            trackSumRepository.deleteAll();
        }catch (IndexNotFoundException e){
            logger.info("Can't delete before test because index is not exist yet.");
        }
    }

    @Test
    public void shouldFindByTrackId(){
        TrackSum refTrackSum = trackSumRepository.save(generateTrackSum(UUID.randomUUID().toString(), TrackStatus.STARTED));
        assertNotNull(refTrackSum);
        TrackSum resultTrackSum = trackSumRepository.findByTrackId(refTrackSum.getTrackId());
        assertNotNull(refTrackSum);
        assertEquals(refTrackSum.getFirstName(), resultTrackSum.getFirstName());
    }


}
