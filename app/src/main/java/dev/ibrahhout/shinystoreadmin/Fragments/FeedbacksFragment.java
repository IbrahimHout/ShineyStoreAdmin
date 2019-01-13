package dev.ibrahhout.shinystoreadmin.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.ibrahhout.shinystoreadmin.Adapters.FeedbacksAdapter;
import dev.ibrahhout.shinystoreadmin.Models.FeedbackModel;
import dev.ibrahhout.shinystoreadmin.R;
import dev.ibrahhout.shinystoreadmin.Utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbacksFragment extends Fragment {

    ArrayList<FeedbackModel> models;
    FeedbacksAdapter adapter;


    @BindView(R.id.progressFeedback)
    ProgressBar progressFeedback;
    @BindView(R.id.feedbackRecyvlerView)
    RecyclerView feedbackRecyvlerView;
    Unbinder unbinder;


    public FeedbacksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedbacks, container, false);
        unbinder = ButterKnife.bind(this, view);

        models = new ArrayList<>();
        adapter = new FeedbacksAdapter(getContext(), models);

        feedbackRecyvlerView.setAdapter(adapter);
        feedbackRecyvlerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        FirebaseDatabase.getInstance().getReference().child(Constants.FEEDBACK_NODE).keepSynced(true);
        // Inflate the layout for this fragment
        FirebaseDatabase.getInstance().getReference().child(Constants.FEEDBACK_NODE)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        models.add(dataSnapshot.getValue(FeedbackModel.class));
                        adapter.notifyDataSetChanged();
                        if (progressFeedback != null) {

                            progressFeedback.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
