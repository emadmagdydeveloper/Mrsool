package com.appzone.mrsool.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appzone.mrsool.models.QueryModel;
import com.appzone.mrsool.models.UserModel;
import com.appzone.mrsool.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Preferences {

    private static Preferences instance=null;

    private Preferences() {
    }

    public static Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_userData(Context context, UserModel userModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",userData);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data","");
        UserModel userModel = gson.fromJson(user_data,UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",session);
        editor.apply();

    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }

    public void SaveLanguage(Context context , String lang)
    {
        SharedPreferences preferences = context.getSharedPreferences("language",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lang",lang);
        editor.apply();
    }

    public String getLanguage(Context context,String defLanguage)
    {
        SharedPreferences preferences = context.getSharedPreferences("language",Context.MODE_PRIVATE);
        String lang = preferences.getString("lang",defLanguage);
        return lang;
    }

    public void saveLoginFragmentState(Context context,int state)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("state",state);
        editor.apply();
    }
    public int getFragmentState(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        return preferences.getInt("state",0);
    }

    public void saveQuery(Context context, QueryModel queryModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        String gson = preferences.getString("queries","");


        if (gson.isEmpty())
        {
            List<QueryModel> queryModelList = new ArrayList<>();
            queryModelList.add(queryModel);

            String queryListGson = new Gson().toJson(queryModelList);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("queries",queryListGson);
            editor.apply();
            Log.e("list1",queryModelList.size()+"");

        }else
            {
                List<QueryModel> queryModelList = new Gson().fromJson(gson, new TypeToken<List<QueryModel>>(){}.getType());
                if (queryModelList!=null)
                {

                    if (queryModelList.size()>0)
                    {
                        Log.e("list2",queryModelList.size()+"");

                        if (!isQueryIn(queryModel,queryModelList))
                        {
                            if (queryModelList.size()<10)
                            {
                                queryModelList.add(queryModel);
                            }else
                            {
                                queryModelList.set(0,queryModel);

                            }

                            Log.e("list3",queryModelList.size()+"");

                            String queryListGson = new Gson().toJson(queryModelList);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("queries",queryListGson);
                            editor.apply();
                        }

                    }







                }

            }

    }

    public List<QueryModel> getAllQueries(Context context)
    {
        List<QueryModel> queryModelList = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        String gson = preferences.getString("queries","");

        if (!gson.isEmpty())
        {
            List<QueryModel> queryModelList2 = new Gson().fromJson(gson, new TypeToken<List<QueryModel>>(){}.getType());

            queryModelList.addAll(queryModelList2);
        }

        return queryModelList;

    }

    private boolean isQueryIn(QueryModel queryModel,List<QueryModel> queryModelList)
    {
        boolean isIn = false;
        for (QueryModel queryModel1 :queryModelList)
        {
            if (queryModel1.getKeyword().equals(queryModel.getKeyword()))
            {
                isIn = true;
            }
        }

        return isIn;
    }
    public void ClearPreference(Context context)
    {
        SharedPreferences preferences1 = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.apply();

        SharedPreferences preferences2 = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();

        SharedPreferences preferences3 = context.getSharedPreferences("fragment_state",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.clear();
        editor3.apply();

        SharedPreferences preferences4 = context.getSharedPreferences("language",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor4 = preferences4.edit();
        editor4.clear();
        editor4.apply();

        SharedPreferences preferences5 = context.getSharedPreferences("search",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor5 = preferences5.edit();
        editor5.clear();
        editor5.apply();

    }
}
