package com.tavisca;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Our own example protocol mapper.
 */
public class HelloWorldMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    /*
     * A config which keycloak uses to display a generic dialog to configure the token.
     */
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    /*
     * The ID of the token mapper. Is public, because we need this id in our data-setup project to
     * configure the protocol mapper in keycloak.
     */
    public static final String PROVIDER_ID = "oidc-hello-world-mapper";

    static {
        // The builtin protocol mapper let the user define under which claim name (key)
        // the protocol mapper writes its value. To display this option in the generic dialog
        // in keycloak, execute the following method.
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        // The builtin protocol mapper let the user define for which tokens the protocol mapper
        // is executed (access token, id token, user info). To add the config options for the different types
        // to the dialog execute the following method. Note that the following method uses the interfaces
        // this token mapper implements to decide which options to add to the config. So if this token
        // mapper should never be available for some sort of options, e.g. like the id token, just don't
        // implement the corresponding interface.
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, HelloWorldMapper.class);
    }

    @Override
    public String getDisplayCategory() {
        return "Token mapper";
    }

    @Override
    public String getDisplayType() {
        return "Hello World Mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds data to the claim";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }


    @Override
    protected void setClaim(final IDToken token, final ProtocolMapperModel mappingModel, final UserSessionModel userSession,
                            final KeycloakSession keycloakSession, ClientSessionContext clientSessionCtx) {
        // adds our data to the token. Uses the parameters like the claim name which were set by the user
        // when this protocol mapper was configured in keycloak. Note that the parameters which can
        // be configured in keycloak for this protocol mapper were set in the static intializer of this class.
        //
        // Sets a static "Hello world" string, but we could write a dynamic value like a group attribute here too.

        try {
            List<String> userId = keycloakSession.getContext().getRequestHeaders().getRequestHeader("userId");
            Map<String, Object> stringObjectMap = sendGet(userId.get(0));
            stringObjectMap.forEach((k, v) -> System.out.println("**************** "+ k + "***** " + v));
            OIDCAttributeMapperHelper.mapClaim(token, mappingModel,stringObjectMap );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> sendGet(String userId) throws IOException {
        URL obj = new URL("http://service:8087/user/"+userId);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(con.getInputStream(),
                    new TypeReference<Map<String, Object>>(){});
        }
        return null;
    }



}
