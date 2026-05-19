package java.service.impl;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.entity.*;
import java.DAO.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
// 入住登记管理（独立类）
public class CheckInManager {
    private final ClientDAO clientDAO = ClientDAO.getInstance();
    private final BedDAO bedDAO = BedDAO.getInstance();
    private final BedDetailsDAO bedDetailsDAO = BedDetailsDAO.getInstance();
    private final ClientNursingSettingDAO clientNursingSettingDAO = ClientNursingSettingDAO.getInstance();

    private static final String BUILDING = "606";

    //多条件查询客户 默认查自理老人
    public List<Client> queryClientList(String clientName, String elderType) {
        List<Client> resultList = new ArrayList<>();
        List<Client> allClient = clientDAO.findAll();
        for (Client client : allClient) {
            if (clientName != null && !client.getName().contains(clientName)) {
                continue;
            }
            List<ClientNursingSetting> settingList = clientNursingSettingDAO.findByClientId(client.getId().longValue());
            String realType = settingList == null || settingList.isEmpty() ? "自理老人" : "护理老人";
            if (elderType == null) {
                if ("自理老人".equals(realType)) {
                    resultList.add(client);
                }
            } else {
                if (elderType.equals(realType)) {
                    resultList.add(client);
                }
            }
        }
        return resultList;
    }

    //入住登记
    public String checkInClient(Client client) {
        client.setBuildingNo(BUILDING);
        List<ClientNursingSetting> settings = clientNursingSettingDAO.findByClientId(client.getId().longValue());
        client.setType(settings == null || settings.isEmpty() ? "自理老人" : "护理老人");
        clientDAO.save(client);

        Optional<Bed> bedOptional = bedDAO.findByBuildingAndRoomAndBed(BUILDING, Integer.valueOf(client.getRoomNo()), client.getBedNo());
        if (bedOptional.isPresent()) {
            Bed bed = bedOptional.get();
            bed.setBedStatus(2);
            bedDAO.update(bed);
            BedDetails details = new BedDetails();
            details.setCustomerId(client.getId());
            details.setBedId(bed.getId().intValue());
            details.setStartDate(new java.util.Date());
            details.setIsDeleted(0);
            bedDetailsDAO.save(details);
        }
        return "入住登记成功";
    }

    //获取所有房间号
    public List<String> getAllRoomNo() {
        List<Bed> bedList = bedDAO.findAll();
        List<String> roomList = new ArrayList<>();
        for (Bed bed : bedList) {
            String roomStr = String.valueOf(bed.getRoomNo());
            if (!roomList.contains(roomStr)) {
                roomList.add(roomStr);
            }
        }
        return roomList;
    }

    //根据房间获取空闲床位
    public List<Bed> getFreeBedByRoom(Integer roomNo) {
        List<Bed> allBeds = bedDAO.findAll();
        List<Bed> freeBedList = new ArrayList<>();
        for (Bed bed : allBeds) {
            if (BUILDING.equals(bed.getBuilding()) && roomNo.equals(bed.getRoomNo()) && bed.getBedStatus() == 1) {
                freeBedList.add(bed);
            }
        }
        return freeBedList;
    }

    //逻辑删除客户
    public void deleteClient(Integer clientId) {
        clientDAO.softDelete(clientId);
        List<BedDetails> detailsList = bedDetailsDAO.findByCustomerId(clientId);
        if (detailsList != null && !detailsList.isEmpty()) {
            BedDetails details = detailsList.get(0);
            details.setIsDeleted(1);
            bedDetailsDAO.update(details);
            Optional<Bed> bedOpt = bedDAO.findById(details.getBedId().longValue());
            if (bedOpt.isPresent()) {
                Bed bed = bedOpt.get();
                bed.setBedStatus(1);
                bedDAO.update(bed);
            }
        }
    }

    //修改客户信息
    public void updateClient(Client client) {
        clientDAO.update(client);
    }
}