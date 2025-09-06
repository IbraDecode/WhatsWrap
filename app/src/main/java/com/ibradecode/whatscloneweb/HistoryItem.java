package com.ibradecode.whatscloneweb;

public class HistoryItem {
    private String phone;
    private String message;
    private String timestamp;

    public HistoryItem(String phone, String message, String timestamp) {
        this.phone = phone;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        // Convert timestamp to more readable format
        try {
            String[] parts = timestamp.split(" ");
            if (parts.length == 2) {
                String[] dateParts = parts[0].split("-");
                String[] timeParts = parts[1].split(":");
                
                if (dateParts.length == 3 && timeParts.length == 3) {
                    String day = dateParts[2];
                    String month = dateParts[1];
                    String year = dateParts[0];
                    String hour = timeParts[0];
                    String minute = timeParts[1];
                    
                    return day + "/" + month + "/" + year + " " + hour + ":" + minute;
                }
            }
        } catch (Exception e) {
            // If parsing fails, return original timestamp
        }
        return timestamp;
    }
}

