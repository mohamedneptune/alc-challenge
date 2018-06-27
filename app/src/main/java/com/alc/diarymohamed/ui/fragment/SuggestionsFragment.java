package com.alc.diarymohamed.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.shared.Constants;

public class SuggestionsFragment extends Fragment implements View.OnClickListener{

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(SuggestionsFragment.class);

    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private Button mSuggestionsSendButton;
    private EditText mSuggestionEditText;
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();

        mView = inflater.inflate(R.layout.fragment_suggestions, container, false);

        mSuggestionsSendButton = (Button) mView.findViewById(R.id.suggestions_send_button);
        mSuggestionEditText = (EditText)  mView.findViewById(R.id.suggestions_edit_text);

        mSuggestionsSendButton.setOnClickListener(this);

        mSuggestionsSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSuggestions();
            }
        });

        return mView;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //SLF_LOGGER.info("onresume");

    }

    @Override
    public void onClick(View v) {

    }

    private void sendSuggestions(){


        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.emailAdmin});
        email.putExtra(Intent.EXTRA_SUBJECT, "Country Prefix suggestions");
        email.putExtra(Intent.EXTRA_TEXT,
                mSuggestionEditText.getText().toString());
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Send Email to admin :"));
    }
}
