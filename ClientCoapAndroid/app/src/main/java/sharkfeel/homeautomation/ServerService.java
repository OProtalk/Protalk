package sharkfeel.homeautomation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

import java.util.Timer;
import java.util.TimerTask;

public class ServerService extends Service {

    CoapServer server;

    @Override
    public void onCreate() {
        this.server = new CoapServer();
        server.add(new BedroomResource());
        server.add(new LivingResource());
        server.add(new BathroomResource());
        server.add(new KitchenResource());
        server.add(new YardResource());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        server.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        server.destroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    class KitchenResource extends CoapResource {

        public KitchenResource() {

            // set resource identifier
            super("Kitchen");
            add(new Led1Resource());
            add(new PPMResource());
            add(new TempResource());
            add(new HumResource());
            add(new AtmResource());
            add(new LuxResource());

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }
    }
    class YardResource extends CoapResource {

        public YardResource() {

            // set resource identifier
            super("Yard");
            add(new Led1Resource());
            add(new DoorResource());
            add(new TempResource());
            add(new HumResource());
            add(new AtmResource());
            add(new LuxResource());

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }
    }
    class BathroomResource extends CoapResource {

        public BathroomResource() {

            // set resource identifier
            super("Bathroom");
            add(new Led1Resource());
            add(new Led2Resource());
            add(new TempResource());
            add(new HumResource());
            add(new AtmResource());
            add(new LuxResource());

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }
    }
    class BedroomResource extends CoapResource {

        public BedroomResource() {

            // set resource identifier
            super("Bedroom");
            add(new Led1Resource());
            add(new Led2Resource());
            add(new Led3Resource());
            add(new WindowResource());
            add(new TempResource());
            add(new HumResource());
            add(new AtmResource());
            add(new LuxResource());

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }
    }
    class LivingResource extends CoapResource {

        public LivingResource() {

            // set resource identifier
            super("Living");
            add(new Led1Resource());
            add(new Led2Resource());
            add(new TempResource());
            add(new HumResource());
            add(new AtmResource());
            add(new LuxResource());

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }
    }

    class Led1Resource extends CoapResource {

        public Led1Resource() {

            // set resource identifier
            super("Led2");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            exchange.respond(exchange.getRequestText());

        }
    }

    class Led2Resource extends CoapResource {

        public Led2Resource() {

            // set resource identifier
            super("Led3");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            exchange.respond(exchange.getRequestText());

        }
    }

    class Led3Resource extends CoapResource {

        public Led3Resource() {

            // set resource identifier
            super("Led1");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            exchange.respond(exchange.getRequestText());

        }
    }


    class WindowResource extends CoapResource {

        public WindowResource() {

            // set resource identifier
            super("Window");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            exchange.respond(exchange.getSourceAddress() + exchange.getRequestText());
        }
    }

    class DoorResource extends CoapResource {

        public DoorResource() {

            // set resource identifier
            super("Door");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello Android!");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            exchange.respond(exchange.getSourceAddress() + exchange.getRequestText());
        }
    }

    class PPMResource extends CoapResource {

        int i = 50;
        public PPMResource() {

            // set resource identifier
            super("PPM");
            setObservable(true);
            setObserveType(CoAP.Type.CON);
            getAttributes().setObservable();


            Timer timer = new Timer();
            timer.schedule(new UpdateTask(), 0, 5000);

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        private class UpdateTask extends TimerTask {
            @Override
            public void run() {
                changed();
            }
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.setMaxAge(5);
            exchange.respond(String.valueOf(i++));
            // respond to the request
            //exchange.respond("Hello Android!");
        }
    }
    class AtmResource extends CoapResource {

        int i = 50;
        public AtmResource() {

            // set resource identifier
            super("Atmosphere");

            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.respond(String.valueOf(i++));
        }
    }

    class LuxResource extends CoapResource {

        int i = 80;
        public LuxResource() {

            // set resource identifier
            super("Lux");
            setObservable(true);
            setObserveType(CoAP.Type.CON);
            getAttributes().setObservable();


            Timer timer = new Timer();
            timer.schedule(new UpdateTask(), 0, 5000);

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        private class UpdateTask extends TimerTask {
            @Override
            public void run() {
                changed();
            }
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.setMaxAge(5);
            exchange.respond(String.valueOf(i++));
            // respond to the request
            //exchange.respond("Hello Android!");
        }
    }

    class TempResource extends CoapResource {

        int i = 0;
        public TempResource() {

            // set resource identifier
            super("Temperature");
            setObservable(true);
            setObserveType(CoAP.Type.CON);
            getAttributes().setObservable();


            Timer timer = new Timer();
            timer.schedule(new UpdateTask(), 0, 5000);

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        private class UpdateTask extends TimerTask {
            @Override
            public void run() {
                changed();
            }
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.setMaxAge(5);
            exchange.respond(String.valueOf(i++));
            // respond to the request
            //exchange.respond("Hello Android!");
        }
    }



    class HumResource extends CoapResource {

        int i = 20;
        public HumResource() {

            // set resource identifier
            super("Humidity");
            setObservable(true);
            setObserveType(CoAP.Type.CON);
            getAttributes().setObservable();


            Timer timer = new Timer();
            timer.schedule(new UpdateTask(), 0, 5000);

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        private class UpdateTask extends TimerTask {
            @Override
            public void run() {
                changed();
            }
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            exchange.setMaxAge(5);
            exchange.respond(String.valueOf(i++));
            // respond to the request
            //exchange.respond("Hello Android!");
        }
    }
}