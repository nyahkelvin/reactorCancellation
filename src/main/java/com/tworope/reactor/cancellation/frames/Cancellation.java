/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tworope.reactor.cancellation.frames;

import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import java.util.Date;

/**
 *
 * @author tobah
 */
public interface Cancellation extends VertexFrame{
    
    @Property("cancellation_date")
    public Date getCancellationDate();
    
    @Property("cancellation_date")
    public void setCancellationDate(Date cancellationDate);
    
    @Property("reason")
    public String getReason();
    
    @Property("reason")
    public void setReason(String reason);
}
