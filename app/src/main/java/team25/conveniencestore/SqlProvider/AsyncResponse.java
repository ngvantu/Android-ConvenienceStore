package team25.conveniencestore.SqlProvider;
import java.util.List;

import team25.conveniencestore.models.GooglePlace;

public interface AsyncResponse {
    void processFinish( List<GooglePlace> output);
}