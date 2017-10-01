package co.devcon.selfcheckout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

import co.devcon.selfcheckout.adapter.CartAdapter;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MAINACTIVITY";

    private CartAdapter mAdapter;

    // MQTT
    private final static String MQTT_SERVER = "tcp://xfero.xyz:1883";
    private final static String MQTT_TOPIC= "sensor";
    private MqttAndroidClient mMQTTClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CartAdapter();
        recyclerView.setAdapter(mAdapter);
        // Connect to mqtt
        mQTTConnect();

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQTTDisconnect();
    }

    private void mQTTConnect() {
        String clientId = MqttClient.generateClientId();
        mMQTTClient = new MqttAndroidClient(getApplicationContext(), MQTT_SERVER, clientId);
        mMQTTClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                // TODO what happen when the message arrived - add record to adapter
                if(topic.equalsIgnoreCase(MQTT_TOPIC)) {
                    String payload = Arrays.toString(message.getPayload());
                    //
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        try {
            IMqttToken token = mMQTTClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        mMQTTClient.subscribe(MQTT_TOPIC, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect MQTT
     */
    private void mQTTDisconnect() {

        try {
            IMqttToken token = mMQTTClient.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "MQTT Successfully Connected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "MQTT Failure Connected");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
