package in.ac.iitm.students.complaint_box.others;

import android.content.Context;
import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import in.ac.iitm.students.complaint_box.objects.h_CommentObj;

/**
 * Created by harshitha on 11/7/17.
 */

public class h_CmntDataParser {

    Context context;
    private InputStream stream;
    private ArrayList<h_CommentObj> commentArray;

    public h_CmntDataParser(String string, Context c) {
        stream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        commentArray = new ArrayList<>();
        context = c;
    }

    public ArrayList<h_CommentObj> pleaseParseMyData() throws IOException {

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

    public ArrayList<h_CommentObj> readCommentsArray(JsonReader reader) throws IOException {
        ArrayList<h_CommentObj> comments = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            comments.add(readComment(reader));
        }
        reader.endArray();
        return comments;

    }

    public h_CommentObj readComment(JsonReader reader) throws IOException {

        h_CommentObj hCommentObj = new h_CommentObj();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                hCommentObj.setName(reader.nextString());
            } else if (name.equals("rollno")) {
                hCommentObj.setRollNo(reader.nextString());
            } else if (name.equals("roomno")) {
                hCommentObj.setRoomNo(reader.nextString());
            } else if (name.equals("comment")) {
                hCommentObj.setCommentStr(reader.nextString());
            } else if (name.equals("datetime")) {
                hCommentObj.setDate(reader.nextString());
            } else if (name.equals("error")) {
                reader.nextString();
            } else if (name.equals("status")) {
                reader.nextString();
                reader.endObject();
                return h_CommentObj.getErrorCommentObject();

            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return hCommentObj;
    }

}
