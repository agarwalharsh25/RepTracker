package com.reptracker.android.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.reptracker.android.activities.CaptureSignatureActivity;
import com.reptracker.android.activities.HomeActivity;
import com.reptracker.android.databinding.FragmentVisitDoctorBinding;
import com.reptracker.android.models.PendingVisit.Visits;
import com.reptracker.android.models.Visit;
import com.reptracker.android.utilities.DBHandler;
import com.reptracker.android.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class VisitDoctorFragment extends Fragment {

    private static final String TAG = VisitDoctorFragment.class.getSimpleName();

    private String [] permissions = {RECORD_AUDIO, CAMERA,  WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 100;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private FragmentVisitDoctorBinding binding;
    private Utility utility = new Utility();
    private Visits visit;
    private boolean statusSignature = false, statusPicture = false;
    private String picturePath, signaturePath, audioPath;

    private MediaRecorder recorder = null;

    public VisitDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        if (getArguments() != null) {
            visit = getArguments().getParcelable("Visit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVisitDoctorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(VisitDoctorFragment.super.getActivity()).onBackPressed();
            }
        });

        utility.setTextView(binding.doctorName, visit.getDoctorName());
        utility.setTextView(binding.doctorSpeciality, visit.getDoctorSpeciality());
        utility.setTextView(binding.contactNumber, visit.getDoctorContact());

        if (checkPermissions() && getActivity() != null) {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_PERMISSION_CODE);
        }

        binding.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.presentationTitle.setText("Presentation started");
                binding.record.setVisibility(View.GONE);
                binding.pauseResume.setVisibility(View.VISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    binding.pause.setVisibility(View.VISIBLE);
                }
                binding.stop.setVisibility(View.VISIBLE);
                utility.saveVisitTimeStamp(visit.getVisitId(), DBHandler.COLUMN_PRESENTATION_STARTED, getContext());
                startRecording();
            }
        });

        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.presentationTitle.setText("Presentation paused");
                binding.pause.setVisibility(View.GONE);
                binding.resume.setVisibility(View.VISIBLE);
                pauseRecording();
            }
        });

        binding.resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.presentationTitle.setText("Presentation resumed");
                binding.pause.setVisibility(View.VISIBLE);
                binding.resume.setVisibility(View.GONE);
                resumeRecording();
            }
        });

        binding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.presentationBlock.setVisibility(View.GONE);
                utility.saveVisitTimeStamp(visit.getVisitId(), DBHandler.COLUMN_PRESENTATION_FINISHED, getContext());
                stopRecording();

                binding.signatureHeading.setVisibility(View.VISIBLE);
                binding.captureImageButton.setVisibility(View.VISIBLE);
            }
        });

        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility.saveVisitTimeStamp(visit.getVisitId(), DBHandler.COLUMN_VISIT_FINISHED, getContext());

                uploadFile("signatures", signaturePath, ".jpg");
                uploadFile("pictures", picturePath, ".jpg");
                uploadFile("audios", audioPath, ".3gp");
                saveVisitStatus();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        binding.captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusSignature = false;
                statusPicture = false;

                Intent i = new Intent(getContext(), CaptureSignatureActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

    }

    private void startRecording() {
        audioPath = utility.createDirectory("Audios") + "RepTracker_aud_" + System.currentTimeMillis() + ".3gp";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed", e);
            return;
        }

        recorder.start();
    }

    private void pauseRecording() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            recorder.pause();
        }
    }

    private void resumeRecording() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            recorder.resume();
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }


    private void uploadFile(String type, String filePath, String extension) {
        Uri file = Uri.fromFile(new File(filePath));
        UploadTask uploadTask = storage.getReference().child("visits/" + type + "/" + visit.getVisitId() + extension)
                .putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error uploading file", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "File successfully uploaded!: " + taskSnapshot.getMetadata());
            }
        });
    }

    private void saveVisitStatus() {
        DBHandler dbHandler = new DBHandler(getContext());
        Visit visitDb = dbHandler.fetchVisitData(visit.getVisitId());

        EditText notesEditText = binding.notesEditText;

        db.collection("visits").document(visit.getVisitId())
                .update(
                        "status", Utility.VisitStatus.COMPLETED,
                        "notes", notesEditText.getText().toString(),
                        "timestamps.presentationFinished", visitDb.getPresentationFinished(),
                        "timestamps.presentationStarted", visitDb.getPresentationStarted(),
                        "timestamps.reachedHospital", visitDb.getReachedHospital(),
                        "timestamps.visitFinished", visitDb.getVisitFinished(),
                        "timestamps.visitStarted", visitDb.getVisitStarted()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        dbHandler.deleteVisit(visit.getVisitId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK && data.getBooleanExtra("signatureCaptured", false)) {

                signaturePath = data.getStringExtra("signaturePath");
                picturePath = data.getStringExtra("picturePath");

                statusSignature = setImageView(binding.signature, signaturePath, 90);
                statusPicture = setImageView(binding.picture, picturePath, 180);

                if (statusSignature && statusPicture)
                    binding.pictureHeading.setVisibility(View.VISIBLE);
                    binding.finish.setVisibility(View.VISIBLE);
                    binding.notes.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean setImageView(ImageView imageView, String filePath, float angle) {
        File imgFile = new  File(filePath);

        if(imgFile.exists()){
            Picasso.get().load(imgFile).fit().rotate(angle).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (recorder != null) {
            recorder.stop();
            recorder = null;
        }
    }

    private boolean checkPermissions() {
        return getContext() != null
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                + ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionsGranted = false;
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length == 3) {
                    permissionsGranted = (grantResults[0] + grantResults[1] + grantResults[2]) == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }
        if (!permissionsGranted) {
            Log.d(TAG, "Permission not granted");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

}
