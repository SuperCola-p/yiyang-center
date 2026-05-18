package java.entity;

import java.io.Serializable;
import java.util.Date;

    /**
     * 床位详细信息表 (beddetails)
     * 存储床位详细信息（包含顾客ID、床位ID、起始日期、结束日期、床位详情信息）
     */
    public class BedDetails implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 主键
         */
        private Integer id;

        /**
         * 床位起始日期
         */
        private Date startDate;

        /**
         * 床位结束日期
         */
        private Date endDate;

        /**
         * 床位详情信息
         */
        private String bedDetails;

        /**
         * 顾客ID
         */
        private Integer customerId;

        /**
         * 床位ID
         */
        private Integer bedId;

        /**
         * 逻辑删除标记（0：显示；1：隐藏）
         */
        private Integer isDeleted;

        // -------------------- Getter & Setter --------------------

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getBedDetails() {
            return bedDetails;
        }

        public void setBedDetails(String bedDetails) {
            this.bedDetails = bedDetails;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public Integer getBedId() {
            return bedId;
        }

        public void setBedId(Integer bedId) {
            this.bedId = bedId;
        }

        public Integer getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(Integer isDeleted) {
            this.isDeleted = isDeleted;
        }

        @Override
        public String toString() {
            return "BedDetails{" +
                    "id=" + id +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", bedDetails='" + bedDetails + '\'' +
                    ", customerId=" + customerId +
                    ", bedId=" + bedId +
                    ", isDeleted=" + isDeleted +
                    '}';
        }
    }

