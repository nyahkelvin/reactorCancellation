/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tworope.reactor.cancellation.util;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

/**
 *
 * @author kelvinashu
 */
public class ReactiveFrame {

    OrientGraphFactory factory = new OrientGraphFactory("remote:localhost/tworope", "root", "root");
    FramedGraph<OrientGraph> framedGraph;
    OrientGraph graph = null;

    public ReactiveFrame() {
        this.framedGraph = null;
    }

    public OrientGraph getFactory() {
        try {
            graph = factory.getTx();
        } catch (Exception x) {
            System.out.println("Factory creation error: " + x);
        }
        return graph;
    }
    
    public OrientGraphFactory getOrientGraphFactory(){
        return factory;
    }

    public FramedGraph<OrientGraph> getFrameGraph() {
        try {
            framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
        } catch (Exception x) {
            System.out.println("Factory creation error: " + x);
        }
        return framedGraph;
    }

}
