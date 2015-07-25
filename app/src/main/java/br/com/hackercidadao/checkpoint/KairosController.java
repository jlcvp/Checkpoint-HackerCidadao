package br.com.hackercidadao.checkpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Leonardo on 25/07/2015.
 */
public class KairosController {
    Kairos kairos = new Kairos();
    private static String app_id = "20f5a1bf";
    private static String api_key = "e25e05b627001a0f468d667083c60cb5";
    private static String galery_id = "campus1";
    private static KairosController ourInstance;
    private static KairosListener listener;



    public static KairosController getInstance(Context ctx) {
        return new KairosController(ctx);
    }

    private KairosController(Context ctx) {
        kairos.setAuthentication(ctx, app_id, api_key);
        listener = new KairosListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("HUE","Sucesso Kairos\n"+s);

            }

            @Override
            public void onFail(String s) {
                Log.i("HUE","FAIL\n"+s);
            }
        };
    }


    public void detect(Bitmap bm)
    {

        try {
            kairos.recognize(bm, galery_id,null,null,null,null,listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
