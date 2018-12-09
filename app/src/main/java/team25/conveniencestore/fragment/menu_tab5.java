package team25.conveniencestore.fragment;

import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import team25.conveniencestore.R;

public class menu_tab5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tab5);


       // initview();
    }
    /*
    private void initview() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setMessage("Convinience Map" +
                "Bản đồ cửa hàng tiện lợi" +
                "* Phiên bản: 1.0.0" +
                "* Tác giả: Tú Nguyễn, Nghĩa Nguyễn, Tiến Nguyễn, Mập Nguyễn, Đức Tài" +
                "- DH Khoa Học Tự Nhiên, ĐHQG Tp. Hồ Chí Minh" +
                "* Ứng dụng được phát triển nhằm đáp ứng như cầu tìm kiếm của hàng tiện lợi nhanh chóng hơn, hiệu quả hơn");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        alertDialog.show();

    }
*/
}
