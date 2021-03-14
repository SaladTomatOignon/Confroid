package fr.uge.confroid.web;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import fr.uge.confroid.web.dto.CryptedConfroidPackage;

public class JsonConfroidPackageRequest extends JsonRequest<CryptedConfroidPackage> {

    public JsonConfroidPackageRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<CryptedConfroidPackage> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
    }

    @Override
    protected Response<CryptedConfroidPackage> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            return Response.success(
                    new Gson().fromJson(jsonString, CryptedConfroidPackage.class), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
