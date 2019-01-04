package edu.umsl.site.entity;

import org.springframework.http.MediaType;

public class ImageAttachment
{
    private String name; //The name of the uploaded image

    private MediaType mimeContentType; //The type of the image (MediaType.JPEG, MediaType.PNG, etc).

    private long size; //The size of the image (in MB I believe)

    //The byte array of the image, which will be written to the response and retrieved from the database.
    private byte[] contents;

    //Constructor that requires all fields
    public ImageAttachment(MediaType mimeContentType, long size, byte[] contents) {
        this.mimeContentType = mimeContentType;
        this.size = size;
        this.contents = contents;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public MediaType getMimeContentType() {
        return mimeContentType;
    }

    public void setMimeContentType(MediaType mimeContentType) {
        this.mimeContentType = mimeContentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContents()
    {
        return contents;
    }

    public void setContents(byte[] contents)
    {
        this.contents = contents;
    }

}