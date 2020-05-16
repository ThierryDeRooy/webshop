package com.webshop.service;

import com.webshop.entity.Order;
import com.webshop.entity.WebUser;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailService {
    public void sendOrderMail(Order order) throws MessagingException;
    public void sendOrderMail(Order order, File fileAttach) throws MessagingException;
    public void resendOrderMail(Order order) throws MessagingException;
    public void resendOrderMail(Order order, File fileAttach) throws MessagingException;
    public void sendUsername(WebUser webUser) throws MessagingException;
}
