/**                     
    * Project:  短视频辅助器
    * Comments: 主要功能类，无障碍服务，语音唤醒
    * JDK version used: <JDK1.8>
    * Author： Bunny     Github: https://github.com/bunny-chz/
    * Create Date：2022-03-17
    * Version: 1.0
    */

package com.bunny.shortvideoassist;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class shortVideoAssist extends AccessibilityService implements EventListener {
    private static final String TAG = "Test";
    private boolean isFirst = true;
    private int X=0, Y=0;

    private EventManager wakeup;
    private String mResult;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        X = intent.getIntExtra("width", 0);
        Y = intent.getIntExtra("height", 0);
        wakeup = EventManagerFactory.create(this, "wp");
        wakeup.registerListener(this);
        start();
        setResult("defaultValue");
        String val = "X: " + X + "   Y: " + Y;
        Log.d(TAG, val);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null || event.getPackageName() == null || event.getPackageName().equals("")){
            return;
        }

        if (event.getPackageName().equals("com.ss.android.ugc.aweme")){
            final AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo != null) {
                Log.d(TAG, "nodeinfo:" + nodeInfo.getClassName());
            }

            if (!isFirst){
                return;
            }

            if (isFirst){
                isFirst = false;
            }
            StartThreadGesture();
        }
    }

    //开启一个子线程
    private void StartThreadGesture() {
        new Thread(){
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(35);
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        }.start();
    }


    @SuppressLint("HandlerLeak")
    private final Handler handler=new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                //ClickIgnoreRemind();
                String result = getResult();
                switch (result) {
                    case "下一首":
                        ScrollUp();
                        setResult("defaultValue");
                        Log.d("滑动", "下个视频");
                        break;
                    case "上一首":
                        ScrollDown();
                        setResult("defaultValue");
                        Log.d("滑动", "上个视频");
                        break;
                    case "播放":
                        SingleClick();
                        setResult("defaultValue");
                        Log.d("单击", "播放");
                        break;
                    case "暂停":
                        SingleClick();
                        setResult("defaultValue");
                        Log.d("单击", "暂停");
                        break;
                    case "点赞点赞":
                        ClickLike();
                        setResult("defaultValue");
                        Log.d("双击", "点赞点赞");
                        break;
                    case "查看评论":
                        ClickOnComment();
                        setResult("defaultValue");
                        Log.d("单击", "看评论区");
                        break;
                    case "关闭评论":
                        ClickOffComment();
                        setResult("defaultValue");
                        Log.d("单击", "关评论区");
                        break;
                }
            }
        }
    };

    private void ScrollUp(){
        final android.graphics.Path path = new Path();
        path.moveTo((int)(X/2), (int)(Y*2/3));
        path.lineTo((int)(X/2), 0);

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 50, 100)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "ScrollUp finish.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "ScrollUp cancel.");
            }
        }, null);

    }

    private void ScrollDown(){
        final android.graphics.Path path = new Path();
        path.moveTo((int)(X/2), (int)(Y/3));
        path.lineTo((int)(X/2), (int)(Y));

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 50, 100)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "ScrollDown finish.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "ScrollDown cancel.");
            }
        }, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SingleClick(){
        final android.graphics.Path path = new Path();
        path.moveTo((int)(X/2), (int)(Y/2));

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 50)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "click finish.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "scroll cancel.");
            }
        }, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ClickOnComment(){
        final android.graphics.Path path = new Path();
        path.moveTo((int)(X*9/10), (int)(Y*65/100));

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 50)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "click finish.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "scroll cancel.");
            }
        }, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ClickOffComment(){
        final android.graphics.Path path = new Path();
        path.moveTo((int)(X/2), (int)(Y/6));

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 50)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "click finish.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "scroll cancel.");
            }
        }, null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ClickLike(){
        final Path path = new Path();
        path.moveTo((int)(X/2), (int)(Y/2));

        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 100, 1)
        ).build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Path path2 = new Path();
                path2.moveTo((int)(X/2), (int)(Y/2));

                GestureDescription.Builder builder2 = new GestureDescription.Builder();

                GestureDescription gestureDescription2 = builder2.addStroke(
                        new GestureDescription.StrokeDescription(path2, 100, 1)
                ).build();

                shortVideoAssist.this.dispatchGesture(gestureDescription2, null, null);

                Log.d(TAG, "ClickLike finish.");
                path.close();
                path2.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "ClickLike cancel.");
            }
        }, null);

    }
    private void ClickIgnoreRemind() {
        AccessibilityNodeInfo rootInfo1 = shortVideoAssist.this.getRootInActiveWindow();
        List<AccessibilityNodeInfo> listInfo1 = rootInfo1.findAccessibilityNodeInfosByText("忽略提醒");
        List<AccessibilityNodeInfo> listInfo2 = rootInfo1.findAccessibilityNodeInfosByText("我知道了");
        List<AccessibilityNodeInfo> listInfo3 = rootInfo1.findAccessibilityNodeInfosByText("关闭");
        if (listInfo1.size() ==1 || listInfo3.size() ==1 || listInfo2.size() ==1){
            for (int i=0; i<listInfo1.size();i++){
                listInfo1.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.d(TAG, "find the 忽略提醒 button");
            }
            for (int i=0; i<listInfo2.size();i++){
                listInfo2.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.d(TAG, "find the 我知道了 button");
            }
            for (int i=0; i<listInfo3.size();i++){
                listInfo3.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.d(TAG, "find the 关闭 button");
            }
        }
    }
    private void start() {
        Map<String, Object> params = new TreeMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        InFileStream.setContext(this);
        String json;
        json = new JSONObject(params).toString();
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    private void stop() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {

        if (params != null && !params.isEmpty()) {
            try {
                JSONObject json = new JSONObject(params);
                String word = json.getString("word");
                Log.d("WORD",word);
                setResult(word);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getResult(){
        return mResult;
    }

    public void setResult(String result){
        this.mResult = result;
    }

    @Override
    public void onInterrupt() {
        stop();
        wakeup.send(SpeechConstant.WAKEUP_STOP, "{}", null, 0, 0);
        Log.d(TAG, "AccessibilityEvent end.");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(getApplicationContext(), "无障碍服务开启成功，若出现错误，请重新开启", Toast.LENGTH_LONG).show();
        Log.d(TAG,"onServiceConnected: success.");
    }

}
