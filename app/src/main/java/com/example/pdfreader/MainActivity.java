package com.example.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity {
    public static final int RC_FILE_PICKER_PERM = 321;
    private ArrayList<String> docPaths = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyPermissions.requestPermissions(this, "Picker",
                RC_FILE_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER);
        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            onPickDoc();
        } else {
            EasyPermissions.requestPermissions(this, "Picker",
                    RC_FILE_PICKER_PERM, FilePickerConst.PERMISSIONS_FILE_PICKER);
        }
    }


    public void onPickDoc() {
        String[] csv = { ".pdf" };
        FilePickerBuilder.getInstance()
                .setMaxCount(1)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.PickerTabLayout)
                .setActivityTitle("Please select doc")
                .addFileSupport("PDF",csv )
                .enableDocSupport(true)
                .enableSelectAll(false)
                .sortDocumentsBy(SortingTypes.name)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .pickFile(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    readPDF(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;
        }
    }

    private void readPDF(ArrayList<String> path){
        try {
            String parsedText="";
            PdfReader reader = new PdfReader(path.get(0));
            int n = reader.getNumberOfPages();
            for (int i = 0; i <n ; i++) {
                parsedText   = parsedText+PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
            }
            System.out.println(parsedText);
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
