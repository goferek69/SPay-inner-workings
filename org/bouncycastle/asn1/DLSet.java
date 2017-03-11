package org.bouncycastle.asn1;

import java.util.Enumeration;

public class DLSet extends ASN1Set {
    private int bodyLength;

    public DLSet() {
        this.bodyLength = -1;
    }

    public DLSet(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
        this.bodyLength = -1;
    }

    public DLSet(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector, false);
        this.bodyLength = -1;
    }

    public DLSet(ASN1Encodable[] aSN1EncodableArr) {
        super(aSN1EncodableArr, false);
        this.bodyLength = -1;
    }

    private int getBodyLength() {
        if (this.bodyLength < 0) {
            Enumeration objects = getObjects();
            int i = 0;
            while (objects.hasMoreElements()) {
                i = ((ASN1Encodable) objects.nextElement()).toASN1Primitive().toDLObject().encodedLength() + i;
            }
            this.bodyLength = i;
        }
        return this.bodyLength;
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        ASN1OutputStream dLSubStream = aSN1OutputStream.getDLSubStream();
        int bodyLength = getBodyLength();
        aSN1OutputStream.write(49);
        aSN1OutputStream.writeLength(bodyLength);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dLSubStream.writeObject((ASN1Encodable) objects.nextElement());
        }
    }

    int encodedLength() {
        int bodyLength = getBodyLength();
        return bodyLength + (StreamUtil.calculateBodyLength(bodyLength) + 1);
    }
}