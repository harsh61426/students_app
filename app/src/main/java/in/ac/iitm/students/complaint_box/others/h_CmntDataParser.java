package in.ac.iitm.students.complaint_box.others;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import in.ac.iitm.students.complaint_box.objects.CommentObj;

/**
 * Created by harshitha on 11/7/17.
 */

public class h_CmntDataParser {

    Context context;
    private InputStream stream;
    private ArrayList<CommentObj> commentArray;

    public h_CmntDataParser(String string, Context c) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        commentArray = new ArrayList<>();
        context = c;
    }

    public ArrayList<CommentObj> pleaseParseMyData() throws IOException {

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.setLenient(true);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return readCommentsArray(reader);
        } finally {

            reader.close();

        }

    }

    public ArrayList<CommentObj> readCommentsArray(JsonReader reader) throws IOException {
        ArrayList<CommentObj> comments = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            comments.add(readComment(reader));
        }
        reader.endArray();
        return comments;

    }

    public CommentObj readComment(JsonReader reader) throws IOException {

        CommentObj hCommentObj = new CommentObj();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.e("name",name);
            if (name.equals("name")) {
                hCommentObj.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                hCommentObj.setRollNo(reader.nextString());
            } else if (name.equals("roomno")) {
                hCommentObj.setRoomNo(reader.nextString());
            } else if (name.equals("hostel")) {
                hCommentObj.setRoomNo(reader.nextString());
            } else if (name.equals("comment")) {
                hCommentObj.setCommentStr(reader.nextString());
            } else if (name.equals("datetime")) {
                hCommentObj.setDate(reader.nextString());
            } else if (name.equals("status")) {
                reader.nextString();
            } else if (name.equals("error")) {
                reader.nextString();
                //Log.e("reader",reader+"");
                reader.endObject();
                return CommentObj.getErrorCommentObject();

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return hCommentObj;
    }

}
