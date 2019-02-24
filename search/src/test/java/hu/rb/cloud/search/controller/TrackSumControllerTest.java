package hu.rb.cloud.search.controller;
import org.springframework.data.domain.PageImpl;

import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Page;
import hu.rb.cloud.search.service.TrackSumServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrackSumControllerTest {

    @InjectMocks
    private TrackSumController trackSumController;

    @Mock
    private TrackSumServiceImpl trackSumService;

    @Test
    public void shouldReturnTrackSumPage(){
        List<TrackSum> trackSumList = new ArrayList<TrackSum>();
        trackSumList.add(new TrackSum());
        trackSumList.add(new TrackSum());
        AggregatedPageImpl aggregatedPage = new AggregatedPageImpl<TrackSum>(trackSumList,null,2);
        Page page = new Page();
        page.setPage(1);
        page.setSize(10);
        page.setSearchText("aaa");
        when(trackSumService.search(page)).thenReturn(aggregatedPage);
        PageImpl<TrackSum> trackSums = trackSumController.getAllTracks(page);
        assertNotNull(trackSums);
        assertEquals(trackSums.getNumberOfElements(),2);
        assertEquals(trackSums.getPageable().getPageNumber(), 1);
    }

    @Test
    public void shouldReturnEmptyPageImplTrackSumPage(){
        Page page = new Page();
        page.setPage(1);
        page.setSize(10);
        page.setSearchText("aaa");
        when(trackSumService.search(any())).thenReturn(null);
        PageImpl<TrackSum> trackSums = trackSumController.getAllTracks(page);
        assertNotNull(trackSums);
        assertEquals(trackSums.getNumberOfElements(),0);
        assertEquals(trackSums.getPageable().getPageNumber(), 1);
    }
}
