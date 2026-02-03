package sig;

import model.RN;
import java.io.FileWriter;
import java.util.List;

public class GeoJsonWriter {

    public static void write(List<RN> rns, String file) throws Exception {
        FileWriter fw = new FileWriter(file);

        fw.write("{\"type\":\"FeatureCollection\",\"features\":[");

        for (RN rn : rns) {
            fw.write("{\"type\":\"Feature\",");
            fw.write("\"properties\":{\"nom\":\"" + rn.getNom() + "\"},");
            fw.write("\"geometry\":" + rn.getGeoJson());
            fw.write("},");
        }

        fw.write("]}");
        fw.close();
    }
}
