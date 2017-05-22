package shop.imake.utils;

/**
 * Created by Alice
 * 2017/5/13
 */
public class DataHolder {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}
    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}