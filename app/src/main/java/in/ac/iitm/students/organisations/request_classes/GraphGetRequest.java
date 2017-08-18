package in.ac.iitm.students.organisations.request_classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import in.ac.iitm.students.organisations.activities.main.PostActivity;
import in.ac.iitm.students.organisations.adapters.PostApapter;
import in.ac.iitm.students.organisations.object_items.Posts;
import in.ac.iitm.students.others.MySingleton;


/**
 * Created by rohithram on 28/6/17.
 */

public class GraphGetRequest  {

    String url_main = "https://graph.facebook.com/v2.10/";

    public Void dorequest(final Context context, final String key, String url, Bundle params, final ArrayList<Posts> postList, final ProgressDialog pd, final String reaction_url)  {

        JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                (Request.Method.GET,url+key, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            final JSONObject jsonresponse = response;
                            final JSONArray postsjson = jsonresponse.getJSONArray("data");
                            for (int i = 0; i < postsjson.length(); i++) {
                                JSONObject postjs = postsjson.getJSONObject(i);
                                if(postjs.has("id")) {

                                    final Posts post = new Posts(postjs.getString("id"));

                                    if (postjs.has("message")) {
                                        post.message = postjs.getString("message");
                                    }
                                    else if(postjs.has("story")){
                                        post.message = postjs.getString("story");
                                    }
                                    if(PostActivity.isLitsoc || PostActivity.isTechsoc){
                                        post.created_time = postjs.getString("updated_time");
                                    }
                                    else {
                                        post.created_time = postjs.getString("created_time");
                                    }
                                    String url_2 = ",type,source,full_picture,attachments,reactions.summary(true){total_count}";
                                    String target_url = url_main+post.id + "/?fields=" + reaction_url +url_2+"&access_token="+key;
                                    getPostattachments(target_url,context,post,postList,pd,postsjson,i);

                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }finally {

                        }
                    }
                }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });

                    // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(context).addToRequestQueue(jsObjRequest1);

        return null;
    }

    private Void getPostattachments(String url1, Context context, final Posts post, final List<Posts> postList, final ProgressDialog  pd, final JSONArray postsjson , final int finalI1){

        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET,url1, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject jsonresponse2 = response;
                            post.type = jsonresponse2.getString("type");
                            if (jsonresponse2.has("full_picture")) {
                                post.img_url = jsonresponse2.getString("full_picture");
                            }
                            if (jsonresponse2.getString("type").equals("video")) {
                                post.vid_url = jsonresponse2.getString("source");
                            }
                            if(jsonresponse2.has("attachments")){
                                JSONObject attachments = jsonresponse2.getJSONObject("attachments");
                                JSONArray attach_data = attachments.getJSONArray("data");
                                if (attach_data.length() >= 0) {
                                    JSONObject attachmentsjson = attach_data.getJSONObject(0);
                                    if (attachmentsjson.has("subattachments")) {
                                        JSONObject subattachments = attachmentsjson.getJSONObject("subattachments");
                                        if (subattachments.has("data")) {
                                            JSONArray subdata = subattachments.getJSONArray("data");
                                            for (int k = 0; k < subdata.length(); k++) {
                                                JSONObject jsoni = subdata.getJSONObject(k);
                                                if (jsoni.has("media")) {
                                                    JSONObject mediajson = jsoni.getJSONObject("media");
                                                    JSONObject imagejson = mediajson.getJSONObject("image");
                                                    post.sub_imgurls.add(imagejson.getString("src"));
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            JSONObject Likejson = jsonresponse2.getJSONObject("like");
                            if (Likejson.has("summary")) {
                                JSONObject like = Likejson.getJSONObject("summary");
                                post.like_count = like.getInt("total_count");
                            }
                            JSONObject hahajson = jsonresponse2.getJSONObject("haha");
                            if (hahajson.has("summary")) {
                                JSONObject haha = hahajson.getJSONObject("summary");
                                post.haha_count = haha.getInt("total_count");
                            }

                            JSONObject wowjson = jsonresponse2.getJSONObject("wow");
                            if (wowjson.has("summary")) {
                                JSONObject wow = wowjson.getJSONObject("summary");
                                post.wow_count = wow.getInt("total_count");
                            }

                            JSONObject angryjson = jsonresponse2.getJSONObject("angry");
                            if (angryjson.has("summary")) {
                                JSONObject angry = angryjson.getJSONObject("summary");
                                post.angry_count = angry.getInt("total_count");
                            }

                            JSONObject sadjson = jsonresponse2.getJSONObject("sad");
                            if (sadjson.has("summary")) {
                                JSONObject sad = sadjson.getJSONObject("summary");
                                post.sad_count = sad.getInt("total_count");
                            }

                            JSONObject lovejson = jsonresponse2.getJSONObject("love");
                            if (lovejson.has("summary")) {
                                JSONObject love = lovejson.getJSONObject("summary");
                                post.love_count = love.getInt("total_count");
                            }

                            JSONObject reactionsjson = jsonresponse2.getJSONObject("reactions");
                            if (reactionsjson.has("summary")) {
                                JSONObject likes = reactionsjson.getJSONObject("summary");
                                post.count = likes.getInt("total_count");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {

                            Collections.sort(postList, new Comparator<Posts>() {
                                @Override
                                public int compare(Posts o1, Posts o2) {
                                    Date date1 = null,date2=null;
                                    try {
                                        date1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ").parse(o1.created_time);
                                        date2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ").parse(o2.created_time);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (date1 == null || date2 == null)
                                        return 0;
                                    if(date1.equals(date2))
                                        return 0;
                                    else if(date1.before(date2))
                                        return 1;
                                    else
                                        return -1;
                                }
                            });


                            postList.add(post);

                            if(finalI1 == postsjson.length()-1){
                                pd.dismiss();



                                if(PostActivity.isYoutube){
                                    PostActivity.pageadapter.notifyDataSetChanged();
                                }
                                else if(PostActivity.isT5e){
                                    PostActivity.pageadapter2.notifyDataSetChanged();
                                }
                                else if(PostActivity.isLitsoc || PostActivity.isTechsoc){
                                    PostActivity.pageadapter3.notifyDataSetChanged();
                                }
                                else {
                                    PostActivity.pageadapter1.notifyDataSetChanged();
                                }

                            }

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest2);
        return null;
    }

}
