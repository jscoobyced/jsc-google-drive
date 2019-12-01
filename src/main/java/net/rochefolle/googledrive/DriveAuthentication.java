package net.rochefolle.googledrive;

import java.io.IOException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

public class DriveAuthentication extends AuthorizationCodeInstalledApp {

    public DriveAuthentication(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        super(flow, receiver);
    }

    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        System.out.println(authorizationUrl.build());
    }
}