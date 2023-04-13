package com.dsstudio.farmy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SettingsFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://plant-diseases-classifier-default-rtdb.firebaseio.com/");

    String[] fields = {"Field 1", "Field 2", "Field 3", "Field 4"};
    ArrayList<String> agentsList = new ArrayList<>();

    AutoCompleteTextView autoCompleteSelectField, autoCompleteSelectAgent;
    ArrayAdapter<String> adapterFields, adapterAgents;
    TextInputEditText txtAgentEmail, txtSubject, txtSenderEmail, txtMessage;
    Button btnSend;
    String agEmail;

    @Override
    public void onResume() {
        super.onResume();

        adapterAgents = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item, agentsList);
        autoCompleteSelectAgent.setAdapter(adapterAgents);

        autoCompleteSelectAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String agent = adapterView.getItemAtPosition(i).toString();
                // Toast.makeText(getActivity(), "Agent: " + agent, Toast.LENGTH_SHORT).show();
                databaseReference.child("Agents").child(agent).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Toast.makeText(getActivity(), "Agent: " + snapshot.getValue(), Toast.LENGTH_SHORT).show();
                            // txtAgentEmail.setText(snapshot.getValue().toString());
                            agEmail = snapshot.getValue().toString();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        autoCompleteSelectAgent = v.findViewById(R.id.autoCompleteSelectAgent);
        // txtAgentEmail = v.findViewById(R.id.agentEmail);
        txtSubject = v.findViewById(R.id.subject);
        txtSenderEmail = v.findViewById(R.id.senderEmail);
        txtMessage = v.findViewById(R.id.description);
        btnSend = v.findViewById(R.id.submitQuestion);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String agentEmail = agEmail;
                String subject = txtSubject.getText().toString();
                String senderEmail = txtSenderEmail.getText().toString();
                String message = txtMessage.getText().toString();

                if (subject.isEmpty()) {
                    txtSubject.setError("Subject is required");
                    txtSubject.requestFocus();
                    return;
                }

                if (senderEmail.isEmpty()) {
                    txtSenderEmail.setError("Sender email is required");
                    txtSenderEmail.requestFocus();
                    return;
                }

                if (message.isEmpty()) {
                    txtMessage.setError("Message is required");
                    txtMessage.requestFocus();
                    return;
                }

                try {
                    String stringSenderEmail = senderEmail;
                    String stringReceiverEmail = agentEmail;
                    String stringPasswordSenderEmail = "wrkirpzltloazwyz";

                    String stringHost = "smtp.gmail.com";

                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");

                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                    mimeMessage.setSubject(subject);
                    mimeMessage.setText(message);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                    Toast.makeText(getActivity(), "Your Question has been recorded", Toast.LENGTH_SHORT).show();
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        databaseReference.child("Agents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agentsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String agent = ds.getKey();
                    agentsList.add(agent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}