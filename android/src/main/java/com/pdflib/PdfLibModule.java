package com.pdflib;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.facebook.react.bridge.NoSuchKeyException;



import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.pdflib.factories.PDDocumentFactory;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.pdflib.factories.PDPageFactory;

@ReactModule(name = PdfLibModule.NAME)
public class PdfLibModule extends ReactContextBaseJavaModule {
  public static final String NAME = "PdfLib";

  private final ReactApplicationContext reactContext;

  public PdfLibModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    PDFBoxResourceLoader.init(reactContext);
    PDPageFactory.init(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

   @ReactMethod
  public void createPDF(ReadableMap documentActions, Promise promise) {
    try {
      PDDocument document = PDDocumentFactory.create(documentActions);
      promise.resolve(PDDocumentFactory.write(document, documentActions.getString("path")));
    } catch (NoSuchKeyException e) {
      e.printStackTrace();
      promise.reject(e);
    } catch (IOException e) {
      e.printStackTrace();
      promise.reject(e);
    }
  }

  @ReactMethod
  public void modifyPDF(ReadableMap documentActions, Promise promise) {
    try {
      PDDocument document = PDDocumentFactory.modify(documentActions);
      promise.resolve(PDDocumentFactory.write(document, documentActions.getString("path")));
    } catch (NoSuchKeyException e) {
      e.printStackTrace();
      promise.reject(e);
    } catch (IOException e ) {
      e.printStackTrace();
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getDocumentsDirectory(Promise promise) {
    promise.resolve(reactContext.getFilesDir().getPath());
  }

  @ReactMethod
  public void unloadAsset(String assetName, String destPath, Promise promise) {
    try {
      InputStream is = reactContext.getAssets().open(assetName);
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      is.close();

      File destFile = new File(destPath);
      File dirFile = new File(destFile.getParent());
      dirFile.mkdirs();

      FileOutputStream fos = new FileOutputStream(destFile);
      fos.write(buffer);
      fos.close();
      promise.resolve(destPath);
    } catch (IOException e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getAssetPath(String assetName, Promise promise) {
    promise.reject(new Exception(
      "PDFLib.getAssetPath() is only available on iOS. Try PDFLib.unloadAsset()"
    ));
  }

  @ReactMethod
  public void measureText(String text, String fontName, int fontSize, Promise promise) {
    try {
      PDDocument document = new PDDocument();
      PDFont font = PDType0Font.load(document, reactContext.getApplicationContext().getAssets().open("fonts/" + fontName + ".ttf"));
      float width = font.getStringWidth(text) / 1000 * fontSize;
      float height = (font.getFontDescriptor().getCapHeight()) / 1000 * fontSize;
      WritableMap map = Arguments.createMap();
      map.putInt("width", (int)width);
      map.putInt("height", (int)height);
      promise.resolve(map);
    } catch (IOException e) {
      promise.reject(e);
    }
  }

}
