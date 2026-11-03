package com.demo.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

/**
 * 公共基类：提供「重定向后仍可提示一次」的 flash 消息。
 */
public abstract class BaseAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private String flashMessage;
    private String flashType;

    protected void flash(String type, String message) {
        Map<String, Object> session = ActionContext.getContext().getSession();
        session.put("flashType", type);
        session.put("flashMessage", message);
    }

    /** 页面读取一次后即从 session 中移除 */
    public String getFlashMessage() {
        if (flashMessage == null) {
            Map<String, Object> session = ActionContext.getContext().getSession();
            Object msg = session.remove("flashMessage");
            Object type = session.remove("flashType");
            flashMessage = msg == null ? "" : msg.toString();
            flashType = type == null ? "success" : type.toString();
        }
        return flashMessage;
    }

    public String getFlashType() {
        getFlashMessage();
        return flashType == null ? "success" : flashType;
    }
}
