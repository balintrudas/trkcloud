package hu.rb.cloud.search.service;

import hu.rb.cloud.search.client.AccountClient;
import hu.rb.cloud.search.model.TrackStatus;
import hu.rb.cloud.search.model.TrackSum;
import hu.rb.cloud.search.model.dto.Account;
import hu.rb.cloud.search.model.dto.Track;
import hu.rb.cloud.search.model.dto.TrackStats;
import hu.rb.cloud.search.repository.TrackSumRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


@Service
public class TrackSumServiceImpl implements TrackSumService {

    @Autowired
    private TrackSumRepository trackSumRepository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public TrackSum save(TrackSum trackSum) {
        return trackSumRepository.save(trackSum);
    }

    @Override
    public TrackSum map(Track track) {
        Assert.notNull(track,"Message cannot be null");
        Assert.notNull(track.getTrackId(),"TrackId field is required");
        TrackSum trackSum = findByTrackId(track.getTrackId());
        if(trackSum != null) {
            if (!trackSum.getTrackStatus().equals(TrackStatus.FINISHED)) {
                trackSum.setEndDate(track.getEndDate());
                trackSum.setTrackStatus(track.getTrackStatus());
            }
        }else{
            Account account = accountClient.getAccount(null,track.getAccountId());
            Assert.notNull(account, "Can't find account");
            trackSum = new TrackSum();
            trackSum.setTrackId(track.getTrackId());
            trackSum.setTrackStatus(TrackStatus.STARTED);
            trackSum.setStartDate(track.getCreated());
            trackSum.setUsername(account.getUsername());
            trackSum.setFirstName(account.getFirstName());
            trackSum.setLastName(account.getLastName());
            trackSum.setVehicleName(account.getCurrentVehicle().getName());
        }
        return save(trackSum);
    }

    @Override
    public TrackSum map(TrackStats trackStats) {
        Assert.notNull(trackStats,"Message cannot be null");
        Assert.notNull(trackStats.getTrackId(),"TrackId field is required");
        TrackSum trackSum = findByTrackId(trackStats.getTrackId());
        if(trackSum!=null){
            trackSum.setAverageSpeed(trackStats.getAverageSpeed());
            trackSum.setDistance(trackStats.getDistance());
            trackSum.setDuration(trackStats.getDuration());
            trackSum = save(trackSum);
        }
        return trackSum;
    }

    @Override
    public TrackSum findByTrackId(String trackId) {
        return trackSumRepository.findByTrackId(trackId);
    }

    @Override
    public void deleteAll() {
        trackSumRepository.deleteAll();
    }

    @Override
    public Long countAll() {
        return trackSumRepository.count();
    }

    @Override
    public Page search(hu.rb.cloud.search.model.dto.Page page){
        QueryStringQueryBuilder queryBuilderRoot =  QueryBuilders.queryStringQuery(page.getSearchText());
        QueryStringQueryBuilder queryBuilderRootWildcard =  QueryBuilders.queryStringQuery("*" + page.getSearchText() + "*");
        QueryStringQueryBuilder queryStringQuery = queryBuilderRoot.lenient(true).fuzziness(Fuzziness.TWO);
        QueryStringQueryBuilder queryStringQueryWildcard = queryBuilderRootWildcard.lenient(true).fuzziness(Fuzziness.TWO);
        QueryBuilder queryBulder = QueryBuilders.boolQuery()
                .should(queryStringQuery.field("username"))
                .should(queryStringQueryWildcard.field("username"))
                .should(queryStringQuery.field("firstName"))
                .should(queryStringQueryWildcard.field("firstName"))
                .should(queryStringQuery.field("lastName"))
                .should(queryStringQueryWildcard.field("lastName"))
                .should(queryStringQuery.field("trackStatus"))
                .should(queryStringQueryWildcard.field("trackStatus"))
                .should(queryStringQuery.field("vehicleName"))
                .should(queryStringQueryWildcard.field("vehicleName"));

        if (page.getSearchText() == null || page.getSearchText().isEmpty()) {
            queryBulder = matchAllQuery();
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBulder)
                .build();
        searchQuery.setPageable(page);
        return elasticsearchTemplate.queryForPage(searchQuery, TrackSum.class);
    }
}
