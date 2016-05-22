package network.upnp.components;

/**
 * A <tt>PortMappingEntry</tt> is the class used to represent port mappings on
 * the GatewayDevice.
 *
 * A port mapping on the GatewayDevice will allow all packets directed to port
 * <tt>externalPort</tt> of the external IP address of the GatewayDevice
 * using the specified <tt>protocol</tt> to be redirected to port
 * <tt>internalPort</tt> of <tt>internalClient</tt>.
 *
 * @see network.upnp.components.gateway.wetorrent.upnp.GatewayDevice
 * @see network.upnp.components.gateway.wetorrent.upnp.GatewayDevice#getExternalIPAddress()
 */
public class PortMappingEntry {

    /**
     * The internal port
     */
    private int internalPort;
    /**
     * The external port of the mapping (the one on the GatewayDevice)
     */
    private int externalPort;
    /**
     * The remote host this mapping is associated with
     */
    private String remoteHost;
    /**
     * The internal host this mapping is associated with
     */
    private String internalClient;
    /**
     * The protocol associated with this mapping (i.e. <tt>TCP</tt> or
     * <tt>UDP</tt>)
     */
    private String protocol;
    /**
     * A flag that tells whether the mapping is enabled or not
     * (<tt>"1"</tt> for enabled, <tt>"0"</tt> for disabled)
     */
    private String enabled;
    /**
     * A human readable description of the port mapping (used for display
     * purposes)
     */
    private String portMappingDescription;

    /**
     * Creates a new PortMappingEntry
     */
    public PortMappingEntry() {}

    /**
     * Gets the internal port for this mapping
     * @return the {@link #internalPort}
     */
    public int getInternalPort() {
        return internalPort;
    }

    /**
     * Sets the {@link #internalPort}
     * @param internalPort the port to use
     */
    public void setInternalPort(int internalPort) {
        this.internalPort = internalPort;
    }

    /**
     * Gets the external (remote) port for this mapping
     * @return the {@link #externalPort}
     */
    public int getExternalPort() {
        return externalPort;
    }

    /**
     * Sets the {@link #externalPort}
     * @param externalPort the port to use
     */
    public void setExternalPort(int externalPort) {
        this.externalPort = externalPort;
    }

    /**
     * Gets the remote host this mapping is associated with
     * @return the {@link #remoteHost}
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Sets the {@link #remoteHost}
     * @param remoteHost the host to set
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * Gets the internal host this mapping is associated with
     * @return the {@link internalClient}
     */
    public String getInternalClient() {
        return internalClient;
    }

    /**
     * Sets the {@link #internalClient}
     * @param internalClient the client to set
     */
    public void setInternalClient(String internalClient) {
        this.internalClient = internalClient;
    }

    /**
     * Gets the protocol associated with this mapping
     * @return {@link #protocol}
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the {@link #protocol} associated with this mapping
     * @param protocol one of <tt>TCP</tt> or <tt>UDP</tt>
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Gets the enabled flag (<tt>"1"</tt> if enabled, <tt>"0"</tt> otherwise)
     * @return {@link #enabled}
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * Sets the {@link #enabled} flag
     * @param enabled <tt>"1"</tt> for enabled, <tt>"0"</tt> for disabled
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the port mapping description
     * @return {@link #portMappingDescription}
     */
    public String getPortMappingDescription() {
        return portMappingDescription;
    }

    /**
     * Sets the {@link #portMappingDescription}
     * @param portMappingDescription the description to set
     */
    public void setPortMappingDescription(String portMappingDescription) {
        this.portMappingDescription = portMappingDescription;
    }
}
