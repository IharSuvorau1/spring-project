package com.epam.ui;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.vaadin.server.VaadinServlet;

@WebServlet(
    asyncSupported=false,
    urlPatterns={"/*","/VAADIN/*"},
    initParams={
        @WebInitParam(name="ui", value="com.epam.ui.VaadinApplicationUI")
    })
public class VaadinApplicationServlet extends VaadinServlet { }
