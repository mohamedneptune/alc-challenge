package com.alc.diarymohamed.ui.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.shared.Globals;
import com.alc.diarymohamed.ui.activity.MainActivity;


/**
 * Created by Med on 21/02/2018.
 */

public class ContactCursorAdapter extends CursorRecyclerViewAdapter<ContactCursorAdapter.ContactsViewHolder>
        implements View.OnClickListener{

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ContactCursorAdapter.class);
    private static final String TAG = "ContactCursorAdapter";
    private Bundle mBundle;

    public ContactCursorAdapter(Context context, Cursor cursor, String id) {
        super(context, cursor, id);
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contact, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder viewHolder, Cursor cursor) {
        final String username = cursor.getString(cursor.getColumnIndex(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data
                        .DISPLAY_NAME
        ));

        viewHolder.setUsername(username);
        final long contactId = getItemId(cursor.getPosition());
        long photoId = cursor.getLong(cursor.getColumnIndex(
                ContactsContract.Data.PHOTO_ID
        ));

        final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);

        viewHolder.setUserNumber(Globals.getContactNumber(contactUri,mContext));

        if (photoId != 0) {
            Uri photUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                    .CONTENT_DIRECTORY);
            viewHolder.imageViewContactDisplay.setImageURI(photUri);
        } else
            viewHolder.imageViewContactDisplay.setImageResource(R.drawable.facebook_avatar);

        final long contactIdTest = getItemId(cursor.getPosition());

        viewHolder.mRootLayout.setOnClickListener(this);
        viewHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveSelectedUnserNumberToSharedPreference(Globals.getContactNumber(contactUri,mContext),
                            username);
                    replaceFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContactUsername, textViewContactUsernumber;
        ImageView imageViewContactDisplay;
        LinearLayout mRootLayout;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            mRootLayout = (LinearLayout) itemView.findViewById(R.id.contact_root_layout);
            textViewContactUsername = (TextView) itemView.findViewById(R.id
                    .text_view_contact_username);
            textViewContactUsernumber = (TextView) itemView.findViewById(R.id
                    .text_view_contact_usernumber);
            imageViewContactDisplay = (ImageView) itemView.findViewById(R.id
                    .image_view_contact_display);
        }

        public void setUsername(String username) {
            textViewContactUsername.setText(username);
        }

        public void setUserNumber(String userNumber) {
            textViewContactUsernumber.setText(userNumber);
        }
    }

    public void saveSelectedUnserNumberToSharedPreference(String selectedUserNumber, String selectedUserName) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("selected_user_number", selectedUserNumber);
        editor.putString("selected_user_name", selectedUserName);
        editor.apply();
    }

    public void replaceFragment(){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
