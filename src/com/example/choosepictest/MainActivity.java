package com.example.choosepictest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener
{
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	public static final int CROP_PHOTO1 = 3;
	
	private Button takephoto;
	private Button chooseFromAlbum;
	private ImageView picture;
	private Uri imageUri;
	private Uri imageUri1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		takephoto = (Button) findViewById(R.id.take_photo);
		chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
		picture = (ImageView) findViewById(R.id.picture);
		takephoto.setOnClickListener(this);
		chooseFromAlbum.setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.take_photo:
			File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
			if(outputImage.exists())
			{
				outputImage.delete();
			}
			try
			{
				outputImage.createNewFile();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			imageUri = Uri.fromFile(outputImage);
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TAKE_PHOTO);
			break;
		case R.id.choose_from_album:
			File outputImage1 = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
			if(outputImage1.exists())
				outputImage1.delete();
			
			Intent intent1 = new Intent("android.intent.action.GET_CONTENT");
			intent1.setType("image/*");
			intent1.putExtra("crop", true);
			intent1.putExtra("scale", true);
			intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
			startActivityForResult(intent1, CROP_PHOTO1);
			
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch(requestCode)
		{
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK)
			{
				Intent  intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
		case CROP_PHOTO:
			if(resultCode == RESULT_OK)
			{
				try 
				{
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
			else
			break;
		case CROP_PHOTO1:
				try 
				{
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri1));
					picture.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
			
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}	
}




















