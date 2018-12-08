package team25.conveniencestore.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>();

    public void setText(String input) {
        text.setValue(input);
    }

    public LiveData<String> getText() {
        return text;
    }
}
