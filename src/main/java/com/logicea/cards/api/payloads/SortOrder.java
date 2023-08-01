package com.logicea.cards.api.payloads;

public enum SortOrder {
    ASC("ascending"),
    DESC("descending");
    private final String value;

    public String getOrder(){return this.value;}
    SortOrder(String value) {
        this.value = value;
    }

    public static boolean isDescending(String value) { return DESC.value.equalsIgnoreCase(value);}

    public static boolean isAscending(String value){
        return ASC.value.equalsIgnoreCase(value);
    }
}
