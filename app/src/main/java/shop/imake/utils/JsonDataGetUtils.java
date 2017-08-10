package shop.imake.utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import shop.imake.model.TelephonePayNum;

/**
 * 获取本地Json数据工具
 */

public class JsonDataGetUtils {

    public static List<TelephonePayNum> getTelPayNumList(String json) {
        List<TelephonePayNum> dataList = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < json.length(); i++) {
                String num = (String) jsonArray.get(i);
//                TelephonePayNum model=new TelephonePayNum(num);
//                dataList.add(model);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dataList;
    }

}
