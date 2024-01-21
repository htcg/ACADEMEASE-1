package com.example.project_acadeamease1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity3 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView showHidePasswordButton;
    private EditText boxPassword;
    private boolean isPasswordVisible = false;
    private TextView passwordStrengthTextView;
    private EditText boxEmail;
    private TextView emailValidationTextView;
    private TextView textView;
    private ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextfname, editTextlname, editTextusername, editTextpassword,
            editTextschoolname, editTextyearlevel, editTextbirthday, editTextage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        progressBar = findViewById(R.id.progressBar);
        editTextfname = findViewById(R.id.boxfirstname);
        editTextlname = findViewById(R.id.boxlastname);
        editTextusername = findViewById(R.id.boxusername);
        editTextpassword = findViewById(R.id.boxpassword);
        editTextschoolname = findViewById(R.id.boxschoolname);
        editTextyearlevel = findViewById(R.id.boxyearlevel);
        editTextbirthday = findViewById(R.id.boxBirthDate);
        editTextage = findViewById(R.id.boxage);

        Button signupbutton = findViewById(R.id.signupbut);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textfname = editTextfname.getText().toString();
                String textlname = editTextlname.getText().toString();
                String textusername = editTextusername.getText().toString();
                String textpassword = editTextpassword.getText().toString();
                String textschoolname = editTextschoolname.getText().toString();
                String textyearlevel = editTextyearlevel.getText().toString();
                String textbirthday = editTextbirthday.getText().toString();
                String textage = editTextage.getText().toString();

                if (TextUtils.isEmpty(textfname) || TextUtils.isEmpty(textlname) ||
                        TextUtils.isEmpty(textusername) || TextUtils.isEmpty(textpassword) ||
                        TextUtils.isEmpty(textschoolname) || TextUtils.isEmpty(textyearlevel)) {
                    Toast.makeText(MainActivity3.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textfname, textlname, textusername, textpassword, textschoolname, textyearlevel, textbirthday, textage);
                }
            }
        });

        textView = findViewById(R.id.loginstead);
        showHidePasswordButton = findViewById(R.id.showHidePasswordButton);
        updateShowHideButtonText();

        showHidePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        boxPassword = findViewById(R.id.boxpassword);
        passwordStrengthTextView = findViewById(R.id.passcheck);

        boxPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updatePasswordVisibility();
                checkPasswordStrength(editable.toString());
            }
        });

        boxEmail = findViewById(R.id.boxusername);
        emailValidationTextView = findViewById(R.id.emailvalidation);

        boxEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEmailValidity(editable.toString());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        EditText boxBirthdDate = findViewById(R.id.boxBirthDate);
        boxBirthdDate.setInputType(InputType.TYPE_NULL);

        ImageView calendarIcon = findViewById(R.id.calendarIcon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        textView = findViewById(R.id.loginstead);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
                Toast.makeText(MainActivity3.this, "Go to Log In", Toast.LENGTH_SHORT).show();
            }
        });

        TextView underlineTextView = findViewById(R.id.loginstead);
        String text = "Already have an account? Log In";
        SpannableString ss = new SpannableString(text);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ss.setSpan(underlineSpan, 25, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        underlineTextView.setText(ss);

        textView = findViewById(R.id.upload_Photo);
        profileImageView = findViewById(R.id.imageViewprofile);

        String text1 = "UPLOAD PROFILE";
        SpannableString ss1 = new SpannableString(text1);
        UnderlineSpan underlineSpan1 = new UnderlineSpan();
        ss1.setSpan(underlineSpan1, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startUploadActivity();
            }
        };

        ss1.setSpan(clickableSpan, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#113946")), 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(ss1);
    }

    private void registerUser(String textfname, String textlname, String textusername, String textpassword, String textschoolname, String textyearlevel, String textbirthday, String textage) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textusername, textpassword)
                .addOnCompleteListener(MainActivity3.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(textfname)
                                    .build();

                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> displayNameTask) {
                                            if (displayNameTask.isSuccessful()) {
                                                firebaseUser.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> emailVerificationTask) {
                                                                if (emailVerificationTask.isSuccessful()) {
                                                                    Toast.makeText(MainActivity3.this, "User registered successfully", Toast.LENGTH_LONG).show();

                                                                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(MainActivity3.this, "Email verification failed: " + emailVerificationTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    progressBar.setVisibility(View.GONE);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(MainActivity3.this, "Display name setting failed: " + displayNameTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity3.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        updatePasswordVisibility();
        updateShowHideButtonText();
    }

    private void updateShowHideButtonText() {
        String buttonText = isPasswordVisible ? "HIDE" : "SHOW";
        showHidePasswordButton.setText(buttonText);
    }

    private void updatePasswordVisibility() {
        int cursorPosition = boxPassword.getSelectionEnd();
        if (isPasswordVisible) {
            boxPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            boxPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        boxPassword.setSelection(cursorPosition);
    }

    private void checkPasswordStrength(String password) {
        String strongPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (password.isEmpty()) {
            passwordStrengthTextView.setText("");
        } else if (password.matches(strongPattern)) {
            passwordStrengthTextView.setText("Strong Password");
        } else {
            passwordStrengthTextView.setText("Weak Password");
        }
    }

    private void checkEmailValidity(String email) {
        String emailPattern = "^.+@.+\\..+$";

        if (email.isEmpty()) {
            emailValidationTextView.setText("");
        } else if (Pattern.matches(emailPattern, email)) {
            emailValidationTextView.setText("Valid Email");
        } else {
            emailValidationTextView.setText("Not a valid Email");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.YEAR, year);
        selectedDate.set(Calendar.MONTH, month);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int age = calculateAge(selectedDate);

        EditText boxAge = findViewById(R.id.boxage);
        boxAge.setText(String.valueOf(age));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formattedDate = dateFormat.format(selectedDate.getTime());

        TextView phbday = findViewById(R.id.phbday);
        phbday.setText(formattedDate);
    }

    private int calculateAge(Calendar selectedDate) {
        Calendar currentDate = Calendar.getInstance();
        int age = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR);
        if (currentDate.get(Calendar.DAY_OF_YEAR) < selectedDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private void startUploadActivity() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            setCircularImage(selectedImageUri);

            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);


        // Create a shader to paint with a circular pattern
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // Draw a circle on the canvas with the bitmap shader
        canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);

        return output;
    }

    // Method to set the image in circular shape
    private void setCircularImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                // Convert the image URI to a Bitmap
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                // Create a circular bitmap using the original bitmap
                Bitmap circularBitmap = getRoundedBitmap(originalBitmap);

                // Set the circular bitmap to the ImageView
                profileImageView.setImageBitmap(circularBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error handling image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Selected image URI is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonCLick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
