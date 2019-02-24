package hu.rb.cloud.track.controller;

import hu.rb.cloud.track.helper.ModelGenerator;
import hu.rb.cloud.track.helper.WithScope;
import hu.rb.cloud.track.model.Track;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@ActiveProfiles("noDataInit")
public class TrackControllerTest {

    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrackController trackController;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(trackController).build();
    }

    @Test
    @WithAnonymousUser
    public void shouldFailGetAllWhenAnonym() throws Exception {
        thrown.expectCause(is(instanceOf(AccessDeniedException.class)));
        mvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithScope(scope = "server")
    public void shouldReturnGetAll() throws Exception {
        Track track = ModelGenerator.generateTrack(null);
        entityManager.persist(track);
        entityManager.flush();
        entityManager.clear();

        mvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trackId", is(track.getTrackId())));
    }
}
