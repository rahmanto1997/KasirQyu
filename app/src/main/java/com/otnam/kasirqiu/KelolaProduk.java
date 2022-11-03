package com.otnam.kasirqiu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KelolaProduk extends AppCompatActivity implements MainAdapter.FirebaseDataListener {

    private MainAdapter mAdapter;
    private ArrayList<ModelBarang> daftarBarang;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private ImageView mBtnAdd;
    private EditText mEditName;
    private EditText mEditMerk;
    private EditText mEditHarga;
    private RecyclerView mRecyclerView;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_produk);

        if (Build.VERSION.SDK_INT >=19 && Build.VERSION.SDK_INT < 21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >=19){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >=21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("barang");
        mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarBarang = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelBarang barang = mDataSnapshot.getValue(ModelBarang.class);
                    barang.setKey(mDataSnapshot.getKey());
                    daftarBarang.add(barang);
                }
                mAdapter = new MainAdapter(KelolaProduk.this, daftarBarang);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                Toast.makeText(KelolaProduk.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mBtnAdd = findViewById(R.id.add_product);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTambahBarang();
            }
        });

        mBack=findViewById(R.id.arrow_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KelolaProduk.this, HomeScreen.class));
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win= activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        }else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDataClick(final ModelBarang barang, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBarang(barang);
            }
        });

        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataBarang(barang);
            }
        });

        builder.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogTambahBarang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data Barang");
        View view =getLayoutInflater().inflate(R.layout.edit_barang, null);

        mEditName = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                 String namaBarang= mEditName.getText().toString();
                 String merkBarang= mEditMerk.getText().toString();
                 String hargaBarang= mEditHarga.getText().toString();

                 if (!namaBarang.isEmpty() && !merkBarang.isEmpty() && !hargaBarang.isEmpty()) {
                     submitDataBarang(new ModelBarang(namaBarang, merkBarang, hargaBarang));
                 }else {
                     Toast.makeText(KelolaProduk.this, "Data Harus di isi!", Toast.LENGTH_LONG).show();
                 }
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateBarang(final ModelBarang barang) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Edit Data Barang");
        View view = getLayoutInflater().inflate(R.layout.edit_barang, null);

        mEditName = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);

        mEditName.setText(barang.getNama());
        mEditMerk.setText(barang.getMerk());
        mEditHarga.setText(barang.getHarga());
        builder.setView(view);

        if (barang != null){
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    barang.setNama(mEditName.getText().toString());
                    barang.setMerk(mEditMerk.getText().toString());
                    barang.setHarga(mEditHarga.getText().toString());
                    updateDataBarang(barang);
                }
            });
        }

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void submitDataBarang(ModelBarang barang) {
        mDatabaseReference.child("data_barang").push()
                 .setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(KelolaProduk.this, "Data Berhasil Disimpan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDataBarang(ModelBarang barang) {
        mDatabaseReference .child("data_barang").child(barang.getKey())
                .setValue(barang).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void mVoid) {
                        Toast.makeText(KelolaProduk.this, "Data Berhasil Diupdate", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void hapusDataBarang(ModelBarang barang) {
        if (mDatabaseReference !=null) {
            mDatabaseReference.child("data_barang").child(barang.getKey()).
                    removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                  Toast.makeText(KelolaProduk.this, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}