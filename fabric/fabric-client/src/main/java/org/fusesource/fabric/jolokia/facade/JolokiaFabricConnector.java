package org.fusesource.fabric.jolokia.facade;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pWriteRequest;

import javax.management.MalformedObjectNameException;

/**
 * @author Stan Lewis
 */
public class JolokiaFabricConnector {

    public static String FabricManagerMBean = "org.fusesource.fabric:type=Fabric";

    private J4pClient j4p;
    private String userName;
    private String password;
    private String url;
    private FabricServiceFacade fabricServiceFacade;


    /**
     * creates the fabric connector and returns it
     *
     * @param user     the user
     * @param password the password
     * @param url      the url
     * @return the initialized and ready to use connector
     */
    static JolokiaFabricConnector getFabricConnector(String user, String password, String url) {
        JolokiaFabricConnector rc = new JolokiaFabricConnector();
        rc.setUserName(user);
        rc.setPassword(password);
        rc.setUrl(url);
        rc.connect();
        return rc;
    }

    static J4pExecRequest createExecRequest(String operation, Object... args) throws MalformedObjectNameException {
        J4pExecRequest rc = new J4pExecRequest(FabricManagerMBean, operation, args);
        rc.setPreferredHttpMethod("POST");
        return rc;
    }

    static J4pWriteRequest createWriteRequest(String attribute, Object value) throws MalformedObjectNameException {
        J4pWriteRequest answer = new J4pWriteRequest(FabricManagerMBean, attribute, value);
        answer.setPreferredHttpMethod("POST");
        return answer;
    }

    static J4pReadRequest createReadRequest(String attribute) throws MalformedObjectNameException {
        J4pReadRequest answer = new J4pReadRequest(FabricManagerMBean, attribute);
        answer.setPreferredHttpMethod("POST");
        return answer;
    }

    /**
     * connects to a fabric
     */
    public void connect() {
        if (this.j4p != null || this.fabricServiceFacade != null) {
            disconnect();
        }
        this.j4p = J4pClient.url(this.url).user(this.userName).password(this.password).build();

        /* This needs further investigation...
        DefaultHttpClient httpClient = (DefaultHttpClient) j4p.getHttpClient();
        httpClient.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return true;
            }
        });
        */
        this.fabricServiceFacade = new FabricServiceFacade(this);
    }

    /**
     * disconnects from a fabric
     */
    public void disconnect() {
        if (this.j4p != null) {
            this.j4p = null;
        }
        if (this.fabricServiceFacade != null) {
            this.fabricServiceFacade = null;
        }
    }

    /**
     * returns the fabric service implementation
     *
     * @return the fabric service implementation
     */
    public FabricServiceFacade getFabricServiceFacade() {
        return this.fabricServiceFacade;
    }

    /**
     * returns the user name
     *
     * @return the user name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * sets the user name
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * returns the password
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * sets the password
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * returns the url
     *
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * sets the url
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public J4pClient getJ4p() {
        return j4p;
    }
}
