package hu.rb.cloud.search.service;

import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Page;
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
public class TrackSumServiceTest {

    @Autowired
    private TrackSumService trackSumService;

    private static final Log logger = LogFactory
            .getLog(TrackSumServiceTest.class);

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
            trackSumService.deleteAll();
        }catch (IndexNotFoundException e){
            logger.info("Can't delete before test because index is not exist yet.");
        }
    }

    @Test
    public void shouldSaveTrackSum() {
        TrackSum trackSum = generateTrackSum(UUID.randomUUID().toString(), TrackStatus.STARTED);
        TrackSum trackSumResult = trackSumService.save(trackSum);
        assertNotNull(trackSumResult);
        assertEquals(trackSumResult.getTrackId(), trackSum.getTrackId());
        assertNotNull(trackSumResult.getId());
    }

    @Test
    public void shouldFindTrackSum() {
        TrackSum trackSum = generateTrackSum(UUID.randomUUID().toString(), TrackStatus.STARTED);
        TrackSum trackSumResult = trackSumService.save(trackSum);
        assertNotNull(trackSumResult);
        assertNotNull(trackSumResult.getId());
        Page page = new Page();
        page.setSearchText("aaa");
        page.setSize(10);
        page.setPage(0);
        org.springframework.data.domain.Page<TrackSum> searchResult = trackSumService.search(page);
        assertNotNull(searchResult);
        assertEquals(searchResult.getNumberOfElements(), 1);
        assertNotNull(searchResult.getContent());
        assertEquals(searchResult.getContent().size(), 1);
        assertEquals(searchResult.getContent().get(0).getTrackId(), trackSumResult.getTrackId());
    }

}
