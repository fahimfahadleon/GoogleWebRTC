package com.example.webrtctest;



import static com.example.webrtctest.CustomFunctions.setValue;

import org.webrtc.IceCandidate;
import org.webrtc.NativePeerConnectionFactory;
import org.webrtc.PeerConnection;

import kotlin.Unit;

public class CustomPeerConnection extends PeerConnection {
    public CustomPeerConnection(NativePeerConnectionFactory factory) {
        super(factory);
    }

    public Result<Unit> addRtcIceCandidate(IceCandidate iceCandidate){
      return   setValue(()->this.addIceCandidate(iceCandidate));
    }
}
