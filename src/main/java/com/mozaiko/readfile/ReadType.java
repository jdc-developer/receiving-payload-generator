package com.mozaiko.readfile;

public enum ReadType {

    RECEIVING(1, "Recebimento"),
    INVENTORY(2, "Inventário");

    private int id;
    private String description;

    private ReadType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static ReadType toEnum(Integer id) {
        if (id == null) {
            return null;
        }

        for(ReadType x : ReadType.values()) {
            if(id.equals(x.getId())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + id);
    }
}