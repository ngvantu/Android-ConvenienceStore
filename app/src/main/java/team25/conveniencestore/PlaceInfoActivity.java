package team25.conveniencestore;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceInfoActivity extends Activity{

    private TextView place_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        Bundle bundle = getIntent().getExtras();

        place_ID = findViewById(R.id.txtPlaceTittle);

        String placeID = bundle.getString("PLACE_ID");
        if(placeID != null) {
            place_ID.setText(placeID);
        }

    }
}
