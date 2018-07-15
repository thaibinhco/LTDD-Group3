package com.it.heoco.smartebook.activities;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.it.heoco.smartebook.FileIntent;
import com.it.heoco.smartebook.OcrManager;
import com.it.heoco.smartebook.R;
import com.it.heoco.smartebook.lists.SpinnerLanguageItem;
import com.it.heoco.smartebook.lists.TitleLanguageAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by heoco on 11/04/2017.
 */

public class OcrActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY_NAME = MainActivity.APP_DIRECTORY;
    private static final int GALLERY_REQUEST = 1, CAM_REQUEST = 1313;
    private String _tessData;
    private String[] _tessDatas;
    private File _photoFile;
    private String _pathImage;
    private Bitmap _bitmap;


    // Tab Layout
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    // Tab titles
    private String[] tabs = { "Image", "Text" };
    private TabLayout tabLayout;
    //Layout
    public static int[] resourceIds = {
            R.layout.fragment_image
            ,R.layout.fragment_text
    };

    // Ocr View
    ImageButton ibtnCrop, ibtnAccept, ibtnDelete;
    ImageView imgPreview;
    EditText edtResual;

    // Action bar
    private ActionBar mActionBar;

    // Spinner
    private Spinner mSpinner;
    // Title language Spinner data
    private ArrayList<SpinnerLanguageItem> langSpinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        // View
        ibtnCrop = (ImageButton) findViewById(R.id.ibtnCrop);
        ibtnAccept = (ImageButton) findViewById(R.id.ibtnAccept);
        ibtnDelete = (ImageButton) findViewById(R.id.ibtnDelete);
        mSpinner = (Spinner) findViewById(R.id.spinLanguage);

        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Spinner
        langSpinners = getLangSpinner();
        final TitleLanguageAdapter titleLanguageAdapter = new TitleLanguageAdapter(getApplicationContext(), langSpinners);
        mSpinner.setAdapter(titleLanguageAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _tessData = _tessDatas[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // TabLayout
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pages);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        edtResual = (EditText) findViewById(R.id.edtResual);

        ibtnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcrManager ocrManager = new OcrManager(_tessData);
                try {
                    edtResual.setText(ocrManager.startRecognize(_bitmap));
                } catch (Exception ex) {
                    Toast.makeText(OcrActivity.this, "Chọn hình cần scan!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bitmap = null;
                imgPreview.setImageBitmap(_bitmap);
                edtResual.setText("");
            }
        });

        edtResual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ArrayList<SpinnerLanguageItem> getLangSpinner() {
        _tessDatas = new String[] { "eng", "vie" };
        int[] flags = { R.drawable.flag_great_britain, R.drawable.flag_vietnam };
        String[] countryNames = { "English", "VietNam" };

        ArrayList<SpinnerLanguageItem> spinnerLanguageItems = new ArrayList<SpinnerLanguageItem>();
        for (int i = 0; i < flags.length; i++) {
            SpinnerLanguageItem spinnerLanguageItem = new SpinnerLanguageItem(flags[i], countryNames[i]);
            spinnerLanguageItems.add(spinnerLanguageItem);
        }
        return spinnerLanguageItems;
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(resourceIds[index], container, false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_ocr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.atnGallery: {
                startActivityForResult(FileIntent.photoIntent(this, null, false), GALLERY_REQUEST);
                return true;
            }
            case R.id.atnCamera: {
                try {
                    _photoFile = FileIntent.createImageFile(IMAGE_DIRECTORY_NAME);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                startActivityForResult(FileIntent.photoIntent(this, _photoFile, true), CAM_REQUEST);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            _pathImage = FileIntent.photoResult(this, _photoFile, data);
            switch (requestCode) {
                case GALLERY_REQUEST: {
                    previewImage();
                    break;
                }
                case CAM_REQUEST: {
                    previewImage();
                    break;
                }
            }
            edtResual.setText("");
        }
    }

    private void previewImage() {
        try {
            _bitmap = getBitmap(_pathImage);
            imgPreview.setImageBitmap(_bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String fileName) {
        File image = new File(fileName);
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 64;
        return BitmapFactory.decodeFile(image.getPath());
    }
}