package com.urgentrn.urncexchange.models.request;

public class ImageUploadRequest {

    public enum Type {
        DEFAULT,
        NIN_DOCUMENT,
        NIN_DOCUMENT_FRONT,
        NIN_DOCUMENT_BACK,
        PASSPORT_FRONT,
        PASSPORT_BACK,
        DRIVING_LICENSE_FRONT,
        DRIVING_LICENSE_BACK,
        VOTER_ID_FRONT,
        VOTER_ID_BACK,
        WORK_PERMIT_FRONT,
        WORK_PERMIT_BACK,
        NIN_PHOTO,
        EXTRA_DOC
    }

    private String type;
    private String data;
    private String path;

    public ImageUploadRequest(Type type, String data) {
        this(type, data, null);
    }

    public ImageUploadRequest(Type type, String data, String path) {
        setType(type);
        setData(data);
        setPath(path);
    }

    public void setType(Type type) {
        this.type = type.name().toLowerCase();
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
