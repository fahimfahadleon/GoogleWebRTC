package com.example.webrtctest;

import static com.example.webrtctest.CustomFunctions.createValue;
import static com.example.webrtctest.CustomFunctions.setValue;


import org.webrtc.CandidatePairChangeEvent;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.IceCandidateErrorEvent;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlin.Unit;

public class StreamPeerConnection implements PeerConnection.Observer, SdpObserver {


    CustomPeerConnection connection;
    Lock pendingIceMutex = new ReentrantLock();


    ArrayList<IceCandidate> pendingIceCandidates = new ArrayList<>();

    public Result<SessionDescription> createOffer() {
        return createValue(it -> connection.createOffer(it, mediaConstraints));
    }

    public Result<SessionDescription> createAnswer() {
        return createValue(it -> connection.createAnswer(it, mediaConstraints));
    }

    public Result<Unit> setRemoteDescription(SessionDescription sessionDescription) {
        return setValue(() -> {
            connection.setRemoteDescription(
                    it,
                    new SessionDescription(
                            sessionDescription.type,
                            sessionDescription.description
                    )
            );
        }).also(() -> {
            pendingIceMutex.lock();
            try {
                // Your synchronized code here
                for (IceCandidate iceCandidate : pendingIceCandidates) {

                    connection.addRtcIceCandidate(iceCandidate);
                }
                pendingIceCandidates.clear();
            } finally {
                pendingIceMutex.unlock();
            }
        });
    }

    public Result<Unit> setLocalDescription(SessionDescription sessionDescription) {
        SessionDescription sdp = new SessionDescription(sessionDescription.type, sessionDescription.description);
        return setValue(() -> connection.setLocalDescription(this, sdp));
    }

    public Result<Unit> addIceCandidate(IceCandidate iceCandidate) {
        if (connection.getRemoteDescription() == null) {
            pendingIceMutex.lock();
            try {
                pendingIceCandidates.add(iceCandidate);
            } finally {
                pendingIceMutex.unlock();
            }
            return Result.failure(new RuntimeException("RemoteDescription is not set"));
        }

        return connection.addRtcIceCandidate(iceCandidate);
    }


    public void onVideoTrack(RtpTransceiver transceiver) {

    }

    public void onStreamAdded(MediaStream mediaStream) {

    }

    public void onNegotiationNeeded(StreamPeerConnection streamPeerConnection, StreemPeerType type) {

    }

    public StreamPeerConnection() {

    }

    public void onIceCandidate(IceCandidate iceCandidate, StreamPeerType streamPeerType) {

    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

    }

    @Override
    public void onStandardizedIceConnectionChange(PeerConnection.IceConnectionState newState) {
        PeerConnection.Observer.super.onStandardizedIceConnectionChange(newState);
    }

    @Override
    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
        PeerConnection.Observer.super.onConnectionChange(newState);
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {

    }

    @Override
    public void onIceCandidateError(IceCandidateErrorEvent event) {
        PeerConnection.Observer.super.onIceCandidateError(event);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onSelectedCandidatePairChanged(CandidatePairChangeEvent event) {
        PeerConnection.Observer.super.onSelectedCandidatePairChanged(event);
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {

    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onRenegotiationNeeded() {

    }

    @Override
    public void onAddTrack(RtpReceiver receiver, MediaStream[] mediaStreams) {
        PeerConnection.Observer.super.onAddTrack(receiver, mediaStreams);
    }

    @Override
    public void onRemoveTrack(RtpReceiver receiver) {
        PeerConnection.Observer.super.onRemoveTrack(receiver);
    }

    @Override
    public void onTrack(RtpTransceiver transceiver) {
        PeerConnection.Observer.super.onTrack(transceiver);
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {

    }

    @Override
    public void onSetSuccess() {

    }

    @Override
    public void onCreateFailure(String s) {

    }

    @Override
    public void onSetFailure(String s) {

    }







}
