package com.webshop.event;

import com.webshop.entity.WebUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {

    private static final long serialVersionUID = -4113549487933175429L;
    private final WebUser user;
    private final String action;
    private final HttpServletRequest request;

    public UserRegistrationEvent(WebUser user, String action, HttpServletRequest request) {
        super(user);
        this.user = user;
        this.action = action;
        this.request = request;
    }
}
