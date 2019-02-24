package hu.rb.cloud.search.controller;

import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.service.TrackSumService;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RestController
public class TrackSumController {

    @Autowired
    private TrackSumService trackSumService;

    @PostMapping("tracks")
    public PageImpl<TrackSum> getAllTracks(@RequestBody hu.rb.cloud.search.model.dto.Page page) {
        Page aggregatedPage = trackSumService.search(page);
        return new PageImpl<TrackSum>(aggregatedPage == null ? new ArrayList<>() : aggregatedPage.getContent(), page,
                aggregatedPage == null ? 0 : aggregatedPage.getTotalElements());
    }

}
