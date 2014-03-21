package com.mycompany.myproject.mock;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.impl.Base64;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.platform.Verticle;

public class UserManagementVerticle extends Verticle {

	private static final Logger log = LoggerFactory.getLogger(UserManagementVerticle.class);

	private final String authFilePath = "usermanagement/auth_response.json";
	private final String signedPayloadFilePath = "usermanagement/signed_payload.txt";

	public void setAuthResponse(final HttpServerRequest req, final String filePath) {
		vertx.fileSystem().readFile(filePath, new AsyncResultHandler<Buffer>() {

			@Override
			public void handle(AsyncResult<Buffer> event) {
				req.response().setStatusCode(200);
				req.response().setChunked(true);
				req.response().write(event.result().toString());
				req.response().end();
			}

		});
	}

	public void start() {

		RouteMatcher routeMatcher = new RouteMatcher();
		routeMatcher.post("/authenticate", new Handler<HttpServerRequest>() {
			@Override
			public void handle(final HttpServerRequest req) {

				//TODO move this to ReverseProxyVerticle
				String authInfo = req.headers().get("Authorization");
				String prasedAuthInfo = authInfo.replace("Basic", "").trim();
				String decodedAuthInfo = new String(Base64.decode(prasedAuthInfo));
				String[] auth = decodedAuthInfo.split(":");

				if (auth != null && auth.length == 2) {
					log.debug(String.format("%s:%s", auth[0], auth[1]));
				}

				setAuthResponse(req, authFilePath);
			}
		});

		routeMatcher.post("/sign", new Handler<HttpServerRequest>() {
			@Override
			public void handle(final HttpServerRequest req) {

				// sample request I received is not actually the multipart request; it's text/plain ?? 
				/*
				for (Entry<String, String> entry : req.expectMultiPart(true).formAttributes()) {
					log.debug(String.format("%s: %s", entry.getKey(), entry.getValue()));
				}
				*/
				setAuthResponse(req, signedPayloadFilePath);
			}
		});

		vertx.createHttpServer().requestHandler(routeMatcher).listen(9000);
	}
}
