package team25.conveniencestore.SqlProvider;
import java.util.List;

public interface AsyncResponse {
    void processFinish( List<FavoritePlaces> output);
}