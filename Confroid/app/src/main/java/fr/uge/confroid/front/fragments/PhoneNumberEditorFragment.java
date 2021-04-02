package fr.uge.confroid.front.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.StringValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.models.EditorArgs;
import fr.uge.confroid.front.models.EditorOpener;
import fr.uge.confroidlib.annotations.PhoneNumber;

public class PhoneNumberEditorFragment extends EditorFragment implements TextWatcher {
    public static class Opener implements EditorOpener {
        @Override
        public boolean canHandle(EditorArgs args) {
            Value value = args.getValue();
            return value.isString() && args.getAnnotation(PhoneNumber.class).isPresent();
        }

        @Override
        public Fragment createEditor() {
            return new PhoneNumberEditorFragment();
        }
    }

    private final int READ_CONTACT_INTENT_CODE = 10;
    private final int READ_CONTACT_PERMISSION_CODE = 20;

    private EditText input;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_number_editor, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        input = view.findViewById(R.id.input_phone_number);
        input.addTextChangedListener(this);

        Button button = view.findViewById(R.id.btn_pick_contact);
        button.setOnClickListener(__ -> ensureContactPermissionThenPickContact());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_CONTACT_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            onPickContact(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ensureContactPermissionThenPickContact();
            } else {
                Toast.makeText(getContext(), R.string.error_contact_permission, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateValue(new StringValue(s.toString()), false);
    }


    @Override
    public void onUpdateArgs(EditorArgs args) {
        Value value = args.getValue();
        input.setText(value.getString());
    }


    private void onPickContact(Uri uri) {
        String contactNumber = null;
        ContentResolver contentResolver = getContext().getContentResolver();

        Cursor cursorID = contentResolver.query(
            uri,
            new String[]{ ContactsContract.Contacts._ID },
            null, null, null
        );

        String contactID = "";
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
              new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER },
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
              ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
              ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
              new String[]{ contactID }, null
        );

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        input.setText(contactNumber);
    }

    private void ensureContactPermissionThenPickContact() {
        // Check whether the permission is already granted or not.
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.READ_CONTACTS }, READ_CONTACT_PERMISSION_CODE);
            // wait for onRequestPermissionsResult()
            return;
        }

        // The permission is already granted.
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, READ_CONTACT_INTENT_CODE);
    }
}
