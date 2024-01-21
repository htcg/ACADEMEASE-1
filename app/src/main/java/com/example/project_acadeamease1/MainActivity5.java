package com.example.project_acadeamease1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Set OnClickListener for "Edit" text under "YEAR LEVEL"
        TextView yearLevelEdit = findViewById(R.id.yrlvledit);
        underlineText(yearLevelEdit);

        // Set OnClickListener for "Edit" text under "SCHOOL NAME"
        TextView schoolNameEdit = findViewById(R.id.schoolnameedit);
        underlineText(schoolNameEdit);

        // Set OnClickListener for "CHANGE PROFILE" text
        TextView changePhotoTextView = findViewById(R.id.change_photo);
        underlineText(changePhotoTextView);

        }


    // Method to underline a TextView
    private void underlineText(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    public void buttonCLick(View view) {
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }
}
