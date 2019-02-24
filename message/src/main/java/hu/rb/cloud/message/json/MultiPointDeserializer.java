package hu.rb.cloud.message.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonComponent
public class MultiPointDeserializer extends JsonDeserializer<MultiPoint> {

    @Override
    public MultiPoint deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode rootNode = oc.readTree(jp);
        Iterator<JsonNode> elements = rootNode.elements();
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> lineCoordinates = new ArrayList<>();
        while(elements.hasNext()){
            JsonNode item = elements.next();
            lineCoordinates.add(new Coordinate(item.get("longitude").asDouble(), item.get("latitude").asDouble(),
                    item.get("elevation").asDouble()));
        }
        MultiPoint multiPoint = gf.createMultiPoint(
                lineCoordinates.toArray(new Coordinate[lineCoordinates.size()]));
        return multiPoint;
    }
}
