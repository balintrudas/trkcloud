package hu.rb.cloud.track.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.MultiPoint;
import hu.rb.cloud.track.json.MultiPointDeserializer;
import hu.rb.cloud.track.json.MultiPointSerializer;

import javax.persistence.*;
import java.util.Date;

@Entity

public class Track {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String trackId;

    @Column
    private Date created;

    @Column
    private Date modified;

    @Column
    private Date endDate;

    @Column
    private Long accountId;

    @Column
    private Long vehicleId;

    @Column(columnDefinition = "Geometry")
    @JsonDeserialize(using = MultiPointDeserializer.class)
    @JsonSerialize(using = MultiPointSerializer.class)
    private MultiPoint multiPoint;

    @Column
    @Enumerated
    private TrackStatus trackStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public MultiPoint getMultiPoint() {
        return multiPoint;
    }

    public void setMultiPoint(MultiPoint multiPoint) {
        this.multiPoint = multiPoint;
    }

    public TrackStatus getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(TrackStatus trackStatus) {
        this.trackStatus = trackStatus;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
