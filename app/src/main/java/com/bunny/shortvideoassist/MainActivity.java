/**                     
    * Project:  短视频辅助器
    * Comments: 主界面类
    * JDK version used: <JDK1.8>
    * Author： Bunny     Github: https://github.com/bunny-chz/
    * Create Date：2022-03-17
    * Version: 1.0
    */

package com.bunny.shortvideoassist;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.baidu.aip.asrwakeup3.core.R;
import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements EventListener  {
    protected TextView txtLog;
    protected TextView txtResult;
    protected Button btn;
    protected Button btn_accessService;
    protected Button stopBtn;
    private int width = 0;
    private int height = 0;
    Intent intent2;

    private EventManager wakeup;

    private final boolean logTime = true;

    private static final String DESC_TEXT = "此为测试日志，上一个文字显示框，输出你说的文字(只能说以下词语)\n\n测试完无错误，请重启软件，开启无障碍服务正常使用本APP\n由于百度语音唤醒对指令的严格规定，刷抖音的7个手势对应的唤醒指令如下（手势→指令）\n" +
            "播放→\"播放\"\n" +
            "暂停→\"暂停\"\n" +
            "滑动到下个视频→\"下一首\"\n" +
            "滑动到上个视频→\"上一首\"\n" +
            "点赞→\"点赞点赞\"\n" +
            "查看评论区→\"查看评论\"\n" +
            "关闭评论区→\"关闭评论\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bunny.shortvideoassist.R.layout.main_page);
        initView();
        initPermission();
        getDis();
        isServiceEnabled();
        if(!isServiceEnabled()){
            Toast.makeText(this, "无障碍服务已关闭，请点击 ”开启无障碍“ 按钮，前往设置打开", Toast.LENGTH_SHORT).show();
        }
        intent2 = new Intent(MainActivity.this, shortVideoAssist.class);
        intent2.putExtra("width", width);
        intent2.putExtra("height", height);
        startService(intent2);

        wakeup = EventManagerFactory.create(this, "wp");
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(this); //  EventListener 中 onEvent方法
        btn.setOnClickListener(v -> start());
        stopBtn.setOnClickListener(v -> stop());
        btn_accessService.setOnClickListener(v -> {
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        });
    }

    private void start() {
        txtLog.setText("");
        // 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
        Map<String, Object> params = new TreeMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        InFileStream.setContext(this);
        String json; // 这里可以替换成你需要测试的json
        json = new JSONObject(params).toString();
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        printLog("输入参数：" + json);
    }

    private void stop() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent2);
        wakeup.send(SpeechConstant.WAKEUP_STOP, "{}", null, 0, 0);
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
            JSONObject json;
            try {
                json = new JSONObject(params);
                String result = json.getString("word");
                txtResult.setText(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);
    }

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
        txtLog.append(text + "\n");
    }


    private void initView() {
        txtResult = findViewById(R.id.txtResult);
        txtLog = findViewById(R.id.txtLog);
        btn = findViewById(R.id.btn);
        stopBtn = findViewById(R.id.btn_stop);
        btn_accessService = findViewById(com.bunny.shortvideoassist.R.id.btn_accessService);
        txtLog.setText(DESC_TEXT);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isServiceEnabled() {
        AccessibilityManager accessibilityManager = (AccessibilityManager)getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().contains("com.bunny.shortvideoassist")) {
                return true;
            }
        }
        return false;
    }

    public void getDis(){
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        width = (dm.widthPixels);
        height = (dm.heightPixels);
    }

    /**
     再按一次退出主界面操作
     **/
    long exitTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            // ToastUtil.makeToastInBottom("再按一次退出应用", MainMyselfActivity);
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }
}
