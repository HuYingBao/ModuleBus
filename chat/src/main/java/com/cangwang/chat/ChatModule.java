package com.cangwang.chat;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.cangwang.annotation.ModuleGroup;
import com.cangwang.annotation.ModuleUnit;
import com.cangwang.chat.bean.ChatMessage;
import com.cangwang.chat.recycle.ChatAdapter;
import com.cangwang.core.cwmodule.CWModuleContext;
import com.cangwang.core.cwmodule.ex.CWBasicExModule;
import com.cangwang.enums.LayoutLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * 发言模块
 * Created by cangwang on 2018/2/3.
 */
@ModuleGroup({
        @ModuleUnit(templet = "top",layoutlevel = LayoutLevel.LOW),
})
public class ChatModule extends CWBasicExModule{
    private View chatLayout;
    private RecyclerView chatRecyle;
    private ChatAdapter adapter;

    private int i = 10;
    private boolean isScolling =false;

    @Override
    public boolean init(CWModuleContext moduleContext, Bundle extend) {
        super.init(moduleContext, extend);
        initView();
        initData();

        return true;
    }

    private void initView(){
        chatLayout = LayoutInflater.from(context).inflate(R.layout.chat_layout,parentPlugin,true);
        chatRecyle = (RecyclerView) chatLayout.findViewById(R.id.chat_recyle);
        adapter = new ChatAdapter(context);
        chatRecyle.setLayoutManager(new LinearLayoutManager(context));
        chatRecyle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    isScolling = true;
                }else {
                    isScolling = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatRecyle.setAdapter(adapter);
    }

    private void initData(){
        List<ChatMessage> list =new ArrayList<>();
        for (int i=0;i<10;i++){
            ChatMessage m = new ChatMessage("cangwang","abc "+i);
            list.add(m);
        }
        adapter.addMsgList(list);
        chatRecyle.smoothScrollToPosition(10);
        handler.post(runChat);
    }

    public Runnable runChat = new Runnable() {
        @Override
        public void run() {
            adapter.addMsg(new ChatMessage("cangwang","efg"+ (i++)));
            if (isScolling)
                chatRecyle.smoothScrollToPosition(i-1);
            handler.postDelayed(runChat,1000);
        }
    };


    @Override
    public void onDestroy() {
        handler.removeCallbacks(runChat);
        super.onDestroy();
    }
}
