package com.hfut.studyhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.EnsureDialog;
import com.dialog.listener.IDialogEnsureClickListener;
import com.hfut.studyhelper.Server.NetServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
public   static String tesCoo;
  public static   LoadingDialog loadingDialog=null;
    private RoundImageView circleImageView;
    private BottomPopupOption bottomPopupOption;
    private int REQUEST_TAKE_PHOTO_PERMISSION = 1;

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),100 X 100的正方形。
    private static int output_X = 150;
    private static int output_Y = 150;
    private Bitmap photo;
    private  String IMAGE_FILE_NAME ;
    private  String HeadPortrait_PATH;
    private   Uri uritempFile;
  String[] tags=new String[]{
          "Never give up,Never lose the opportunity to succeed.",
          "真理不需色彩,美丽不需涂饰。",
          "活在这珍贵的人间，太阳强烈,水波温柔。",
          "在世间,本就是各人下雪,各人有各人的隐晦与皎洁.",
          "半山腰总是最挤的,你得去山顶看看",
          "我喜欢的人很优秀,我努力的理由是配得上他",
          "乾坤未定,你我皆黑马",
          "总不能还没努力就向生活妥协吧",
          "从不试图摘月,我要这月亮为我而来",
          "同是寒窗苦读,怎愿甘拜下风",
          "中华人民共和国的女孩子绝不认输",
          "我有我要赶去的远方，风雨兼程披星戴月",
          "宇宙山河烂漫 生活点滴温暖 都值得我前进",
          "躲起来的星星也在努力发光 ,你也要加油",
          "那些看似不起波澜的日复一日,会在某天让你看到坚持的意义",
          "即使辛苦,我还是选择去过滚烫的人生。"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDrawerLeftEdgeSize(this,(DrawerLayout)findViewById(R.id.drawer_container),1f);
        loadingDialog=LoadingDialog.getInstance(this);
        TextView textView=findViewById(R.id.tagTextVie);
        Random df = new Random();
        int number = df.nextInt(tags.length);
        textView.setText(tags[number]);
        TableRow tableRow1=findViewById(R.id.whereof);
        tableRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "这个APP开发主要是为了帮助大家打理学习时间，希望大家喜欢!");
                bundle.putString("left", "确认");
                bundle.putString("right", "取消");
                ensureDialog.setArguments(bundle);
                ensureDialog.show(getSupportFragmentManager(), "ensure");

                ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                    @Override
                    public void onEnsureClick() {
                        ensureDialog.dismiss();
                    }
                    @Override
                    public void onCancelClick() {
                        ensureDialog.dismiss();
                    }
                });
            }
        });
        TableRow tableRow2=findViewById(R.id.us);
        tableRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "如果你在使用过程中遇到了任何问题，请加群：608707038");
                bundle.putString("left", "确认");
                bundle.putString("right", "取消");
                ensureDialog.setArguments(bundle);
                ensureDialog.show(getSupportFragmentManager(), "ensure");

                ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                    @Override
                    public void onEnsureClick() {
                        ensureDialog.dismiss();
                    }
                    @Override
                    public void onCancelClick() {
                        ensureDialog.dismiss();
                    }
                });
            }
        });
        TableRow tableRow3=findViewById(R.id.exit_login);
        tableRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "确认要退出登陆?(这将删除保留在这台设备的所有本地信息)");
                bundle.putString("left", "确认");
                bundle.putString("right", "取消");
                ensureDialog.setArguments(bundle);
                ensureDialog.show(getSupportFragmentManager(), "ensure");

                ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                    @Override
                    public void onEnsureClick() {
                        ensureDialog.dismiss();
                    }
                    @Override
                    public void onCancelClick() {
                        NetServer.exitLogin(MainActivity.this);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        LoginFragment loginFragment = new LoginFragment();
                        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                        fragmentTransaction.commit();
                        ensureDialog.dismiss();
                    }
                });
            }
        });
        TableRow tableRow4=findViewById(R.id.clear);
        tableRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "确认清理数据?");
                bundle.putString("left", "确认");
                bundle.putString("right", "取消");
                ensureDialog.setArguments(bundle);
                ensureDialog.show(getSupportFragmentManager(), "ensure");

                ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                    @Override
                    public void onEnsureClick() {
                        ensureDialog.dismiss();
                    }
                    @Override
                    public void onCancelClick() {
                        NetServer.clearData(MainActivity.this);
                        ensureDialog.dismiss();
                    }
                });
            }
        });
        TableRow tableRow5=findViewById(R.id.more);
        tableRow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EnsureDialog ensureDialog = new EnsureDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", "不要再催了,程序员快没头发了");
                bundle.putString("left", "确认");
                bundle.putString("right", "好的");
                ensureDialog.setArguments(bundle);
                ensureDialog.show(getSupportFragmentManager(), "ensure");

                ensureDialog.setOnClickListener(new IDialogEnsureClickListener() {
                    @Override
                    public void onEnsureClick() {
                        ensureDialog.dismiss();
                    }
                    @Override
                    public void onCancelClick() {
                        ensureDialog.dismiss();
                    }
                });
            }
        });
        TableRow tableRow6=findViewById(R.id.socre);
        tableRow6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ScoreActivity.class);
                startActivity(intent);
            }
        });
        circleImageView=findViewById(R.id.imageView4);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomPopupOption = new BottomPopupOption(MainActivity.this);
                bottomPopupOption.setItemText("拍照","相册");
                bottomPopupOption.showPopupWindow();
                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        bottomPopupOption.dismiss();
                        switch (position){
                            case 0:
                                choseHeadImageFromCameraCapture();
                                break;
                            case 1:
                                choseHeadImageFromGallery();
                                break;
                        }
                    }
                });
            }
        });
        try {
            Drawable drawable=Utils.getDrawable(MainActivity.this);
            if(drawable!=null)
                circleImageView.setDrawable(drawable);
        }catch (Exception e){

        }
        ImageView imageView1=findViewById(R.id.im);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartFragment startFragment = new StartFragment();
        fragmentTransaction.add(R.id.fragment_container, startFragment);
        fragmentTransaction.commit();
    }
    /**
     * 设置DrawerLayout全屏滑动，参数为1的时候
     *
     * @param activity               当前activity
     * @param drawerLayout           抽屉
     * @param displayWidthPercentage 滑动弹出侧滑栏的范围，0-1
     */
    private void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout,
                                       float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mLeftDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));

            //获取 Layout 的 ViewDragCallBack 实例“mLeftCallback”
            //更改其属性 mPeekRunnable
            Field leftCallbackField = drawerLayout.getClass().getDeclaredField("mLeftCallback");
            leftCallbackField.setAccessible(true);

            //因为无法直接访问私有内部类，所以该私有内部类实现的接口非常重要，通过多态的方式获取实例
            ViewDragHelper.Callback leftCallback = (ViewDragHelper.Callback) leftCallbackField.get(drawerLayout);
            Field peekRunnableField = leftCallback.getClass().getDeclaredField("mPeekRunnable");
            peekRunnableField.setAccessible(true);
            peekRunnableField.set(leftCallback, new Runnable() {
                @Override
                public void run() {
                }
            });
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalArgumentException ignored) {
        } catch (IllegalAccessException ignored) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());

                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(MainActivity.this,"没有sd卡",Toast.LENGTH_SHORT).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                        Drawable drawable=new BitmapDrawable(bitmap);
                        Utils.setPicture(MainActivity.this,uritempFile.toString());
                        circleImageView.setDrawable(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                choseHeadImageFromCameraCapture();
            } else {
                Toast.makeText(MainActivity.this,"你拒绝了权限，该功能不可用\n可在应用设置里授权拍照哦",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        //6.0以上动态获取权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_TAKE_PHOTO_PERMISSION);

        } else {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 判断存储卡是否可用，存储照片文件
            if (hasSdcard()) {

                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
            startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        }

    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {

        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        String pickPath = putils.getPath(MainActivity.this, uri);
            uritempFile = Uri.parse(putils.getPath(MainActivity.this, uri));
            uri = intent.getData();
            String type = intent.getType();
            if (uri.toString().contains("com.miui.gallery.open")) {
                uri = getImageContentUri(MainActivity.this, new File(getRealFilePath(MainActivity.this, uri)));
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            uritempFile=uri;
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将URI转为图片的路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
