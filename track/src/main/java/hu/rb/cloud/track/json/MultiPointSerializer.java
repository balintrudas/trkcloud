package hu.rb.cloud.track.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPoint;

@JsonComponent
public class MultiPointSerializer extends JsonSerializer<MultiPoint> {

    @Override
    public void serialize(
            MultiPoint value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        List<Coordinate> coordinates = Arrays.asList(value.getCoordinates());
        jgen.writeStartArray();
        for (Coordinate coordinate : coordinates) {
            jgen.writeStartObject();
            jgen.writeNumberField("longitude", coordinate.x);
            jgen.writeNumberField("latitude", coordinate.y);
            jgen.writeNumberField("elevation", coordinate.z);
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}