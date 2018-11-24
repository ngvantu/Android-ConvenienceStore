package team25.conveniencestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import team25.conveniencestore.models.Store;

public class CustomListStore extends AppCompatActivity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result_stores);

    //lv = (ListView) findViewById(R.id.ListViewStore);

        ArrayList<Store> ArrayStore = new ArrayList<Store>();
        ArrayStore.add(new Store("Circle K Bui Vien", Float.valueOf("2"), "135B Tran Hung Dao, Quan 1"));
        ArrayStore.add(new Store("Circle K Bui Vien1",Float.valueOf("3.5"), "135B Tran Hung Dao, Quan 1"));
        ArrayStore.add(new Store("Circle K Bui Vien2",Float.valueOf("4"), "135B Tran Hung Dao, Quan 1"));
        ArrayStore.add(new Store("Circle K Bui Vien3",Float.valueOf("3.8"), "135B Tran Hung Dao, Quan 1"));
        ArrayStore.add(new Store("Circle K Bui Vien4",Float.valueOf("4.3"), "135B Tran Hung Dao, Quan 1"));

    }
}
