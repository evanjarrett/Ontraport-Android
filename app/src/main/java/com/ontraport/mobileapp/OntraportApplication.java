package com.ontraport.mobileapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.mobileapp.main.collection.asynctasks.GetInfoAsyncTask;
import com.ontraport.mobileapp.main.record.RecordAdapter;
import com.ontraport.mobileapp.main.record.asynctasks.CreateAsyncTask;
import com.ontraport.mobileapp.main.record.asynctasks.GetOneAsyncTask;
import com.ontraport.mobileapp.sdk.http.CustomObjectResponse;
import com.ontraport.mobileapp.sdk.http.OkClient;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class OntraportApplication extends Application {

    private static OntraportApplication instance;

    private Ontraport ontraport_api;
    private OkClient client;
    private Meta meta;
    private CustomObjectResponse custom_objects;

    public OntraportApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Ontraport getApi() {
        return ontraport_api;
    }

    public OkClient getClient() {
        return client;
    }

    public Ontraport createApi(String api_id, String api_key) {
        client = new OkClient(getCacheDir());
        ontraport_api = new Ontraport(api_id, api_key, client);
        return ontraport_api;
    }

    public void setApi(Ontraport ontraport_api) {
        this.ontraport_api = ontraport_api;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setCustomObjects(CustomObjectResponse custom_objects) {
        this.custom_objects = custom_objects;
    }

    public CustomObjectResponse getCustomObjects() {
        return custom_objects;
    }

    public Meta.Data getMetaData(int object_id) {
        return meta.getData().get(Integer.toString(object_id));
    }

    public static OntraportApplication getInstance() {
        return instance;
    }

    public void getCollection(CollectionAdapter adapter, RequestParams params) {
        getCollection(adapter, params, false);
    }

    public void getCollection(CollectionAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new GetInfoAsyncTask(adapter, params, force).execute(params);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params) {
        getRecord(adapter, params, false);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new GetOneAsyncTask(adapter, params.getAsInt(Constants.OBJECT_TYPE_ID)).execute(params);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params) {
        createRecord(adapter, params, false);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new CreateAsyncTask(adapter, params.getAsInt(Constants.OBJECT_TYPE_ID)).execute(params);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
