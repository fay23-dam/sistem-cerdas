package com.example.testing_xc_001;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AlertDialog;
public class daftar_penyakit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_penyakit);

        MaterialButton buttonBacterialSpot = findViewById(R.id.button_bacterial_spot);
        buttonBacterialSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExplanationDialog("Bacterial Spot", "Penyakit bakteri yang menyebabkan bercak gelap pada daun, batang, dan buah. Pencegahan melibatkan sanitasi dan penggunaan fungisida.",
                        "Penyakit bakteri pada tanaman seperti Bacterial Spot disebabkan oleh bakteri Xanthomonas campestris. Gejalanya termasuk bercak gelap yang muncul pada daun, batang, dan buah, sering kali menguning di sekitar bercak. Penyebarannya meningkat dalam kondisi cuaca lembab. Pencegahan dan pengendalian melibatkan praktik budidaya yang baik, seperti sanitasi yang baik, pengelolaan kelembaban, dan penggunaan fungisida. Memilih varietas tanaman yang tahan penyakit dan melakukan monitoring serta tindakan pencegahan yang cepat juga sangat penting.");
            }
        });

        MaterialButton buttonSeptoriaLeafSpot = findViewById(R.id.septoria_leaf_spot);
        buttonSeptoriaLeafSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExplanationDialog("Septoria Leaf Spot", "Penyakit jamur yang menyebabkan bercak kecil berwarna cokelat pada daun. Pencegahan melibatkan sanitasi dan penggunaan fungisida.",
                        "Penyakit jamur pada tanaman seperti Septoria Leaf Spot disebabkan oleh jamur Septoria lycopersici. Gejalanya meliputi munculnya bercak kecil berwarna cokelat atau hitam dengan tepi berwarna kekuningan pada daun, yang dapat menyebabkan daun rontok. Penyakit ini menyebar melalui percikan air hujan atau irigasi. Pencegahan dan pengendalian termasuk menjaga sanitasi kebun, menghindari penyiraman dari atas, dan penggunaan fungisida yang sesuai. Tanaman yang terinfeksi sebaiknya dibuang dan dihancurkan untuk mencegah penyebaran lebih lanjut.");
            }
        });

        MaterialButton buttonYellowLeafVirus = findViewById(R.id.yellow_leaf_virus);
        buttonYellowLeafVirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExplanationDialog("Yellow Leaf Virus", "Penyakit virus yang menyebabkan daun menguning dan mengeriting. Pencegahan melibatkan pengendalian vektor dan penggunaan varietas tahan penyakit.",
                        "Penyakit virus pada tanaman seperti Yellow Leaf Virus disebabkan oleh virus yang ditularkan oleh serangga vektor seperti kutu daun. Gejalanya termasuk daun yang menguning, mengeriting, dan kadang-kadang pertumbuhan tanaman terhambat. Penyebaran virus ini dapat dikendalikan dengan mengelola populasi vektor melalui penggunaan insektisida dan praktik pengelolaan hama terpadu. Selain itu, memilih varietas tanaman yang tahan terhadap virus ini dan menjaga sanitasi kebun sangat penting untuk mencegah infeksi.");
            }
        });
    }

    private void showExplanationDialog(String title, String shortMessage, String longMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Buat SpannableStringBuilder untuk menampung teks dengan gaya khusus
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(shortMessage).append("\n\n").append(longMessage);

        // Tambahkan gaya khusus ke teks menggunakan Spannable
        sb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),0, shortMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, shortMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Atur teks yang sudah diberi gaya ke dalam dialog
        builder.setMessage(sb);

        // Tampilkan dialog
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
