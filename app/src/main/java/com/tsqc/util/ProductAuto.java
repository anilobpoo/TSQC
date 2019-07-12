package com.tsqc.util;

import com.tsqc.WebUrlMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by someo_000 on 07-04-2016.
 */
public class ProductAuto {

    public ProductAuto(){

    }

    public List<ProductItem> getProductParseJsonWCF(String sName)
    {
        List<ProductItem> ListData = new ArrayList<ProductItem>();
        try {
            String temp=sName.replace(" ", "%20");
           // URL js = new URL("http://13.228.246.1/app/getbyproduct.php?cn="+temp);
            URL js = new URL(WebUrlMethods.url_ProductAuto+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("Products");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                ListData.add(new ProductItem(r.getString("id"),r.getString("name"),r.getString("price")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;

    }
}
