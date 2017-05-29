package net.opgenorth.boundservicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private var serviceConnection = object : ServiceConnection {
        var binder: Binder? = null
        var isConnected = false
            private set


        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
            binder = null
            Timber.d("OnServiceDisconnected - ${name?.className}")
            updateUiForUnboundService();
            message_textview.text = "onServiceDisconnected - ${name?.className} disconnected"
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this.binder = service as TimestampBinder
            isConnected = binder != null

            var message: String = ""

            if (isConnected) {
                message = "onServiceConnected - bound to service ${name?.className}."
                updateUiForBoundService()
            } else {
                message = "onServiceConnected - not bound to service ${name?.className}"
                updateUiForUnboundService()
            }

            Timber.d(message)
            message_textview.text = message
        }

    }

    private var getTimestampListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            if (serviceConnection.isConnected) {
                message_textview.text = (serviceConnection.binder as TimestampBinder).getFormattedTimestamp()
            } else {
                message_textview.setText(R.string.service_not_connected)
            }

        }

    }

    private var startServiceListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            doBindService()
        }
    }

    private var stopServiceListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            doUnbindService()
            updateUiForUnboundService()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JodaTimeAndroid.init(this.applicationContext)
        setContentView(R.layout.activity_main)

        stop_timestamp_service_button.setOnClickListener(stopServiceListener)
        get_timestamp_button.setOnClickListener(getTimestampListener)
        restart_timestamp_service_button.setOnClickListener(startServiceListener)
    }

    override fun onStart() {
        super.onStart()
        doBindService()
    }

    override fun onResume() {
        super.onResume()
        if (serviceConnection.isConnected) {
            updateUiForBoundService()
        }
        else {
            updateUiForUnboundService()
        }

    }


    override fun onStop() {
        super.onStop()
        doUnbindService()
    }

    private fun doBindService() {
        val serviceToStart = Intent(this, TimestampService::class.java)
        bindService(serviceToStart, serviceConnection, Context.BIND_AUTO_CREATE)
        restart_timestamp_service_button.isEnabled = false
        Timber.d("Requested bindService")
        message_textview.text = ""
    }

    private fun updateUiForBoundService() {
//        stop_timestamp_service_button.setOnClickListener(stopServiceListener)
        stop_timestamp_service_button.isEnabled = true

//        restart_timestamp_service_button.setOnClickListener(getTimestampListener)
        restart_timestamp_service_button.isEnabled = false

//        get_timestamp_button.setOnClickListener(null)
        get_timestamp_button.isEnabled = true
    }

    private fun doUnbindService() {
        unbindService(serviceConnection)
        restart_timestamp_service_button.isEnabled = true;
        Timber.d("Requested unbind service.")
        message_textview.text = "";
    }

    private fun updateUiForUnboundService() {
//        stop_timestamp_service_button.setOnClickListener(null)
        stop_timestamp_service_button.isEnabled = false

//        restart_timestamp_service_button.setOnClickListener(startServiceListener)
        restart_timestamp_service_button.isEnabled = true

//        get_timestamp_button.setOnClickListener(null)
        get_timestamp_button.isEnabled = false
    }
}
