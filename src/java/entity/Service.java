package java.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Service extends Operator implements Serializable {
    private final List<Integer> clientIds = new ArrayList<>();
    private Boolean deleted = false;

    public Service() {
    }

    public Service(String loginCode, String password, String realName) {
        super(loginCode, password, realName);
    }

    public List<Integer> getClientIds() {
        return clientIds;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void addCustomer(Client client) {
        if (client != null && client.getId() != null && !clientIds.contains(client.getId())) {
            clientIds.add(client.getId());
        }
    }

    public void removeCustomer(Client client) {
        if (client != null) {
            clientIds.remove(client.getId());
        }
    }

    public boolean servesClient(Integer clientId) {
        return clientId != null && clientIds.contains(clientId);
    }
}
