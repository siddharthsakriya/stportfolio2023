package uk.ac.ed.inf.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.ed.inf.model.Delivery;
import uk.ac.ed.inf.model.Move;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {
    /**
     * Serialises the given moveList to the given outputFile
     * @param moveList list of moves
     * @param outputFile output file
     */
    public void writeMoveJson(List<Move> moveList, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Move>>() {}).withDefaultPrettyPrinter();
            writer.writeValue(outputFile, moveList);
        } catch (IOException e) {
            System.err.println("Error writing moves to file");
        }
    }

    /**
     * Serialises the given deliveryList to the given outputFile
     * @param deliveries list of deliveries
     * @param outputFile output file
     */
    public void writeOrderJson(List<Delivery> deliveries, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writerFor(new TypeReference<List<Delivery>>() {}).withDefaultPrettyPrinter();
            writer.writeValue(outputFile, deliveries);
        } catch (IOException e) {
            System.err.println("Error writing orders to file");
        }
    }

    /**
     * Serialises the given moveList to a GeoJSON file
     * @param movesList list of moves
     * @param file output file
     */
    public void writeMoveGeoJson(List<Move> movesList, File file){
        JSONObject featureCollections = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONArray features = new JSONArray();
        JSONObject feature = new JSONObject();
        JSONObject geometry = new JSONObject();
        featureCollections.put("type", "FeatureCollection");
        featureCollections.put("features", features);
        features.put(feature);
        feature.put("type", "Feature");
        feature.put("properties", properties);
        feature.put("geometry", geometry);
        geometry.put("type", "LineString");
        geometry.put("coordinates", populateCoordinates(movesList));
        GeoJsonWriter(file, featureCollections.toString());
    }

    /**
     * Writes the given geoJsonString into a GeoJSON file
     * @param geoJsonString large JSON string that contains the GeoJSON
     * @param file output file
     */
    public void GeoJsonWriter(File file, String geoJsonString){
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(geoJsonString);
        } catch (IOException e) {
            System.err.println("Error writing GeoJson to file");
        }
    }

    /**
     * Creates a JSONArray of the given longitude and latitude
     * @param longitude
     * @param latitude
     * @return JSONArray of coordinates
     */
    private JSONArray createCoordinate(double longitude, double latitude) {
        JSONArray coordinate = new JSONArray();
        coordinate.put(longitude);
        coordinate.put(latitude);
        return coordinate;
    }

    /**
     * Creates a JSONArray of the given movesList
     * @param movesList list of moves
     * @return JSONArray of coordinates
     */
    public JSONArray populateCoordinates(List<Move> movesList){
        JSONArray coordinates = new JSONArray();
        for(Move move: movesList){
            coordinates.put(createCoordinate(move.getFromLongitude(), move.getFromLatitude()));
        }
        return coordinates;
    }
}
