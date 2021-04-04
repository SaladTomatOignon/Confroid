package fr.uge.confroid.web;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.uge.confroid.web.dto.CryptedConfroidPackage;

public class JsonStringsRequest extends JsonRequest<List<String>> {

    public JsonStringsRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<List<String>> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }

    @Override
    protected Response<List<String>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            Type lstPkg = new TypeToken<ArrayList<String>>(){}.getType();
            return Response.success(
                    new Gson().fromJson(jsonString, lstPkg), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
