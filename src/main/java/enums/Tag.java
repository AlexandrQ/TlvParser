package main.java.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Tag {
    DATETIME        ((short)1,    Type.UINT32),
    ORDER_NUMBER    ((short)2,        Type.VLN),
    CUSTOMER        ((short)3,     Type.STRING),
    TLV             ((short)4,  null),
    PRODUCT         ((short)11,     Type.STRING),
    PRICE           ((short)12,       Type.VLN),
    QUANTITY        ((short)13,       Type.FVLN),
    TOTAL_PRICE     ((short)14,    Type.VLN);

    private short id;
    private Type inputType;

    Tag(short id, Type inputType) {
        this.id = id;
        this.inputType = inputType;
    }

    public static Optional<Tag> getById(short id) {
        return Arrays.stream(values()).filter(tag -> tag.getId() == id).findFirst();
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Type getInputType() {
        return inputType;
    }

    public void setInputType(Type inputType) {
        this.inputType = inputType;
    }
}
