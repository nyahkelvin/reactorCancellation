/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tworope.reactor.cancellation;

import com.tworope.reactor.cancellation.data.CancellationDAO;
import com.tworope.reactor.cancellation.dto.CancellationDTO;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author tobah
 */
public class CancellationVerticle extends AbstractVerticle {

    private final static Logger LOGGER = Logger.getLogger(CancellationVerticle.class.getName());

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String CONTENT_TYPE_TEXT = "content-type";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new CancellationVerticle());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        startHTTPServer();
    }

    private Future<Void> startHTTPServer() {
        Future<Void> future = Future.future();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*")
                .allowedHeader("Content-Type"));
        router.route().handler(BodyHandler.create());

        router.get("/cancel").handler(this::homeRoute);
        router.post("/cancel").handler(this::saveCancellationRoute);

        server.requestHandler(router::accept).listen(8080, ar -> {
            if (ar.succeeded()) {
                LOGGER.info("Web Server started");
                future.complete();
            } else {
                LOGGER.info("Server failed to start");
                future.fail(ar.cause());
            }
        });

        return future;
    }

    private void homeRoute(RoutingContext context) {

        vertx.<CancellationDTO>executeBlocking(future -> {
            
            CancellationDTO cancellationDTO = null;
            
            try {
                CancellationDAO cancellationDAO = new CancellationDAO();
                cancellationDTO = cancellationDAO.getAllChanges();
                
                System.out.println("Same block code goes here");
                
            } catch (Exception e) {
                System.out.println("Error occurred " + e);
            }
            future.complete(cancellationDTO);
        }, response -> {
            if (response.succeeded()) {
                context.response().putHeader(CONTENT_TYPE_TEXT, JSON_CONTENT_TYPE)
                        .end(Json.encodePrettily(response.result()));
            } else {
                context.response().putHeader(CONTENT_TYPE_TEXT, JSON_CONTENT_TYPE)
                        .end(Json.encodePrettily(new JsonObject().put("error", response.cause())));
                System.out.println("Something happened " + response.cause());
            }
        });

    }
    
    private void saveCancellationRoute(RoutingContext context){
        
        vertx.executeBlocking(future -> {
            
            CancellationDTO cancellationDTO = new CancellationDTO();
            
            cancellationDTO.setCancellationDate(new Date());
            cancellationDTO.setReason(context.request().getParam("reason"));
            
            try {
                CancellationDAO cancellationDAO = new CancellationDAO();
                cancellationDAO.saveCancellation(cancellationDTO);
                
                System.out.println("Same block code goes here");
                
            } catch (Exception e) {
                System.out.println("Error occurred " + e);
            }
            future.complete();
        }, response -> {
            if (response.succeeded()) {
                context.response().putHeader(CONTENT_TYPE_TEXT, JSON_CONTENT_TYPE)
                        .end(Json.encodePrettily(new JsonObject().put("success", response.result())));
            } else {
                context.response().putHeader(CONTENT_TYPE_TEXT, JSON_CONTENT_TYPE)
                        .end(Json.encodePrettily(new JsonObject().put("error", response.cause())));
                System.out.println("Something happened " + response.cause());
            }
        });
    }
}
