/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tworope.reactor.cancellation.data;

import com.orientechnologies.orient.core.id.ORecordId;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tworope.reactor.cancellation.dto.CancellationDTO;
import com.tworope.reactor.cancellation.util.ReactiveFrame;
import com.tworope.reactor.cancellation.frames.Cancellation;

/**
 *
 * @author tobah
 */
public class CancellationDAO {

    public boolean saveCancellation(CancellationDTO cancellationDTO) {
        OrientGraphFactory factory = new ReactiveFrame().getOrientGraphFactory();
        FramedGraph<OrientGraph> framedGraph = null;
        try {

            OrientGraph graph = factory.getTx();
            framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);

            Cancellation cancellation = framedGraph.addVertex("class:Cancellation", Cancellation.class);

            cancellation.setCancellationDate(cancellationDTO.getCancellationDate());
            cancellation.setReason(cancellationDTO.getReason());

            graph.commit();

            System.out.println("Cancellation id before save " + cancellation.asVertex().getId());

            return true;

        } catch (Exception e) {
            System.out.println("exception to add Cancellation " + e);
        } finally {
            if (framedGraph != null) {
                framedGraph.shutdown();
            }
        }
        return false;
    }

    public CancellationDTO saveCancellationDTO(Cancellation cancellation) {

        CancellationDTO cancellationDTO = new CancellationDTO();

        cancellationDTO.setCancellationDate(cancellation.getCancellationDate());
        cancellationDTO.setReason(cancellation.getReason());

        return cancellationDTO;
    }
    
    public CancellationDTO getAllChanges(){
        
        OrientGraphFactory factory = new ReactiveFrame().getOrientGraphFactory();
        FramedGraph<OrientGraph> framedGraph = null;
        CancellationDTO cancellationDTO = null;
        
        try {

            OrientGraph graph = factory.getTx();
            framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);

            ORecordId orid = new ORecordId("#45:0");

            Cancellation cancellation = framedGraph.getVertex(orid, Cancellation.class);

            cancellationDTO = saveCancellationDTO(cancellation);
            
            System.out.println("Cancellation details " + cancellationDTO);
            
        } catch (Exception e) {
            System.out.println("exception to retrieve Cancellation  details" + e);
        } finally {
            if (framedGraph != null) {
                framedGraph.shutdown();
            }
        }
        return cancellationDTO;
    }
}
