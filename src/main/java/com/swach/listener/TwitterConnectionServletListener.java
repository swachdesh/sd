package com.swach.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by swachtest on 01/05/15.
 */
public class TwitterConnectionServletListener implements ServletContextListener {
    TwitterSwachListener tw = new TwitterSwachListener();
    public void contextInitialized(  ServletContextEvent sce){
        tw.start();

    }
    public void contextDestroyed(    ServletContextEvent sce){
        tw.stop();
    }
}

