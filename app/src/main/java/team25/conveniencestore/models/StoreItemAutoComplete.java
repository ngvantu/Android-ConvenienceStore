package team25.conveniencestore.models;

public class StoreItemAutoComplete {

    private String storeName;
    private int logoImage;

    public StoreItemAutoComplete(String name, int logoImage) {
        this.storeName = name;
        this.logoImage = logoImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getLogoImage() {
        return logoImage;
    }
}
