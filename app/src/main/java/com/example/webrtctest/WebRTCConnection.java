package com.example.webrtctest;


import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.os.Build;

import org.webrtc.AudioSource;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Capturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;

import io.getstream.webrtc.android.ui.VideoTextureViewRenderer;

public class WebRTCConnection {

    private PeerConnectionFactory factory;
    private PeerConnection peerConnection;
    private VideoTrack localVideoTrack;
    private VideoTrack remoteVideoTrack;
    Context context;
    EglBase base;


    public void initializeLocalVideo(VideoTextureViewRenderer surface){


        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create(
                Thread.currentThread().getName(),
                base.getEglBaseContext()
        );
        VideoCapturer videoCapturer = createVideoCapturer();
        VideoSource localVideoSource = factory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, surface.getContext(), localVideoSource.getCapturerObserver());
//            videoCapturer.startCapture(1280, 720, 60);
//            videoCapturer.startCapture(1280, 720, 60);
//            videoCapturer.startCapture(720, 480, 30);
        videoCapturer.startCapture(320, 240, 30);

        localStream.addTrack(localVideoTrack);
    }

    public WebRTCConnection(Context context) {
        this.context = context;

      base = EglBase.create();

        JavaAudioDeviceModule module = JavaAudioDeviceModule
                .builder(context)
                .setUseHardwareAcousticEchoCanceler(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                .setUseHardwareNoiseSuppressor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                .setAudioRecordErrorCallback(audioRecordErrorCallback)
                .setAudioTrackErrorCallback(audioTrackErrorCallback)
                .setAudioRecordStateCallback(audioRecordStateCallback)
                .setAudioTrackStateCallback(audioTrackStateCallback)
                .createAudioDeviceModule();
        module.setMicrophoneMute(false);
        module.setSpeakerMute(false);

        PeerConnectionFactory.InitializationOptions i = PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions();
        PeerConnectionFactory.initialize(i);

        DefaultVideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(base.getEglBaseContext(), true,  true);


        DefaultVideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory( base.getEglBaseContext());

        factory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(decoderFactory)
                .setVideoEncoderFactory(encoderFactory)
                .setAudioDeviceModule(module)
                .createPeerConnectionFactory();






        // Create audio and video sources
        AudioSource audioSource = factory.createAudioSource(new MediaConstraints());
        // You need to implement this method





        VideoSource remoteVideoSource = factory.createVideoSource(false);



        // Create local and remote video tracks
        localVideoTrack = factory.createVideoTrack("localVideoTrack", localVideoSource);
        remoteVideoTrack = factory.createVideoTrack("remoteVideoTrack", remoteVideoSource);

        // Create PeerConnection configuration
        PeerConnection.RTCConfiguration configuration = new PeerConnection.RTCConfiguration(new ArrayList<>());
        configuration.iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));

        // Create PeerConnection
        peerConnection = factory.createPeerConnection(configuration, new StreamPeerConnection());
    }

    // Implement the createVideoCapturer method to obtain a video capturer for your device
    private VideoCapturer createVideoCapturer(){
//        Camera1Enumerator enumerator = new Camera1Enumerator(false);
//
//        String[] deviceNames = enumerator.getDeviceNames();
//        String selectedDeviceName = null;
//        for (String deviceName : deviceNames) {
//            if (enumerator.isFrontFacing(deviceName)) {
//                selectedDeviceName = deviceName;
//                break;
//            }
//        }
//
//        return enumerator.createCapturer(selectedDeviceName, null);

        try {
            CameraManager manager  = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (manager == null) {
                throw new RuntimeException("CameraManager was not initialized!");
            }

            String[] ids = manager.getCameraIdList();
            boolean foundCamera = false;
            String cameraId = "";

            for (String id : ids) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);
                Integer cameraLensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);

                if (cameraLensFacing != null && cameraLensFacing == CameraMetadata.LENS_FACING_FRONT) {
                    foundCamera = true;
                    cameraId = id;
                    break;
                }
            }

            if (!foundCamera && ids.length > 0) {
                cameraId = ids[0];
            }

            return new Camera2Capturer(context, cameraId, null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }





    JavaAudioDeviceModule.AudioTrackStateCallback audioTrackStateCallback = new JavaAudioDeviceModule.AudioTrackStateCallback() {
        @Override
        public void onWebRtcAudioTrackStart() {

        }

        @Override
        public void onWebRtcAudioTrackStop() {

        }
    };
JavaAudioDeviceModule.AudioRecordStateCallback audioRecordStateCallback = new JavaAudioDeviceModule.AudioRecordStateCallback() {
    @Override
    public void onWebRtcAudioRecordStart() {

    }

    @Override
    public void onWebRtcAudioRecordStop() {

    }
};
    JavaAudioDeviceModule.AudioTrackErrorCallback audioTrackErrorCallback = new JavaAudioDeviceModule.AudioTrackErrorCallback() {
        @Override
        public void onWebRtcAudioTrackInitError(String s) {

        }

        @Override
        public void onWebRtcAudioTrackStartError(JavaAudioDeviceModule.AudioTrackStartErrorCode audioTrackStartErrorCode, String s) {

        }

        @Override
        public void onWebRtcAudioTrackError(String s) {

        }
    };

    JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = new JavaAudioDeviceModule.AudioRecordErrorCallback() {
        @Override
        public void onWebRtcAudioRecordInitError(String s) {

        }

        @Override
        public void onWebRtcAudioRecordStartError(JavaAudioDeviceModule.AudioRecordStartErrorCode audioRecordStartErrorCode, String s) {

        }

        @Override
        public void onWebRtcAudioRecordError(String s) {

        }
    };

}
