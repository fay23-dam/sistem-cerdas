package com.example.testing_xc_001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testing_xc_001.ml.XCC;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class deteksi extends AppCompatActivity {
    ImageView imageView;
    ImageButton button1, button2;
    int imageSize = 128;
    TextView confidence, solusi, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deteksi);

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });

        result = findViewById(R.id.hasilprediksi);
        confidence = findViewById(R.id.hasilklasifikasi);
        solusi = findViewById(R.id.solusi);
        imageView = findViewById(R.id.imageView);
        button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{"android.permission.CAMERA"}, 100);
                }
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
            XCC model = XCC.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * 3 * 4);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; ++i) {
                for (int j = 0; j < imageSize; ++j) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * 0.003921569f);
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * 0.003921569f);
                    byteBuffer.putFloat((val & 0xFF) * 0.003921569f);
                }
            }

            inputFeature0.loadBuffer(byteBuffer);
            XCC.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0.0f;
            for (int i = 0; i < confidences.length; ++i) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Bacterial_spot", "Septoria_leaf_spot", "Tomato_Yellow_Leaf_Curl_Virus"};

            result.setText(classes[maxPos]);
            updateSolution(classes[maxPos]);

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; ++i) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100.0f));
            }
            confidence.setText(s.toString());

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSolution(String disease) {
        final String more = "Selengkapnya";
        final String solutionText;
        final String explanationTitle;
        final String explanationMessage;

        switch (disease) {
            case "Bacterial_spot":
                explanationTitle = "Bacterial Spot";
                explanationMessage = "Bacterial spot can be managed by ensuring proper plant spacing, avoiding overhead watering, and using copper-based fungicides.";
                solutionText = "1. Ensure proper plant spacing.\n2. Avoid overhead watering.\n3. Use copper-based fungicides.\n";
                break;
            case "Septoria_leaf_spot":
                explanationTitle = "Septoria Leaf Spot";
                explanationMessage = "Manage Septoria leaf spot by removing infected leaves, applying fungicides, and practicing crop rotation.";
                solutionText = "1. Remove infected leaves.\n2. Apply fungicides.\n3. Practice crop rotation.\n";
                break;
            case "Tomato_Yellow_Leaf_Curl_Virus":
                explanationTitle = "Tomato Yellow Leaf Curl Virus";
                explanationMessage = "Control Tomato Yellow Leaf Curl Virus by managing whiteflies, using resistant varieties, and applying insecticides.";
                solutionText = "1. Manage whiteflies.\n2. Use resistant varieties.\n3. Apply insecticides.\n";
                break;
            default:
                explanationTitle = "";
                explanationMessage = "";
                solutionText = "";
                break;
        }

        SpannableString content = new SpannableString(solutionText + more);

        content.setSpan(new UnderlineSpan(), solutionText.length(), content.length(), 0);
        content.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showExplanationDialog(explanationTitle, explanationMessage);
            }
        }, solutionText.length(), content.length(), 0);

        solusi.setText(content);
        solusi.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showExplanationDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            } else {
                // Permission denied
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Camera permission is required to take pictures.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3 && data != null) { // Handle camera result
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                Bitmap image2 = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image2);
                classifyImage(Bitmap.createScaledBitmap(image2, imageSize, imageSize, false));
            } else if (requestCode == 1 && data != null) { // Handle gallery result
                Uri dat = data.getData();
                if (dat != null) {
                    try {
                        Bitmap image3 = MediaStore.Images.Media.getBitmap(getContentResolver(), dat);
                        imageView.setImageBitmap(image3);
                        classifyImage(Bitmap.createScaledBitmap(image3, imageSize, imageSize, false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
