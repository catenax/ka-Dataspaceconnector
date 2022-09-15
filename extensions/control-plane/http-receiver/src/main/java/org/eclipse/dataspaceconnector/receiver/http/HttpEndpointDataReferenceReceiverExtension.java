/*
 *  Copyright (c) 2022 Amadeus
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Amadeus - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.receiver.http;

import dev.failsafe.RetryPolicy;
import okhttp3.OkHttpClient;
import org.eclipse.dataspaceconnector.common.string.StringUtils;
import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.EdcSetting;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.system.Inject;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.edr.EndpointDataReferenceReceiverRegistry;

public class HttpEndpointDataReferenceReceiverExtension implements ServiceExtension {

    @EdcSetting
    private static final String HTTP_RECEIVER_ENDPOINT = "edc.receiver.http.endpoint";

    @EdcSetting
    private static final String HTTP_RECEIVER_AUTH_KEY = "edc.receiver.http.auth-key";

    @EdcSetting
    private static final String HTTP_RECEIVER_AUTH_CODE = "edc.receiver.http.auth-code";

    public static final String SPEC_SEPARATOR=",";

    @Inject
    private EndpointDataReferenceReceiverRegistry receiverRegistry;

    @Inject
    private OkHttpClient httpClient;

    @Inject
    @SuppressWarnings("rawtypes")
    private RetryPolicy retryPolicy;

    @Override
    public String name() {
        return "Http Endpoint Data Reference Receiver";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var endpoint = context.getSetting(HTTP_RECEIVER_ENDPOINT, null);
        if (StringUtils.isNullOrBlank(endpoint)) {
            throw new EdcException(String.format("Missing mandatory http receiver endpoint: `%s`", HTTP_RECEIVER_ENDPOINT));
        }
        var authKey = context.getSetting(HTTP_RECEIVER_AUTH_KEY, null);
        var authCode = context.getSetting(HTTP_RECEIVER_AUTH_CODE, null);

        Monitor monitor= context.getMonitor();

        monitor.debug(String.format("Elaborating http endpoint receiver spec %s",endpoint));

        var endpoints=endpoint.split(",");
        var authKeys= new String[0];
        if(authKey!=null) {
            authKeys=authKey.split(",");
        }
        var authCodes= new String[0];
        if(authCode!=null) {
            authCodes=authCode.split(",");
        }

        if(authKeys.length!=authCodes.length) {
            throw new EdcException(String.format("Length of endpoints %d auth keys %d and auth codes %d do not match", endpoints.length,authKeys.length,authCodes.length));
        }
        if(authKeys.length<endpoints.length) {
            monitor.warning(String.format("There are %d endpoints but only %d authorization infos. Omitting authorization for endpoints %d to %d.",endpoints.length,authKeys.length,authKeys.length+1,endpoints.length));
        }
        for(int count=0;count<endpoints.length;count++) {
            monitor.debug(String.format("About to build and register new http endpoint receiver number %d for address %s",count,endpoints[count]));
            String theEndpoint=endpoints[count];
            String theKey="";
            String theCode="";
            if(authKeys.length>count) {
                theKey=authKeys[count];
                theCode=authCodes[count];
            }
            var receiver = HttpEndpointDataReferenceReceiver.Builder.newInstance()
                    .endpoint(theEndpoint)
                    .authHeader(theKey, theCode)
                    .httpClient(httpClient)
                    .typeManager(context.getTypeManager())
                    .retryPolicy(retryPolicy)
                    .monitor(monitor)
                    .build();
            receiverRegistry.registerReceiver(receiver);
        }
    }

}
