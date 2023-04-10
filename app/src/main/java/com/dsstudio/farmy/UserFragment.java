package com.dsstudio.farmy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAuth auth;
    Button button;
    TextView textView, txtUsername, txtEmail, txtPhone;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://plant-diseases-classifier-default-rtdb.firebaseio.com/");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://plant-diseases-classifier.appspot.com/");

    Button txtUpdateUserDetails, txtLogOut;
    RoundedImageView imgProfile;
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 22;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        txtUpdateUserDetails = v.findViewById(R.id.updateUserDetails);
        txtLogOut = v.findViewById(R.id.logOut);
        imgProfile = v.findViewById(R.id.imgUserProfile);
        txtUsername = v.findViewById(R.id.txtUsername);
        txtEmail = v.findViewById(R.id.txtEmail);
        txtPhone = v.findViewById(R.id.txtPhone);

        txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity()).setTitle("User Account Details").setMessage("Username cannot be changed").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        SessionManager sessionManager = new SessionManager(getActivity());
        String sessionManagerUsername = sessionManager.getUsername();
        txtUsername.setText(sessionManager.getUsername());
        txtEmail.setText(sessionManager.getEmail());
        txtPhone.setText(sessionManager.getPhone());

        databaseReference.child("Users").child(sessionManagerUsername).child("filepath").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String filepath = dataSnapshot.getValue(String.class);
                if (filepath != null) {
                    Picasso.get().load(filepath).into(imgProfile);
                }
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        txtUpdateUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserDetails();
            }
        });

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getActivity());
                sessionManager.logout();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private void UpdateUserDetails() {
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();

        if (email.isEmpty() || phone.isEmpty()) {
            txtEmail.setError("Please enter your email");
            txtPhone.setError("Please enter your phone number");
        } else {
            SessionManager sessionManager = new SessionManager(getActivity());
            String sessionManagerUsername = sessionManager.getUsername();
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(sessionManagerUsername)) {
                        databaseReference.child("Users").child(sessionManagerUsername).child("email").setValue(email);
                        databaseReference.child("Users").child(sessionManagerUsername).child("phone").setValue(phone);

                        sessionManager.setEMAIL(email);
                        sessionManager.setPHONE(phone);
                        Toast.makeText(getActivity(), "Saved Changes", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void UploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SessionManager sessionManager = new SessionManager(getActivity());
                            String sessionManagerUsername = sessionManager.getUsername();
                            databaseReference.child("Users").child(sessionManagerUsername).child("filepath").setValue(uri.toString());

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Saved Changes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imgProfile.setImageURI(filePath);

            UploadImage();
        }
    }
}