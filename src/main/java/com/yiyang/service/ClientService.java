package com.yiyang.service;

import com.yiyang.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    // ==================== 老人基本信息 CRUD ====================

    Client addClient(Client client);

    Client updateClient(Integer id, Client client);

    boolean deleteClient(Integer id);

    Optional<Client> getClientById(Integer id);

    List<Client> getAllClients();

    long getClientCount();

    // ==================== 查询/搜索 ====================

    List<Client> searchClients(String name, String type);

    List<Client> getClientsByType(String type);

    List<Client> getClientsByNursingLevel(String nursingLevel);

    Optional<Client> getClientByBed(String buildingNo, String roomNo, String bedNo);

    // ==================== 护理等级设定 ====================

    void assignNursingLevel(Integer clientId, Long nursingLevelId);
}
