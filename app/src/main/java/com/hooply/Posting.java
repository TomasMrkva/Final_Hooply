package com.hooply;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Posting extends AppCompatActivity {

    public View v;
    public TextView comment1;
    public TextView comment2;
    public TextView comment3;
    public TextView[] commentBoxes;
    public TextView displayBox;
    public int commentIndex = 0;
    public EditText commentinput;
    public int postIndex = 0;
    ImageView imagebox;
    public Post currentpost;
    List<Post> allposts;
    List<Comments> allcomments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postingactivity);

        allposts = ExternalDb.getPosts(3);
        commentinput = findViewById(R.id.owncomment);
        allcomments = new ArrayList<Comments>();
        displayBox = (TextView) findViewById(R.id.uniqueid);
        imagebox = (ImageView) findViewById(R.id.imagestuff);
        commentBoxes= new TextView[]{(TextView)findViewById(R.id.comment1), (TextView) findViewById(R.id.comment2), (TextView) findViewById(R.id.comment2)};

        this.setPost(allposts.get(postIndex));

        Button prev = (Button) findViewById(R.id.prevcomment);
        prev.setEnabled(false);

        Button nextcomment = (Button) findViewById(R.id.nextcomments);
        this.setPost(allposts.get(postIndex));
    }

    public void updateComments() {
        Comments[] showing = new Comments[3];
        int normalindex = 0;
        for (int i = commentIndex; i < commentIndex + 3; i++) {
            try {
                showing[normalindex] = allcomments.get(i);
                normalindex++;
            } catch (Exception e) {
                break;
            }
        }
        this.setComments(showing);
    }

    public void buttonHandler(View view) {
        ((Button) findViewById(R.id.prevcomment)).setEnabled(true);

        if(this.commentIndex + 6 >= this.allcomments.size()){
            ((Button) findViewById(R.id.nextcomments)).setEnabled(false);
        }

        this.commentIndex = this.commentIndex + 3;
        updateComments();
    }

    public void prevHandler(View view) {
        ((Button) findViewById(R.id.nextcomments)).setEnabled(true);

        if(this.commentIndex <= 3){
            ((Button) findViewById(R.id.prevcomment)).setEnabled(false);
        }

        this.commentIndex = this.commentIndex - 3;
        updateComments();
    }

    public void setPost(Post post){


        Pattern pattern = Pattern.compile("(@IMG\\[.*\\])");
        Matcher matcher = pattern.matcher(post.getContent());
        if(matcher.find())
        {
            int startindex = matcher.start();
            int endindex = matcher.end();
            String base64 = post.getContent().substring(startindex +5,endindex);
            Bitmap imagine = Parser.convert64toImg(base64);
            imagebox.setImageBitmap(imagine);
            allcomments = post.getAllComments();
            Comments[] showing = new Comments[3];
            for(int i = commentIndex; i < commentIndex +3; i++){
                showing[i] = allcomments.get(i);
            }
            this.setComments(showing);
        }
        else{
            displayBox.setText(post.getContent());
            displayBox.setText(post.getContent());
            allcomments = post.getAllComments();
            Comments[] showing = new Comments[3];

            for(int i = commentIndex; i < commentIndex +3; i++){
                showing[i] = allcomments.get(i);

            }
            Log.d("thepost",post.getContent());
            Log.d("firstcomm",showing[0].getContent());
            this.setComments(showing);


        }
        currentpost = post;

    }
    public void setComments(Comments[] comments){
        TextView comment = (TextView) findViewById(R.id.comment1);
        TextView comment2 = (TextView) findViewById(R.id.comment2);
        TextView comment3 = (TextView) findViewById(R.id.comment3);
        comment.setText(comments[0].getContent());
        comment2.setText(comments[1].getContent());
        comment3.setText(comments[2].getContent());


    }

    public void setFirst(Comments comments){
        TextView comment = (TextView) findViewById(R.id.comment1);
        comment.setText(comments.getContent());
        allcomments.add(0, comments);
    }

    public void addComment(View view) {
        String input = commentinput.getText().toString();
        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new Timestamp(time);
        String ts =  tsTemp.toString();
        Comments comment = new Comments(GlobalVar.userid,currentpost.getId(),input,ts);
        ExternalDb.insertComment(comment);
        this.setFirst(comment);
    }
}