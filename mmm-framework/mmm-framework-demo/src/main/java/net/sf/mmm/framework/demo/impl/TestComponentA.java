/* $Id$ */
package net.sf.mmm.framework.demo.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;

import net.sf.mmm.framework.demo.api.ComponentA;
import net.sf.mmm.framework.demo.api.ComponentB;

/**
 * TODO This type ...
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@Resource(shareable=false)
public class TestComponentA implements ComponentA {

    private Log logger;

    private ComponentB be;
    
    /**
     * The constructor.
     */
    public TestComponentA() {

        super();
    }
    
    /**
     * This method gets the be.
     *
     * @return the be.
     */
    public ComponentB getBe() {
    
        return this.be;
    }

    
    /**
     * This method sets the be.
     *
     * @param be is the be to set.
     */
    @Resource
    public void setBe(ComponentB be) {
    
        this.be = be;
    }

    /**
     * This method gets the logger.
     *
     * @return the logger.
     */
    public Log getLogger() {
    
        return this.logger;
    }
    
    /**
     * This method sets the logger.
     *
     * @param logger is the logger to set.
     */
    @Resource
    public void setLogger(Log logger) {
    
        this.logger = logger;
    }

    /**
     * @see net.sf.mmm.framework.demo.api.ComponentA#sayAhh()
     */
    public String sayAhh() {
    
        return "Ahhhh ("+getBe().sayBe()+")!";
    }
    
    /**
     * 
     */
    @PostConstruct
    public void init() {
        getLogger().info("A initialized");
    }
    
    @PreDestroy
    public void dispose() {
        getLogger().info("A disposed");
    }
    
}
